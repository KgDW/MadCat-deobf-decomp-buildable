package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.play.client.CPacketChatMessage;

public class AutoKit
extends Module {
    boolean needKit = false;
    public final Setting<String> Name = this.register(new Setting<>("KitName", "1"));

    @Override
    public void onUpdate() {
        if (AutoKit.mc.currentScreen instanceof GuiGameOver) {
            this.needKit = true;
        } else if (this.needKit) {
            AutoKit.mc.player.connection.sendPacket(new CPacketChatMessage("/kit " + this.Name.getValue()));
            this.needKit = false;
        }
    }

    public AutoKit() {
        super("AutoKit", "Auto select kit", Module.Category.MISC);
    }
}

