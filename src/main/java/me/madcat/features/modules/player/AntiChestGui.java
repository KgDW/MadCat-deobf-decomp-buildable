package me.madcat.features.modules.player;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import net.minecraft.inventory.ContainerChest;

public class AntiChestGui
extends Module {
    public AntiChestGui() {
        super("AntiChestGui", "AntiChestGui", Module.Category.PLAYER);
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (AntiChestGui.mc.player.openContainer instanceof ContainerChest) {
            AntiChestGui.mc.player.closeScreen();
        }
    }
}

