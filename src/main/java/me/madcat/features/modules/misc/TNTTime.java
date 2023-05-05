package me.madcat.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;

public class TNTTime
extends Module {
    public TNTTime() {
        super("TNTTime", "show tnt fuse", Module.Category.MISC);
    }

    @Override
    public void onUpdate() {
        for (Entity entity : TNTTime.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityTNTPrimed)) {
                continue;
            }
            String string = String.valueOf(ChatFormatting.GREEN);
            if ((double)((EntityTNTPrimed)entity).getFuse() / 20.0 > 0.0) {
                string = String.valueOf(ChatFormatting.DARK_RED);
            }
            if ((double)((EntityTNTPrimed)entity).getFuse() / 20.0 > 1.0) {
                string = String.valueOf(ChatFormatting.RED);
            }
            if ((double)((EntityTNTPrimed)entity).getFuse() / 20.0 > 2.0) {
                string = String.valueOf(ChatFormatting.YELLOW);
            }
            if ((double)((EntityTNTPrimed)entity).getFuse() / 20.0 > 3.0) {
                string = String.valueOf(ChatFormatting.GREEN);
            }
            entity.setCustomNameTag(string + String.valueOf((double) ((EntityTNTPrimed) entity).getFuse() / 20.0).substring(0, 3) + "s");
            entity.setAlwaysRenderNameTag(true);
        }
    }
}

