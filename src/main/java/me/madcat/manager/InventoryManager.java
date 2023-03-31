package me.madcat.manager;

import me.madcat.util.Wrapper;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryManager
implements Wrapper {
    public int currentPlayerItem;
    private int recoverySlot = -1;

    public void recoverSilent(int n) {
        this.recoverySlot = n;
    }

    public void update() {
        if (this.recoverySlot != -1) {
            InventoryManager.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.recoverySlot == 8 ? 7 : this.recoverySlot + 1));
            InventoryManager.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.recoverySlot));
            int n = InventoryManager.mc.player.inventory.currentItem = this.recoverySlot;
            if (n != this.currentPlayerItem) {
                this.currentPlayerItem = n;
                InventoryManager.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.currentPlayerItem));
            }
            this.recoverySlot = -1;
        }
    }
}

