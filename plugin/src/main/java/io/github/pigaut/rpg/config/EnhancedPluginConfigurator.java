package io.github.pigaut.rpg.config;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.config.itemstack.*;
import io.github.pigaut.rpg.config.misc.*;
import io.github.pigaut.rpg.core.drop.*;
import io.github.pigaut.rpg.core.hologram.*;
import io.github.pigaut.rpg.core.hologram.config.*;
import io.github.pigaut.rpg.core.hologram.style.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.placeholder.custom.*;
import io.github.pigaut.rpg.core.progressbar.*;
import io.github.pigaut.rpg.core.tag.*;
import io.github.pigaut.rpg.core.tag.config.*;
import io.github.pigaut.rpg.hook.craftengine.*;
import io.github.pigaut.rpg.hook.itemsadder.*;
import io.github.pigaut.rpg.module.command.*;
import io.github.pigaut.rpg.module.function.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.function.action.registry.*;
import io.github.pigaut.rpg.module.function.condition.*;
import io.github.pigaut.rpg.module.function.condition.config.*;
import io.github.pigaut.rpg.module.function.condition.registry.*;
import io.github.pigaut.rpg.module.collection.template.*;
import io.github.pigaut.rpg.module.collection.tier.*;
import io.github.pigaut.rpg.module.function.config.*;
import io.github.pigaut.rpg.module.function.execute.*;
import io.github.pigaut.rpg.module.function.foreach.*;
import io.github.pigaut.rpg.module.function.yield.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.config.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.module.generator.config.*;
import io.github.pigaut.rpg.module.generator.phase.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.module.item.*;
import io.github.pigaut.rpg.module.item.config.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.icon.*;
import io.github.pigaut.rpg.module.menu.config.*;
import io.github.pigaut.rpg.module.message.*;
import io.github.pigaut.rpg.module.message.config.*;
import io.github.pigaut.rpg.module.message.type.*;
import io.github.pigaut.rpg.module.message.type.chat.*;
import io.github.pigaut.rpg.module.mob.bossbar.*;
import io.github.pigaut.rpg.module.mob.bossbar.config.*;
import io.github.pigaut.rpg.module.mob.config.*;
import io.github.pigaut.rpg.module.mob.disguise.*;
import io.github.pigaut.rpg.module.mob.model.*;
import io.github.pigaut.rpg.module.mob.options.*;
import io.github.pigaut.rpg.module.mob.template.*;
import io.github.pigaut.rpg.module.particle.*;
import io.github.pigaut.rpg.module.particle.config.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.recipe.config.*;
import io.github.pigaut.rpg.module.skill.*;
import io.github.pigaut.rpg.module.skill.config.*;
import io.github.pigaut.rpg.module.skill.exp.*;
import io.github.pigaut.rpg.module.skill.template.*;
import io.github.pigaut.rpg.module.sound.*;
import io.github.pigaut.rpg.module.sound.config.*;
import io.github.pigaut.rpg.module.stat.*;
import io.github.pigaut.rpg.module.stat.config.*;
import io.github.pigaut.rpg.module.stat.custom.*;
import io.github.pigaut.rpg.module.stat.modifier.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.matcher.*;
import io.github.pigaut.rpg.module.structure.block.matcher.config.*;
import io.github.pigaut.rpg.module.structure.config.*;
import io.github.pigaut.rpg.module.structure.health.*;
import io.github.pigaut.rpg.module.structure.health.config.*;
import io.github.pigaut.rpg.server.*;
import io.github.pigaut.rpg.util.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import net.md_5.bungee.api.chat.*;
import net.momirealms.craftengine.core.block.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class EnhancedPluginConfigurator extends SpigotConfigurator {

    public EnhancedPluginConfigurator(@NotNull RpgMakerPlugin plugin) {
        addLoader(MaterialTag.class, new MaterialTagLoader());

        addLoader(BlockRange.class, new BlockRangeLoader());
        addMapper(BlockRange.class, new BlockRangeMapper());
        addLoader(Offset.class, new OffsetLoader());
        addLoader(TextComponent.class, new TextComponentLoader());

        addLoader(ProgressBar.class, new ProgressBarLoader(plugin));

        addLoader(ItemDrop.class, new ItemDropLoader(plugin));
        addLoader(ExpDrop.class, new ExpDropLoader(plugin));

        addLoader(ItemStack.class, new PluginItemStackLoader(plugin));
        addLoader(ItemTemplate.class, new ItemTemplateLoader(plugin));

        addLoader(BlockMatcher.class, new BlockMatcherLoader());
        addLoader(BlockTemplate.class, new BlockTemplateLoader());
        addLoader(StructureTemplate.class, new StructureTemplateLoader(plugin));

        addLoader(Message.class, new MessageLoader(plugin));
        addLoader(ChatMessage.class, new ChatMessageLoader());
        addLoader(ActionBarMessage.class, new ActionBarMessageLoader(plugin));
        addLoader(TitleMessage.class, new TitleMessageLoader());
        addLoader(BossBarMessage.class, new BossBarMessageLoader(plugin));
        addLoader(HologramMessage.class, new HologramMessageLoader(plugin));

        addLoader(ParticleEffect.class, new ParticleEffectLoader(plugin));
        addLoader(SoundEffect.class, new SoundEffectLoader(plugin));
        addLoader(HologramTemplate.class, new HologramTemplateLoader(plugin));
        addLoader(HologramStyle.class, new HologramStyleLoader(plugin));

        addLoader(IconOptions.class, new IconOptionsLoader());
        addLoader(ButtonMap.class, new ButtonMapLoader());
        addLoader(FixedButton.class, new FixedButtonLoader(plugin));
        addLoader(ButtonTemplate.class, new ButtonTemplateLoader());
        addLoader(Menu.class, new MenuLoader(plugin));

        addLoader(RecipeChoice.class, new RecipeChoiceLoader());
        addLoader(RecipeTemplate.class, new RecipeTemplateLoader(plugin));
        addLoader(MultiRecipe.class, new MultiRecipeLoader(plugin));

        addLoader(MobTemplate.class, new MobTemplateLoader(plugin));
        addLoader(MobOptions.class, new MobOptionsLoader());
        addLoader(MobBossBarTemplate.class, new MobBossBarTemplateLoader(plugin));

        addLoader(ToolDamage.class, new ToolDamageLoader());

        addLoader(GeneratorTemplate.class, new GeneratorTemplateLoader(plugin));
        addLoader(GeneratorPhase.class, new GeneratorPhaseLoader(plugin));

        addLoader(GateTemplate.class, new GateLoader(plugin));
        addLoader(GatePhase.class, new GatePhaseLoader(plugin));

        addLoader(CollectionTemplate.class, new CollectionTemplateLoader(plugin));
        addLoader(CollectionTier.class, new CollectionTierLoader());

        addLoader(SkillTemplate.class, new SkillTemplateLoader(plugin));
        addLoader(SkillStats.class, new SkillStatsLoader());

        addLoader(ExpAmount.class, new ExpAmountLoader(plugin));
        addLoader(ExpYieldFunction.class, new YieldFunctionLoader<>(plugin,
                ExpYieldFunction.class, ExpAmount.class, ExpYieldFunction::new));

        addLoader(Stat.class, new StatLoader(plugin));
        addLoader(StatModifier.class, new StatModifierLoader());
        addLoader(LeveledStatModifier.class, new LeveledStatModifierLoader());
        addLoader(CustomStat.class, new CustomStatLoader());

        addLoader(CustomCommand.class, new CustomCommandLoader(plugin));
        addLoader(CustomPlaceholders.class, new CustomPlaceholdersLoader());

        ConditionRegistry conditions = plugin.getConditions();
        addLoader(Condition.class, conditions);
        addLoader(NotCondition.class, new NotConditionLoader());
        addLoader(OrCondition.class, new OrConditionLoader());
        addLoader(NorCondition.class, new NorConditionLoader());

        ActionRegistry actions = plugin.getActions();
        addLoader(Action.class, actions);

        addLoader(ForEachSource.class, plugin.getForEachSources());
        addLoader(Function.class, new FunctionLoader(plugin));
        addLoader(GlobalFunction.class, new GlobalFunctionLoader());

        addLoader(BooleanYieldFunction.class, new YieldFunctionLoader<>(plugin,
                BooleanYieldFunction.class, Boolean.class, BooleanYieldFunction::new));

        addLoader(StringYieldFunction.class, new YieldFunctionLoader<>(plugin,
                StringYieldFunction.class, String.class, StringYieldFunction::new));

        addLoader(IntegerYieldFunction.class, new YieldFunctionLoader<>(plugin,
                IntegerYieldFunction.class, Integer.class, IntegerYieldFunction::new));

        addLoader(LongYieldFunction.class, new YieldFunctionLoader<>(plugin,
                LongYieldFunction.class, Long.class, LongYieldFunction::new));

        addLoader(DoubleYieldFunction.class, new YieldFunctionLoader<>(plugin,
                DoubleYieldFunction.class, Double.class, DoubleYieldFunction::new));

        addLoader(FloatYieldFunction.class, new YieldFunctionLoader<>(plugin,
                FloatYieldFunction.class, Float.class, FloatYieldFunction::new));

        addLoader(AmountYieldFunction.class, new YieldFunctionLoader<>(plugin,
                AmountYieldFunction.class, Amount.class, AmountYieldFunction::new));

        addLoader(DelayYieldFunction.class, new YieldFunctionLoader<>(plugin,
                DelayYieldFunction.class, Delay.class, DelayYieldFunction::new));

        // Plugin object wrappers (dependency not required)
        boolean libsDisguises = Server.isPluginEnabled("LibsDisguises");
        addLoader(MobDisguiseTemplate.class, libsDisguises ? new MobDisguiseTemplateLoader(plugin) :
                new PluginNotInstalledLoader<>("LibsDisguises", MobDisguiseTemplate.EMPTY));

        boolean modelEngine = Server.isPluginEnabled("ModelEngine");
        addLoader(MobModelTemplate.class, modelEngine ? new MobModelTemplateLoader() :
                new PluginNotInstalledLoader<>("ModelEngine", MobModelTemplate.EMPTY));

        // Direct plugin objects (dependency required)
        if (Server.isPluginEnabled("ItemsAdder")) {
            addLoader(dev.lone.itemsadder.api.CustomBlock.class, new ItemsAdderBlockLoader());
        }

        if (Server.isPluginEnabled("CraftEngine")) {
            addLoader(BlockDefinition.class, new CraftEngineBlockLoader());
        }

    }

}
