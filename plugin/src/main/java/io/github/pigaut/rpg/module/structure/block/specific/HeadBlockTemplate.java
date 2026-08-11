package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.Skull;
import org.jetbrains.annotations.*;

public class HeadBlockTemplate extends AbstractBlockTemplate<Skull> {

    private final BlockFace rotation;
    private final String headTexture;

    public HeadBlockTemplate(@NotNull Material type, @NotNull BlockFace rotation, @Nullable String headTexture) {
        super(type, Skull.class);
        this.rotation = rotation;
        this.headTexture = headTexture;
    }

    @Override
    public boolean matchBlockData(@NotNull Skull blockData, @NotNull Rotation rotation) {
        return blockData.getRotation() == rotation.translateBlockFace(this.rotation);
    }

    @Override
    public void updateBlockData(@NotNull Skull blockData, @NotNull Rotation rotation) {
        blockData.setRotation(rotation.translateBlockFace(this.rotation));
    }

    @Override
    public boolean hasBlockState() {
        return true;
    }

    @Override
    public boolean matchBlockState(@NotNull BlockState blockState) {
        if (!(blockState instanceof org.bukkit.block.Skull skull)) {
            return false;
        }

        if (Server.isPaper() && Server.getVersion() < Version.V1_18_1) {
            return true;
        }

        String foundTexture = SkullUtil.getSkullTexture(skull);
        if (headTexture == null) {
            return foundTexture == null;
        }

        return headTexture.equals(foundTexture);
    }

    @Override
    public void updateBlockState(@NotNull BlockState blockState) {
        if (!(blockState instanceof org.bukkit.block.Skull skull)) {
            return;
        }

        if (Server.isPaper() && Server.getVersion() < Version.V1_18_1) {
            return;
        }

        if (headTexture != null) {
            SkullUtil.setSkullTexture(skull, headTexture);
        }
    }
}
