package me.madcat.features.modules.misc;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.modules.Module;
import me.madcat.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoFish
extends Module {
    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive receive) {
        int n;
        SPacketSoundEffect sPacketSoundEffect;
        if (AutoFish.fullNullCheck()) {
            return;
        }
        if (receive.getPacket() instanceof SPacketSoundEffect && (sPacketSoundEffect = receive.getPacket()).getCategory() == SoundCategory.NEUTRAL && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH && (n = InventoryUtil.findItemInHotbar(Items.FISHING_ROD)) != -1) {
            int n2 = AutoFish.mc.player.inventory.currentItem;
            AutoFish.mc.player.connection.sendPacket(new CPacketHeldItemChange(n));
            AutoFish.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
            AutoFish.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
            if (n2 != -1) {
                mc.getConnection().sendPacket(new CPacketHeldItemChange(n2));
            }
        }
    }

    public AutoFish() {
        super("AutoFish", "auto fishing", Module.Category.MISC);
    }
}

