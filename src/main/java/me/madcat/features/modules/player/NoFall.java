package me.madcat.features.modules.player;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFall
extends Module {
    private static NoFall INSTANCE = new NoFall();
    private final Setting<Integer> distance = this.register(new Setting<>("Distance", 3, 0, 50));

    public NoFall() {
        super("NoFall", "Prevents fall damage", Module.Category.PLAYER);
        this.setInstance();
    }

    public static NoFall INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NoFall();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        if (NoFall.fullNullCheck()) {
            return;
        }
        if (send.getPacket() instanceof CPacketPlayer) {
            if (NoFall.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().equals(Items.ELYTRA)) {
                return;
            }
            if (NoFall.mc.player.fallDistance >= (float) this.distance.getValue()) {
                CPacketPlayer cPacketPlayer = send.getPacket();
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return "Packet";
    }
}

