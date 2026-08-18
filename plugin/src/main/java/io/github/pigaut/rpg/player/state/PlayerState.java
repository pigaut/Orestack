package io.github.pigaut.rpg.player.state;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.flag.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.input.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PlayerState extends FlagHolder {

    @NotNull
    EnhancedPlugin getPlugin();

    @NotNull
    PlayerData getPlayerData();

    UUID getUniqueId();

    String getName();

    Player asPlayer();

    OfflinePlayer asOfflinePlayer();

    void sendRawMessage(String message);

    void sendMessage(String message);

    InventoryView openInventory(Inventory inventory);

    void updateInventory();

    void closeInventory();

    @NotNull MenuView openMenu(Menu menu);

    @NotNull MenuView openMenu(Menu menu, MenuView previousView);

    @NotNull MenuView openMenu(@NotNull Menu menu, @NotNull Context context);

    @NotNull MenuView openMenu(@NotNull Menu menu, @NotNull Context context, MenuView previousView);

    @Nullable MenuView getOpenMenu();

    void setOpenMenu(@Nullable MenuView view);

    void performCommand(String command);

    ItemStack getPlayerHead();

    <T> T getCache(String id, Class<T> type);

    void saveCache(String id, Object value);

    void flushCache(String id, Class<?> type);

    @Nullable Location getFirstSelection();

    void setFirstSelection(@Nullable Location location);

    @Nullable Location getSecondSelection();

    void setSecondSelection(@Nullable Location location);

    boolean isMobSpawnPadsVisible();

    void setMobSpawnPadsVisible(boolean visible);

    boolean isAwaitingInput();

    boolean isAwaitingInput(@NotNull InputSource inputSource);

    void submitInput(@NotNull String input);

    void cancelInputCollection();

    ChatInput<String> collectChatInput();

    <T> ChatInput<T> collectChatInput(@NotNull Class<T> classType);

    <T> ChatInput<T> collectChatInput(@NotNull Parser<T> parser);

    MenuSelection<String> collectMenuSelection();

    <T> MenuSelection<T> collectMenuSelection(@NotNull Class<T> classType);

    <T> MenuSelection<T> collectMenuSelection(@NotNull Parser<T> parser);

    boolean isInCombat();

    void setInCombat(boolean inCombat);

    @Nullable
    Long getLastCombatInteraction();

    @Nullable Mob getMobKiller();

    void setMobKiller(@Nullable Mob mob);

    void attack(@NotNull LivingEntity victim, double damage);

    void damage(@NotNull LivingEntity attacker, double amount);

    @Nullable
    PlayerStat getStat(@NotNull Stat statType);

    void updateStatusBar();

    @NotNull
    String getStatusBar();

    @Nullable
    String getStatusBarMessage();

    void sendActionBar(@NotNull String message, @NotNull BarAlignment align);

    void sendActionBar(@NotNull String message);

    void refreshStats();

    void refreshStats(@NotNull EquipmentSlot slot);

    void clearStats();

    void clearStats(@NotNull EquipmentSlot slot);

    boolean isMaxHealth();

    int getMaxHealth();

    int getHealthRegen();

    int getManaRegen();

    int getHealth();

    void setHealth(int health);

    void resetHealth();

    int getDefense();

    boolean isMaxMana();

    int getMaxMana();

    int getMana();

    void setMana(int mana);

    void resetMana();

    double getStatDisplayAmount(@NotNull Stat stat);

    int getAttackDamage();

    double getExpGainMultiplier();

    int getMiningFortune();

    int getFarmingFortune();

    int getForagingFortune();

    // returns 0 (0%) to 1 (100%)
    double getCritChance();

    double getCritDamageMultiplier();

    int getMovementSpeed();

    int getAttackSpeed();

    // Might include debuffs for efficiency enchant (i.e. total - enchant debuff).
    // Use getStatDisplayAmount to get the visual stat amount to show players
    int getRawMiningSpeed();

    boolean hasCooldown(@NotNull String name);

    void addCooldown(@NotNull String name, int ticksDuration);

    void removeCooldown(@NotNull String name);

    long getLastDamageTime();

    void setLastDamageTime(long time);

}
