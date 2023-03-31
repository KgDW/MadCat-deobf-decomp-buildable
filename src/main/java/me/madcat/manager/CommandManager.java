package me.madcat.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.LinkedList;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.command.commands.BindCommand;
import me.madcat.features.command.commands.ConfigCommand;
import me.madcat.features.command.commands.FriendCommand;
import me.madcat.features.command.commands.HelpCommand;
import me.madcat.features.command.commands.HistoryCommand;
import me.madcat.features.command.commands.ModuleCommand;
import me.madcat.features.command.commands.PrefixCommand;
import me.madcat.features.command.commands.ReloadSoundCommand;

public class CommandManager
extends Feature {
    private String clientMessage = "<MadCat>";
    private String prefix = ".";
    private final ArrayList<Command> commands = new ArrayList();

    private static String strip(String string) {
        if (string.startsWith("\"") && string.endsWith("\"")) {
            return string.substring("\"".length(), string.length() - "\"".length());
        }
        return string;
    }

    public void setPrefix(String string) {
        this.prefix = string;
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public Command getCommandByName(String string) {
        for (Command command : this.commands) {
            if (!command.getName().equals(string)) {
                continue;
            }
            return command;
        }
        return null;
    }

    public void setClientMessage(String string) {
        this.clientMessage = string;
    }

    public void executeCommand(String string) {
        String[] stringArray = string.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String string2 = stringArray[0].substring(1);
        String[] stringArray2 = CommandManager.removeElement(stringArray, 0);
        for (int i = 0; i < stringArray2.length; ++i) {
            if (stringArray2[i] == null) {
                continue;
            }
            stringArray2[i] = CommandManager.strip(stringArray2[i]);
        }
        for (Command command : this.commands) {
            if (!command.getName().equalsIgnoreCase(string2)) {
                continue;
            }
            command.execute(stringArray);
            return;
        }
        Command.sendMessage(ChatFormatting.GRAY + "Command not found, type 'help' for the commands list.");
    }

    public String getPrefix() {
        return this.prefix;
    }

    public CommandManager() {
        super("Command");
        this.commands.add(new BindCommand());
        this.commands.add(new ModuleCommand());
        this.commands.add(new PrefixCommand());
        this.commands.add(new ConfigCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ReloadSoundCommand());
        this.commands.add(new HistoryCommand());
    }

    public String getClientMessage() {
        return this.clientMessage;
    }

    public static String[] removeElement(String[] stringArray, int n) {
        LinkedList<String> linkedList = new LinkedList<>();
        for (int i = 0; i < stringArray.length; ++i) {
            if (i == n) {
                continue;
            }
            linkedList.add(stringArray[i]);
        }
        return linkedList.toArray(stringArray);
    }
}

