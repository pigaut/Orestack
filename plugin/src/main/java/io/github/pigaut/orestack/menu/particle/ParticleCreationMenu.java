package io.github.pigaut.orestack.menu.particle;

import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;

public class ParticleCreationMenu extends FramedMenu {

    private final ConfigSection particleSection;
    private final boolean multiParticleButton;

    public ParticleCreationMenu(ConfigSection particleSection, boolean multiParticleButton) {
        super("Particle Creation", MenuSize.MEDIUM);
        this.particleSection = particleSection;
        this.multiParticleButton = multiParticleButton;
    }

    @Override
    public boolean backOnClose() {
        return true;
    }

    @Override
    public Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[10] = Button.builder()
                .withType(Material.CAMPFIRE)
                .withDisplay("Basic")
                .addLore("")
                .addLeftClickLore("Create a new basic particle")
                .enchanted(true)
                .onLeftClick((view, player) -> {
//                    final Menu chatMessageEditor = new ChatMessageEditor(messageSection);
//                    player.openMenu(chatMessageEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[11] = Button.builder()
                .withType(Material.REDSTONE)
                .withDisplay("Dust (colored)")
                .addLore("")
                .addLeftClickLore("Create a new dust particle")
                .enchanted(true)
                .onLeftClick((view, player) -> {
//                    final Menu chatMessageEditor = new ChatMessageEditor(messageSection);
//                    player.openMenu(chatMessageEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[12] = Button.builder()
                .withType(Material.ARROW)
                .withDisplay("Directional (moving)")
                .addLore("")
                .addLeftClickLore("Create a new directional particle")
                .enchanted(true)
                .onLeftClick((view, player) -> {
//                    final Menu chatMessageEditor = new ChatMessageEditor(messageSection);
//                    player.openMenu(chatMessageEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[13] = Button.builder()
                .withType(Material.POTION)
                .withDisplay("Spell (colored)")
                .addLore("")
                .addLeftClickLore("Create a new spell particle")
                .enchanted(true)
                .onLeftClick((view, player) -> {
//                    final Menu chatMessageEditor = new ChatMessageEditor(messageSection);
//                    player.openMenu(chatMessageEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[14] = Button.builder()
                .withType(Material.NOTE_BLOCK)
                .withDisplay("Note (colored)")
                .addLore("")
                .addLeftClickLore("Create a new note particle")
                .enchanted(true)
                .onLeftClick((view, player) -> {
//                    final Menu chatMessageEditor = new ChatMessageEditor(messageSection);
//                    player.openMenu(chatMessageEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[15] = Button.builder()
                .withType(Material.STONE)
                .withDisplay("Material")
                .addLore("")
                .addLeftClickLore("Create a new material particle")
                .enchanted(true)
                .onLeftClick((view, player) -> {
//                    final Menu chatMessageEditor = new ChatMessageEditor(messageSection);
//                    player.openMenu(chatMessageEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[16] = Button.builder()
                .withType(Material.GLOWSTONE_DUST)
                .withDisplay("Dust Transition (colored)")
                .addLore("")
                .addLeftClickLore("Create a new dust transition particle")
                .enchanted(true)
                .onLeftClick((view, player) -> {
//                    final Menu chatMessageEditor = new ChatMessageEditor(messageSection);
//                    player.openMenu(chatMessageEditor, view.getPreviousView());
                })
                .buildButton();

        return buttons;
    }

}
