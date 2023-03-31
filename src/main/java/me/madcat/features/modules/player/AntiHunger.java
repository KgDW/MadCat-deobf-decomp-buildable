package me.madcat.features.modules.player;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiHunger extends Module
{
    public final Setting<Boolean> ground;

    public AntiHunger() {
        super("AntiHunger", "Prevents you from getting Hungry", Category.PLAYER);
        this.ground = this.register(new Setting("Ground", true));
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send send) {
    }
}
 