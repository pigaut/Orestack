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
import io.github.pigaut.voxel.menu.fixed.*;
import io.github.pigaut.voxel.menu.template.button.*;
import org.bukkit.*;

public class OrestackMenu extends FixedMenu {

    private final OrestackPlugin plugin;

    public OrestackMenu(OrestackPlugin plugin) {
        super("Orestack v" + plugin.getVersion(), 54);
        this.plugin = plugin;
    }

    @Override
    public Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        ButtonLayout.apply(buttons, Buttons.GRAY_PANEL, 0, 1, 7, 8, 9, 17, 18,
                26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 51, 52, 53);

        buttons[11] = Button.builder()
                .withType(Material.ITEM_FRAME)
                .withDisplay("&a&lItems")
                .addLore("")
                .addLore("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.openMenu(new ItemGroupsMenu(plugin)))
                .buildButton();

        buttons[15] = Button.builder()
                .withType(Material.NAME_TAG)
                .withDisplay("&b&lMessages")
                .addLore("")
                .addLore("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.openMenu(new MessageGroupsMenu(plugin)))
                .buildButton();

        buttons[19] = Button.builder()
                .withType(Material.SCAFFOLDING)
                .withDisplay("&e&lStructures")
                .addLore("")
                .addLore("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.openMenu(new StructureGroupsMenu(plugin)))
                .buildButton();

        buttons[22] = Button.builder()
                .withType(Material.PISTON)
                .withDisplay("&6&lGenerators")
                .addLore("")
                .addLore("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.openMenu(new GeneratorGroupsMenu(plugin)))
                .buildButton();

        buttons[25] = Button.builder()
                .withType(Material.CAMPFIRE)
                .withDisplay("&d&lParticle Effects")
                .addLore("")
                .addLore("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.openMenu(new ParticleGroupsMenu(plugin)))
                .buildButton();

        buttons[29] = Button.builder()
                .withType(Material.ANVIL)
                .withDisplay("&7&lFunctions")
                .addLore("")
                .addLore("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.openMenu(new FunctionGroupsMenu(plugin)))
                .buildButton();

        buttons[33] = Button.builder()
                .withType(Material.JUKEBOX)
                .withDisplay("&3&lSound Effects")
                .addLore("")
                .addLore("&eLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.openMenu(new SoundGroupsMenu(plugin)))
                .buildButton();

        buttons[48] = Button.builder()
                .withType(Material.GOLDEN_PICKAXE)
                .withDisplay("&fWand")
                .enchanted(true)
                .onLeftClick((view, player, event) -> player.performCommand("orestack wand"))
                .buildButton();

        buttons[49] = new CloseMenuButton();
        buttons[50] = new PluginReloadButton(plugin);

        return buttons;
    }

}
