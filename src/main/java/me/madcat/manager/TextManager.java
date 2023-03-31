package me.madcat.manager;

import me.madcat.features.gui.font.CFont;
import me.madcat.util.MathUtil;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import java.awt.Font;
import me.madcat.features.modules.client.FontMod;
import me.madcat.features.gui.font.CFontRenderer;
import me.madcat.util.Timer;
import me.madcat.features.gui.font.CustomFont;
import me.madcat.features.Feature;

public class TextManager extends Feature
{
    public int scaledWidth;
    private final CustomFont ClickGuiFont;
    private final Timer idleTimer;
    public int scaledHeight;
    private boolean idling;
    public final CFontRenderer iconFont;
    private CustomFont customFont;
    public int scaleFactor;

    public String getIdleSign() {
        if (this.idleTimer.passedMs(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }

    public int getFontHeight() {
        return TextManager.mc.fontRenderer.FONT_HEIGHT;
    }

    public void drawStringlogo(final String s, final float n, final float n2, final int n3) {
        this.iconFont.drawStringWithShadow(s, n, n2, n3);
    }

    public int getStringWidth(final String s) {
        if (FontMod.INSTANCE().isEnabled() && FontMod.INSTANCE().clientFont.getValue()) {
            return this.customFont.getStringWidth(s);
        }
        return TextManager.mc.fontRenderer.getStringWidth(s);
    }

    public void drawStringClickGui(final String s, final float n, final float n2, final int n3) {
        this.ClickGuiFont.drawStringWithShadow(s, n, n2, n3);
    }

    public void init() {
        try {
            this.setFontRenderer(new Font(FontMod.INSTANCE().fontName.getValue(), FontMod.INSTANCE().fontStyle.getValue(), FontMod.INSTANCE().fontSize.getValue()), FontMod.INSTANCE().antiAlias.getValue(), FontMod.INSTANCE().fractionalMetrics.getValue());
        }
        catch (final Exception ignored) {}
    }

    public void updateResolution() {
        int guiScalePercentage = TextManager.mc.gameSettings.guiScale;
        if (guiScalePercentage <= 0) {
            this.scaledWidth = TextManager.mc.displayWidth;
            this.scaledHeight = TextManager.mc.displayHeight;
        }
    }

    public void setFontRenderer(final Font font, final boolean b, final boolean b2) {
        this.customFont = new CustomFont(font, b, b2);
    }

    public void drawString(final String text, final float x, final float y, final int color, final boolean shadow) {
        if (FontMod.INSTANCE().isEnabled() && FontMod.INSTANCE().clientFont.getValue()) {
            if (shadow) {
                this.customFont.drawStringWithShadow(text, x, y, color);
            }
            else {
                this.customFont.drawString(text, x, y, color);
            }
            return;
        }
        TextManager.mc.fontRenderer.drawString(text, x, y, color, shadow);
    }

    public int getStringCWidth(final String s) {
        return this.ClickGuiFont.getStringWidth(s);
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public TextManager() {
        this.iconFont = new CFontRenderer(new CFont.CustomFont("/assets/minecraft/textures/fonts/IconFont.ttf", 20.0f, 0), true, false);
        this.idleTimer = new Timer();
        this.ClickGuiFont = new CustomFont(new Font("Arial", 0, 17), true, true);
        this.customFont = new CustomFont(new Font("Arial", 0, 17), true, true);
        this.updateResolution();
    }

    public int getStringMCWidth(final String s) {
        return TextManager.mc.fontRenderer.getStringWidth(s);
    }
}
