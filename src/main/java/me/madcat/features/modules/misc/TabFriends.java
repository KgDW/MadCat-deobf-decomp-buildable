package me.madcat.features.modules.misc;

import me.madcat.MadCat;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.madcat.features.setting.Setting;
import me.madcat.features.modules.Module;

public class TabFriends extends Module
{
    public static String color;
    public static TabFriends INSTANCE;
    public static Setting<Boolean> prefix;
    public Setting<FriendColor> mode;

    public TabFriends() {
        super("TabFriends", "Renders your friends differently in the tablist", Category.MISC);
        this.mode = this.register(new Setting("Color", FriendColor.Green));
        TabFriends.prefix = this.register(new Setting("Prefix", true));
        TabFriends.INSTANCE = this;
    }

    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String name = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (!MadCat.friendManager.isFriend(name)) {
            return name;
        }
        if (TabFriends.prefix.getValue()) {
            return "§7[" + TabFriends.color + "F§7] " + TabFriends.color + name;
        }
        return TabFriends.color + name;
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case DarkRed: {
                TabFriends.color = "§4";
                break;
            }
            case Red: {
                TabFriends.color = "§c";
                break;
            }
            case Gold: {
                TabFriends.color = "§6";
                break;
            }
            case Yellow: {
                TabFriends.color = "§e";
                break;
            }
            case DarkGreen: {
                TabFriends.color = "§2";
                break;
            }
            case Green: {
                TabFriends.color = "§a";
                break;
            }
            case Aqua: {
                TabFriends.color = "§b";
                break;
            }
            case DarkAqua: {
                TabFriends.color = "§3";
                break;
            }
            case DarkBlue: {
                TabFriends.color = "§1";
                break;
            }
            case Blue: {
                TabFriends.color = "§9";
                break;
            }
            case LightPurple: {
                TabFriends.color = "§d";
                break;
            }
            case DarkPurple: {
                TabFriends.color = "§5";
                break;
            }
            case Gray: {
                TabFriends.color = "§7";
                break;
            }
            case DarkGray: {
                TabFriends.color = "§8";
                break;
            }
            case Black: {
                TabFriends.color = "§0";
                break;
            }
            case None: {
                TabFriends.color = "";
                break;
            }
        }
    }

    static {
        TabFriends.color = "";
    }

    private enum FriendColor
    {
        DarkRed,
        Red,
        Gold,
        Yellow,
        DarkGreen,
        Green,
        Aqua,
        DarkAqua,
        DarkBlue,
        Blue,
        LightPurple,
        DarkPurple,
        Gray,
        DarkGray,
        Black,
        None;
    }
}
