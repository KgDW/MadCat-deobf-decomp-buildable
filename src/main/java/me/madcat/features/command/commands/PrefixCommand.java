package me.madcat.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.features.command.Command;

public class PrefixCommand
extends Command {
    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length == 1) {
            Command.sendMessage(ChatFormatting.GREEN + "Current prefix is " + MadCat.commandManager.getPrefix());
            return;
        }
        MadCat.commandManager.setPrefix(stringArray[0]);
        Command.sendMessage("Prefix changed to " + ChatFormatting.GRAY + stringArray[0]);
    }
}

