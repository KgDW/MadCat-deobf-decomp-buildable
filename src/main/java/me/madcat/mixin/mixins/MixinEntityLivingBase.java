package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.madcat.features.modules.render.Model;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityLivingBase.class })
public class MixinEntityLivingBase
{
    @Inject(method = { "getArmSwingAnimationEnd" }, at = { @At("HEAD") }, cancellable = true)
    private void getArmSwingAnimationEnd(final CallbackInfoReturnable<Integer> info) {
        final Model mod = Model.INSTANCE();
        if (mod.isOn() && mod.slowSwing.getValue()) {
            info.setReturnValue(15);
        }
        else if (mod.isOn() && mod.customSwing.getValue() && mod.swing.getValue() == Model.Swing.SERVER) {
            info.setReturnValue(-1);
        }
    }
}
