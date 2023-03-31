package me.madcat.features.modules.render;

import me.madcat.event.events.PacketEvent;
import me.madcat.event.events.Render2DEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.FadeUtils;
import me.madcat.util.RenderUtil;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HurtCam
extends Module {
    private final FadeUtils hurt;
    public final Setting<Integer> blue;
    public final Setting<Integer> Height;
    public final Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting<>("Green", 255, 0, 255));

    @SubscribeEvent
    public void onPacketSend(PacketEvent packetEvent) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (packetEvent.getPacket() instanceof SPacketEntityStatus && ((SPacketEntityStatus)packetEvent.getPacket()).getOpCode() == 2 && HurtCam.mc.player.equals(((SPacketEntityStatus)packetEvent.getPacket()).getEntity(HurtCam.mc.world))) {
            this.hurt.reset();
        }
    }

    @Override
    public void onRender2D(Render2DEvent render2DEvent) {
        if (this.hurt.easeOutQuad() != 1.0) {
            int n = (int)((double) this.Height.getValue() * (1.0 - this.hurt.easeOutQuad()));
            RenderUtil.drawGradientRect(0, 0, this.renderer.scaledWidth, n, ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255), ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 0));
            RenderUtil.drawGradientRect(0, this.renderer.scaledHeight - n, this.renderer.scaledWidth, n, ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 0), ColorUtil.toRGBA(this.red.getValue(), this.green.getValue(), this.blue.getValue(), 255));
        }
    }

    public HurtCam() {
        super("HurtCam", "nice render", Module.Category.RENDER);
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255));
        this.Height = this.register(new Setting<>("Height", 100, 10, 350));
        this.hurt = new FadeUtils(1000L);
    }
}

