package io.github.pigaut.rpg.player.state;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.module.mob.spawnpad.visibility.*;
import io.github.pigaut.rpg.module.stat.attribute.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.input.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.rpg.util.collection.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class SimplePlayerState implements PlayerState {

    private final UUID playerId;
    private final String playerName;

    private final EnhancedJavaPlugin plugin;
    private final PlayerData playerData;

    private final Context context;
    private final Set<String> flags = new HashSet<>();
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();
    private final TypeMap<String, Object> cache = new TypeMap<>();
    private long lastDamageTime = 0;

    private final StatsMap stats;
    private String statusBar = "";
    private @Nullable String statusBarMessage = null;
    private int health = 100;
    private int mana = 100;

    private @Nullable MenuView openMenu = null;
    private @Nullable Location firstSelection = null;
    private @Nullable Location secondSelection = null;
    private @Nullable InputCollector inputCollector = null;
    private @Nullable VisibleMobSpawnPads visibleMobSpawnPads = null;
    private @Nullable Long lastCombatInteraction = null;
    private @Nullable Mob mobKiller = null;

    public SimplePlayerState(@NotNull EnhancedJavaPlugin plugin, @NotNull Player player) {
        this.plugin = plugin;
        this.playerId = player.getUniqueId();
        this.playerName = player.getName();
        this.playerData = plugin.getPlayerData().load(player);
        this.context = Context.fromPlayer(plugin, player, this);
        this.stats = new StatsMap();
        reloadStats();
        this.health = stats.getTotalMaxHealth();
        this.mana = stats.getTotalMaxMana();
        updateStatusBar();
    }

    @Override
    public @NotNull EnhancedPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull PlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return playerId;
    }

    @Override
    public @NotNull String getName() {
        return playerName;
    }

    @Override
    public Player asPlayer() {
        return Bukkit.getPlayer(playerId);
    }

    @Override
    public OfflinePlayer asOfflinePlayer() {
        return Bukkit.getOfflinePlayer(playerId);
    }

    @Override
    public void sendRawMessage(String message) {
        asPlayer().sendMessage(message);
    }

    @Override
    public void sendMessage(String message) {
        Player player = asPlayer();
        Context context = Context.fromPlayer(plugin, player, this);
        String parsedMessage = PlaceholderUtil.parseAll(context, message);
        PlayerUtil.sendChat(player, parsedMessage);
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        return asPlayer().openInventory(inventory);
    }

    @Override
    public void updateInventory() {
        asPlayer().updateInventory();
    }

    @Override
    public void closeInventory() {
        asPlayer().closeInventory();
    }

    @Override
    public @NotNull MenuView openMenu(@NotNull Menu menu) {
        return openMenu(menu, openMenu);
    }

    @Override
    public @NotNull MenuView openMenu(@NotNull Menu menu, MenuView previousView) {
        Context context = Context.fromPlayer(plugin, this);
        return openMenu(menu, context, previousView);
    }

    @Override
    public @NotNull MenuView openMenu(@NotNull Menu menu, @NotNull Context context) {
        return openMenu(menu, context, openMenu);
    }

    @Override
    public @NotNull MenuView openMenu(@NotNull Menu menu, @NotNull Context context, MenuView previousView) {
        MenuView view = menu.createView(this, previousView, context);
        view.open();
        return view;
    }

    @Override
    public @Nullable MenuView getOpenMenu() {
        return openMenu;
    }

    @Override
    public void setOpenMenu(@Nullable MenuView view) {
        this.openMenu = view;
    }

    @Override
    public void performCommand(String command) {
        Player player = asPlayer();
        Context context = Context.fromPlayer(plugin, player, this);
        player.performCommand(PlaceholderUtil.parseAll(context, command));
    }

    @Override
    public ItemStack getPlayerHead() {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(asPlayer());
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public <T> T getCache(String id, Class<T> type) {
        return cache.get(id, type);
    }

    @Override
    public void saveCache(String id, Object value) {
        cache.put(id, value.getClass(), value);
    }

    @Override
    public void flushCache(String id, Class<?> type) {
        cache.remove(id, type);
    }

    @Override
    public @Nullable Location getFirstSelection() {
        return firstSelection;
    }

    @Override
    public void setFirstSelection(@Nullable Location location) {
        this.firstSelection = location;
    }

    @Override
    public @Nullable Location getSecondSelection() {
        return secondSelection;
    }

    @Override
    public void setSecondSelection(@Nullable Location location) {
        this.secondSelection = location;
    }

    @Override
    public boolean isMobSpawnPadsVisible() {
        return visibleMobSpawnPads != null;
    }

    @Override
    public void setMobSpawnPadsVisible(boolean visible) {
        if (visible) {
            if (visibleMobSpawnPads == null) {
                visibleMobSpawnPads = new VisibleMobSpawnPads(plugin, playerId);
                visibleMobSpawnPads.start();
            }
        } else {
            if (visibleMobSpawnPads != null) {
                visibleMobSpawnPads.stop();
                visibleMobSpawnPads = null;
            }
        }
    }

    @Override
    public boolean isAwaitingInput() {
        return inputCollector != null && inputCollector.isCollecting();
    }

    @Override
    public boolean isAwaitingInput(@NotNull InputSource inputSource) {
        return isAwaitingInput() && inputCollector.getInputSource() == inputSource;
    }

    @Override
    public void submitInput(@NotNull String input) {
        Preconditions.checkState(isAwaitingInput(), "Player is currently not being prompted for input.");
        inputCollector.accept(input);
        if (!inputCollector.isCollecting()) {
            inputCollector = null;
        }
    }

    @Override
    public void cancelInputCollection() {
        if (inputCollector != null) {
            if (inputCollector.isCollecting()) {
                inputCollector.cancel();
            }
            this.inputCollector = null;
        }
    }

    @Override
    public ChatInput<String> collectChatInput() {
        ChatInput<String> chatInput = new ChatInput<>(plugin, this, Parsers.STRING);
        registerInputCollector(chatInput);
        return chatInput;
    }

    @Override
    public <T> ChatInput<T> collectChatInput(@NotNull Class<T> classType) {
        ChatInput<T> chatInput = new ChatInput<>(plugin, this, Parsers.getByType(classType));
        registerInputCollector(chatInput);
        return chatInput;
    }

    @Override
    public <T> ChatInput<T> collectChatInput(@NotNull Parser<T> parser) {
        ChatInput<T> chatInput = new ChatInput<>(plugin, this, parser);
        registerInputCollector(chatInput);
        return chatInput;
    }

    @Override
    public MenuSelection<String> collectMenuSelection() {
        MenuSelection<String> menuSelection = new MenuSelection<>(this, Parsers.STRING);
        registerInputCollector(menuSelection);
        return menuSelection;
    }

    @Override
    public <T> MenuSelection<T> collectMenuSelection(@NotNull Class<T> classType) {
        MenuSelection<T> menuSelection = new MenuSelection<>(this, Parsers.getByType(classType));
        registerInputCollector(menuSelection);
        return menuSelection;
    }

    @Override
    public <T> MenuSelection<T> collectMenuSelection(@NotNull Parser<T> parser) {
        MenuSelection<T> menuSelection = new MenuSelection<>(this, parser);
        registerInputCollector(menuSelection);
        return menuSelection;
    }

    @Override
    public boolean isInCombat() {
        return lastCombatInteraction != null;
    }

    @Override
    public void setInCombat(boolean inCombat) {
        this.lastCombatInteraction = inCombat ? System.currentTimeMillis() : null;
    }

    @Override
    public @Nullable Long getLastCombatInteraction() {
        return lastCombatInteraction;
    }

    @Override
    public @Nullable Mob getMobKiller() {
        return mobKiller;
    }

    @Override
    public void setMobKiller(@Nullable Mob mobKiller) {
        this.mobKiller = mobKiller;
    }

    @Override
    public void attack(@NotNull LivingEntity victim, double damage) {
        Player playerAttacker = asPlayer();
        if (playerAttacker == null) {
            return;
        }

        setInCombat(true);

        double critChance = getCritChance();
        if (Probability.test(critChance)) {
            damage *= getCritDamageMultiplier();
        }

        PlayerState playerVictim = plugin.getPlayerState(victim);
        if (playerVictim != null && plugin.getSettings().isStats()) {
            EntityUtil.knockback(playerAttacker, victim);
            playerVictim.damage(playerAttacker, damage);
            return;
        }

        Mob mobVictim = plugin.getMob(victim);
        if (mobVictim != null) {
            EntityUtil.knockback(playerAttacker, victim);
            mobVictim.damage(playerAttacker, damage);
            return;
        }

        EntityUtil.attack(playerAttacker, victim, damage);
    }

    @Override
    public void damage(@NotNull LivingEntity attacker, double damageAmount) {
        Player player = asPlayer();
        if (player == null) {
            return;
        }

        setInCombat(true);

        if (plugin.getSettings().isStats()) {
            damageAmount *= damageAmount * StatUtil.getDefenseDamageReduction(getDefense());
            setHealth((int) (health - damageAmount));
        } else {
            EntityUtil.damage(player, damageAmount);
        }

        if (player.getHealth() == 0) {
            this.mobKiller = plugin.getMob(attacker);
        }
    }

    @Override
    public @Nullable PlayerStat getStat(@NotNull Stat statType) {
        return stats.get(statType);
    }

    @Override
    public @Nullable String getStatusBarMessage() {
        return statusBarMessage;
    }

    @Override
    public void updateStatusBar() {
        Settings settings = plugin.getSettings();
        String statusBar = settings.getStatusBar();
        this.statusBar = PlaceholderUtil.parseAll(context, statusBar);
        if (statusBarMessage != null) {
            if (settings.isInsertMessagesInStatusBar()) {
                int length = settings.getInsertedMessageLength();
                if (barMessageAlignment == null) {
                    barMessageAlignment = settings.getInsertedMessageAlign();
                }
                this.statusBar = StringUtil.insertInto(this.statusBar, statusBarMessage, length, barMessageAlignment);
            } else {
                this.statusBar = statusBarMessage;
            }
        }
    }

    @Override
    public @NotNull String getStatusBar() {
        return statusBar;
    }

    private final AtomicLong barMessageId = new AtomicLong(0);
    private BarAlignment barMessageAlignment = null;

    @Override
    public void sendActionBar(@NotNull String message) {
        Settings settings = plugin.getSettings();
        sendActionBar(message, settings.getInsertedMessageAlign());
    }

    @Override
    public void sendActionBar(@NotNull String message, @NotNull BarAlignment alignment) {
        Settings settings = plugin.getSettings();
        if (settings.isShowStatusBar()) {
            long id = barMessageId.incrementAndGet();
            statusBarMessage = PlaceholderUtil.parseAll(context, message);
            barMessageAlignment = alignment;
            updateStatusBar();

            Delay duration = settings.getStatusBarMessageDuration();
            plugin.getScheduler().runTaskLater(duration.toTicks(), () -> {
                if (barMessageId.get() == id) {
                    statusBarMessage = null;
                    barMessageAlignment = settings.getInsertedMessageAlign();
                    updateStatusBar();
                }
            });
            return;
        }

        Player player = asPlayer();
        String parsedMessage = PlaceholderUtil.parseAll(context, message);
        PlayerUtil.sendActionBar(player, parsedMessage);
    }

    @Override
    public void reloadStats() {
        stats.clear();

        Settings settings = plugin.getSettings();
        stats.put(BaseStats.MAX_HEALTH, new PlayerStat(settings.getBaseMaxHealth()));
        stats.put(BaseStats.MAX_MANA, new PlayerStat(settings.getBaseMaxMana()));
        stats.put(BaseStats.DEFENSE, new PlayerStat(settings.getBaseDefense()));
        stats.put(BaseStats.DAMAGE, new PlayerStat(settings.getBaseDamage()));
        stats.put(BaseStats.HEALTH_REGEN, new PlayerStat(settings.getBaseHealthRegen()));
        stats.put(BaseStats.MANA_REGEN, new PlayerStat(settings.getBaseManaRegen()));
        stats.put(BaseStats.CRIT_DAMAGE, new PlayerStat(settings.getBaseCritDamage()));
        stats.put(BaseStats.CRIT_CHANCE, new PlayerStat(settings.getBaseCritChance()));
        stats.put(BaseStats.MINING_FORTUNE, new PlayerStat(settings.getBaseMiningFortune()));
        stats.put(BaseStats.FARMING_FORTUNE, new PlayerStat(settings.getBaseFarmingFortune()));
        stats.put(BaseStats.FORAGING_FORTUNE, new PlayerStat(settings.getBaseForagingFortune()));

        stats.put(BaseStats.MOVEMENT_SPEED, new AttributePlayerStat(playerId, Attributes.MOVEMENT_SPEED,
                plugin.getNamespacedKey("movement_speed"), StatUtil.MOVEMENT_SPEED_MULTIPLIER));

        stats.put(BaseStats.ATTACK_SPEED, new AttributePlayerStat(playerId, Attributes.ATTACK_SPEED,
                plugin.getNamespacedKey("attack_speed")));

        if (Server.getVersion() >= Version.V1_21) {
            stats.put(BaseStats.MINING_SPEED, new AttributePlayerStat(playerId, Attributes.MINING_EFFICIENCY,
                    plugin.getNamespacedKey("mining_speed"), StatUtil.MINING_EFFICIENCY_MULTIPLIER));
        }

        stats.putAll(settings.getCustomStats());
        refreshStats();
    }

    @Override
    public void refreshStats() {
        for (EquipmentSlot slot : PlayerUtil.EQUIPMENT_SLOTS) {
            refreshStats(slot);
        }
    }

    @Override
    public void refreshStats(@NotNull EquipmentSlot slot) {
        clearStats(slot);

        Player player = asPlayer();
        ItemStack item = player.getInventory().getItem(slot);
        Settings settings = plugin.getSettings();

        for (Stat stat : plugin.getStats().getAll()) {
            PlayerStat playerStat = getStat(stat);
            if (playerStat == null) {
                continue;
            }

            Integer statLevel = plugin.getItems().getStatTotalLevel(item, stat);
            if (statLevel != null) {
                playerStat.setEquipment(slot, statLevel);
            }

            if (stat == BaseStats.MINING_SPEED) {
                double debuff = 0;
                if (slot == EquipmentSlot.HAND) {
                    debuff -= ItemUtil.getMiningEfficiencyByType(item) * StatUtil.MINING_EFFICIENCY_MULTIPLIER;
                }
                if (settings.isEfficiencyEnchantAsStat()) {
                    debuff -= ItemUtil.getMiningEfficiencyByEnchant(item) * StatUtil.MINING_EFFICIENCY_MULTIPLIER;
                }
                playerStat.setEquipmentBuff(slot, debuff);
            }
        }

        int maxHealth = getMaxHealth();
        if (health > maxHealth) {
            setHealth(maxHealth);
        }

        int maxMana = getMaxMana();
        if (mana > maxMana) {
            setMana(maxMana);
        }
    }

    @Override
    public void clearStats() {
        for (PlayerStat stat : stats.getAll()) {
            stat.clear();
        }
    }

    @Override
    public void clearStats(@NotNull EquipmentSlot slot) {
        for (PlayerStat stat : stats.getAll()) {
            stat.setEquipment(slot, 0);
            stat.setEquipmentBuff(slot, 0);
        }
    }

    @Override
    public boolean isMaxHealth() {
        return health >= getMaxHealth();
    }

    @Override
    public int getMaxHealth() {
        return stats.getTotalMaxHealth();
    }

    @Override
    public int getHealthRegen() {
        return stats.getTotalHealthRegen();
    }

    @Override
    public int getManaRegen() {
        return stats.getTotalManaRegen();
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        Player player = asPlayer();
        if (player == null) {
            return;
        }

        this.health = Math.max(0, Math.min(health, getMaxHealth()));
        updateStatusBar();

        if (this.health == 0) {
            player.setHealth(0);
            return;
        }

        int maxHealth = getMaxHealth();
        int hearts = (this.health * 20) / maxHealth;
        player.setHealth(Math.max(1, hearts));
    }

    @Override
    public void resetHealth() {
        setHealth(stats.getTotalMaxHealth());
    }

    @Override
    public int getDefense() {
        return stats.getTotalDefense();
    }

    @Override
    public boolean isMaxMana() {
        return mana == getMaxMana();
    }

    @Override
    public int getMaxMana() {
        return stats.getTotalMaxMana();
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, getMaxMana()));
        updateStatusBar();
    }

    @Override
    public void resetMana() {
        setMana(getMaxMana());
    }

    @Override
    public double getStatDisplayAmount(@NotNull Stat statType) {
        PlayerStat stat = getStat(statType);
        return stat != null ? stat.getDisplayTotal() : 0;
    }

    @Override
    public int getAttackDamage() {
        PlayerStat stat = getStat(BaseStats.DAMAGE);
        return stat != null ? (int) stat.getTotal() : 1;
    }

    @Override
    public double getExpGainMultiplier() {
        PlayerStat stat = getStat(BaseStats.EXP_GAIN);
        return stat != null ? stat.getTotal() : 1;
    }

    @Override
    public int getRawMiningSpeed() {
        PlayerStat stat = getStat(BaseStats.MINING_SPEED);
        return stat != null ? (int) stat.getDisplayTotal() : 0;
    }

    @Override
    public int getMiningFortune() {
        PlayerStat stat = getStat(BaseStats.MINING_FORTUNE);
        return stat != null ? (int) stat.getTotal() : 0;
    }

    @Override
    public int getFarmingFortune() {
        PlayerStat stat = getStat(BaseStats.FARMING_FORTUNE);
        return stat != null ? (int) stat.getTotal() : 0;
    }

    @Override
    public int getForagingFortune() {
        PlayerStat stat = getStat(BaseStats.FORAGING_FORTUNE);
        return stat != null ? (int) stat.getTotal() : 0;
    }

    @Override
    public double getCritChance() {
        PlayerStat stat = getStat(BaseStats.CRIT_CHANCE);
        return stat != null ? Math.max(0, Math.min(stat.getTotal(), 1)) : 0;
    }

    @Override
    public double getCritDamageMultiplier() {
        PlayerStat stat = getStat(BaseStats.CRIT_DAMAGE);
        return stat != null ? stat.getTotal() : 1;
    }

    @Override
    public int getMovementSpeed() {
        PlayerStat stat = getStat(BaseStats.MOVEMENT_SPEED);
        return stat != null ? (int) stat.getTotal() : 0;
    }

    @Override
    public int getAttackSpeed() {
        PlayerStat stat = getStat(BaseStats.ATTACK_SPEED);
        return stat != null ? (int) stat.getTotal() : 0;
    }

    @Override
    public boolean hasCooldown(@NotNull String name) {
        Long expiresAt = cooldowns.get(name);
        return expiresAt != null && expiresAt > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(@NotNull String name, int ticksDuration) {
        long expiresAt = System.currentTimeMillis() + TicksUtil.toMillis(ticksDuration);
        cooldowns.put(name, expiresAt);

        plugin.getScheduler().runTaskLater(ticksDuration, () -> {
            cooldowns.remove(name, expiresAt);
        });
    }

    @Override
    public void removeCooldown(@NotNull String name) {
        cooldowns.remove(name);
    }

    @Override
    public long getLastDamageTime() {
        return lastDamageTime;
    }

    @Override
    public void setLastDamageTime(long lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
    }

    @Override
    public boolean hasFlag(@NotNull String flag) {
        return flags.contains(flag);
    }

    @Override
    public @NotNull Collection<String> getFlags() {
        return new ArrayList<>(flags);
    }

    @Override
    public void addFlag(@NotNull String flag) {
        flags.add(flag);
    }

    @Override
    public void addTemporaryFlag(@NotNull String flag, int ticks) {
        flags.add(flag);
        plugin.getScheduler().runTaskLater(ticks, () -> flags.remove(flag));
    }

    @Override
    public void removeFlag(@NotNull String flag) {
        flags.remove(flag);
    }

    private void registerInputCollector(@NotNull InputCollector inputCollector) {
        if (isAwaitingInput()) {
            this.inputCollector.cancel();
        }
        this.inputCollector = inputCollector;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SimplePlayerState that)) return false;
        return Objects.equals(playerId, that.playerId);
    }

}
