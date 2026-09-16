package io.github.pigaut.rpg.module.item.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.placeholder.custom.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.module.item.power.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.components.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ItemTemplateLoader implements ConfigLoader<ItemTemplate> {

    private final EnhancedPlugin plugin;

    public ItemTemplateLoader(@NotNull EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid item";
    }

    @Override
    public @NotNull ItemTemplate loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        String name = section.getKey();
        String group = Group.byItemFile(section.getRoot().getFile());
        ItemStack item = section.getRequired(ItemStack.class);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            throw new InvalidConfigException(section, "type", "Current item type doesn't support item meta");
        }

        Settings settings = plugin.getSettings();
        String category = section.getString("category")
                .require(settings::isItemCategory, "Could not find item category")
                .withDefault(settings.getDefaultItemCategory());

        String rarity = section.getString("rarity")
                .require(settings::isItemRarity, "Could not find item rarity")
                .withDefault(settings.getDefaultItemRarity());

        if (!meta.hasDisplayName()) {
            meta.setDisplayName(rarity != null ? settings.getItemNameByRarity(rarity) : settings.getDefaultItemName());
        }

        if (!meta.hasLore()) {
            meta.setLore(settings.getDefaultItemLore());
        }

        List<String> description = section.getStringList("description")
                .formatEach(line -> ColorUtil.startsWithColor(line) ? line : settings.getDefaultDescriptionColor() + line)
                .orEmpty();

        List<String> abilitiesDescription = new ArrayList<>();
        String abilityDivider = settings.getAbilityLoreDivider();
        for (String templateName : settings.getAbilityTemplateNames()) {
            String key = CaseFormatter.toKebabCase(templateName + "-abilities");
            if (!section.isSet(key)) {
                continue;
            }

            ConfigField field = section.getRequiredField(key);
            List<String> abilityTemplate = settings.getAbilityTemplate(templateName);
            if (abilityTemplate == null) {
                section.collectError(new InvalidConfigException(field, "Could not find ability template with name: " + templateName));
                continue;
            }

            CustomPlaceholders placeholders = field.getRequired(CustomPlaceholders.class);
            placeholders.withDefault("name", "NOT SET");
            placeholders.withDefault("trigger", "");
            placeholders.withDefault("description", List.of());
            placeholders.withDefault("mana", List.of());
            placeholders.withDefault("cooldown", List.of());

            if (abilityDivider != null && !abilitiesDescription.isEmpty()) {
                abilitiesDescription.add(abilityDivider);
            }

            Context abilityContext = placeholders.asContext(plugin);
            abilitiesDescription.addAll(PlaceholderUtil.parseAll(abilityContext, abilityTemplate));
        }

        ToolBreakingPower toolBreakingPower = null;
        for (BreakingPower breakingPower : settings.getBreakingPowers()) {
            if (section.isSet(CaseFormatter.toKebabCase(breakingPower.getName()))) {
                int amount = section.getInteger(breakingPower.getName())
                        .require(Requirements.positive())
                        .withDefault(1);
                toolBreakingPower = new ToolBreakingPower(breakingPower, amount);
                break;
            }
        }

        boolean unplaceable = section.getBoolean("unplaceable")
                .withDefault(false);

        Integer maxUses = section.getInteger("max-uses")
                .require(Requirements.positive())
                .withDefault(null);

        Amount uses = section.get("uses", Amount.class)
                .require(Requirements.positiveAmount())
                .withDefault(null);

        if (maxUses != null && uses != null && uses.maxValue() > maxUses) {
            throw new InvalidConfigException(section, "uses", "uses must be less than or equal to max-uses");
        }
        if (uses == null) {
            uses = maxUses != null ? Amount.fixed(maxUses) : null;
        }
        if (uses != null && maxUses == null) {
            maxUses = (int) uses.maxValue();
        }

        Map<Stat, Amount> stats = new HashMap<>();
        for (KeyedScalar scalar : section.getSectionOrEmpty("stats").getNestedScalars()) {
            Stat stat = scalar.getKey(Stat.class);
            if (stat == BaseStats.MINING_SPEED && Server.getVersion() < Version.V1_21) {
                section.collectWarning(new InvalidConfigException(scalar, "Mining speed stat is only available in 1.21+"));
                continue;
            }

            Amount amount = scalar.getRequired(Amount.class);
            stats.put(stat, amount);
        }

        Function onMineBlock = section.get("on-mine-block", Function.class).withDefault(null);
        Function onBlockBreak = section.get("on-block-break", Function.class).withDefault(null);
        Function onLeftClick = section.get("on-left-click", Function.class).withDefault(null);
        Function onRightClick = section.get("on-right-click", Function.class).withDefault(null);
        Function onLeftClickBlock = section.get("on-left-click-block", Function.class).withDefault(null);
        Function onLeftClickAir = section.get("on-left-click-air", Function.class).withDefault(null);
        Function onRightClickBlock = section.get("on-right-click-block", Function.class).withDefault(null);
        Function onRightClickAir = section.get("on-right-click-air", Function.class).withDefault(null);
        Function onSwapHand = section.get("on-swap|on-swap-hand", Function.class).withDefault(null);
        Function onDrop = section.get("on-drop", Function.class).withDefault(null);

        return new ItemTemplate(plugin, name, group,
                item, meta,
                description, abilitiesDescription,
                toolBreakingPower,
                category, rarity,
                unplaceable, maxUses, uses,
                stats,
                onMineBlock, onBlockBreak,
                onLeftClick, onRightClick,
                onLeftClickBlock, onLeftClickAir,
                onRightClickBlock, onRightClickAir,
                onSwapHand, onDrop);
    }

}
