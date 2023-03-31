package me.madcat.features.modules.player;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoArmor
extends Module {
    private int numOfTotems;
    private int preferredTotemSlot;

    public AutoArmor() {
        super("AutoArmor", "Tweaks for wear armors", Module.Category.PLAYER);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int n) {
        HashMap<Integer, ItemStack> hashMap = new HashMap<>();
        while (n <= 44) {
            hashMap.put(n, AutoArmor.mc.player.inventoryContainer.getInventory().get(n));
            ++n;
        }
        return hashMap;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_HELMET, 5)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 5, 0, ClickType.PICKUP, AutoArmor.mc.player);
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_CHESTPLATE, 6)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, AutoArmor.mc.player);
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_LEGGINGS, 7)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 7, 0, ClickType.PICKUP, AutoArmor.mc.player);
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack().getItem().equals(Items.AIR) && this.findTotems(Items.DIAMOND_BOOTS, 8)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 8, 0, ClickType.PICKUP, AutoArmor.mc.player);
            return;
        }
        if (AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA) && AutoArmor.mc.player.onGround && !FastElytra.INSTANCE().isEnabled() && this.findTotems(Items.DIAMOND_CHESTPLATE, 6)) {
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, AutoArmor.mc.player);
            AutoArmor.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, AutoArmor.mc.player);
        }
    }

    private boolean findTotems(ItemArmor itemArmor, int n) {
        this.numOfTotems = 0;
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(Integer.MIN_VALUE);
        AutoArmor.getInventorySlots(9).forEach((arg_0, arg_1) -> this.findTotems(itemArmor, atomicInteger, arg_0, arg_1));
        if (AutoArmor.mc.player.inventoryContainer.getSlot(n).getStack().getItem().equals(Items.DIAMOND_HELMET)) {
            ++this.numOfTotems;
        }
        return this.numOfTotems != 0;
    }

    private void findTotems(ItemArmor itemArmor, AtomicInteger atomicInteger, Integer n, ItemStack itemStack) {
        int n2 = 0;
        if (itemStack.getItem().equals(itemArmor)) {
            n2 = itemStack.getCount();
            if (atomicInteger.get() < n2) {
                atomicInteger.set(n2);
                this.preferredTotemSlot = n;
            }
        }
        this.numOfTotems += n2;
    }
}

