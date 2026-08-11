package io.github.pigaut.rpg.module.generator.tool;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.item.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.exception.*;
import io.github.pigaut.rpg.module.generator.global.*;
import io.github.pigaut.rpg.module.generator.instanced.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class GeneratorTool extends Tool {

    private final RpgMakerPlugin plugin;

    private final NamespacedKey GENERATOR_TEMPLATE_KEY;
    private final NamespacedKey GENERATOR_ROTATION_KEY;
    private final NamespacedKey GENERATOR_PER_PLAYER_KEY;
    private final NamespacedKey GENERATOR_OFFSET_X_KEY;
    private final NamespacedKey GENERATOR_OFFSET_Y_KEY;
    private final NamespacedKey GENERATOR_OFFSET_Z_KEY;

    public GeneratorTool(RpgMakerPlugin plugin) {
        super(plugin, "generator");
        this.plugin = plugin;

        GENERATOR_TEMPLATE_KEY  = plugin.getNamespacedKey("generator_template");
        GENERATOR_ROTATION_KEY  = plugin.getNamespacedKey("generator_rotation");
        GENERATOR_PER_PLAYER_KEY = plugin.getNamespacedKey("generator_per_player");
        GENERATOR_OFFSET_X_KEY  = plugin.getNamespacedKey("generator_offset_x");
        GENERATOR_OFFSET_Y_KEY  = plugin.getNamespacedKey("generator_offset_y");
        GENERATOR_OFFSET_Z_KEY  = plugin.getNamespacedKey("generator_offset_z");

        setItemTemplate(new ItemStack(Material.TERRACOTTA));
        setItemMetaTemplate(meta -> MetaEditor.of(meta)
                .withAllFlags()
                .withName("&b&l{generator_tool_template_tc} Generator &f&l({generator_tool_rotation_uc})")
                .addLine("&8Use this tool to place and")
                .addLine("&8remove generators in your worlds.")
                .addEmptyLine()
                .addLine("&8&m                                   ")
                .addLine("&aRotation: &f{generator_tool_rotation_tc}")
                .addLine("&aPer-Player: &f{generator_tool_per_player}")
                .addLine("&aOffset: &f{generator_tool_offset_x}, {generator_tool_offset_y}, {generator_tool_offset_z}")
                .addLine("&8&m                                   ")
                .addEmptyLine()
                .addLine("&cLeft-Click: &fTo remove generator")
                .addLine("&eRight-Click: &fTo place generator")
                .addLine("&6Press Q: &fTo cycle rotation")
                .addEmptyLine()
                .addLine("&dPress F: &fTo change settings")
        );

        onLeftClickBlock(event -> {
            Player player = event.getPlayer();
            Location location = event.getClickedBlock().getLocation();

            Generator generator = plugin.getGenerator(player, location);
            GeneratorTemplate heldTemplate = getGeneratorTemplate(event.getItem());
            if (generator == null || !generator.getTemplate().equals(heldTemplate)) {
                return;
            }

            Context context = Context.fromPlayer(plugin, player).with(Generator.class, generator);
            if (!player.hasPermission("orestack.generator.break")) {
                plugin.sendMessage(player, context, "cannot-break-generator");
                return;
            }

            generator.remove();
            PlayerUtil.sendActionBar(player, plugin.getTranslation("broke-generator"));
        });

        onRightClickBlock(event -> {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            Location location = event.getClickedBlock().getLocation();

            double offsetX = getOffsetX(item);
            double offsetY = getOffsetY(item);
            double offsetZ = getOffsetZ(item);
            location.add(offsetX, offsetY, offsetZ);

            if (plugin.getGenerators().isGenerator(location)) {
                plugin.sendMessage(player, Context.fromPlayer(plugin, player), "generator-occupied-block");
                return;
            }

            GeneratorTemplate generatorTemplate = getGeneratorTemplate(item);
            if (generatorTemplate == null) {
                plugin.sendMessage(player, Context.fromPlayer(plugin, player), "generator-not-exists");
                return;
            }

            Context context = Context.fromPlayer(plugin, player).with(GeneratorTemplate.class, generatorTemplate);
            if (!player.hasPermission("rpg-maker.generator.place")) {
                plugin.sendMessage(player, context, "cannot-place-generator");
                return;
            }

            Rotation rotation = getRotation(item);
            GeneratorPlaceEvent generatorPlaceEvent = new GeneratorPlaceEvent(
                    player, location, generatorTemplate.getName(),
                    generatorTemplate.getOccupiedBlocks(location, rotation));
            Server.callEvent(generatorPlaceEvent);

            if (generatorPlaceEvent.isCancelled()) {
                PlayerUtil.sendActionBar(player, plugin.getTranslation("generator-conflict"));
                return;
            }

            boolean perPlayer = isPerPlayer(item);
            plugin.getRegionScheduler(location).runTaskLater(1, () -> {
                try {
                    if (!perPlayer) {
                        GlobalGenerator.create(generatorTemplate, location);
                    } else {
                        VirtualGenerator.create(generatorTemplate, location);
                    }
                    PlayerUtil.sendActionBar(player, plugin.getTranslation("placed-generator"));
                }
                catch (GeneratorCreateException e) {
                    PlayerUtil.sendActionBar(player, context, e.getMessage());
                }
            });
        });

        onDropItem((player, item) -> {
           setRotation(item, getNextRotation(item));
        });

        onSwapHand((player, item) -> {
            PlayerState playerState = plugin.getPlayerState(player);
            PlayerInventory inventory = player.getInventory();
            playerState.openMenu(new GeneratorToolEditor(this, item, inventory.getHeldItemSlot()));
        });

    }

    public @Nullable String getGeneratorTemplateName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        return PersistentData.getString(meta, GENERATOR_TEMPLATE_KEY);
    }

    public @Nullable GeneratorTemplate getGeneratorTemplate(@NotNull ItemStack item) {
        String name = getGeneratorTemplateName(item);
        return name != null ? plugin.getGeneratorTemplate(name) : null;
    }

    public @Nullable String getRotationData(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        return PersistentData.getString(meta, GENERATOR_ROTATION_KEY);
    }

    public @NotNull Rotation getRotation(@NotNull ItemStack item) {
        String rotationName = getRotationData(item);
        if (rotationName == null) {
            return Rotation.NONE;
        }

        if (rotationName.equalsIgnoreCase("RANDOM")) {
            return Rotation.random();
        }
        Rotation parsed = ParseUtil.parseEnumOrNull(Rotation.class, rotationName);
        return parsed != null ? parsed : Rotation.NONE;
    }

    public boolean isPerPlayer(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        return PersistentData.hasTag(meta, GENERATOR_PER_PLAYER_KEY);
    }

    public double getOffsetX(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return 0;
        }
        return PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_X_KEY, 0);
    }

    public double getOffsetY(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return 0;
        }
        return PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_Y_KEY, 0);
    }

    public double getOffsetZ(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return 0;
        }
        return PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_Z_KEY, 0);
    }

    public @NotNull ItemStack createItem(@NotNull GeneratorTemplate generatorTemplate) {
        ItemStack item = getItemTemplate();
        item.setType(generatorTemplate.getItemType());
        updateItem(item, generatorTemplate.getName(), "NONE", false, 0, 0, 0);
        return item;
    }

    public void setGeneratorTemplate(@NotNull ItemStack item, @NotNull GeneratorTemplate generatorTemplate) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String rotation  = PersistentData.getStringOrDefault(meta, GENERATOR_ROTATION_KEY, "NONE");
        boolean perPlayer = PersistentData.hasTag(meta, GENERATOR_PER_PLAYER_KEY);
        double x = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_X_KEY, 0);
        double y = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_Y_KEY, 0);
        double z = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_Z_KEY, 0);
        item.setType(generatorTemplate.getItemType());
        updateItem(item, generatorTemplate.getName(), rotation, perPlayer, x, y, z);
    }

    public void setRotation(@NotNull ItemStack item, @NotNull String rotation) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String template  = PersistentData.getString(meta, GENERATOR_TEMPLATE_KEY);
        boolean perPlayer = PersistentData.hasTag(meta, GENERATOR_PER_PLAYER_KEY);
        double x = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_X_KEY, 0);
        double y = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_Y_KEY, 0);
        double z = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_Z_KEY, 0);
        updateItem(item, template, rotation, perPlayer, x, y, z);
    }

    public void setPerPlayer(@NotNull ItemStack item, boolean perPlayer) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String template  = PersistentData.getString(meta, GENERATOR_TEMPLATE_KEY);
        String rotation  = PersistentData.getStringOrDefault(meta, GENERATOR_ROTATION_KEY, "NONE");
        double x = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_X_KEY, 0);
        double y = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_Y_KEY, 0);
        double z = PersistentData.getDoubleOrDefault(meta, GENERATOR_OFFSET_Z_KEY, 0);
        updateItem(item, template, rotation, perPlayer, x, y, z);
    }

    public void setOffset(@NotNull ItemStack item, double x, double y, double z) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String template  = PersistentData.getString(meta, GENERATOR_TEMPLATE_KEY);
        String rotation  = PersistentData.getStringOrDefault(meta, GENERATOR_ROTATION_KEY, "NONE");
        boolean perPlayer = PersistentData.hasTag(meta, GENERATOR_PER_PLAYER_KEY);
        updateItem(item, template, rotation, perPlayer, x, y, z);
    }

    public @NotNull String getNextRotation(@NotNull ItemStack item) {
        String rotation = getRotationData(item);
        if (rotation == null) {
            return "NONE";
        }
        return switch (rotation) {
            case "NONE" -> "RIGHT";
            case "RIGHT" -> "BACK";
            case "BACK" -> "LEFT";
            case "LEFT" -> "RANDOM";
            default -> "NONE";
        };
    }

    private void updateItem(@NotNull ItemStack item, @Nullable String templateName,
                            @NotNull String rotation, boolean perPlayer,
                            double offsetX, double offsetY, double offsetZ) {
        ItemMeta meta = getItemMetaTemplate(item);

        if (templateName != null) {
            PersistentData.setString(meta, GENERATOR_TEMPLATE_KEY, templateName);
        }
        PersistentData.setString(meta, GENERATOR_ROTATION_KEY, rotation);
        if (perPlayer) {
            PersistentData.setTag(meta, GENERATOR_PER_PLAYER_KEY);
        } else {
            PersistentData.remove(meta, GENERATOR_PER_PLAYER_KEY);
        }
        PersistentData.setDouble(meta, GENERATOR_OFFSET_X_KEY, offsetX);
        PersistentData.setDouble(meta, GENERATOR_OFFSET_Y_KEY, offsetY);
        PersistentData.setDouble(meta, GENERATOR_OFFSET_Z_KEY, offsetZ);

        Context context = Context.builder(plugin)
                .withPlaceholder("generator_tool_template", templateName != null ? templateName : "none")
                .withPlaceholder("generator_tool_rotation", rotation)
                .withPlaceholder("generator_tool_per_player", perPlayer ? "yes" : "no")
                .withPlaceholder("generator_tool_offset_x", offsetX)
                .withPlaceholder("generator_tool_offset_y", offsetY)
                .withPlaceholder("generator_tool_offset_z", offsetZ)
                .build();

        PlaceholderUtil.parseAll(context, meta);
        item.setItemMeta(meta);
    }

}
