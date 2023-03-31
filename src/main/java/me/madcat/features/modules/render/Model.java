package me.madcat.features.modules.render;

import me.madcat.event.events.RenderItemEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Model
extends Module {
    private static Model INSTANCE = new Model();
    public final Setting<Settings> settings = this.register(new Setting<>("Settings", Settings.TRANSLATE));
    public final Setting<Boolean> noEatAnimation = this.register(new Setting<Object>("NoEatAnimation", Boolean.FALSE, this::new0));
    public final Setting<Double> eatX = this.register(new Setting<Object>("EatX", 3.5, -5.0, 15.0, this::new1));
    public final Setting<Double> eatY = this.register(new Setting<Object>("EatY", 2.1, -5.0, 15.0, this::new2));
    public final Setting<Boolean> doBob = this.register(new Setting<Object>("ItemBob", Boolean.TRUE, this::new3));
    public final Setting<Double> mainX = this.register(new Setting<Object>("MainX", 1.2, -2.0, 4.0, this::new4));
    public final Setting<Double> mainY = this.register(new Setting<Object>("MainY", -0.95, -3.0, 3.0, this::new5));
    public final Setting<Double> mainZ = this.register(new Setting<Object>("MainZ", -1.45, -5.0, 5.0, this::new6));
    public final Setting<Double> offX = this.register(new Setting<Object>("OffX", 1.2, -2.0, 4.0, this::new7));
    public final Setting<Double> offY = this.register(new Setting<Object>("OffY", -0.95, -3.0, 3.0, this::new8));
    public final Setting<Double> offZ = this.register(new Setting<Object>("OffZ", -1.45, -5.0, 5.0, this::new9));
    public final Setting<Boolean> spinY = this.register(new Setting<>("SpinX", Boolean.FALSE, this::new10));
    public final Setting<Boolean> spinX = this.register(new Setting<>("SpinY", Boolean.FALSE, this::new11));
    public final Setting<Boolean> customSwing = this.register(new Setting<>("CustomSwing", Boolean.FALSE, this::new12));
    public final Setting<Swing> swing = this.register(new Setting<>("Swing", Swing.MAINHAND, this::new13));
    public final Setting<Boolean> slowSwing = this.register(new Setting<>("SlowSwing", Boolean.FALSE, this::new14));
    public final Setting<Boolean> noSway = this.register(new Setting<>("NoSway", Boolean.FALSE, this::new15));

    public Model() {
        super("Model", "Change the position of the arm", Module.Category.RENDER);
        this.setInstance();
    }

    public static Model INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Model();
        }
        return INSTANCE;
    }

    @Override
    public void onTick() {
        if (this.swing.getValue() == Swing.OFFHAND && this.customSwing.getValue()) {
            Model.mc.player.swingingHand = EnumHand.OFF_HAND;
        }
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onItemRender(RenderItemEvent renderItemEvent) {
        renderItemEvent.setMainX(this.mainX.getValue());
        renderItemEvent.setMainY(this.mainY.getValue());
        renderItemEvent.setMainZ(this.mainZ.getValue());
        renderItemEvent.setOffX(-this.offX.getValue());
        renderItemEvent.setOffY(this.offY.getValue());
        renderItemEvent.setOffZ(this.offZ.getValue());
    }

    private boolean new15(Boolean bl) {
        return this.settings.getValue() == Settings.OTHERS;
    }

    private boolean new14(Boolean bl) {
        return this.settings.getValue() == Settings.OTHERS;
    }

    private boolean new13(Swing swing) {
        return this.settings.getValue() == Settings.OTHERS && this.customSwing.getValue();
    }

    private boolean new12(Boolean bl) {
        return this.settings.getValue() == Settings.OTHERS;
    }

    private boolean new11(Boolean bl) {
        return this.settings.getValue() == Settings.TRANSLATE;
    }

    private boolean new10(Boolean bl) {
        return this.settings.getValue() == Settings.TRANSLATE;
    }

    private boolean new9(Object object) {
        return this.settings.getValue() == Settings.TRANSLATE;
    }

    private boolean new8(Object object) {
        return this.settings.getValue() == Settings.TRANSLATE;
    }

    private boolean new7(Object object) {
        return this.settings.getValue() == Settings.TRANSLATE;
    }

    private boolean new6(Object object) {
        return this.settings.getValue() == Settings.TRANSLATE;
    }

    private boolean new5(Object object) {
        return this.settings.getValue() == Settings.TRANSLATE;
    }

    private boolean new4(Object object) {
        return this.settings.getValue() == Settings.TRANSLATE;
    }

    private boolean new3(Object object) {
        return this.settings.getValue() == Settings.TWEAKS;
    }

    private boolean new2(Object object) {
        return this.settings.getValue() == Settings.TWEAKS && !this.noEatAnimation.getValue();
    }

    private boolean new1(Object object) {
        return this.settings.getValue() == Settings.TWEAKS && !this.noEatAnimation.getValue();
    }

    private boolean new0(Object object) {
        return this.settings.getValue() == Settings.TWEAKS;
    }

    public enum Swing {
        MAINHAND,
        OFFHAND,
        SERVER

    }

    private enum Settings {
        TRANSLATE,
        TWEAKS,
        OTHERS

    }
}

