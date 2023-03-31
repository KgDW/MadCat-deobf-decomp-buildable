package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn
extends Module {
    public AutoRespawn() {
        super("AutoRespawn", "Auto Respawn when dead", Module.Category.MISC);
    }

    @Override
    public void onUpdate() {
        if (AutoRespawn.mc.currentScreen instanceof GuiGameOver) {
            AutoRespawn.mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }
}

