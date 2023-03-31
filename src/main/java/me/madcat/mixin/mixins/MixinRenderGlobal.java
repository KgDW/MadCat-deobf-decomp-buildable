package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.madcat.event.events.BlockBreakEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderGlobal.class })
public class MixinRenderGlobal
{
    @Inject(method = { "sendBlockBreakProgress" }, at = { @At("HEAD") })
    public void onSendingBlockBreakProgressPre(final int breakerId, final BlockPos pos, final int progress, final CallbackInfo ci) {
        final BlockBreakEvent event = new BlockBreakEvent(breakerId, pos, progress);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
}
