package me.madcat.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.features.command.Command;
import me.madcat.manager.FriendManager;

public class FriendCommand
extends Command {
    public FriendCommand() {
        super("friend", new String[]{"<add/del/name/clear>", "<name>"});
    }

    @Override
    public void execute(String[] stringArray) {
        if (stringArray.length == 1) {
            if (MadCat.friendManager.getFriends().isEmpty()) {
                FriendCommand.sendMessage("Friend list empty D:.");
            } else {
                StringBuilder string = new StringBuilder("Friends: ");
                for (FriendManager.Friend friend : MadCat.friendManager.getFriends()) {
                    try {
                        string.append(friend.getUsername()).append(", ");
                    }
                    catch (Exception exception) {}
                }
                FriendCommand.sendMessage(string.toString());
            }
            return;
        }
        if (stringArray.length == 2) {
            if (stringArray[0].equals("reset")) {
                MadCat.friendManager.onLoad();
                FriendCommand.sendMessage("Friends got reset.");
                return;
            }
            FriendCommand.sendMessage(stringArray[0] + (MadCat.friendManager.isFriend(stringArray[0]) ? " is friended." : " isn't friended."));
            return;
        }
        if (stringArray.length >= 2) {
            switch (stringArray[0]) {
                case "add": {
                    MadCat.friendManager.addFriend(stringArray[1]);
                    FriendCommand.sendMessage(ChatFormatting.GREEN + stringArray[1] + " has been friended");
                    return;
                }
                case "del": {
                    MadCat.friendManager.removeFriend(stringArray[1]);
                    FriendCommand.sendMessage(ChatFormatting.RED + stringArray[1] + " has been unfriended");
                    return;
                }
            }
            FriendCommand.sendMessage("Unknown Command, try friend add/del (name)");
        }
    }
}

