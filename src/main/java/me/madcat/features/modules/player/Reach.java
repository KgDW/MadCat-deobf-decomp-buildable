package me.madcat.features.modules.player;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;

public class Reach
extends Module {
    private final Setting<Integer> Reach = this.register(new Setting<>("Reach", 6, 5, 10));

    public Reach() {
        super("Reach", "reach", Module.Category.PLAYER);
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        me.madcat.features.modules.player.Reach.mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(this.Reach.getValue());
    }

    @Override
    public void onDisable() {
        me.madcat.features.modules.player.Reach.mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(5.0);
    }
}

