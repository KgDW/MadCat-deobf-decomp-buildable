package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import net.minecraft.world.GameType;

public class Gamemode
extends Module {
    GameType gamemode;

    @Override
    public void onTick() {
        if (Gamemode.mc.player == null) {
            return;
        }
        Gamemode.mc.playerController.setGameType(GameType.CREATIVE);
    }

    @Override
    public void onDisable() {
        if (Gamemode.mc.player == null) {
            return;
        }
        Gamemode.mc.playerController.setGameType(this.gamemode);
    }

    @Override
    public void onEnable() {
        this.gamemode = Gamemode.mc.playerController.getCurrentGameType();
    }

    public Gamemode() {
        super("Gamemode", "fake gamemode", Module.Category.MISC);
    }
}

