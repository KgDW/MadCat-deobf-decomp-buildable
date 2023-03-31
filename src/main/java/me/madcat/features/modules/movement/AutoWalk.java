package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoWalk
extends Module {
    public AutoWalk() {
        super("AutoWalk", "AutoForwardMove", Module.Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent inputUpdateEvent) {
        inputUpdateEvent.getMovementInput().moveForward = 1.0f;
    }
}

