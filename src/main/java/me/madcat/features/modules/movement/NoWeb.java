package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;

public final class NoWeb
extends Module {
    public NoWeb() {
        super("NoWeb", "Prevents you from getting slowed down in webs", Module.Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (NoWeb.mc.player.isDead) {
            NoWeb.mc.player.isDead = false;
        }
    }
}

