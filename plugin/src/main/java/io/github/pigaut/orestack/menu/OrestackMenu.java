package io.github.pigaut.orestack.menu;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.function.*;
import io.github.pigaut.orestack.menu.generator.*;
import io.github.pigaut.orestack.menu.item.*;
import io.github.pigaut.orestack.menu.message.*;
import io.github.pigaut.orestack.menu.particle.*;
import io.github.pigaut.orestack.menu.sound.*;
import io.github.pigaut.orestack.menu.structure.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import org.bukkit.*;

public class OrestackMenu extends FramedMenu {

    private final OrestackPlugin plugin;

    public OrestackMenu(OrestackPlugin plugin) {
        super("Orestack v" + plugin.getVersion(), 54);
        this.plugin = plugin;
    }

    @Override
    public Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[11] = Button.builder()
                .type(Material.ITEM_FRAME)
                .name("&a&lItems")
                .addEmptyLine()
                .addLine("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player) -> player.openMenu(new ItemGroupsMenu(plugin)))
                .buildButton();

        buttons[15] = Button.builder()
                .type(Material.NAME_TAG)
                .name("&b&lMessages")
                .addEmptyLine()
                .addLine("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player) -> player.openMenu(new MessageGroupsMenu(plugin)))
                .buildButton();

        buttons[19] = Button.builder()
                .type(Material.SCAFFOLDING)
                .name("&e&lStructures")
                .addEmptyLine()
                .addLine("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player) -> player.openMenu(new StructureGroupsMenu(plugin)))
                .buildButton();

        buttons[22] = Button.builder()
                .type(Material.PISTON)
                .name("&6&lGenerators")
                .addEmptyLine()
                .addLine("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player) -> player.openMenu(new GeneratorGroupsMenu(plugin)))
                .buildButton();

        buttons[25] = Button.builder()
                .type(Material.CAMPFIRE)
                .name("&d&lParticle Effects")
                .addEmptyLine()
                .addLine("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player) -> player.openMenu(new ParticleGroupsMenu(plugin)))
                .buildButton();

        buttons[29] = Button.builder()
                .type(Material.ANVIL)
                .name("&7&lFunctions")
                .addEmptyLine()
                .addLine("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player) -> player.openMenu(new FunctionGroupsMenu(plugin)))
                .buildButton();

        buttons[33] = Button.builder()
                .type(Material.JUKEBOX)
                .name("&3&lSound Effects")
                .addEmptyLine()
                .addLine("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player) -> player.openMenu(new SoundGroupsMenu(plugin)))
                .buildButton();

        buttons[48] = Button.builder()
                .type(Material.GOLDEN_PICKAXE)
                .name("&fWand")
                .enchanted(true)
                .onLeftClick((view, player) -> player.performCommand("orestack wand"))
                .buildButton();

        buttons[50] = new PluginReloadButton(plugin);

        buttons[53] = new GuiReopenDelayButton(plugin);

        return buttons;
    }

    @Override
    public Button getToolbarButton5() {
        return Buttons.CLOSE;
    }

}
