package me.madcat.mixin.mixins;

import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Locale.class}, priority=100)
public class MixinLocaleFont {
    @Inject(method={"checkUnicode"}, at={@At(value="HEAD")}, cancellable=true)
    public void checkUnicode(CallbackInfo ci) {
        ci.cancel();
    }
}
