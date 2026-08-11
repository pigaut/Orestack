package io.github.pigaut.rpg.module.gate.tool;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class GateToolEditor extends FramedMenu {

    private final GateTool tool;
    private final ItemStack item;
    private final int itemSlot;

    public GateToolEditor(@NotNull GateTool tool, @NotNull ItemStack item, int itemSlot) {
        super("Gate Editor", MenuSize.MEDIUM);
        this.tool = tool;
        this.item = item.clone();
        this.itemSlot = itemSlot;
    }

    @Override
    public void onOpen(MenuView view) {
        Context context = view.getContext();
        context.addPlaceholder("gate_tool_template", tool.getGateTemplateName(item));
        context.addPlaceholder("gate_tool_rotation", tool.getRotation(item));
        context.addPlaceholder("gate_tool_per_player", tool.isPerPlayer(item) ? "Coming Soon" : "Coming Soon");
        context.addPlaceholder("gate_tool_offset_x", tool.getOffsetX(item));
        context.addPlaceholder("gate_tool_offset_y", tool.getOffsetY(item));
        context.addPlaceholder("gate_tool_offset_z", tool.getOffsetZ(item));
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
                            .addValue(Material.OAK_PLANKS, "oak_planks")
                            .addValue(Material.SPRUCE_PLANKS, "spruce_planks")
                            .addValue(Material.BIRCH_PLANKS, "birch_planks")
                            .addValue(Material.JUNGLE_PLANKS, "jungle_planks")
                            .addValue(Material.ACACIA_PLANKS, "acacia_planks")
                            .addValue(Material.DARK_OAK_PLANKS, "dark_oak_planks")
                            .addValue(MaterialUtil.getMaterialOrDefault("MANGROVE_PLANKS"), "mangrove_planks")
                            .addValue(MaterialUtil.getMaterialOrDefault("CHERRY_PLANKS"), "cherry_planks")
                            .addValue(MaterialUtil.getMaterialOrDefault("BAMBOO_PLANKS"), "bamboo_planks")
                            .addValue(Material.STONE_BRICKS, "stone_bricks")
                            .addValue(Material.MOSSY_STONE_BRICKS, "mossy_stone_bricks")
                            .addValue(Material.CRACKED_STONE_BRICKS, "cracked_stone_bricks")
                            .addValue(Material.CHISELED_STONE_BRICKS, "chiseled_stone_bricks")
                            .addValue(Material.BRICKS, "bricks")
                            .addValue(MaterialUtil.getMaterialOrDefault("MUD_BRICKS"), "mud_bricks")
                            .addValue(MaterialUtil.getMaterialOrDefault("DEEPSLATE_BRICKS"), "deepslate_bricks")
                            .addValue(MaterialUtil.getMaterialOrDefault("DEEPSLATE_TILES"), "deepslate_tiles")
                            .addValue(MaterialUtil.getMaterialOrDefault("POLISHED_DEEPSLATE"), "polished_deepslate")
                            .addValue(Material.POLISHED_GRANITE, "polished_granite")
                            .addValue(Material.POLISHED_DIORITE, "polished_diorite")
                            .addValue(Material.POLISHED_ANDESITE, "polished_andesite")
                            .addValue(Material.QUARTZ_BLOCK, "quartz_block")
                            .addValue(Material.SMOOTH_QUARTZ, "smooth_quartz")
                            .addValue(Material.PRISMARINE, "prismarine")
                            .addValue(Material.PRISMARINE_BRICKS, "prismarine_bricks")
                            .addValue(Material.DARK_PRISMARINE, "dark_prismarine")
                            .addValue(Material.NETHER_BRICKS, "nether_bricks")
                            .addValue(Material.RED_NETHER_BRICKS, "red_nether_bricks")
                            .addValue(Material.BLACKSTONE, "blackstone")
                            .addValue(Material.POLISHED_BLACKSTONE, "polished_blackstone")
                            .addValue(Material.POLISHED_BLACKSTONE_BRICKS, "polished_blackstone_bricks")
                            .addValue(Material.END_STONE_BRICKS, "end_stone_bricks")
                            .addValue(Material.PURPUR_BLOCK, "purpur_block")
                            .addValue(Material.WHITE_CONCRETE, "white_concrete")
                            .addValue(Material.WHITE_TERRACOTTA, "white_terracotta")
                            .addValue(Material.WHITE_WOOL, "white_wool")
                            .addValue(Material.SMOOTH_SANDSTONE, "smooth_sandstone")
                            .addValue(Material.SMOOTH_RED_SANDSTONE, "smooth_red_sandstone")
                            .onInput(item::setType)
                            .start();
                })
                .buildButton();

        buttons[11] = Button.builder()
                .type(Material.COMPASS)
                .name("&f&lRotation")
                .addLine("&8The rotation applied when")
                .addLine("&8this gate is placed.")
                .addEmptyLine()
                .addLine("&f&l{gate_tool_rotation_tc}")
                .addEmptyLine()
                .addLeftClickLine("To cycle the rotation")
                .onLeftClick((view, playerState) -> {
                    String nextRotation = tool.getNextRotation(item);
                    tool.setRotation(item, nextRotation);
                    view.getContext().addPlaceholder("gate_tool_rotation", nextRotation);
                    view.update();
                })
                .buildButton();

        buttons[12] = Button.builder()
                .type(tool.isPerPlayer(item) ? Material.LIME_DYE : Material.GRAY_DYE)
                .name("&f&lPer-Player")
                .addLine("&8Whether this gate")
                .addLine("&8spawns individually per player.")
                .addEmptyLine()
                .addLine("&f&l{gate_tool_per_player}")
                .addEmptyLine()
                .addLeftClickLine("To toggle per-player")
                .onLeftClick((view, playerState) -> {
                    boolean perPlayer = !tool.isPerPlayer(item);
                    tool.setPerPlayer(item, perPlayer);
                    view.getContext().addPlaceholder("gate_tool_per_player", perPlayer ? "yes" : "no");
                    view.update();
                })
                .buildButton();

        buttons[14] = Button.builder()
                .type(Material.RED_DYE)
                .name("&f&lOffset X")
                .addLine("&8The X offset applied when")
                .addLine("&8this gate is placed.")
                .addEmptyLine()
                .addLine("&f&l{gate_tool_offset_x}")
                .addEmptyLine()
                .addLeftClickLine("To set the X offset")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Double.class)
                            .description("Enter x-offset in chat")
                            .onInput(offsetX -> {
                                tool.setOffset(item, offsetX, tool.getOffsetY(item), tool.getOffsetZ(item));
                                view.getContext().addPlaceholder("gate_tool_offset_x", offsetX);
                            })
                            .start();
                })
                .buildButton();

        buttons[15] = Button.builder()
                .type(Material.GREEN_DYE)
                .name("&f&lOffset Y")
                .addLine("&8The Y offset applied when")
                .addLine("&8this gate is placed.")
                .addEmptyLine()
                .addLine("&f&l{gate_tool_offset_y}")
                .addEmptyLine()
                .addLeftClickLine("To set the Y offset")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Double.class)
                            .description("Enter y-offset in chat")
                            .onInput(offsetY -> {
                                tool.setOffset(item, tool.getOffsetX(item), offsetY, tool.getOffsetZ(item));
                                view.getContext().addPlaceholder("gate_tool_offset_y", offsetY);
                            })
                            .start();
                })
                .buildButton();

        buttons[16] = Button.builder()
                .type(Material.BLUE_DYE)
                .name("&f&lOffset Z")
                .addLine("&8The Z offset applied when")
                .addLine("&8this gate is placed.")
                .addEmptyLine()
                .addLine("&f&l{gate_tool_offset_z}")
                .addEmptyLine()
                .addLeftClickLine("To set the Z offset")
                .onLeftClick((view, playerState) -> {
                    playerState.collectChatInput(Double.class)
                            .description("Enter z-offset in chat")
                            .onInput(offsetZ -> {
                                tool.setOffset(item, tool.getOffsetX(item), tool.getOffsetY(item), offsetZ);
                                view.getContext().addPlaceholder("gate_tool_offset_z", offsetZ);
                            })
                            .start();
                })
                .buildButton();

        return buttons;
    }

}