package me.madcat.features.modules.movement;

import me.madcat.MadCat;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class ReverseStep
extends Module {
    private static ReverseStep INSTANCE = new ReverseStep();
    private final Setting<Integer> speed = this.register(new Setting<>("Speed", 8, 1, 20));
    private final Setting<Boolean> inliquid = this.register(new Setting<>("Liquid", false));
    private final Setting<Cancel> canceller = this.register(new Setting<>("CancelType", Cancel.None));

    public ReverseStep() {
        super("ReverseStep", "Rapid decline", Module.Category.MOVEMENT);
        this.setInstance();
    }

    public static ReverseStep INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ReverseStep();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (ReverseStep.fullNullCheck()) {
            return;
        }
        if (ReverseStep.mc.player.isSneaking() || ReverseStep.mc.player.isDead || ReverseStep.mc.player.collidedHorizontally || !ReverseStep.mc.player.onGround || ReverseStep.mc.player.isInWater() && !this.inliquid.getValue() || ReverseStep.mc.player.isInLava() && !this.inliquid.getValue() || ReverseStep.mc.player.isOnLadder() || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() || MadCat.moduleManager.isModuleEnabled("Burrow") || ReverseStep.mc.player.noClip || MadCat.moduleManager.isModuleEnabled("Packetfly") || MadCat.moduleManager.isModuleEnabled("Phase") || ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && this.canceller.getValue() == Cancel.Shift || ReverseStep.mc.gameSettings.keyBindSneak.isKeyDown() && this.canceller.getValue() == Cancel.Both || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && this.canceller.getValue() == Cancel.Space || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown() && this.canceller.getValue() == Cancel.Both || MadCat.moduleManager.isModuleEnabled("Strafe")) {
            return;
        }
        for (double d = 0.0; d < 90.5; d += 0.01) {
            if (ReverseStep.mc.world.getCollisionBoxes(ReverseStep.mc.player, ReverseStep.mc.player.getEntityBoundingBox().offset(0.0, -d, 0.0)).isEmpty()) {
                continue;
            }
            ReverseStep.mc.player.motionY = (float)(-this.speed.getValue()) / 10.0f;
            break;
        }
    }

    public enum Cancel {
        None,
        Space,
        Shift,
        Both

    }
}

