package me.madcat.mixin.mixins;

import java.util.List;
import me.madcat.features.modules.misc.ExtraTab;
import me.madcat.features.modules.misc.TabFriends;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GuiPlayerTabOverlay.class})
public class MixinGuiPlayerTabOverlay
        extends Gui {
    @Redirect(method={"renderPlayerlist"}, at=@At(value="INVOKE", target="Ljava/util/List;subList(II)Ljava/util/List;"))
    public List<NetworkPlayerInfo> subListHook(List<NetworkPlayerInfo> list, int fromIndex, int toIndex) {
        return list.subList(fromIndex, ExtraTab.INSTANCE().isEnabled() ? Math.min((Integer)ExtraTab.INSTANCE().size.getValue(), list.size()) : toIndex);
    }

    @Inject(method={"getPlayerName"}, at={@At(value="HEAD")}, cancellable=true)
    public void getPlayerNameHook(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable<String> info) {
        if (TabFriends.INSTANCE.isEnabled()) {
            info.setReturnValue((String) TabFriends.getPlayerName(networkPlayerInfoIn));
        }
    }
}
