package me.madcat.mixin.mixins;

import java.awt.Color;
import me.madcat.MadCat;
import me.madcat.features.gui.particle.Particle;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.misc.ShulkerViewer;
import me.madcat.util.ColorUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiScreen.class})
public class MixinGuiScreen
        extends Gui {
    private final Particle.Util particles = new Particle.Util(300);
    final Minecraft mc = Minecraft.getMinecraft();
    private boolean hoveringShulker;
    private ItemStack shulkerStack;
    private String shulkerName;

    @Inject(method={"renderToolTip"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderToolTipHook(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ShulkerViewer.INSTANCE().isOn() && stack.getItem() instanceof ItemShulkerBox) {
            ShulkerViewer.INSTANCE().renderShulkerToolTip(stack, x, y, null);
            info.cancel();
        }
        if (stack.getItem() instanceof ItemShulkerBox) {
            this.hoveringShulker = true;
            this.shulkerStack = stack;
            this.shulkerName = stack.getDisplayName();
        } else {
            this.hoveringShulker = false;
        }
    }

    @Inject(method={"drawScreen"}, at={@At(value="HEAD")})
    public void drawScreenHook(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        if (this.mc.currentScreen != null) {
            if (((Boolean)ClickGui.INSTANCE().particles.getValue()).booleanValue()) {
                this.particles.drawParticles();
            }
            if (this.mc.world != null && ((Boolean)ClickGui.INSTANCE().background.getValue()).booleanValue() && !(this.mc.currentScreen instanceof GuiChat) && this.mc.playerController.getCurrentGameType() != GameType.CREATIVE) {
                RenderUtil.drawVGradientRect(0.0f, 0.0f, MadCat.textManager.scaledWidth, MadCat.textManager.scaledHeight, new Color(0, 0, 0, 0).getRGB(), ColorUtil.toRGBA(MadCat.colorManager.getCurrent((Integer)ClickGui.INSTANCE().backgroundAlpha.getValue())));
            }
            if (this.mc.world == null) {
                MadCat.textManager.drawStringWithShadow("MadCat 3.0 QQ 589191561", 2.0f, 2.0f, MadCat.colorManager.getCurrent().getRGB());
            }
        }
    }

    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")})
    public void mouseClickedHook(int mouseX, int mouseY, int mouseButton, CallbackInfo info) {
        if (mouseButton == 2 && this.hoveringShulker && ShulkerViewer.INSTANCE().isOn()) {
            ShulkerViewer.drawShulkerGui(this.shulkerStack, this.shulkerName);
        }
    }
}
 