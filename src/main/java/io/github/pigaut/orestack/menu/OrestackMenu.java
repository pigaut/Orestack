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
import org.bukkit.*;

public class OrestackMenu extends FixedMenu {

    private final GeneratorGroupsMenu generatorGroupsMenu;
    private final ItemGroupsMenu itemGroupsMenu;
    private final StructureGroupsMenu structureGroupsMenu;
    private final MessageGroupsMenu messageGroupsMenu;
    private final ParticleGroupsMenu particleGroupsMenu;
    private final SoundGroupsMenu soundGroupsMenu;
    private final FunctionGroupsMenu functionGroupsMenu;

    public OrestackMenu(OrestackPlugin plugin) {
        super("Orestack v" + plugin.getVersion(), 54);
        this.generatorGroupsMenu = new GeneratorGroupsMenu(plugin);
        this.itemGroupsMenu = new ItemGroupsMenu(plugin);
        this.structureGroupsMenu = new StructureGroupsMenu(plugin);
        this.messageGroupsMenu = new MessageGroupsMenu(plugin);
        this.particleGroupsMenu = new ParticleGroupsMenu(plugin);
        this.soundGroupsMenu = new SoundGroupsMenu(plugin);
        this.functionGroupsMenu = new FunctionGroupsMenu(plugin);

        this.setButtons(Buttons.GRAY_PANEL,
                0, 1, 7, 8,
                9, 17,
                18, 26,
                27, 35,
                36, 37, 43, 44,
                45, 46, 47, 51, 52, 53);

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
                .onLeftClick((view, event) -> view.getViewer().openMenu(messageGroupsMenu))
                .buildButton();

        buttons[19] = Button.builder()
                .withType(Material.SCAFFOLDING)
                .withDisplay("&e&lStructures")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(structureGroupsMenu))
                .buildButton();

        buttons[22] = Button.builder()
                .withType(Material.PISTON)
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
                .onLeftClick((view, event) -> view.getViewer().openMenu(particleGroupsMenu))
                .buildButton();

        buttons[29] = Button.builder()
                .withType(Material.ANVIL)
                .withDisplay("&8&lFunctions")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(functionGroupsMenu))
                .buildButton();

        buttons[33] = Button.builder()
                .withType(Material.JUKEBOX)
                .withDisplay("&3&lSound Effects")
                .addLore("&7Left-Click: &fView all groups")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().openMenu(soundGroupsMenu))
                .buildButton();

        buttons[48] = Button.builder()
                .withType(Material.GOLDEN_PICKAXE)
                .withDisplay("&f&lWand")
                .addLore("&7Left-Click: &fGet a wand")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().performCommand("orestack wand"))
                .buildButton();

        buttons[49] = Button.builder()
                .withType(Material.BARRIER)
                .withDisplay("&c&lClose")
                .addLore("&7Left-Click: &fClose this menu")
                .enchanted(true)
                .onLeftClick((view, event) -> view.getViewer().closeInventory())
                .buildButton();

        buttons[50] = Button.builder()
                .withType(Material.OAK_BUTTON)
                .withDisplay("&f&lReload")
                .addLore("&7Left-Click: &fReload the plugin")
                .enchanted(true)
                .onLeftClick((view, event) -> {
                    view.close();
                    view.getViewer().performCommand("orestack reload");
                })
                .buildButton();

    }

}
