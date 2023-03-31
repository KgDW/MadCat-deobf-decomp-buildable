package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { Locale.class }, priority = 100)
public class MixinLocaleFont
{
    @Inject(method = { "checkUnicode" }, at = { @At("HEAD") }, cancellable = true)
    public void checkUnicode(final CallbackInfo ci) {
        ci.cancel();
    }
}
 