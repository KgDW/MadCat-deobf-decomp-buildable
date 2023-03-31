package me.madcat.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.features.command.Command;

public class HelpCommand
extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] stringArray) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : MadCat.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + MadCat.commandManager.getPrefix() + command.getName());
        }
    }
}

