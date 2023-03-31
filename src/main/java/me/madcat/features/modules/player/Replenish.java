package me.madcat.features.modules.player;

import java.util.ArrayList;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.Timer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Replenish
extends Module {
    private final Setting<Integer> delay = this.register(new Setting<>("Delay", 0, 0, 10));
    private final Setting<Integer> Stack = this.register(new Setting<>("Stack", 50, 50, 64));
    private final Timer timer = new Timer();
    private final ArrayList<Item> Hotbar = new ArrayList();

    public Replenish() {
        super("Replenish", "Replenishes your hotbar", Module.Category.PLAYER);
    }

    @Override
    public void onEnable() {
        if (Replenish.fullNullCheck()) {
            return;
        }
        this.Hotbar.clear();
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Replenish.mc.player.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty() && !this.Hotbar.contains(itemStack.getItem())) {
                this.Hotbar.add(itemStack.getItem());
                continue;
            }
            this.Hotbar.add(Items.AIR);
        }
    }

    @Override
    public void onUpdate() {
        if (Replenish.mc.currentScreen != null) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValue() * 1000)) {
            return;
        }
        for (int i = 0; i < 9; ++i) {
            if (!this.RefillSlotIfNeed(i)) {
                continue;
            }
            this.timer.reset();
            return;
        }
    }

    private boolean RefillSlotIfNeed(int n) {
        ItemStack itemStack = Replenish.mc.player.inventory.getStackInSlot(n);
        if (itemStack.isEmpty() || itemStack.getItem() == Items.AIR) {
            return false;
        }
        if (!itemStack.isStackable()) {
            return false;
        }
        if (itemStack.getCount() >= itemStack.getMaxStackSize()) {
            return false;
        }
        if (itemStack.getCount() >= this.Stack.getValue()) {
            return false;
        }
        for (int i = 9; i < 36; ++i) {
            ItemStack itemStack2 = Replenish.mc.player.inventory.getStackInSlot(i);
            if (itemStack2.isEmpty() || !this.CanItemBeMergedWith(itemStack, itemStack2)) continue;
            Replenish.mc.playerController.windowClick(Replenish.mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, Replenish.mc.player);
            Replenish.mc.playerController.updateController();
            return true;
        }
        return false;
    }

    private boolean CanItemBeMergedWith(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack.getItem() == itemStack2.getItem() && itemStack.getDisplayName().equals(itemStack2.getDisplayName());
    }
}

