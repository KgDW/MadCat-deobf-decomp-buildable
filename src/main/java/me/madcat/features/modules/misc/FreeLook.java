package me.madcat.features.modules.misc;

import me.madcat.event.events.TurnEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.Setting;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FreeLook
extends Module {
    public final Setting<Bind> bind = this.register(new Setting<>("Bind", new Bind(-1)));
    private float dPitch;
    boolean enabled = false;
    private float dYaw;

    @Override
    public void onDisable() {
        this.enabled = false;
        FreeLook.mc.gameSettings.thirdPersonView = 0;
    }

    @Override
    public void onTick() {
        if (FreeLook.mc.currentScreen == null && this.bind.getValue().isDown()) {
            if (!this.enabled) {
                this.dYaw = 0.0f;
                this.dPitch = 0.0f;
                FreeLook.mc.gameSettings.thirdPersonView = 1;
            }
            this.enabled = true;
        } else {
            if (this.enabled) {
                FreeLook.mc.gameSettings.thirdPersonView = 0;
            }
            this.enabled = false;
        }
        if (FreeLook.mc.gameSettings.thirdPersonView != 1 && this.enabled) {
            this.enabled = false;
            FreeLook.mc.gameSettings.thirdPersonView = 0;
        }
    }

    @SubscribeEvent
    public void onTurn(TurnEvent turnEvent) {
        if (FreeLook.mc.gameSettings.thirdPersonView > 0 && this.enabled) {
            this.dYaw = (float)((double)this.dYaw + (double)turnEvent.getYaw() * 0.15);
            this.dPitch = (float)((double)this.dPitch - (double)turnEvent.getPitch() * 0.15);
            this.dPitch = MathHelper.clamp(this.dPitch, -90.0f, 90.0f);
            turnEvent.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup cameraSetup) {
        if (FreeLook.mc.gameSettings.thirdPersonView > 0 && this.enabled) {
            cameraSetup.setYaw(cameraSetup.getYaw() + this.dYaw);
            cameraSetup.setPitch(cameraSetup.getPitch() + this.dPitch);
        }
    }

    public FreeLook() {
        super("FreeLook", "Rotate your camera and not your player in 3rd person", Module.Category.MISC);
    }
}

