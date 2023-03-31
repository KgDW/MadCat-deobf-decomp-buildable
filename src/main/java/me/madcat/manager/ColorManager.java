package me.madcat.manager;

import java.awt.Color;
import me.madcat.features.gui.components.Component;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.util.ColorUtil;

public class ColorManager {
    private float red = 1.0f;
    private float blue = 1.0f;
    private float alpha = 1.0f;
    private float green = 1.0f;
    private Color color = new Color(this.red, this.green, this.blue, this.alpha);

    public void setRed(float f) {
        this.red = f;
        this.updateColor();
    }

    public void setBlue(float f) {
        this.blue = f;
        this.updateColor();
    }

    public void setColor(int n, int n2, int n3, int n4) {
        this.red = (float)n / 255.0f;
        this.green = (float)n2 / 255.0f;
        this.blue = (float)n3 / 255.0f;
        this.alpha = (float)n4 / 255.0f;
        this.updateColor();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getColorAsInt() {
        return ColorUtil.toRGBA(this.color);
    }

    public void setGreen(float f) {
        this.green = f;
        this.updateColor();
    }

    public Color getCurrent(int n) {
        if (this.isRainbow()) {
            return new Color(this.getRainbow().getRed(), this.getRainbow().getGreen(), this.getRainbow().getBlue(), n);
        }
        return new Color(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), n);
    }

    public ColorManager() {
        Color current = new Color(-1);
    }

    public int getColorWithAlpha(int n) {
        if (ClickGui.INSTANCE().rainbow.getValue()) {
            return ColorUtil.rainbow(Component.counter1[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB();
        }
        return ColorUtil.toRGBA(new Color(this.red, this.green, this.blue, (float)n / 255.0f));
    }

    public Color getColor() {
        return this.color;
    }

    public int getColorAsIntFullAlpha() {
        return ColorUtil.toRGBA(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 255));
    }

    public void setAlpha(float f) {
        this.alpha = f;
        this.updateColor();
    }

    public Color getRainbow() {
        return ColorUtil.rainbow((int)ClickGui.INSTANCE().rainbowSaturation.getValue().floatValue());
    }

    public void updateColor() {
        this.setColor(new Color(this.red, this.green, this.blue, this.alpha));
    }

    public Color getCurrent() {
        if (this.isRainbow()) {
            return this.getRainbow();
        }
        return new Color(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), ClickGui.INSTANCE().alpha.getValue());
    }

    public boolean isRainbow() {
        return ClickGui.INSTANCE().rainbow.getValue();
    }

    public void setColor(float f, float f2, float f3, float f4) {
        this.red = f;
        this.green = f2;
        this.blue = f3;
        this.alpha = f4;
        this.updateColor();
    }
}

