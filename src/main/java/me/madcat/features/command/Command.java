package me.madcat.features.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.modules.client.Notifications;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

public abstract class Command
extends Feature {
    protected final String name;
    protected final String[] commands;

    public Command(String string) {
        super(string);
        this.name = string;
        this.commands = new String[]{""};
    }

    public Command(String string, String[] stringArray) {
        super(string);
        this.name = string;
        this.commands = stringArray;
    }

    public static void sendMessage(String string) {
        Command.sendSilentMessage(MadCat.commandManager.getClientMessage() + " " + ChatFormatting.GRAY + string);
        Notifications.notifyList.add(new Notifications.Notifys(string));
    }

    public static void sendSilentMessage(String string) {
        if (Command.nullCheck()) {
            return;
        }
        Command.mc.player.sendMessage(new ChatMessage(string));
    }

    public static String getCommandPrefix() {
        return MadCat.commandManager.getPrefix();
    }

    public static char coolLineThing() {
        return '§';
    }

    public abstract void execute(String[] var1);

    @Override
    public String getName() {
        return this.name;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public static class ChatMessage
    extends TextComponentBase {
        private final String text;

        public ChatMessage(String string) {
            Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher matcher = pattern.matcher(string);
            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                String string2 = matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, string2);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }

        public String getUnformattedComponentText() {
            return this.text;
        }

        public ITextComponent createCopy() {
            return null;
        }

        public ITextComponent shallowCopy() {
            return new ChatMessage(this.text);
        }
    }
}

