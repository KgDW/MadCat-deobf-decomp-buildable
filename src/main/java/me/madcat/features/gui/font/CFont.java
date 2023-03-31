package me.madcat.features.gui.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFont {
    protected final CharData[] charData = new CharData[256];
    protected final int charOffset = 0;
    private final float imgSize = 512.0f;
    protected Font font;
    protected boolean antiAlias;
    protected boolean fractionalMetrics;
    protected int fontHeight = -1;
    protected DynamicTexture tex;

    public CFont(Font font, boolean bl, boolean bl2) {
        this.font = font;
        this.antiAlias = bl;
        this.fractionalMetrics = bl2;
        this.tex = this.setupTexture(font, bl, bl2, this.charData);
    }

    public CFont(CustomFont customFont, boolean bl, boolean bl2) {
        try {
            Font font;
            this.font = font = Font.createFont(0, CFont.class.getResourceAsStream(customFont.getFile())).deriveFont(customFont.getSize()).deriveFont(customFont.getType());
            this.antiAlias = bl;
            this.fractionalMetrics = bl2;
            this.tex = this.setupTexture(font, bl, bl2, this.charData);
        }
        catch (FontFormatException | IOException exception) {
            // empty catch block
        }
    }

    protected DynamicTexture setupTexture(Font font, boolean bl, boolean bl2, CharData[] charDataArray) {
        BufferedImage bufferedImage = this.generateFontImage(font, bl, bl2, charDataArray);
        try {
            return new DynamicTexture(bufferedImage);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    protected BufferedImage generateFontImage(Font font, boolean bl, boolean bl2, CharData[] charDataArray) {
        this.getClass();
        int n = 512;
        BufferedImage bufferedImage = new BufferedImage(n, n, 2);
        Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
        graphics2D.setFont(font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, n, n);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, bl2 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, bl ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, bl ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int n2 = 0;
        int n3 = 0;
        int n4 = 1;
        for (int i = 0; i < charDataArray.length; ++i) {
            char c = (char)i;
            CharData charData = new CharData();
            Rectangle2D rectangle2D = fontMetrics.getStringBounds(String.valueOf(c), graphics2D);
            charData.width = rectangle2D.getBounds().width + 8;
            charData.height = rectangle2D.getBounds().height;
            if (n3 + charData.width >= n) {
                n3 = 0;
                n4 += n2;
                n2 = 0;
            }
            if (charData.height > n2) {
                n2 = charData.height;
            }
            charData.storedX = n3;
            charData.storedY = n4;
            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height;
            }
            charDataArray[i] = charData;
            graphics2D.drawString(String.valueOf(c), n3 + 2, n4 + fontMetrics.getAscent());
            n3 += charData.width;
        }
        return bufferedImage;
    }

    public void drawChar(CharData[] charDataArray, char c, float f, float f2) throws ArrayIndexOutOfBoundsException {
        try {
            this.drawQuad(f, f2, charDataArray[c].width, charDataArray[c].height, charDataArray[c].storedX, charDataArray[c].storedY, charDataArray[c].width, charDataArray[c].height);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void drawQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = f5 / 512.0f;
        float f10 = f6 / 512.0f;
        float f11 = f7 / 512.0f;
        float f12 = f8 / 512.0f;
        GL11.glTexCoord2f(f9 + f11, f10);
        GL11.glVertex2d(f + f3, f2);
        GL11.glTexCoord2f(f9, f10);
        GL11.glVertex2d(f, f2);
        GL11.glTexCoord2f(f9, f10 + f12);
        GL11.glVertex2d(f, f2 + f4);
        GL11.glTexCoord2f(f9, f10 + f12);
        GL11.glVertex2d(f, f2 + f4);
        GL11.glTexCoord2f(f9 + f11, f10 + f12);
        GL11.glVertex2d(f + f3, f2 + f4);
        GL11.glTexCoord2f(f9 + f11, f10);
        GL11.glVertex2d(f + f3, f2);
    }

    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    public int getStringWidth(String string) {
        int n = 0;
        for (char c : string.toCharArray()) {
            if (c >= this.charData.length || c < '\u0000') continue;
            int n2 = this.charData[c].width - 8;
            this.getClass();
            n += n2;
        }
        return n / 2;
    }

    public boolean isAntiAlias() {
        return this.antiAlias;
    }

    public void setAntiAlias(boolean bl) {
        if (this.antiAlias != bl) {
            this.antiAlias = bl;
            this.tex = this.setupTexture(this.font, bl, this.fractionalMetrics, this.charData);
        }
    }

    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }

    public void setFractionalMetrics(boolean bl) {
        if (this.fractionalMetrics != bl) {
            this.fractionalMetrics = bl;
            this.tex = this.setupTexture(this.font, this.antiAlias, bl, this.charData);
        }
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
        this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }

    protected static class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;

        protected CharData() {
        }
    }

    public static class CustomFont {
        final float size;
        final String file;
        final int style;

        public CustomFont(String string, float f, int n) {
            this.file = string;
            this.size = f;
            this.style = n;
        }

        public float getSize() {
            return this.size;
        }

        public String getFile() {
            return this.file;
        }

        public int getType() {
            if (this.style > 3) {
                return 3;
            }
            return Math.max(this.style, 0);
        }
    }
}

