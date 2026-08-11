package io.github.pigaut.rpg.module.generator.tool;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GeneratorToolEditor extends FramedMenu {

    private final GeneratorTool tool;
    private final ItemStack item;
    private final int itemSlot;

    public GeneratorToolEditor(@NotNull GeneratorTool tool, @NotNull ItemStack item, int itemSlot) {
        super("Generator Editor", MenuSize.MEDIUM);
        this.tool = tool;
        this.item = item.clone();
        this.itemSlot = itemSlot;
    }

    @Override
    public void onOpen(MenuView view) {
        Context context = view.getContext();
        context.addPlaceholder("generator_tool_template", tool.getGeneratorTemplateName(item));
        context.addPlaceholder("generator_tool_rotation", tool.getRotation(item));
        context.addPlaceholder("generator_tool_per_player", tool.isPerPlayer(item) ? "yes" : "no");
        context.addPlaceholder("generator_tool_offset_x", tool.getOffsetX(item));
        context.addPlaceholder("generator_tool_offset_y", tool.getOffsetY(item));
        context.addPlaceholder("generator_tool_offset_z", tool.getOffsetZ(item));
    }

    @Override
    public void onClose(MenuView view) {
        Player player = view.getViewer().asPlayer();
        player.getInventory().setItem(itemSlot, item);
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
                            .addValue(Material.COBBLESTONE, "cobblestone")
                            .addValue(Material.OAK_LOG, "oak_log")
                            .addValue(Material.SPRUCE_LOG, "spruce_log")
                            .addValue(Material.BIRCH_LOG, "birch_log")
                            .addValue(Material.JUNGLE_LOG, "jungle_log")
                            .addValue(Material.ACACIA_LOG, "acacia_log")
                            .addValue(Material.DARK_OAK_LOG, "dark_oak_log")
                            .addValue(MaterialUtil.getMaterialOrDefault("MANGROVE_LOG"), "mangrove_log")
                            .addValue(MaterialUtil.getMaterialOrDefault("CHERRY_LOG"), "cherry_log")
                            .addValue(Material.STONE, "stone")
                            .addValue(Material.IRON_ORE, "iron_ore")
                            .addValue(Material.GOLD_ORE, "gold_ore")
                            .addValue(Material.COAL_ORE, "coal_ore")
                            .addValue(Material.DIAMOND_ORE, "diamond_ore")
                            .addValue(Material.EMERALD_ORE, "emerald_ore")
                            .addValue(Material.LAPIS_ORE, "lapis_ore")
                            .addValue(Material.REDSTONE_ORE, "redstone_ore")
                            .addValue(MaterialUtil.getMaterialOrDefault("COPPER_ORE"), "copper_ore")
                            .addValue(Material.NETHER_QUARTZ_ORE, "nether_quartz_ore")
                            .addValue(Material.NETHER_GOLD_ORE, "nether_gold_ore")
                            .addValue(Material.ANCIENT_DEBRIS, "ancient_debris")
                            .addValue(MaterialUtil.getMaterialOrDefault("DEEPSLATE"), "deepslate")
                            .addValue(Material.CLAY, "clay")
                            .addValue(Material.DIRT, "dirt")
                            .addValue(Material.OBSIDIAN, "obsidian")
                            .addValue(Material.NETHERRACK, "netherrack")
                            .addValue(Material.BASALT, "basalt")
                            .addValue(Material.BLACKSTONE, "blackstone")
                            .addValue(Material.END_STONE, "end_stone")
                            .onInput(item::setType)
                            .start();
                })
                .buildButton();

        buttons[11] = Button.builder()
                .type(Material.COMPASS)
                .name("&f&lRotation")
                .addLine("&8The rotation applied when")
                .addLine("&8this generator is placed.")
                .addEmptyLine()
                .addLine("&f&l{generator_tool_rotation_tc}")
                .addEmptyLine()
                .addLeftClickLine("To cycle the rotation")
                .onLeftClick((view, playerState) -> {
                    String nextRotation = tool.getNextRotation(item);
                    tool.setRotation(item, nextRotation);
                    view.getContext().addPlaceholder("generator_tool_rotation", nextRotation);
                    view.update();
                })
                .buildButton();

        buttons[12] = Button.builder()
                .type(tool.isPerPlayer(item) ? Material.LIME_DYE : Material.GRAY_DYE)
                .name("&f&lPer-Player")
                .addLine("&8Whether this generator")
                .addLine("&8spawns individually per player.")
                .addEmptyLine()
                .addLine("&f&l{generator_tool_per_player}")
                .addEmptyLine()
                .addLeftClickLine("To toggle per-player")
                .onLeftClick((view, playerState) -> {
                    boolean perPlayer = !tool.isPerPlayer(item);
                    tool.setPerPlayer(item, perPlayer);
                    view.getContext().addPlaceholder("generator_tool_per_player", perPlayer ? "yes" : "no");
                    view.update();
                })
                .buildButton();

        buttons[14] = Button.builder()
                .type(Material.RED_DYE)
                .name("&f&lOffset X")
                .addLine("&8The X offset applied when")
                .addLine("&8this generator is placed.")
                .addEmptyLine()
                .addLine("&f&l{generator_tool_offset_x}")
                .addEmptyLine()
                .addLeftClickLine("To set the X offset")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Double.class)
                            .description("Enter x-offset in chat")
                            .onInput(offsetX -> {
                                tool.setOffset(item, offsetX, tool.getOffsetY(item), tool.getOffsetZ(item));
                                view.getContext().addPlaceholder("generator_tool_offset_x", offsetX);
                            })
                            .start();
                })
                .buildButton();

        buttons[15] = Button.builder()
                .type(Material.GREEN_DYE)
                .name("&f&lOffset Y")
                .addLine("&8The Y offset applied when")
                .addLine("&8this generator is placed.")
                .addEmptyLine()
                .addLine("&f&l{generator_tool_offset_y}")
                .addEmptyLine()
                .addLeftClickLine("To set the Y offset")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Double.class)
                            .description("Enter y-offset in chat")
                            .onInput(offsetY -> {
                                tool.setOffset(item, tool.getOffsetX(item), offsetY, tool.getOffsetZ(item));
                                view.getContext().addPlaceholder("generator_tool_offset_y", offsetY);
                            })
                            .start();
                })
                .buildButton();

        buttons[16] = Button.builder()
                .type(Material.BLUE_DYE)
                .name("&f&lOffset Z")
                .addLine("&8The Z offset applied when")
                .addLine("&8this generator is placed.")
                .addEmptyLine()
                .addLine("&f&l{generator_tool_offset_z}")
                .addEmptyLine()
                .addLeftClickLine("To set the Z offset")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Double.class)
                            .description("Enter z-offset in chat")
                            .onInput(offsetZ -> {
                                tool.setOffset(item, tool.getOffsetX(item), tool.getOffsetY(item), offsetZ);
                                view.getContext().addPlaceholder("generator_tool_offset_z", offsetZ);
                            })
                            .start();
                })
                .buildButton();

        return buttons;
    }

}