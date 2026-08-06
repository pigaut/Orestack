package io.github.pigaut.rpg.menu;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.menu.function.*;
import io.github.pigaut.rpg.menu.generator.*;
import io.github.pigaut.rpg.menu.item.*;
import io.github.pigaut.rpg.menu.message.*;
import io.github.pigaut.rpg.menu.particle.*;
import io.github.pigaut.rpg.menu.sound.*;
import io.github.pigaut.rpg.menu.structure.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.core.menu.template.button.*;
import io.github.pigaut.rpg.core.menu.template.menu.*;
import io.github.pigaut.rpg.menu.function.*;
import io.github.pigaut.rpg.menu.generator.*;
import io.github.pigaut.rpg.menu.item.*;
import io.github.pigaut.rpg.menu.message.*;
import io.github.pigaut.rpg.menu.particle.*;
import io.github.pigaut.rpg.menu.sound.*;
import io.github.pigaut.rpg.menu.structure.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class OrestackMenu extends FramedMenu {

    private final RpgMakerPlugin plugin;

    public OrestackMenu(RpgMakerPlugin plugin) {
        super("Orestack v" + plugin.getVersion(), 54);
        this.plugin = plugin;
    }

    @Override
    public Button[] createButtons(@NotNull Context context) {
        Button[] buttons = super.createButtons(context);

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
