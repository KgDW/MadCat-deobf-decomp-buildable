package me.madcat.features.modules.player;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.world.GameType;
import org.lwjgl.input.Mouse;

public class AntiEatDesync
extends Module {
    private final Setting<Boolean> update = this.register(new Setting<>("Update", true));
    private final Setting<Boolean> packetUpdate = this.register(new Setting<>("PacketUpdate", true));

    public AntiEatDesync() {
        super("EatSync", "0d4 is gay", Module.Category.PLAYER);
    }

    @Override
    public void onUpdate() {
        if (AntiEatDesync.mc.currentScreen == null && Mouse.isButtonDown(1) && AntiEatDesync.mc.playerController.getCurrentGameType() != GameType.SPECTATOR && AntiEatDesync.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFood) {
            if (this.update.getValue()) {
                AntiEatDesync.mc.playerController.updateController();
            }
            if (this.packetUpdate.getValue()) {
                AntiEatDesync.mc.player.connection.sendPacket(new CPacketHeldItemChange(AntiEatDesync.mc.player.inventory.currentItem));
            }
            AntiEatDesync.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }
}

