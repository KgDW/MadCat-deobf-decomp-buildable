package me.madcat.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.List;
import java.util.UUID;
import me.madcat.features.command.Command;
import me.madcat.util.PlayerUtil;

public class HistoryCommand
extends Command {
    public HistoryCommand() {
        super("history", new String[]{"<player>"});
    }
    
    @Override
    public void execute(String[] stringArray) {
        block5: {
            List<String> list;
            UUID uUID;
            if (stringArray.length == 1 || stringArray.length == 0) {
                HistoryCommand.sendMessage(ChatFormatting.RED + "Please specify a player.");
            }
            try {
                uUID = PlayerUtil.getUUIDFromName(stringArray[0]);
            }
            catch (Exception exception) {
                HistoryCommand.sendMessage("An error occured.");
                return;
            }
            {
                list = PlayerUtil.getHistoryOfNames(uUID);
                if (list == null) break block5;
            }
            HistoryCommand.sendMessage(stringArray[0] + "Â´s name history:");
            for (String string : list) {
                HistoryCommand.sendMessage(string);
            }
            return;
        }
        HistoryCommand.sendMessage("No names found.");
    }
}

