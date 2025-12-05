package io.github.pigaut.orestack.menu.message;

import io.github.pigaut.orestack.menu.message.editor.*;
import io.github.pigaut.voxel.menu.*;
import io.github.pigaut.voxel.menu.button.*;
import io.github.pigaut.voxel.menu.template.button.*;
import io.github.pigaut.voxel.menu.template.menu.*;
import io.github.pigaut.yaml.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class MessageCreationMenu extends FramedMenu {

    private final ConfigSection messageSection;
    private final boolean multiMessageButton;

    public MessageCreationMenu(ConfigSection messageSection, boolean multiMessageButton) {
        super("Message Creation", MenuSize.SMALL);
        this.messageSection = messageSection;
        this.multiMessageButton = multiMessageButton;
    }

    @Override
    public Button getFrameButton() {
        return Buttons.LIGHT_BLUE_PANEL;
    }

    @Override
    public boolean backOnClose() {
        return true;
    }

    @Override
    public @Nullable Button[] createButtons() {
        final Button[] buttons = super.createButtons();

        buttons[10] = Button.builder()
                .withType(Material.BOOK)
                .enchanted(true)
                .withDisplay("Chat")
                .addLore("")
                .addLeftClickLore("To create a new chat message")
                .onLeftClick((view, player) -> {
                    final Menu chatMessageEditor = new ChatMessageEditor(messageSection);
                    player.openMenu(chatMessageEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[11] = Button.builder()
                .withType(Material.MAP)
                .enchanted(true)
                .withDisplay("Action Bar")
                .addLore("")
                .addLeftClickLore("To create a new action bar message")
                .onLeftClick((view, player) -> {
                    final Menu actionbarEditor = new ActionbarEditor(messageSection);
                    player.openMenu(actionbarEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[12] = Button.builder()
                .withType(Material.NAME_TAG)
                .enchanted(true)
                .withDisplay("Title")
                .addLore("")
                .addLeftClickLore("To create a new title message")
                .onLeftClick((view, player) -> {
                    final Menu titleEditor = new TitleEditor(messageSection);
                    player.openMenu(titleEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[14] = Button.builder()
                .withType(Material.DRAGON_HEAD)
                .enchanted(true)
                .withDisplay("Boss Bar")
                .addLore("")
                .addLeftClickLore("To create a new boss bar message")
                .onLeftClick((view, player) -> {
                    final Menu bossbarEditor = new BossbarEditor(messageSection);
                    player.openMenu(bossbarEditor, view.getPreviousView());
                })
                .buildButton();

        buttons[15] = Button.builder()
                .withType(Material.BEACON)
                .enchanted(true)
                .withDisplay("Hologram")
                .addLore("&c&o[!] Requires DecentHolograms")
                .addLore("")
                .addLeftClickLore("To create a new hologram message")
                .onLeftClick((view, player) -> {
                    final Menu hologramMessageEditor = new HologramMessageEditor(messageSection);
                    player.openMenu(hologramMessageEditor, view.getPreviousView());
                })
                .buildButton();

        if (multiMessageButton) {
            buttons[16] = Button.builder()
                    .withType(Material.BOOKSHELF)
                    .enchanted(true)
                    .withDisplay("Multi Message")
                    .addLore("")
                    .addLeftClickLore("To create a new multi message")
                    .onLeftClick((view, player) -> {
                        final Menu multiMessageEditor = new MultiMessageEditor(messageSection.convertToSequence());
                        player.openMenu(multiMessageEditor, view.getPreviousView());
                    })
                    .buildButton();
        }

        return buttons;
    }
}
