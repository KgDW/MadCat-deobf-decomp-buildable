package me.madcat.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

public class MCF
extends Module {
    private boolean clicked = false;

    public MCF() {
        super("MCF", "Middleclick Friends", Module.Category.MISC);
    }

    @Override
    public void onUpdate() {
        if (MCF.fullNullCheck()) {
            return;
        }
        if (MCF.mc.currentScreen == null && Mouse.isButtonDown(2)) {
            if (!this.clicked && MCF.mc.currentScreen == null) {
                this.onClick();
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    private void onClick() {
        Entity entity;
        RayTraceResult rayTraceResult = MCF.mc.objectMouseOver;
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY && (entity = rayTraceResult.entityHit) instanceof EntityPlayer) {
            if (MadCat.friendManager.isFriend(entity.getName())) {
                MadCat.friendManager.removeFriend(entity.getName());
                Command.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " has been unfriended.");
            } else {
                MadCat.friendManager.addFriend(entity.getName());
                Command.sendMessage(ChatFormatting.AQUA + entity.getName() + ChatFormatting.AQUA + " has been friended.");
            }
        }
        this.clicked = true;
    }
}

