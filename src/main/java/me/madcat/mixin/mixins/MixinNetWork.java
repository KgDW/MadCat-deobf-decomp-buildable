package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.madcat.event.events.PacketEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { NetworkManager.class }, priority = 312312)
public class MixinNetWork
{
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void packetReceived(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            final PacketEvent.Receive event = new PacketEvent.Receive(packet);
            MinecraftForge.EVENT_BUS.post((Event)event);
            if (event.isCanceled() && callbackInfo.isCancellable()) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void sendPacket(final Packet<?> packetIn, final CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            final PacketEvent.Send event = new PacketEvent.Send(packetIn);
            MinecraftForge.EVENT_BUS.post((Event)event);
            if (event.isCanceled() && callbackInfo.isCancellable()) {
                callbackInfo.cancel();
            }
        }
    }
}
 