package me.madcat.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.features.modules.Module;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class FakeKick
extends Module {
    public FakeKick() {
        super("FakeKick", "Say you are get banned", Module.Category.MISC);
    }

    @Override
    public void onEnable() {
        if (mc.isSingleplayer()) {
            this.disable();
            return;
        }
        if (mc.getConnection() != null) {
            mc.getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString(ChatFormatting.RED + "You are get banned")));
        }
        this.disable();
    }
}

