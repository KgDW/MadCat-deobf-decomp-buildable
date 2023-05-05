package me.madcat.mixin.mixins;

import me.madcat.features.modules.client.HUD;
import me.madcat.features.modules.render.NoLag;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngame.class})
public class MixinGuiInGame
        extends Gui {
    @Inject(method={"renderPotionEffects"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderPotionEffectsHook(ScaledResolution scaledRes, CallbackInfo info) {
        HUD mod = HUD.INSTANCE();
        if (((Boolean)mod.potionIcons.getValue()).booleanValue() && mod.isOn()) {
            info.cancel();
        }
    }

    @Inject(method={"renderScoreboard"}, at={@At(value="HEAD")}, cancellable=true)
    protected void renderScoreboardHook(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo info) {
        NoLag mod = NoLag.INSTANCE;
        if (((Boolean)mod.scoreBoards.getValue()).booleanValue() && mod.isOn()) {
            info.cancel();
        }
    }
}
