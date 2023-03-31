package me.madcat.features.modules.misc;

import java.util.Objects;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.Timer;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class KillEffects
extends Module {
    private final Timer timer;
    private final Setting<Boolean> self;
    private static KillEffects INSTANCE = new KillEffects();
    private final Setting<Lightning> lightning = this.register(new Setting<>("Lightning", Lightning.NORMAL));
    private final Setting<KillSound> killSound = this.register(new Setting<>("KillSound", KillSound.OFF));

    public static KillEffects INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new KillEffects();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onDeath(EntityPlayer entityPlayer) {
        if (entityPlayer == null || entityPlayer == KillEffects.mc.player && !this.self.getValue() || entityPlayer.getHealth() > 0.0f || KillEffects.mc.player.isDead || Feature.nullCheck() || Feature.fullNullCheck()) {
            return;
        }
        if (this.timer.passedMs(1500L)) {
            SoundEvent soundEvent;
            if (this.lightning.getValue() != Lightning.OFF) {
                KillEffects.mc.world.spawnEntity(new EntityLightningBolt(KillEffects.mc.world, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, true));
                if (this.lightning.getValue() == Lightning.NORMAL) {
                    KillEffects.mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 0.5f, 1.0f);
                }
            }
            if (this.killSound.getValue() != KillSound.OFF && (soundEvent = this.getSound()) != null) {
                KillEffects.mc.player.playSound(soundEvent, 1.0f, 1.0f);
            }
            this.timer.reset();
        }
    }

    public KillEffects() {
        super("KillEffects", "jajaja hypixel mode", Module.Category.MISC);
        this.self = this.register(new Setting<>("Self", true));
        this.timer = new Timer();
        this.setInstance();
    }

    private SoundEvent getSound() {
        if (Objects.requireNonNull(this.killSound.getValue()) == KillSound.HYPIXEL) {
            return SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
        }
        return null;
    }

    private enum Lightning {
        NORMAL,
        SILENT,
        OFF

    }

    private enum KillSound {
        HYPIXEL,
        OFF

    }
}

