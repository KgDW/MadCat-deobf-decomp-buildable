package me.madcat.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Bind;
import me.madcat.manager.ModuleManager;
import org.lwjgl.input.Keyboard;

public class BindCommand
extends Command {
    public BindCommand() {
        super("bind", new String[]{"<module>", "<bind>"});
    }

    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length == 1) {
            BindCommand.sendMessage("Please specify a module.");
            return;
        }
        String string = stringArray[1];
        String string2 = stringArray[0];
        Module module = ModuleManager.getModuleByName(string2);
        if (module == null) {
            BindCommand.sendMessage("Unknown module '" + string2 + "'!");
            return;
        }
        if (string == null) {
            BindCommand.sendMessage(module.getName() + " is bound to " + ChatFormatting.GRAY + module.getBind().toString());
            return;
        }
        int n = Keyboard.getKeyIndex(string.toUpperCase());
        if (string.equalsIgnoreCase("none")) {
            n = -1;
        }
        if (n == 0) {
            BindCommand.sendMessage("Unknown key '" + string + "'!");
            return;
        }
        module.bind.setValue(new Bind(n));
        BindCommand.sendMessage("Bind for " + ChatFormatting.GREEN + module.getName() + ChatFormatting.WHITE + " set to " + ChatFormatting.GRAY + string.toUpperCase());
    }
}

