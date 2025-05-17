package io.github.pigaut.orestack.menu;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.menu.generator.*;
import io.github.pigaut.orestack.menu.item.*;
import io.github.pigaut.orestack.menu.structure.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.fixed.*;
import org.bukkit.*;

public class OrestackMenu extends FixedMenu {

    private final GeneratorGroupsMenu generatorGroupsMenu;
    private final ItemGroupsMenu itemGroupsMenu;
    private final StructureGroupsMenu structureGroupsMenu;

    public OrestackMenu(OrestackPlugin plugin) {
        super("Orestack v" + plugin.getVersion(), 45);
        this.generatorGroupsMenu = new GeneratorGroupsMenu(plugin);
        this.itemGroupsMenu = new ItemGroupsMenu(plugin);
        this.structureGroupsMenu = new StructureGroupsMenu(plugin);

        this.setButtons(Buttons.GRAY_PANEL, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 28, 34, 35, 36, 37, 38, 42, 43, 44);

        buttons[11] = Button.builder()
                .withType(Material.ITEM_FRAME)
                .withDisplay("&a&lItems")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(itemGroupsMenu))
                .buildButton();

        buttons[15] = Button.builder()
                .withType(Material.NAME_TAG)
                .withDisplay("&b&lMessages")
                .addLore("&fLeft-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(plugin.getMenu("generator_menu")))
                .buildButton();

        buttons[19] = Button.builder()
                .withType(Material.SCAFFOLDING)
                .withDisplay("&e&lStructures")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(structureGroupsMenu))
                .buildButton();

        buttons[22] = Button.builder()
                .withType(Material.FURNACE_MINECART)
                .withDisplay("&6&lGenerators")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(generatorGroupsMenu))
                .buildButton();

        buttons[25] = Button.builder()
                .withType(Material.CAMPFIRE)
                .withDisplay("&d&lParticle Effects")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(plugin.getMenu("generator_menu")))
                .buildButton();

        buttons[29] = Button.builder()
                .withType(Material.ANVIL)
                .withDisplay("&8&lFunctions")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(plugin.getMenu("generator_menu")))
                .buildButton();

        buttons[33] = Button.builder()
                .withType(Material.JUKEBOX)
                .withDisplay("&3&lSound Effects")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(plugin.getMenu("generator_menu")))
                .buildButton();

        buttons[39] = Button.builder()
                .withType(Material.GOLDEN_PICKAXE)
                .withDisplay("&f&lWand")
                .addLore("&7Left-Click: &fGet a wand")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().performCommand("orestack wand"))
                .buildButton();

        buttons[40] = Button.builder()
                .withType(Material.BARRIER)
                .withDisplay("&c&lClose")
                .addLore("&7Left-Click: &fClose this menu")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().closeInventory())
                .buildButton();

        buttons[41] = Button.builder()
                .withType(Material.OAK_BUTTON)
                .withDisplay("&f&lReload")
                .addLore("&7Left-Click: &fReload the plugin")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().performCommand("orestack reload"))
                .buildButton();

    }

}
