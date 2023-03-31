package me.madcat.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import me.madcat.MadCat;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.misc.AutoEZ;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;

public class PopCounter
extends Module {
    public static final HashMap<String, Integer> TotemPopContainer = new HashMap();
    private static PopCounter INSTANCE = new PopCounter();

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops", Module.Category.MISC);
        this.setInstance();
    }

    public static PopCounter INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        TotemPopContainer.clear();
    }

    public void onDeath(EntityPlayer entityPlayer) {
        if (TotemPopContainer.containsKey(entityPlayer.getName())) {
            int n = TotemPopContainer.get(entityPlayer.getName());
            TotemPopContainer.remove(entityPlayer.getName());
            if (n == 1) {
                if (PopCounter.mc.player.equals(entityPlayer)) {
                    if (MadCat.moduleManager.isModuleEnabled("PopCounter")) {
                        Command.sendMessage(ChatFormatting.BLUE + "You died after popping " + ChatFormatting.RED + n + ChatFormatting.RED + " Totem!");
                    }
                    if (AutoEZ.INSTANCE().isEnabled() && AutoEZ.INSTANCE().whenSelf.getValue()) {
                        PopCounter.mc.player.connection.sendPacket(new CPacketChatMessage(AutoEZ.INSTANCE().SelfString.getValue()));
                    }
                } else {
                    if (MadCat.moduleManager.isModuleEnabled("PopCounter")) {
                        Command.sendMessage(ChatFormatting.RED + entityPlayer.getName() + " died after popping " + ChatFormatting.GREEN + n + ChatFormatting.RED + " Totem!");
                    }
                    if (AutoEZ.INSTANCE().isEnabled() && (!MadCat.friendManager.isFriend(entityPlayer.getName()) || AutoEZ.INSTANCE().whenFriend.getValue())) {
                        if (AutoEZ.INSTANCE().poped.getValue()) {
                            PopCounter.mc.player.connection.sendPacket(new CPacketChatMessage(AutoEZ.INSTANCE().EzString.getValue() + " " + entityPlayer.getName() + " popping" + n + " Totem!"));
                        } else {
                            PopCounter.mc.player.connection.sendPacket(new CPacketChatMessage(AutoEZ.INSTANCE().EzString.getValue() + " " + entityPlayer.getName()));
                        }
                    }
                }
            } else if (PopCounter.mc.player.equals(entityPlayer)) {
                if (MadCat.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.BLUE + "You died after popping " + ChatFormatting.RED + n + ChatFormatting.RED + " Totems!");
                }
                if (AutoEZ.INSTANCE().isEnabled() && AutoEZ.INSTANCE().whenSelf.getValue()) {
                    PopCounter.mc.player.connection.sendPacket(new CPacketChatMessage(AutoEZ.INSTANCE().SelfString.getValue()));
                }
            } else {
                if (MadCat.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.RED + entityPlayer.getName() + " died after popping " + ChatFormatting.GREEN + n + ChatFormatting.RED + " Totems!");
                }
                if (AutoEZ.INSTANCE().isEnabled() && (!MadCat.friendManager.isFriend(entityPlayer.getName()) || AutoEZ.INSTANCE().whenFriend.getValue())) {
                    if (AutoEZ.INSTANCE().poped.getValue()) {
                        PopCounter.mc.player.connection.sendPacket(new CPacketChatMessage(AutoEZ.INSTANCE().EzString.getValue() + " " + entityPlayer.getName() + " popping" + n + " Totem!"));
                    } else {
                        PopCounter.mc.player.connection.sendPacket(new CPacketChatMessage(AutoEZ.INSTANCE().EzString.getValue() + " " + entityPlayer.getName()));
                    }
                }
            }
        }
    }

    public void onTotemPop(EntityPlayer entityPlayer) {
        int n = 1;
        if (TotemPopContainer.containsKey(entityPlayer.getName())) {
            n = TotemPopContainer.get(entityPlayer.getName());
            TotemPopContainer.put(entityPlayer.getName(), ++n);
        } else {
            TotemPopContainer.put(entityPlayer.getName(), n);
        }
        if (n == 1) {
            if (PopCounter.mc.player.equals(entityPlayer)) {
                if (MadCat.moduleManager.isModuleEnabled("PopCounter")) {
                    Command.sendMessage(ChatFormatting.BLUE + "You popped " + ChatFormatting.RED + n + ChatFormatting.RED + " Totem.");
                }
            } else if (MadCat.moduleManager.isModuleEnabled("PopCounter")) {
                Command.sendMessage(ChatFormatting.RED + entityPlayer.getName() + " popped " + ChatFormatting.GREEN + n + ChatFormatting.RED + " Totem.");
            }
        } else if (PopCounter.mc.player.equals(entityPlayer)) {
            if (MadCat.moduleManager.isModuleEnabled("PopCounter")) {
                Command.sendMessage(ChatFormatting.BLUE + "You popped " + ChatFormatting.RED + n + ChatFormatting.RED + " Totems.");
            }
        } else if (MadCat.moduleManager.isModuleEnabled("PopCounter")) {
            Command.sendMessage(ChatFormatting.RED + entityPlayer.getName() + " popped " + ChatFormatting.GREEN + n + ChatFormatting.RED + " Totems.");
        }
    }
}

