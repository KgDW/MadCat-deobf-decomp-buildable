package me.madcat.mixin.mixins;

import java.util.List;
import me.madcat.MadCat;
import me.madcat.features.modules.client.BetterChat;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.util.ColorUtil;
import me.madcat.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiNewChat.class})
public class MixinGuiNewChat
        extends Gui {
    @Shadow
    public boolean isScrolled;
    final Minecraft mc = Minecraft.getMinecraft();
    private float percentComplete;
    private long prevMillis = System.currentTimeMillis();
    private boolean configuring;
    private float animationPercent;

    @Redirect(method={"drawChat"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectHook(int left, int top, int right, int bottom, int color) {
        int rectColor = 0;
        BetterChat mod = BetterChat.INSTANCE;
        ClickGui gui = ClickGui.INSTANCE();
        int n = ((Boolean)mod.colorRect.getValue()).booleanValue() ? (((Boolean)gui.rainbow.getValue()).booleanValue() ? ColorUtil.toARGB(ColorUtil.rainbow((int)((Float)gui.rainbowSaturation.getValue()).floatValue()).getRed(), ColorUtil.rainbow((int)((Float)gui.rainbowSaturation.getValue()).floatValue()).getGreen(), ColorUtil.rainbow((int)((Float)gui.rainbowSaturation.getValue()).floatValue()).getBlue(), 45) : ColorUtil.toARGB((Integer)gui.red.getValue(), (Integer)gui.green.getValue(), (Integer)gui.blue.getValue(), 45)) : (rectColor = color);
        if (mod.isOn()) {
            if (((Boolean)mod.rect.getValue()).booleanValue()) {
                Gui.drawRect(left, top, right, bottom, rectColor);
            } else {
                Gui.drawRect(left, top, right, bottom, 0);
            }
        } else {
            Gui.drawRect(left, top, right, bottom, color);
        }
    }

    @Redirect(method={"drawChat"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadow(FontRenderer fontRenderer, String text, float x, float y, int color) {
        MadCat.textManager.getClass();
        if (text.contains("\u00a7\u0028")) {
            this.mc.fontRenderer.drawStringWithShadow(text, x, y, MadCat.colorManager.getCurrent().getRGB());
        } else {
            this.mc.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        return 0;
    }

    @Redirect(method={"setChatLine"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=0, remap=false))
    public int drawnChatLinesSize(List<ChatLine> list) {
        return BetterChat.INSTANCE.isOn() && (Boolean)BetterChat.INSTANCE.infinite.getValue() != false ? -2147483647 : list.size();
    }

    @Redirect(method={"setChatLine"}, at=@At(value="INVOKE", target="Ljava/util/List;size()I", ordinal=2, remap=false))
    public int chatLinesSize(List<ChatLine> list) {
        return BetterChat.INSTANCE.isOn() && (Boolean)BetterChat.INSTANCE.infinite.getValue() != false ? -2147483647 : list.size();
    }

    @Shadow
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    private void updatePercentage(long diff) {
        if (this.percentComplete < 1.0f) {
            this.percentComplete += 0.004f * (float)diff;
        }
        this.percentComplete = MathUtil.clamp(this.percentComplete, 0.0f, 1.0f);
    }

    @Inject(method={"drawChat"}, at={@At(value="HEAD")}, cancellable=true)
    private void modifyChatRendering(CallbackInfo ci) {
        if (this.configuring) {
            ci.cancel();
            return;
        }
        long current = System.currentTimeMillis();
        long diff = current - this.prevMillis;
        this.prevMillis = current;
        this.updatePercentage(diff);
        float t = this.percentComplete;
        this.animationPercent = MathUtil.clamp(1.0f - (t -= 1.0f) * t * t * t, 0.0f, 1.0f);
    }

    @Inject(method={"drawChat"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", ordinal=0, shift=At.Shift.AFTER)})
    private void translate(CallbackInfo ci) {
        float y = 1.0f;
        if (!this.isScrolled) {
            y += (9.0f - 9.0f * this.animationPercent) * this.getChatScale();
        }
        GlStateManager.translate(0.0f, y, 0.0f);
    }

    @ModifyArg(method={"drawChat"}, at=@At(value="INVOKE", target="Ljava/util/List;get(I)Ljava/lang/Object;", ordinal=0, remap=false), index=0)
    private int getLineBeingDrawn(int line) {
        return line;
    }

    @Inject(method={"printChatMessageWithOptionalDeletion"}, at={@At(value="HEAD")})
    private void resetPercentage(CallbackInfo ci) {
        this.percentComplete = 0.0f;
    }

    @ModifyVariable(method={"setChatLine"}, at=@At(value="STORE"), ordinal=0)
    private List<ITextComponent> setNewLines(List<ITextComponent> original) {
        return original;
    }
}
 