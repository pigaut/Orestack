package io.github.pigaut.rpg.module.menu.config;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.entries.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.menu.*;
import io.github.pigaut.rpg.core.menu.button.*;
import io.github.pigaut.rpg.module.menu.button.*;
import io.github.pigaut.rpg.module.menu.entries.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.convert.format.*;
import org.bukkit.event.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MenuLoader implements ConfigLoader<Menu> {

    private final EnhancedPlugin plugin;

    public MenuLoader(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getErrorDescription() {
        return "invalid menu";
    }

    @Override
    public @NotNull Menu loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String menuName = scalar.toString();

        Menu menu = plugin.getMenu(menuName);
        if (menu == null) {
            throw new InvalidConfigException(scalar, "Could not find menu with name: " + menuName);
        }

        return menu;
    }

    @Override
    public @NotNull Menu loadFromSection(@NotNull ConfigSection section) throws InvalidConfigException {
        ConfigRoot root = section.getRoot();
        String type = section.getString("type", CaseStyle.SNAKE)
                .withDefault("fixed");

        String title = section.getRequiredString("title");
        int size = section.getInteger("rows")
                .mapIfValid(rows -> rows * 9)
                .requireOrThrow(InventoryUtil::isValidChestSize, "Chest rows must be a value between 1-6");
        boolean keepOpen = section.getBoolean("keep-open").withDefault(false);
        boolean backtrack = section.getBoolean("backtrack").withDefault(false);

        int rows = size / 9;
        int columns = 9;
        String[][] buttonLayout = section.getStringMatrix("layout", rows, columns);
        ButtonMap buttonsByName = root.getRequired(ButtonMap.class);

        ButtonTemplate[] buttonTemplates = new ButtonTemplate[size];
        List<Integer> entrySlots = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int index = (row * columns) + column;
                String buttonName = buttonLayout[row][column];

                if (buttonName.equalsIgnoreCase("_")) {
                    buttonTemplates[index] = null;
                    continue;
                }

                if (buttonName.equalsIgnoreCase("?")) {
                    if (!type.equals("paged")) {
                        throw new InvalidConfigException(section, "layout", "Only paged menus support entry button: ?");
                    }

                    entrySlots.add(index);
                    continue;
                }

                ButtonTemplate button;
                if (buttonsByName.contains(buttonName)) {
                    button = buttonsByName.get(buttonName);
                } else {
                    button = plugin.getButton(buttonName);
                }

                if (button == null) {
                    throw new InvalidConfigException(section, "layout", "Could not find button with id: " + buttonName);
                }

                buttonTemplates[index] = button;
            }
        }

        String name = root.getName();
        String group = Group.byMenuFile(root.getFile());

        if (type.equals("fixed")) {
            return MenuBuilder.fixed(name, group)
                    .title(title)
                    .size(size)
                    .keepOpen(keepOpen)
                    .backtrack(backtrack)
                    .createButtons(context -> {
                        Button[] buttons = new Button[size];
                        for (int i = 0; i < size; i++) {
                            ButtonTemplate buttonTemplate = buttonTemplates[i];
                            if (buttonTemplate != null) {
                                buttons[i] = buttonTemplate.createButton(context);
                            }
                        }
                        return buttons;
                    })
                    .build();
        }

        if (type.equals("paged")) {
            MenuEntries entries = (context, buttonTemplate) -> new ArrayList<>();
            return MenuBuilder.paged(name, group)
                    .title(title)
                    .size(size)
                    .keepOpen(keepOpen)
                    .backtrack(backtrack)
                    .createButtons(context -> {
                        Button[] buttons = new Button[size];
                        for (int i = 0; i < size; i++) {
                            ButtonTemplate buttonTemplate = buttonTemplates[i];
                            if (buttonTemplate != null) {
                                buttons[i] = buttonTemplate.createButton(context);
                            }
                        }
                        return buttons;
                    })
                    .entries(entries)
                    .build();
        }

        if (type.equals("atlas")) {
            int minAtlasWidth = InventoryUtil.getInventoryLength(InventoryType.CHEST, size);
            int minAtlasHeight = InventoryUtil.getInventoryHeight(InventoryType.CHEST, size);

            int atlasWidth = Math.max(section.getRequiredInteger("atlas.width"), minAtlasWidth);
            int atlasHeight = Math.max(section.getRequiredInteger("atlas.height"), minAtlasHeight);

            String[][] atlasLayout = section.getStringMatrix("atlas.layout", atlasHeight, atlasWidth);
            ButtonTemplate[][] atlasTemplates = new ButtonTemplate[atlasWidth][atlasHeight];

            for (int row = 0; row < atlasHeight; row++) {
                for (int col = 0; col < atlasWidth; col++) {
                    String buttonName = atlasLayout[row][col];
                    if (buttonName.equalsIgnoreCase("_")) {
                        continue;
                    }

                    ButtonTemplate button;
                    if (buttonsByName.contains(buttonName)) {
                        button = buttonsByName.get(buttonName);
                    } else {
                        button = plugin.getButton(buttonName);
                    }

                    if (button == null) {
                        throw new InvalidConfigException(section, "atlas.layout", "Could not find button with id: " + buttonName);
                    }

                    atlasTemplates[col][row] = button;
                }
            }

            return MenuBuilder.atlas(name, group)
                    .title(title)
                    .size(size)
                    .keepOpen(keepOpen)
                    .backtrack(backtrack)
                    .atlasWidth(atlasWidth)
                    .atlasHeight(atlasHeight)
                    .createButtons(context -> {
                        Button[] buttons = new Button[size];
                        for (int i = 0; i < size; i++) {
                            ButtonTemplate buttonTemplate = buttonTemplates[i];
                            if (buttonTemplate != null) {
                                buttons[i] = buttonTemplate.createButton(context);
                            }
                        }
                        return buttons;
                    })
                    .createAtlas(context -> {
                        Button[][] atlas = new Button[atlasWidth][atlasHeight];
                        for (int col = 0; col < atlasWidth; col++) {
                            for (int row = 0; row < atlasHeight; row++) {
                                ButtonTemplate buttonTemplate = atlasTemplates[col][row];
                                if (buttonTemplate != null) {
                                    atlas[col][row] = buttonTemplate.createButton(context);
                                }
                            }
                        }
                        return atlas;
                    })
                    .build();

        }

        throw new InvalidConfigException(section, "type", "Unknown menu type: " + type);
    }

    @Override
    public @NotNull Menu loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        return loadFromSection(sequence.getRequiredSection(0));
    }

}
