package io.github.pigaut.rpg.module.mob.spawnpad.tool;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class MobSpawnPadToolEditor extends FramedMenu {

    private final MobSpawnPadTool tool;
    private final ItemStack item;
    private final int itemSlot;

    public MobSpawnPadToolEditor(@NotNull MobSpawnPadTool tool, @NotNull ItemStack item, int itemSlot) {
        super("Mob Spawn Pad Editor", MenuSize.MEDIUM);
        this.tool = tool;
        this.item = item.clone();
        this.itemSlot = itemSlot;
    }

    @Override
    public void onOpen(MenuView view) {
        Context context = view.getContext();
        context.addPlaceholder("mob_spawn_pad_template", tool.getMobTemplateName(item));
        String spawnDelayData = tool.getSpawnDelayData(item);
        context.addPlaceholder("mob_spawn_pad_delay", spawnDelayData != null ? spawnDelayData : "none");
        context.addPlaceholder("mob_spawn_pad_range", tool.getSpawnRange(item));
        context.addPlaceholder("mob_spawn_pad_activation_range", tool.getActivationRange(item));
    }

    @Override
    public Button getToolbarButton3() {
        return Button.builder()
                .type(Material.GREEN_CONCRETE)
                .name("&aApply Changes and Close")
                .enchanted(true)
                .onLeftClick((view, playerState) -> {
                    Player player = view.getViewer().asPlayer();
                    player.getInventory().setItem(itemSlot, item);
                    view.close();
                })
                .buildButton();
    }

    @Override
    public Button getToolbarButton5() {
        return getFrameButton();
    }

    @Override
    public Button getToolbarButton7() {
        return Button.builder()
                .type(Material.RED_CONCRETE)
                .name("&cDiscard Changes and Close")
                .enchanted(true)
                .onLeftClick((view, playerState) -> view.close())
                .buildButton();
    }

    @Override
    public Button[] createButtons(@NotNull Context context) {
        Button[] buttons = super.createButtons(context);

        buttons[10] = Button.builder()
                .type(item.getType())
                .name("&f&lIcon")
                .addEmptyLine()
                .addLeftClickLine("To set the icon")
                .onLeftClick((menuView, playerState) -> {
                    playerState.collectMenuSelection(Material.class)
                            .addValue(Material.SPAWNER, "spawner")
                            .addValue(Material.ROTTEN_FLESH, "rotten_flesh")
                            .addValue(Material.BONE, "bone")
                            .addValue(Material.SLIME_BALL, "slime_ball")
                            .addValue(Material.GUNPOWDER, "gunpowder")
                            .addValue(Material.ENDER_PEARL, "ender_pearl")
                            .addValue(Material.SPIDER_EYE, "spider_eye")
                            .addValue(Material.GHAST_TEAR, "ghast_tear")
                            .addValue(Material.MAGMA_CREAM, "magma_cream")
                            .addValue(Material.BLAZE_ROD, "blaze_rod")
                            .addValue(Material.PRISMARINE_SHARD, "prismarine_shard")
                            .addValue(Material.FIRE_CHARGE, "fire_charge")
                            .addValue(Material.LEATHER, "leather")
                            .addValue(Material.HONEYCOMB, "honeycomb")
                            .addValue(Material.PUFFERFISH, "pufferfish")
                            .addValue(Material.INK_SAC, "ink_sac")
                            .addValue(Material.NETHER_STAR, "nether_star")
                            .onInput(item::setType)
                            .start();
                })
                .buildButton();

        buttons[12] = Button.builder()
                .type(Material.TARGET)
                .name("&f&lSpawn Range")
                .addLine("&8The maximum range in which")
                .addLine("&8the mob will spawn.")
                .addEmptyLine()
                .addLine("&f&l{mob_spawn_pad_range} blocks")
                .addEmptyLine()
                .addLeftClickLine("To set the spawn range")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Double.class)
                            .description("Enter spawn range in chat")
                            .onInput(spawnRange -> {
                                tool.setSpawnRange(item, spawnRange);
                                view.getContext().addPlaceholder("mob_spawn_pad_range", spawnRange);
                            })
                            .start();
                })
                .buildButton();

        buttons[14] = Button.builder()
                .type(Material.REPEATER)
                .name("&f&lSpawn Delay")
                .addLine("&8The delay after which")
                .addLine("&8the mob will respawn.")
                .addEmptyLine()
                .addLine("&f&l{mob_spawn_pad_delay}")
                .addEmptyLine()
                .addLeftClickLine("To set the spawn delay")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Delay.class)
                            .description("Enter spawn delay in chat")
                            .onInput(spawnDelay -> {
                                tool.setSpawnDelay(item, spawnDelay);
                                view.getContext().addPlaceholder("mob_spawn_pad_delay", spawnDelay);
                            })
                            .start();
                })
                .buildButton();

        buttons[16] = Button.builder()
                .type(Material.TRIPWIRE_HOOK)
                .name("&f&lActivation Range")
                .addLine("&8The range a player needs to be")
                .addLine("&8in to trigger this spawn pad.")
                .addEmptyLine()
                .addLine("&f&l{mob_spawn_pad_activation_range} blocks")
                .addEmptyLine()
                .addLeftClickLine("To set the activation range")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Double.class)
                            .description("Enter the activation range in chat")
                            .onInput(activationRange -> {
                                tool.setActivationRange(item, activationRange);
                                view.getContext().addPlaceholder("mob_spawn_pad_activation_range", activationRange);
                            })
                            .start();
                })
                .buildButton();

        return buttons;
    }

}
