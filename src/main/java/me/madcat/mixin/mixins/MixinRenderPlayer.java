package me.madcat.mixin.mixins;

import me.madcat.MadCat;
import me.madcat.features.modules.render.ESP2D;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderPlayer.class})
public class MixinRenderPlayer {
    @Inject(method={"renderEntityName"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
        if (MadCat.moduleManager.isModuleEnabled("NameTags") || ESP2D.INSTANCE().isEnabled() && ((Boolean)ESP2D.INSTANCE().tagsValue.getValue()).booleanValue()) {
            info.cancel();
        }
    }
}
