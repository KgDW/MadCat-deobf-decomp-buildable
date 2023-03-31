package me.madcat.features.modules.render;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class CrystalChams
extends Module {
    public static CrystalChams INSTANCE;
    private final Setting<Page> page = this.register(new Setting<>("Settings", Page.GLOBAL));
    public final Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255, this::new0));
    public final Setting<Integer> green = this.register(new Setting<>("Green", 255, 0, 255, this::new1));
    public final Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 0, 255, this::new2));
    public final Setting<Integer> alpha = this.register(new Setting<>("Alpha", 255, 0, 255, this::new3));
    public final Setting<Boolean> fill = this.register(new Setting<>("Fill", Boolean.TRUE, this::new4));
    public final Setting<Boolean> xqz = this.register(new Setting<>("XQZ", Boolean.TRUE, this::new5));
    public final Setting<Boolean> wireframe = this.register(new Setting<>("Wireframe", Boolean.TRUE, this::new6));
    public final Setting<Model> model = this.register(new Setting<>("Model", Model.XQZ, this::new7));
    public final Setting<Boolean> glint = this.register(new Setting<>("Glint", Boolean.FALSE, this::new8));
    public final Setting<Float> scale = this.register(new Setting<>("Scale", 1.0f, 0.1f, 1.0f, this::new9));
    public final Setting<Boolean> changeSpeed = this.register(new Setting<>("ChangeSpeed", Boolean.FALSE, this::new10));
    public final Setting<Float> spinSpeed = this.register(new Setting<>("SpinSpeed", 1.0f, 0.0f, 10.0f, this::new11));
    public final Setting<Float> floatFactor = this.register(new Setting<>("FloatFactor", 1.0f, 0.0f, 1.0f, this::new12));
    public final Setting<Float> lineWidth = this.register(new Setting<>("LineWidth", 1.0f, 0.1f, 3.0f, this::new13));
    public final Setting<Boolean> rainbow = this.register(new Setting<>("Rainbow", Boolean.FALSE, this::new14));
    public final Setting<Boolean> lineColor = this.register(new Setting<>("LineColor", Boolean.TRUE, this::new15));
    public final Setting<Integer> lineRed = this.register(new Setting<>("lineRed", 255, 0, 255, this::new16));
    public final Setting<Integer> lineGreen = this.register(new Setting<>("lineGreen", 255, 0, 255, this::new17));
    public final Setting<Integer> lineBlue = this.register(new Setting<>("lineBlue", 255, 0, 255, this::new18));
    public final Setting<Integer> lineAlpha = this.register(new Setting<>("lineAlpha", 255, 0, 255, this::new19));
    public final Setting<Boolean> modelColor = this.register(new Setting<>("ModelColor", Boolean.TRUE, this::new20));
    public final Setting<Integer> modelRed = this.register(new Setting<>("modelRed", 255, 0, 255, this::new21));
    public final Setting<Integer> modelGreen = this.register(new Setting<>("modelGreen", 255, 0, 255, this::new22));
    public final Setting<Integer> modelBlue = this.register(new Setting<>("modelBlue", 255, 0, 255, this::new23));
    public final Setting<Integer> modelAlpha = this.register(new Setting<>("modelAlpha", 255, 0, 255, this::new24));

    public CrystalChams() {
        super("CrystalChams", "Draws a pretty ESP around end crystals", Module.Category.RENDER);
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        String string = null;
        if (this.fill.getValue()) {
            string = "Fill";
        } else if (this.wireframe.getValue()) {
            string = "Wireframe";
        }
        if (this.wireframe.getValue() && this.fill.getValue()) {
            string = "Both";
        }
        return string;
    }

    private boolean new24(Integer n) {
        return this.page.getValue() == Page.COLORS && this.modelColor.getValue();
    }

    private boolean new23(Integer n) {
        return this.page.getValue() == Page.COLORS && this.modelColor.getValue();
    }

    private boolean new22(Integer n) {
        return this.page.getValue() == Page.COLORS && this.modelColor.getValue();
    }

    private boolean new21(Integer n) {
        return this.page.getValue() == Page.COLORS && this.modelColor.getValue();
    }

    private boolean new20(Boolean bl) {
        return this.page.getValue() == Page.COLORS;
    }

    private boolean new19(Integer n) {
        return this.page.getValue() == Page.COLORS && this.lineColor.getValue();
    }

    private boolean new18(Integer n) {
        return this.page.getValue() == Page.COLORS && this.lineColor.getValue();
    }

    private boolean new17(Integer n) {
        return this.page.getValue() == Page.COLORS && this.lineColor.getValue();
    }

    private boolean new16(Integer n) {
        return this.page.getValue() == Page.COLORS && this.lineColor.getValue();
    }

    private boolean new15(Boolean bl) {
        return this.page.getValue() == Page.COLORS;
    }

    private boolean new14(Boolean bl) {
        return this.page.getValue() == Page.COLORS;
    }

    private boolean new13(Float f) {
        return this.page.getValue() == Page.COLORS;
    }

    private boolean new12(Float f) {
        return this.page.getValue() == Page.GLOBAL && this.changeSpeed.getValue();
    }

    private boolean new11(Float f) {
        return this.page.getValue() == Page.GLOBAL && this.changeSpeed.getValue();
    }

    private boolean new10(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new9(Float f) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new8(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new7(Model model) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new6(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new5(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL && this.fill.getValue();
    }

    private boolean new4(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new3(Integer n) {
        return this.page.getValue() == Page.COLORS;
    }

    private boolean new2(Integer n) {
        return this.page.getValue() == Page.COLORS;
    }

    private boolean new1(Integer n) {
        return this.page.getValue() == Page.COLORS;
    }

    private boolean new0(Integer n) {
        return this.page.getValue() == Page.COLORS;
    }

    public enum Model {
        XQZ,
        VANILLA,
        OFF

    }

    public enum Page {
        COLORS,
        GLOBAL

    }
}

