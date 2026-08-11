package io.github.pigaut.rpg.module.gate.tool;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.api.event.gate.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.core.item.*;
import io.github.pigaut.rpg.core.placeholder.*;
import io.github.pigaut.rpg.core.tool.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.exception.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.module.generator.tool.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.convert.parse.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class GateTool extends Tool {

    private final RpgMakerPlugin plugin;

    private final NamespacedKey GATE_TEMPLATE_KEY;
    private final NamespacedKey GATE_ROTATION_KEY;
    private final NamespacedKey GATE_PER_PLAYER_KEY;
    private final NamespacedKey GATE_OFFSET_X_KEY;
    private final NamespacedKey GATE_OFFSET_Y_KEY;
    private final NamespacedKey GATE_OFFSET_Z_KEY;

    public GateTool(RpgMakerPlugin plugin) {
        super(plugin, "gate");
        this.plugin = plugin;

        GATE_TEMPLATE_KEY   = plugin.getNamespacedKey("gate_template");
        GATE_ROTATION_KEY   = plugin.getNamespacedKey("gate_rotation");
        GATE_PER_PLAYER_KEY = plugin.getNamespacedKey("gate_per_player");
        GATE_OFFSET_X_KEY   = plugin.getNamespacedKey("gate_offset_x");
        GATE_OFFSET_Y_KEY   = plugin.getNamespacedKey("gate_offset_y");
        GATE_OFFSET_Z_KEY   = plugin.getNamespacedKey("gate_offset_z");

        setItemTemplate(new ItemStack(Material.TERRACOTTA));
        setItemMetaTemplate(meta -> MetaEditor.of(meta)
                .withAllFlags()
                .withName("&b&l{gate_tool_template_tc} Gate &f&l({gate_rotation_uc})")
                .addLine("&8Use this tool to place and")
                .addLine("&8remove gates in your worlds.")
                .addEmptyLine()
                .addLine("&8&m                                   ")
                .addLine("&aRotation: &f{gate_tool_rotation_tc}")
                .addLine("&aPer-Player: &c{gate_tool_per_player}")
                .addLine("&aOffset: &f{gate_tool_offset_x}, {gate_tool_offset_y}, {gate_tool_offset_z}")
                .addLine("&8&m                                   ")
                .addEmptyLine()
                .addLine("&cLeft-Click: &fTo remove gate")
                .addLine("&eRight-Click: &fTo place gate")
                .addLine("&6Press Q: &fTo cycle rotation")
                .addEmptyLine()
                .addLine("&dPress F: &fTo change settings")
        );

        onLeftClickBlock(event -> {
            Player player = event.getPlayer();
            Location location = event.getClickedBlock().getLocation();

            Gate gate = plugin.getGate(location);
            GateTemplate heldTemplate = getGateTemplate(event.getItem());
            if (gate == null || !gate.getTemplate().equals(heldTemplate)) {
                return;
            }

            Context context = Context.fromPlayer(plugin, player).with(Gate.class, gate);
            if (!player.hasPermission("rpg-maker.gate.break")) {
                plugin.sendMessage(player, context, "cannot-break-gate");
                return;
            }

            gate.remove();
            PlayerUtil.sendActionBar(player, plugin.getTranslation("broke-gate"));
        });

        onRightClickBlock(event -> {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            Location location = event.getClickedBlock().getLocation();

            double offsetX = getOffsetX(item);
            double offsetY = getOffsetY(item);
            double offsetZ = getOffsetZ(item);
            location.add(offsetX, offsetY, offsetZ);

            if (plugin.getGates().isGate(location)) {
                plugin.sendMessage(player, Context.fromPlayer(plugin, player), "gate-occupied-block");
                return;
            }

            GateTemplate gateTemplate = getGateTemplate(item);
            if (gateTemplate == null) {
                plugin.sendMessage(player, Context.fromPlayer(plugin, player), "gate-not-exists");
                return;
            }

            Context context = Context.fromPlayer(plugin, player).with(GateTemplate.class, gateTemplate);
            if (!player.hasPermission("orestack.gate.place")) {
                plugin.sendMessage(player, context, "cannot-place-gate");
                return;
            }

            Rotation rotation = getRotation(item);
            GatePlaceEvent gatePlaceEvent = new GatePlaceEvent(
                    player, location, gateTemplate.getName(),
                    gateTemplate.getOccupiedBlocks(location, rotation));
            Server.callEvent(gatePlaceEvent);

            if (gatePlaceEvent.isCancelled()) {
                PlayerUtil.sendActionBar(player, plugin.getTranslation("gate-conflict"));
                return;
            }

            boolean perPlayer = isPerPlayer(item);
            plugin.getRegionScheduler(location).runTaskLater(1, () -> {
                try {
                    Gate.create(gateTemplate, location, rotation);
                    PlayerUtil.sendActionBar(player, plugin.getTranslation("placed-gate"));
                }
                catch (GateCreateException e) {
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
            playerState.openMenu(new GateToolEditor(this, item, inventory.getHeldItemSlot()));
        });
    }

    public @Nullable String getGateTemplateName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        return PersistentData.getString(meta, GATE_TEMPLATE_KEY);
    }

    public @Nullable GateTemplate getGateTemplate(@NotNull ItemStack item) {
        String name = getGateTemplateName(item);
        return name != null ? plugin.getGateTemplate(name) : null;
    }

    public @Nullable String getRotationData(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        return PersistentData.getString(meta, GATE_ROTATION_KEY);
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
        return PersistentData.hasTag(meta, GATE_PER_PLAYER_KEY);
    }

    public double getOffsetX(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return 0;
        }
        return PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_X_KEY, 0);
    }

    public double getOffsetY(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return 0;
        }
        return PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_Y_KEY, 0);
    }

    public double getOffsetZ(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return 0;
        }
        return PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_Z_KEY, 0);
    }

    public @NotNull ItemStack createItem(@NotNull GateTemplate gateTemplate) {
        ItemStack item = getItemTemplate();
        item.setType(gateTemplate.getItemType());
        updateItem(item, gateTemplate.getName(), "NONE", false, 0, 0, 0);
        return item;
    }

    public void setGateTemplate(@NotNull ItemStack item, @NotNull GateTemplate gateTemplate) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String rotation  = PersistentData.getStringOrDefault(meta, GATE_ROTATION_KEY, "NONE");
        boolean perPlayer = PersistentData.hasTag(meta, GATE_PER_PLAYER_KEY);
        double x = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_X_KEY, 0);
        double y = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_Y_KEY, 0);
        double z = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_Z_KEY, 0);
        item.setType(gateTemplate.getItemType());
        updateItem(item, gateTemplate.getName(), rotation, perPlayer, x, y, z);
    }

    public void setRotation(@NotNull ItemStack item, @NotNull String rotation) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String template  = PersistentData.getString(meta, GATE_TEMPLATE_KEY);
        boolean perPlayer = PersistentData.hasTag(meta, GATE_PER_PLAYER_KEY);
        double x = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_X_KEY, 0);
        double y = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_Y_KEY, 0);
        double z = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_Z_KEY, 0);
        updateItem(item, template, rotation, perPlayer, x, y, z);
    }

    public void setPerPlayer(@NotNull ItemStack item, boolean perPlayer) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String template  = PersistentData.getString(meta, GATE_TEMPLATE_KEY);
        String rotation  = PersistentData.getStringOrDefault(meta, GATE_ROTATION_KEY, "NONE");
        double x = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_X_KEY, 0);
        double y = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_Y_KEY, 0);
        double z = PersistentData.getDoubleOrDefault(meta, GATE_OFFSET_Z_KEY, 0);
        updateItem(item, template, rotation, perPlayer, x, y, z);
    }

    public void setOffset(@NotNull ItemStack item, double x, double y, double z) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        String template  = PersistentData.getString(meta, GATE_TEMPLATE_KEY);
        String rotation  = PersistentData.getStringOrDefault(meta, GATE_ROTATION_KEY, "NONE");
        boolean perPlayer = PersistentData.hasTag(meta, GATE_PER_PLAYER_KEY);
        updateItem(item, template, rotation, perPlayer, x, y, z);
    }

    public @NotNull String getNextRotation(@NotNull ItemStack item) {
        Rotation rotation = getRotation(item);
        return switch (rotation.toString()) {
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
            PersistentData.setString(meta, GATE_TEMPLATE_KEY, templateName);
        }
        PersistentData.setString(meta, GATE_ROTATION_KEY, rotation);
        if (perPlayer) {
            PersistentData.setTag(meta, GATE_PER_PLAYER_KEY);
        } else {
            PersistentData.remove(meta, GATE_PER_PLAYER_KEY);
        }
        PersistentData.setDouble(meta, GATE_OFFSET_X_KEY, offsetX);
        PersistentData.setDouble(meta, GATE_OFFSET_Y_KEY, offsetY);
        PersistentData.setDouble(meta, GATE_OFFSET_Z_KEY, offsetZ);

        Context context = Context.builder(plugin)
                .withPlaceholder("gate_tool_template", templateName != null ? templateName : "none")
                .withPlaceholder("gate_tool_rotation", rotation)
                .withPlaceholder("gate_tool_per_player", perPlayer ? "Coming Soon" : "Coming Soon")
                .withPlaceholder("gate_tool_offset_x", offsetX)
                .withPlaceholder("gate_tool_offset_y", offsetY)
                .withPlaceholder("gate_tool_offset_z", offsetZ)
                .build();

        PlaceholderUtil.parseAll(context, meta);
        item.setItemMeta(meta);
    }

}