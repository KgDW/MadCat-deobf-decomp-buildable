package me.madcat.features.modules.client;

import me.madcat.event.events.PerspectiveEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FovMod
extends Module {
    private static FovMod INSTANCE = new FovMod();
    private final Setting<Page> page = this.register(new Setting<>("Settings", Page.FOV));
    private final Setting<Boolean> customFov = this.register(new Setting<>("CustomFov", Boolean.FALSE, this::new0));
    private final Setting<Float> fov = this.register(new Setting<>("FOV", 120.0f, 10.0f, 180.0f, this::new1));
    private final Setting<Boolean> aspectRatio = this.register(new Setting<>("AspectRatio", Boolean.FALSE, this::new2));
    private final Setting<Float> aspectFactor = this.register(new Setting<>("AspectFactor", 1.8f, 0.1f, 3.0f, this::new3));
    private final Setting<Boolean> defaults = this.register(new Setting<>("Defaults", Boolean.FALSE, this::new4));
    private final Setting<Float> sprint = this.register(new Setting<>("SprintAdd", 1.15f, 1.0f, 2.0f, this::new5));
    private final Setting<Float> speed = this.register(new Setting<>("SwiftnessAdd", 1.15f, 1.0f, 2.0f, this::new6));

    public FovMod() {
        super("FovMod", "FOV modifier", Module.Category.CLIENT);
        this.setInstance();
    }

    public static FovMod INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new FovMod();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue()) {
            FovMod.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue());
        }
        if (this.defaults.getValue()) {
            this.sprint.setValue(1.15f);
            this.speed.setValue(1.15f);
            this.defaults.setValue(false);
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent fOVUpdateEvent) {
        float f = 1.0f;
        if (fOVUpdateEvent.getEntity().isSprinting()) {
            f = this.sprint.getValue();
            if (fOVUpdateEvent.getEntity().isPotionActive(MobEffects.SPEED)) {
                f = this.speed.getValue();
            }
        }
        fOVUpdateEvent.setNewfov(f);
    }

    @SubscribeEvent
    public void onPerspectiveUpdate(PerspectiveEvent perspectiveEvent) {
        if (this.aspectRatio.getValue()) {
            perspectiveEvent.setAspect(this.aspectFactor.getValue());
        }
    }

    private boolean new6(Float f) {
        return this.page.getValue() == Page.ADVANCED;
    }

    private boolean new5(Float f) {
        return this.page.getValue() == Page.ADVANCED;
    }

    private boolean new4(Boolean bl) {
        return this.page.getValue() == Page.ADVANCED;
    }

    private boolean new3(Float f) {
        return this.page.getValue() == Page.FOV && this.aspectRatio.getValue();
    }

    private boolean new2(Boolean bl) {
        return this.page.getValue() == Page.FOV;
    }

    private boolean new1(Float f) {
        return this.page.getValue() == Page.FOV && this.customFov.getValue();
    }

    private boolean new0(Boolean bl) {
        return this.page.getValue() == Page.FOV;
    }

    public enum Page {
        FOV,
        ADVANCED

    }
}

