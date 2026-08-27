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

    public ItemTemplateLoader(EnhancedPlugin plugin) {
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
        if (!meta.hasLore()) {
            meta.setLore(settings.getDefaultItemLore());
        }

        List<String> description = section.getStringList("description")
                .formatEach(line -> ColorUtil.startsWithColor(line) ? line :
                        settings.getDefaultDescriptionColor() + line)
                .orEmpty();

        if (!description.isEmpty()) {
            description.addAll(0, settings.getDescriptionHeader());
            description.addAll(settings.getDescriptionFooter());
        }

        List<String> abilitiesDescription = section.getStringList("ability-description")
                .orEmpty();

        if (abilitiesDescription.isEmpty()) {
            abilitiesDescription = new ArrayList<>();
            List<String> divider = settings.getAbilityDescriptionDivider();

            for (ConfigField field : section.getNestedFields("ability-descriptions|abilities")) {
                CustomPlaceholders placeholders = field.getRequired(CustomPlaceholders.class);
                placeholders.withDefault("name", "NOT SET");
                placeholders.withDefault("trigger", "");
                placeholders.withDefault("description", List.of());
                placeholders.withDefault("mana", List.of());
                placeholders.withDefault("cooldown", List.of());

                Context abilityContext = placeholders.asContext(plugin);
                if (!abilitiesDescription.isEmpty() && !divider.isEmpty()) {
                    abilitiesDescription.addAll(divider);
                }

                List<String> abilityDescription = settings.getAbilityDescriptionTemplate();
                abilitiesDescription.addAll(PlaceholderUtil.parseAll(abilityContext, abilityDescription));
            }

            if (!abilitiesDescription.isEmpty()) {
                abilitiesDescription.addAll(0, settings.getAbilityDescriptionHeader());
                abilitiesDescription.addAll(settings.getAbilityDescriptionFooter());
            }
        }

        String rarity = section.getString("rarity")
                .require(settings::isItemRarity, "Could not find item rarity")
                .withDefault(null);

        if (!meta.hasDisplayName()) {
            if (rarity != null) {
                meta.setDisplayName(settings.getItemNameByRarity(rarity));
            } else {
                meta.setDisplayName(settings.getDefaultItemName());
            }
        }

        ToolBreakingPower toolBreakingPower = null;
        for (BreakingPower breakingPower : settings.getBreakingPowers()) {
            if (section.isSet(CaseFormatter.toKebabCase(breakingPower.getName()))) {
                int amount = section.getInteger(breakingPower.getName())
                        .require(Requirements.positive())
                        .withDefault(1);

                Context context = Context.fromPlugin(plugin).addPlaceholder("amount", amount);
                String display = PlaceholderUtil.parseAll(context, breakingPower.getDisplay());
                toolBreakingPower = new ToolBreakingPower(breakingPower, display, amount);
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
                toolBreakingPower, rarity,
                unplaceable, maxUses, uses,
                stats,
                onBlockBreak, onLeftClick, onRightClick,
                onLeftClickBlock, onLeftClickAir,
                onRightClickBlock, onRightClickAir,
                onSwapHand, onDrop);
    }

}
