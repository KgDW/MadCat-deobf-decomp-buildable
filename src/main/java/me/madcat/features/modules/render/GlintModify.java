package me.madcat.features.modules.render;

import java.awt.Color;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class GlintModify
extends Module {
    public final Setting<Integer> blue;
    public final Setting<Integer> green;
    public final Setting<Boolean> rainbow;
    public final Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255));
    private static GlintModify INSTANCE = new GlintModify();

    private void setInstance() {
        INSTANCE = this;
    }

    public GlintModify() {
        super("GlintModify", "Changes the enchant glint color", Module.Category.RENDER);
        this.green = this.register(new Setting<>("Green", 255, 0, 255));
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255));
        this.rainbow = this.register(new Setting<>("Rainbow", false));
        this.setInstance();
    }

    public void cycleRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.red.setValue(n >> 16 & 0xFF);
        this.green.setValue(n >> 8 & 0xFF);
        this.blue.setValue(n & 0xFF);
    }

    public static Color getColor(long l, float f) {
        if (!GlintModify.INSTANCE.rainbow.getValue()) {
            return new Color(GlintModify.INSTANCE.red.getValue(), GlintModify.INSTANCE.green.getValue(), GlintModify.INSTANCE.blue.getValue());
        }
        float f2 = (float)(System.nanoTime() + l) / 1.0E10f % 1.0f;
        long l2 = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(f2, 1.0f, 1.0f)), 16);
        Color color = new Color((int)l2);
        return new Color((float)color.getRed() / 255.0f * f, (float)color.getGreen() / 255.0f * f, (float)color.getBlue() / 255.0f * f, (float)color.getAlpha() / 255.0f);
    }

    public static GlintModify INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new GlintModify();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.rainbow.getValue()) {
            this.cycleRainbow();
        }
    }
}

