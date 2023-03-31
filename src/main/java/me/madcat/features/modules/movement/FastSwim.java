package me.madcat.features.modules.movement;

import me.madcat.event.events.MoveEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastSwim
extends Module {
    public final Setting<Double> waterHorizontal = this.register(new Setting<>("WaterHorizontal", 3.0, 1.0, 20.0));
    public final Setting<Double> waterVertical = this.register(new Setting<>("WaterVertical", 3.0, 1.0, 20.0));
    public final Setting<Double> lavaHorizontal = this.register(new Setting<>("LavaHorizontal", 4.0, 1.0, 20.0));
    public final Setting<Double> lavaVertical = this.register(new Setting<>("LavaVertical", 4.0, 1.0, 20.0));

    public FastSwim() {
        super("FastSwim", "Swim fast", Module.Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onMove(MoveEvent moveEvent) {
        if (FastSwim.mc.player.isInLava() && !FastSwim.mc.player.onGround) {
            moveEvent.setX(moveEvent.getX() * this.lavaHorizontal.getValue());
            moveEvent.setZ(moveEvent.getZ() * this.lavaHorizontal.getValue());
            moveEvent.setY(moveEvent.getY() * this.lavaVertical.getValue());
        } else if (FastSwim.mc.player.isInWater() && !FastSwim.mc.player.onGround) {
            moveEvent.setX(moveEvent.getX() * this.waterHorizontal.getValue());
            moveEvent.setZ(moveEvent.getZ() * this.waterHorizontal.getValue());
            moveEvent.setY(moveEvent.getY() * this.waterVertical.getValue());
        }
    }
}

