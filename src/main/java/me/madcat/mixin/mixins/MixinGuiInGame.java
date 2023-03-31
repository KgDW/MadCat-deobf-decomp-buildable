package me.madcat.mixin.mixins;

import me.madcat.features.modules.render.NoLag;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.madcat.features.modules.client.HUD;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.Gui;

@Mixin({ GuiIngame.class })
public class MixinGuiInGame extends Gui
{
    @Inject(method = { "renderPotionEffects" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPotionEffectsHook(final ScaledResolution scaledRes, final CallbackInfo info) {
        final HUD mod = HUD.INSTANCE();
        if (mod.potionIcons.getValue() && mod.isOn()) {
            info.cancel();
        }
    }

    @Inject(method = { "renderScoreboard" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderScoreboardHook(final ScoreObjective objective, final ScaledResolution scaledRes, final CallbackInfo info) {
        final NoLag mod = NoLag.INSTANCE;
        if (mod.scoreBoards.getValue() && mod.isOn()) {
            info.cancel();
        }
    }
}
 