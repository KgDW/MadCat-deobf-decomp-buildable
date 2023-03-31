package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.madcat.features.modules.client.Capes;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractClientPlayer.class })
public abstract class MixinAbstractClientPlayer
{
    @Inject(method = { "getLocationCape" }, at = { @At("HEAD") }, cancellable = true)
    public void getLocationCape(final CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (Capes.INSTANCE().isEnabled()) {
            final ResourceLocation cape = Capes.INSTANCE().Cape();
            callbackInfoReturnable.setReturnValue(cape);
        }
    }
}
