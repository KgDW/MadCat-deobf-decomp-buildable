package me.madcat.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class NickHider
extends Module {
    private static NickHider instance;
    public final Setting<String> NameString = this.register(new Setting<>("Name", "New Name Here..."));

    public NickHider() {
        super("NameChanger", "Changes name", Module.Category.MISC);
        instance = this;
    }

    public static NickHider INSTANCE() {
        if (instance == null) {
            instance = new NickHider();
        }
        return instance;
    }

    @Override
    public void onEnable() {
        Command.sendMessage(ChatFormatting.GRAY + "Success! Name succesfully changed to " + ChatFormatting.GREEN + this.NameString.getValue());
    }
}

