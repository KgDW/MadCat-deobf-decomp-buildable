package me.madcat.features.modules.render;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HidePlayer
extends Module {
    private final Setting<Float> range = this.register(new Setting<>("Range", 6.0f, 1.0f, 12.0f));

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<EntityLivingBase> pre) {
        double d;
        if (pre.getEntity() instanceof EntityPlayer && pre.getEntity() != HidePlayer.mc.player && (d = pre.getEntity().getDistance(HidePlayer.mc.player)) < (double) this.range.getValue()) {
            pre.setCanceled(true);
        }
    }

    public HidePlayer() {
        super("HidePlayer", "ZombieUtil", Module.Category.RENDER);
    }
}

