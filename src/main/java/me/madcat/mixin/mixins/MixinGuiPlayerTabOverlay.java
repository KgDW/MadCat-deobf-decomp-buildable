package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import me.madcat.features.modules.misc.TabFriends;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.madcat.features.modules.misc.ExtraTab;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.List;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.Gui;

@Mixin({ GuiPlayerTabOverlay.class })
public class MixinGuiPlayerTabOverlay extends Gui
{
    @Redirect(method = { "renderPlayerlist" }, at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/util/List;"))
    public List<NetworkPlayerInfo> subListHook(final List<NetworkPlayerInfo> list, final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, ExtraTab.INSTANCE().isEnabled() ? Math.min(ExtraTab.INSTANCE().size.getValue(), list.size()) : toIndex);
    }

    @Inject(method = { "getPlayerName" }, at = { @At("HEAD") }, cancellable = true)
    public void getPlayerNameHook(final NetworkPlayerInfo networkPlayerInfoIn, final CallbackInfoReturnable<String> info) {
        if (TabFriends.INSTANCE.isEnabled()) {
            info.setReturnValue(TabFriends.getPlayerName(networkPlayerInfoIn));
        }
    }
}
 