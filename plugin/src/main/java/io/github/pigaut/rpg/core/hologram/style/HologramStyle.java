package io.github.pigaut.rpg.core.hologram.style;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class HologramStyle {

    private int viewDistance;
    private int updateInterval;
    private boolean seeThrough;
    private boolean shadow;
    private Display.Billboard billboard;
    private TextDisplay.TextAlignment alignment;
    private Display.Brightness brightness;
    private Color background;
    private float scaleX, scaleY, scaleZ;
    private float shadowStrength;
    private float shadowRadius;

    public HologramStyle(int viewDistance, int updateInterval, boolean seeThrough, boolean shadow,
                         Display.Billboard billboard, TextDisplay.TextAlignment alignment,
                         Display.Brightness brightness, Color background,
                         float scaleX, float scaleY, float scaleZ,
                         float shadowStrength, float shadowRadius) {
        this.viewDistance = viewDistance;
        this.updateInterval = updateInterval;
        this.seeThrough = seeThrough;
        this.shadow = shadow;
        this.billboard = billboard;
        this.alignment = alignment;
        this.brightness = brightness;
        this.background = background;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.shadowStrength = shadowStrength;
        this.shadowRadius = shadowRadius;
    }

    public static @NotNull HologramStyle createDefaultStyle() {
        return new HologramStyle(48, 0, false, true,
                Display.Billboard.CENTER,
                TextDisplay.TextAlignment.CENTER,
                null,
                Color.fromARGB(0, 0, 0, 0),
                1.0f, 1.0f, 1.0f,
                1.0f, 0.1f
        );
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public boolean isSeeThrough() {
        return seeThrough;
    }

    public void setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public @NotNull Display.Billboard getBillboard() {
        return billboard;
    }

    public void setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
    }

    public @NotNull TextDisplay.TextAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;
    }

    public @NotNull Display.Brightness getBrightness() {
        return brightness;
    }

    public void setBrightness(Display.Brightness brightness) {
        this.brightness = brightness;
    }

    public @NotNull Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }

    public float getShadowStrength() {
        return shadowStrength;
    }

    public void setShadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
    }

    public float getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public @NotNull HologramStyle copy() {
        return new HologramStyle(
                this.viewDistance, this.updateInterval, this.seeThrough, this.shadow,
                this.billboard, this.alignment, this.brightness, this.background,
                this.scaleX, this.scaleY, this.scaleZ, this.shadowStrength, this.shadowRadius
        );
    }

}
