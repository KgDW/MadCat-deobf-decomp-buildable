package me.madcat.features.modules.render;

import me.madcat.event.events.NoRenderEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender
extends Module {
    public static NoRender INSTANCE = new NoRender();
    public final Setting<Boolean> armor = this.register(new Setting<>("Armor", true));
    public final Setting<Boolean> fire = this.register(new Setting<>("Fire", true));
    public final Setting<Boolean> blind = this.register(new Setting<>("Blind", true));
    public final Setting<Boolean> nausea = this.register(new Setting<>("Nausea", true));
    public final Setting<Boolean> fog = this.register(new Setting<>("Fog", true));
    public final Setting<Boolean> noWeather = this.register(new Setting<>("Weather", Boolean.TRUE, "AntiWeather"));
    public final Setting<Boolean> hurtCam = this.register(new Setting<>("HurtCam", true));
    public final Setting<Boolean> totemPops = this.register(new Setting<>("TotemPop", Boolean.TRUE, "Removes the Totem overlay."));
    public final Setting<Boolean> blocks = this.register(new Setting<>("Block", true));

    public NoRender() {
        super("NoRender", "Prevent some animation", Module.Category.RENDER);
        this.setInstance();
    }

    public static NoRender INSTANCE() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new NoRender();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.blind.getValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (!this.nausea.getValue()) {
            return;
        }
        if (this.noWeather.getValue()) {
            NoRender.mc.world.getWorldInfo().setRaining(false);
        }
        if (!NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            return;
        }
        NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
    }

    @SubscribeEvent
    public void NoRenderEventListener(NoRenderEvent noRenderEvent) {
        if (noRenderEvent.getStage() == 0 && this.armor.getValue()) {
            noRenderEvent.setCanceled(true);
            return;
        }
        if (noRenderEvent.getStage() != 1) {
            return;
        }
        if (!this.hurtCam.getValue()) {
            return;
        }
        noRenderEvent.setCanceled(true);
    }

    @SubscribeEvent
    public void fog_density(EntityViewRenderEvent.FogDensity fogDensity) {
        if (!this.fog.getValue()) {
            fogDensity.setDensity(0.0f);
            fogDensity.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void blockOverlayEventListener(RenderBlockOverlayEvent renderBlockOverlayEvent) {
        if (!this.fire.getValue()) {
            return;
        }
        if (renderBlockOverlayEvent.getOverlayType() != RenderBlockOverlayEvent.OverlayType.FIRE) {
            return;
        }
        renderBlockOverlayEvent.setCanceled(true);
    }
}

