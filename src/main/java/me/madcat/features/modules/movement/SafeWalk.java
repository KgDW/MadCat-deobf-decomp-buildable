package me.madcat.features.modules.movement;

import me.madcat.event.events.MoveEvent;
import me.madcat.features.modules.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SafeWalk
extends Module {
    public SafeWalk() {
        super("SafeWalk", "stop at the edge", Module.Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onMove(MoveEvent moveEvent) {
        double d = moveEvent.getX();
        double d2 = moveEvent.getY();
        double d3 = moveEvent.getZ();
        if (SafeWalk.mc.player.onGround) {
            double d4 = 0.05;
            while (d != 0.0 && this.isOffsetBBEmpty(d, -1.0, 0.0)) {
                if (d < d4 && d >= -d4) {
                    d = 0.0;
                    continue;
                }
                if (d > 0.0) {
                    d -= d4;
                    continue;
                }
                d += d4;
            }
            while (d3 != 0.0 && this.isOffsetBBEmpty(0.0, -1.0, d3)) {
                if (d3 < d4 && d3 >= -d4) {
                    d3 = 0.0;
                    continue;
                }
                if (d3 > 0.0) {
                    d3 -= d4;
                    continue;
                }
                d3 += d4;
            }
            while (d != 0.0 && d3 != 0.0 && this.isOffsetBBEmpty(d, -1.0, d3)) {
                double d5 = d < d4 && d >= -d4 ? 0.0 : (d = d > 0.0 ? d - d4 : d + d4);
                if (d3 < d4 && d3 >= -d4) {
                    d3 = 0.0;
                    continue;
                }
                if (d3 > 0.0) {
                    d3 -= d4;
                    continue;
                }
                d3 += d4;
            }
        }
        moveEvent.setX(d);
        moveEvent.setY(d2);
        moveEvent.setZ(d3);
    }

    public boolean isOffsetBBEmpty(double d, double d2, double d3) {
        EntityPlayerSP entityPlayerSP = SafeWalk.mc.player;
        return SafeWalk.mc.world.getCollisionBoxes(entityPlayerSP, entityPlayerSP.getEntityBoundingBox().offset(d, d2, d3)).isEmpty();
    }
}

