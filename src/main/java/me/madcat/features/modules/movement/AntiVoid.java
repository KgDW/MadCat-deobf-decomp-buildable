package me.madcat.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class AntiVoid
extends Module {
    public final Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.BOUNCE));

    public AntiVoid() {
        super("AntiVoid", "Glitches you up from void", Module.Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (AntiVoid.fullNullCheck()) {
            return;
        }
        double d = AntiVoid.mc.player.posY;
        if (d <= 0.5) {
            Command.sendMessage(ChatFormatting.RED + "Player " + ChatFormatting.GREEN + AntiVoid.mc.player.getName() + ChatFormatting.RED + " is in the void!");
            if (this.mode.getValue().equals(Mode.BOUNCE)) {
                AntiVoid.mc.player.moveVertical = 10.0f;
                AntiVoid.mc.player.jump();
            }
            if (this.mode.getValue().equals(Mode.LAUNCH)) {
                AntiVoid.mc.player.moveVertical = 100.0f;
                AntiVoid.mc.player.jump();
            }
        } else {
            AntiVoid.mc.player.moveVertical = 0.0f;
        }
    }

    @Override
    public void onDisable() {
        AntiVoid.mc.player.moveVertical = 0.0f;
    }

    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue().equals(Mode.BOUNCE)) {
            return "Bounce";
        }
        if (this.mode.getValue().equals(Mode.LAUNCH)) {
            return "Launch";
        }
        return null;
    }

    public enum Mode {
        BOUNCE,
        LAUNCH

    }
}

