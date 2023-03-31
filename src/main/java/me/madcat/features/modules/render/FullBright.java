package me.madcat.features.modules.render;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class FullBright
extends Module {
    float oldBright;
    public final Setting<SwingMode> mode = this.register(new Setting<>("Swing", SwingMode.Gamma));

    @Override
    public void onDisable() {
        FullBright.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        if (this.mode.getValue() == SwingMode.Gamma) {
            FullBright.mc.gameSettings.gammaSetting = this.oldBright;
        }
    }

    @Override
    public void onEnable() {
        this.oldBright = FullBright.mc.gameSettings.gammaSetting;
    }

    public FullBright() {
        super("FullBright", "FullBright", Module.Category.RENDER);
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.mode.getValue() == SwingMode.Potion) {
            FullBright.mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80950, 1, false, false)));
        }
        if (this.mode.getValue() == SwingMode.Gamma) {
            FullBright.mc.gameSettings.gammaSetting = 100.0f;
        }
    }

    public enum SwingMode {
        Gamma,
        Potion

    }
}

