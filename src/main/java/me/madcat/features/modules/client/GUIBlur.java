package me.madcat.features.modules.client;

import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;

public class GUIBlur
extends Module {
    final Minecraft mc = Minecraft.getMinecraft();

    public GUIBlur() {
        super("GUIBlur", "iq", Module.Category.CLIENT);
    }

    @Override
    public void onDisable() {
        if (this.mc.world != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public void onUpdate() {
        if (this.mc.world != null) {
            if (ClickGui.INSTANCE().isEnabled() || this.mc.currentScreen instanceof GuiContainer || this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiConfirmOpenLink || this.mc.currentScreen instanceof GuiEditSign || this.mc.currentScreen instanceof GuiGameOver || this.mc.currentScreen instanceof GuiOptions || this.mc.currentScreen instanceof GuiIngameMenu || this.mc.currentScreen instanceof GuiVideoSettings || this.mc.currentScreen instanceof GuiScreenOptionsSounds || this.mc.currentScreen instanceof GuiControls || this.mc.currentScreen instanceof GuiCustomizeSkin || this.mc.currentScreen instanceof GuiModList) {
                if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                    if (this.mc.entityRenderer.getShaderGroup() != null) {
                        this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                    }
                    try {
                        this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else if (this.mc.entityRenderer.getShaderGroup() != null && this.mc.currentScreen == null) {
                    this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            } else if (this.mc.entityRenderer.getShaderGroup() != null) {
                this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }
}

