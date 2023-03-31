package me.madcat.mixin.mixins;

import me.madcat.event.events.MoveEvent;
import net.minecraft.entity.MoverType;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.madcat.features.modules.exploit.BetterPortal;
import me.madcat.event.events.PushEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import me.madcat.event.events.UpdateWalkingPlayerEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.madcat.event.events.ChatEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.entity.AbstractClientPlayer;

@Mixin(value = { EntityPlayerSP.class }, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    public MixinEntityPlayerSP(final Minecraft p_i47378_1_, final World p_i47378_2_, final NetHandlerPlayClient p_i47378_3_, final StatisticsManager p_i47378_4_, final RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
    }

    @Inject(method = { "sendChatMessage" }, at = { @At("HEAD") }, cancellable = true)
    public void sendChatMessage(final String message, final CallbackInfo ci) {
        final ChatEvent chatEvent = new ChatEvent(message);
        MinecraftForge.EVENT_BUS.post((Event)chatEvent);
    }

    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") }, cancellable = true)
    private void preMotion(final CallbackInfo info) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("RETURN") })
    private void postMotion(final CallbackInfo info) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }

    @Inject(method = { "pushOutOfBlocks" }, at = { @At("HEAD") }, cancellable = true)
    private void pushOutOfBlocksHook(final double x, final double y, final double z, final CallbackInfoReturnable<Boolean> ci) {
        final PushEvent event = new PushEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        ci.setReturnValue(false);
    }

    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
    public void closeScreenHook(final EntityPlayerSP entityPlayerSP) {
        if (!BetterPortal.INSTANCE().isEnabled() || !BetterPortal.INSTANCE().portalChat.getValue()) {
            entityPlayerSP.closeScreen();
        }
    }

    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    public void displayGuiScreenHook(final Minecraft mc, final GuiScreen screen) {
        if (!BetterPortal.INSTANCE().isEnabled() || !BetterPortal.INSTANCE().portalChat.getValue()) {
            mc.displayGuiScreen(screen);
        }
    }

    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal = 2))
    public void onLivingUpdate(final EntityPlayerSP entityPlayerSP, final boolean sprinting) {
        entityPlayerSP.setSprinting(sprinting);
    }

    @Redirect(method = { "move" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(final AbstractClientPlayer player, final MoverType moverType, final double x, final double y, final double z) {
        final MoveEvent event = new MoveEvent(0, moverType, x, y, z);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            super.move(event.getType(), event.getX(), event.getY(), event.getZ());
        }
    }
}
 