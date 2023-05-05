package me.madcat.mixin.mixins;

import me.madcat.MadCat;
import me.madcat.features.modules.movement.InventoryMove;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={MovementInputFromOptions.class}, priority=10001)
public class MixinMovementInputFromOptions
        extends MovementInput {
    @Redirect(method={"updatePlayerMoveState"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(KeyBinding keyBinding) {
        int keyCode = keyBinding.getKeyCode();
        if (keyCode <= 0) {
            return keyBinding.isKeyDown();
        }
        if (keyCode >= 256) {
            return keyBinding.isKeyDown();
        }
        if (!MadCat.moduleManager.isModuleEnabled("InvMove")) {
            return keyBinding.isKeyDown();
        }
        if (Minecraft.getMinecraft().currentScreen == null) {
            return keyBinding.isKeyDown();
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            return keyBinding.isKeyDown();
        }
        if (keyCode == 42 && !((Boolean)InventoryMove.INSTANCE().shift.getValue()).booleanValue()) {
            return keyBinding.isKeyDown();
        }
        return Keyboard.isKeyDown(keyCode);
    }
}
