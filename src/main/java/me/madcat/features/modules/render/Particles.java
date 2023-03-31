package me.madcat.features.modules.render;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;

public class Particles
extends Module {
    public Particles() {
        super("Particles", "Display Particle", Module.Category.RENDER);
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        int n = (int)(Math.random() * 5.0 + 0.0);
        int n2 = (int)(Math.random() * 3.0 + 1.0);
        int n3 = (int)(Math.random() * 5.0 - 1.0);
        int n4 = (int)(Math.random() * 47.0 + 1.0);
        if (n4 != 1 && n4 != 2 && n4 != 41) {
            Particles.mc.effectRenderer.spawnEffectParticle(n4, Particles.mc.player.posX + 1.5 + (double)(-n), Particles.mc.player.posY + (double)n2, Particles.mc.player.posZ + 1.5 + (double)(-n3), 0.0, 0.5, 0.0, 10);
        }
    }
}

