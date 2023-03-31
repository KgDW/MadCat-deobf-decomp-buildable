package me.madcat.mixin.mixins;

import me.madcat.MadCat;
import me.madcat.features.gui.particle.Particle;
import me.madcat.features.modules.misc.ShulkerViewer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GuiScreen.class })
public class MixinGuiScreen extends Gui
{
    private final Particle.Util particles;
    final Minecraft mc;
    private boolean hoveringShulker;
    private ItemStack shulkerStack;
    private String shulkerName;

    public MixinGuiScreen() {
        this.particles = new Particle.Util(300);
        this.mc = Minecraft.getMinecraft();
    }

    @Inject(method = {"renderToolTip"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderToolTipHook(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ShulkerViewer.INSTANCE().isOn() && stack.getItem() instanceof ItemShulkerBox) {
            ShulkerViewer.INSTANCE().renderShulkerToolTip(stack, x, y, null);
            info.cancel();
        }
    }

    @Inject(method = { "drawScreen" }, at = { @At("HEAD") })
    public void drawScreenHook(final int mouseX, final int mouseY, final float mouseButton, final CallbackInfo callbackInfo) {
        if (this.mc.world == null) {
                MadCat.textManager.drawStringWithShadow("MadCat 3.0 Q\u7fa4 589191561", 2.0f, 2.0f, MadCat.colorManager.getCurrent().getRGB());
            }
        }

    @Inject(method = { "mouseClicked" }, at = { @At("HEAD") })
    public void mouseClickedHook(final int mouseX, final int mouseY, final int mouseButton, final CallbackInfo info) {
        if (mouseButton == 2 && this.hoveringShulker && ShulkerViewer.INSTANCE().isOn()) {
            ShulkerViewer.drawShulkerGui(this.shulkerStack, this.shulkerName);
        }
    }
}
 