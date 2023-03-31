package me.madcat.features.modules.player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class FastElytra
extends Module {
    private static FastElytra INSTANCE = new FastElytra();
    private int preferredTotemSlot;
    private int numOfTotems;

    private boolean findTotems2() {
        this.numOfTotems = 0;
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(Integer.MIN_VALUE);
        FastElytra.getInventorySlots(9).forEach((arg_0, arg_1) -> this.findTotems21(atomicInteger, arg_0, arg_1));
        if (FastElytra.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.DIAMOND_CHESTPLATE)) {
            ++this.numOfTotems;
        }
        return this.numOfTotems != 0;
    }

    public FastElytra() {
        super("FastElytra", "Tweaks for wear Elytra", Module.Category.PLAYER);
        this.setInstance();
    }

    public static FastElytra INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new FastElytra();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (!FastElytra.mc.player.onGround) {
            if (this.findTotems() && !FastElytra.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
                FastElytra.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, FastElytra.mc.player);
                FastElytra.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, FastElytra.mc.player);
                FastElytra.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, FastElytra.mc.player);
            }
        } else if (this.findTotems2() && FastElytra.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
            FastElytra.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, FastElytra.mc.player);
            FastElytra.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, FastElytra.mc.player);
            FastElytra.mc.playerController.windowClick(0, this.preferredTotemSlot, 0, ClickType.PICKUP, FastElytra.mc.player);
        }
    }

    private boolean findTotems() {
        this.numOfTotems = 0;
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(Integer.MIN_VALUE);
        FastElytra.getInventorySlots(9).forEach((arg_0, arg_1) -> this.findTotems0(atomicInteger, arg_0, arg_1));
        if (FastElytra.mc.player.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.ELYTRA)) {
            ++this.numOfTotems;
        }
        return this.numOfTotems != 0;
    }

    private static Map<Integer, ItemStack> getInventorySlots(int n) {
        HashMap<Integer, ItemStack> hashMap = new HashMap<>();
        while (n <= 44) {
            hashMap.put(n, FastElytra.mc.player.inventoryContainer.getInventory().get(n));
            ++n;
        }
        return hashMap;
    }

    private void findTotems21(AtomicInteger atomicInteger, Integer n, ItemStack itemStack) {
        int n2 = 0;
        if (itemStack.getItem().equals(Items.DIAMOND_CHESTPLATE)) {
            n2 = itemStack.getCount();
            if (atomicInteger.get() < n2) {
                atomicInteger.set(n2);
                this.preferredTotemSlot = n;
            }
        }
        this.numOfTotems += n2;
    }

    private void findTotems0(AtomicInteger atomicInteger, Integer n, ItemStack itemStack) {
        int n2 = 0;
        if (itemStack.getItem().equals(Items.ELYTRA)) {
            n2 = itemStack.getCount();
            if (atomicInteger.get() < n2) {
                atomicInteger.set(n2);
                this.preferredTotemSlot = n;
            }
        }
        this.numOfTotems += n2;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

