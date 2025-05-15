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

        final Button panel = Button.builder()
                .withType(Material.GRAY_STAINED_GLASS_PANE)
                .buildButton();

        this.setButtons(panel, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 28, 34, 35, 36, 37, 38, 42, 43, 44);

        buttons[11] = Button.builder()
                .withType(Material.ITEM_FRAME)
                .withDisplay("Items")
                .addLore("Click to view all items")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(itemGroupsMenu))
                .buildButton();

        buttons[15] = Button.builder()
                .withType(Material.NAME_TAG)
                .withDisplay("Messages")
                .addLore("Click to view all messages")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(plugin.getMenu("generator_menu")))
                .buildButton();

        buttons[19] = Button.builder()
                .withType(Material.SCAFFOLDING)
                .withDisplay("Structures")
                .addLore("Click to view all structures")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(structureGroupsMenu))
                .buildButton();

        buttons[22] = Button.builder()
                .withType(Material.FURNACE_MINECART)
                .withDisplay("Generator Templates")
                .addLore("Click to view all generator templates")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(generatorGroupsMenu))
                .buildButton();

        buttons[25] = Button.builder()
                .withType(Material.CAMPFIRE)
                .withDisplay("Particles")
                .addLore("Click to view all particle effects")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(plugin.getMenu("generator_menu")))
                .buildButton();

        buttons[29] = Button.builder()
                .withType(Material.ANVIL)
                .withDisplay("Functions")
                .addLore("Click to view all functions")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(plugin.getMenu("generator_menu")))
                .buildButton();

        buttons[33] = Button.builder()
                .withType(Material.JUKEBOX)
                .withDisplay("Sounds")
                .addLore("Click to view all sound effects")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(plugin.getMenu("generator_menu")))
                .buildButton();

        buttons[39] = Button.builder()
                .withType(Material.GOLDEN_PICKAXE)
                .withDisplay("Wand")
                .addLore("Click to get a wand")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().performCommand("orestack wand"))
                .buildButton();

        buttons[40] = Button.builder()
                .withType(Material.BARRIER)
                .withDisplay("Close")
                .addLore("Click to close")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().closeInventory())
                .buildButton();

        buttons[41] = Button.builder()
                .withType(Material.OAK_BUTTON)
                .withDisplay("Reload")
                .addLore("Click to reload the plugin")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().performCommand("orestack reload"))
                .buildButton();

    }

}
