package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.madcat.util.MathUtil;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import me.madcat.MadCat;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.madcat.util.ColorUtil;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.client.BetterChat;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.Gui;

@Mixin({ GuiNewChat.class })
public class MixinGuiNewChat extends Gui
{
    @Shadow
    public boolean isScrolled;
    final Minecraft mc;
    private float percentComplete;
    private long prevMillis;
    private boolean configuring;
    private float animationPercent;

    public MixinGuiNewChat() {
        this.mc = Minecraft.getMinecraft();
        this.prevMillis = System.currentTimeMillis();
    }

    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectHook(final int left, final int top, final int right, final int bottom, final int color) {
        final BetterChat mod = BetterChat.INSTANCE;
        final ClickGui gui = ClickGui.INSTANCE();
        final int rectColor = mod.colorRect.getValue() ? (gui.rainbow.getValue() ? ColorUtil.toARGB(ColorUtil.rainbow((int)(double)gui.rainbowSaturation.getValue()).getRed(), ColorUtil.rainbow((int)(double)gui.rainbowSaturation.getValue()).getGreen(), ColorUtil.rainbow((int)(double)gui.rainbowSaturation.getValue()).getBlue(), 45) : ColorUtil.toARGB(gui.red.getValue(), gui.green.getValue(), gui.blue.getValue(), 45)) : color;
        if (mod.isOn()) {
            if (mod.rect.getValue()) {
                Gui.drawRect(left, top, right, bottom, rectColor);
            }
            else {
                Gui.drawRect(left, top, right, bottom, 0);
            }
        }
        else {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }

    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadow(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        MadCat.textManager.getClass();
        if (text.contains("§(")) {
            this.mc.fontRenderer.drawStringWithShadow(text, x, y, MadCat.colorManager.getCurrent().getRGB());
        }
        else {
            this.mc.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        return 0;
    }

    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0, remap = false))
    public int drawnChatLinesSize(final List<ChatLine> list) {
        return (BetterChat.INSTANCE.isOn() && BetterChat.INSTANCE.infinite.getValue()) ? -2147483647 : list.size();
    }

    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2, remap = false))
    public int chatLinesSize(final List<ChatLine> list) {
        return (BetterChat.INSTANCE.isOn() && BetterChat.INSTANCE.infinite.getValue()) ? -2147483647 : list.size();
    }

    @Shadow
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    private void updatePercentage(final long diff) {
        if (this.percentComplete < 1.0f) {
            this.percentComplete += 0.004f * diff;
        }
        this.percentComplete = MathUtil.clamp(this.percentComplete, 0.0f, 1.0f);
    }

    @Inject(method = { "drawChat" }, at = { @At("HEAD") }, cancellable = true)
    private void modifyChatRendering(final CallbackInfo ci) {
        if (this.configuring) {
            ci.cancel();
            return;
        }
        final long current = System.currentTimeMillis();
        final long diff = current - this.prevMillis;
        this.prevMillis = current;
        this.updatePercentage(diff);
        float t = this.percentComplete;
        this.animationPercent = MathUtil.clamp(1.0f - --t * t * t * t, 0.0f, 1.0f);
    }

    @Inject(method = { "drawChat" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", ordinal = 0, shift = At.Shift.AFTER) })
    private void translate(final CallbackInfo ci) {
        float y = 1.0f;
        if (!this.isScrolled) {
            y += (9.0f - 9.0f * this.animationPercent) * this.getChatScale();
        }
        GlStateManager.translate(0.0f, y, 0.0f);
    }

    @ModifyArg(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0, remap = false), index = 0)
    private int getLineBeingDrawn(final int line) {
        return line;
    }

    @Inject(method = { "printChatMessageWithOptionalDeletion" }, at = { @At("HEAD") })
    private void resetPercentage(final CallbackInfo ci) {
        this.percentComplete = 0.0f;
    }

    @ModifyVariable(method = { "setChatLine" }, at = @At("STORE"), ordinal = 0)
    private List<ITextComponent> setNewLines(final List<ITextComponent> original) {
        return original;
    }
}
