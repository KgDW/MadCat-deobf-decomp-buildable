package me.madcat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.madcat.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil
implements Wrapper {
    public static void switchToHotbarSlot(Class clazz, boolean bl) {
        int n = InventoryUtil.findHotbarBlock(clazz);
        if (n > -1) {
            InventoryUtil.switchToHotbarSlot(n, bl);
        }
    }

    public static int findItemInventorySlot(Item item, boolean bl) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() != item) continue;
            if (entry.getKey() == 45 && !bl) {
                continue;
            }
            atomicInteger.set(entry.getKey());
            return atomicInteger.get();
        }
        return atomicInteger.get();
    }

    public static boolean isNull(ItemStack itemStack) {
        return itemStack == null || itemStack.getItem() instanceof ItemAir;
    }

    public static boolean isInstanceOf(ItemStack itemStack, Class clazz) {
        if (itemStack == null) {
            return false;
        }
        Item item = itemStack.getItem();
        if (clazz.isInstance(item)) {
            return true;
        }
        if (item instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(item);
            return clazz.isInstance(block);
        }
        return false;
    }

    public static int findArmorSlot(EntityEquipmentSlot entityEquipmentSlot, boolean bl) {
        int n = -1;
        float f = 0.0f;
        for (int i = 9; i < 45; ++i) {
            boolean bl2 = false;
            ItemArmor itemArmor;
            ItemStack itemStack = InventoryUtil.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.AIR || !(itemStack.getItem() instanceof ItemArmor) || (itemArmor = (ItemArmor)itemStack.getItem()).getEquipmentSlot() != entityEquipmentSlot) continue;
            float f2 = itemArmor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, itemStack);
            boolean bl3 = bl && EnchantmentHelper.hasBindingCurse(itemStack) || (bl2 = false);
            if (!(f2 > f) || bl2) continue;
            f = f2;
            n = i;
        }
        return n;
    }

    public static List<Integer> findEmptySlots(boolean bl) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> slot : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!slot.getValue().isEmpty() && slot.getValue().getItem() != Items.AIR) {
                continue;
            }
            arrayList.add(slot.getKey());
        }
        if (bl) {
            for (int i = 1; i < 5; ++i) {
                Slot slot = InventoryUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack itemStack = slot.getStack();
                if (!itemStack.isEmpty() && itemStack.getItem() != Items.AIR) {
                    continue;
                }
                arrayList.add(i);
            }
        }
        return arrayList;
    }

    public static int getItemHotbars(Item item) {
        for (int i = 0; i < 36; ++i) {
            Item item2 = InventoryUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (Item.getIdFromItem(item2) != Item.getIdFromItem(item)) {
                continue;
            }
            return i;
        }
        return -1;
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return InventoryUtil.getInventorySlots();
    }

    public static boolean isBlock(Item item, Class clazz) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).getBlock();
            return clazz.isInstance(block);
        }
        return false;
    }

    public static int findItemInHotbar(Item item) {
        int n = -1;
        for (int i = 0; i < 9; ++i) {
            if (InventoryUtil.mc.player.inventory.getStackInSlot(i).getItem() != item) continue;
            n = i;
            break;
        }
        return n;
    }

    public static int findHotbarBlock(Block block) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (itemStack == ItemStack.EMPTY || !(itemStack.getItem() instanceof ItemBlock)) continue;
            if (((ItemBlock)itemStack.getItem()).getBlock() != block) {
                continue;
            }
            return i;
        }
        return -1;
    }

    public static void switchToHotbarSlot(int n, boolean bl) {
        if (InventoryUtil.mc.player.inventory.currentItem == n || n < 0) {
            return;
        }
        if (bl) {
            InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(n));
            InventoryUtil.mc.playerController.updateController();
        } else {
            InventoryUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(n));
            InventoryUtil.mc.player.inventory.currentItem = n;
            InventoryUtil.mc.playerController.updateController();
        }
    }

    public static int getItemDurability(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        return itemStack.getMaxDamage() - itemStack.getItemDamage();
    }

    public static int getEmptyXCarry() {
        for (int i = 1; i < 5; ++i) {
            Slot slot = InventoryUtil.mc.player.inventoryContainer.inventorySlots.get(i);
            ItemStack itemStack = slot.getStack();
            if (!itemStack.isEmpty() && itemStack.getItem() != Items.AIR) {
                continue;
            }
            return i;
        }
        return -1;
    }

    public static boolean holdingItem(Class clazz) {
        ItemStack itemStack = InventoryUtil.mc.player.getHeldItemMainhand();
        boolean bl = InventoryUtil.isInstanceOf(itemStack, clazz);
        if (!bl) {
            bl = InventoryUtil.isInstanceOf(itemStack, clazz);
        }
        return bl;
    }

    public static int findArmorSlot(EntityEquipmentSlot entityEquipmentSlot, boolean bl, boolean bl2) {
        int n = InventoryUtil.findArmorSlot(entityEquipmentSlot, bl);
        if (n == -1 && bl2) {
            float f = 0.0f;
            for (int i = 1; i < 5; ++i) {
                boolean bl3 = false;
                ItemArmor itemArmor;
                Slot slot = InventoryUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack itemStack = slot.getStack();
                if (itemStack.getItem() == Items.AIR || !(itemStack.getItem() instanceof ItemArmor) || (itemArmor = (ItemArmor)itemStack.getItem()).getEquipmentSlot() != entityEquipmentSlot) continue;
                float f2 = itemArmor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, itemStack);
                boolean bl4 = bl && EnchantmentHelper.hasBindingCurse(itemStack) || (bl3 = false);
                if (!(f2 > f) || bl3) continue;
                f = f2;
                n = i;
            }
        }
        return n;
    }

    private static Map<Integer, ItemStack> getInventorySlots() {
        HashMap<Integer, ItemStack> hashMap = new HashMap<>();
        for (int i = 9; i <= 44; ++i) {
            hashMap.put(i, InventoryUtil.mc.player.inventoryContainer.getInventory().get(i));
        }
        return hashMap;
    }

    public static int findItemInventorySlot(Item item, boolean bl, boolean bl2) {
        int n = InventoryUtil.findItemInventorySlot(item, bl);
        if (n == -1 && bl2) {
            for (int i = 1; i < 5; ++i) {
                Slot slot = InventoryUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack itemStack = slot.getStack();
                if (itemStack.getItem() != Items.AIR && itemStack.getItem() == item) {
                    n = i;
                }
            }
        }
        return n;
    }

    public static int findAnyBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (itemStack == ItemStack.EMPTY || !(itemStack.getItem() instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }

    public static void switchToItem(Item item) {
        int n = InventoryUtil.getItemHotbar(item);
        if (n == -1) {
            return;
        }
        InventoryUtil.switchToHotbarSlot(n, false);
    }

    public static int getItemHotbar(Item item) {
        for (int i = 0; i < 9; ++i) {
            Item item2 = InventoryUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (Item.getIdFromItem(item2) != Item.getIdFromItem(item)) {
                continue;
            }
            return i;
        }
        return -1;
    }

    public static void switchToSlot(int n) {
        if (InventoryUtil.mc.player.inventory.currentItem == n || n < 0) {
            return;
        }
        InventoryUtil.mc.player.inventory.currentItem = n;
        InventoryUtil.mc.playerController.updateController();
    }

    public static int pickItem(int n) {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            if (Item.getIdFromItem(InventoryUtil.mc.player.inventory.mainInventory.get(i).getItem()) != n) continue;
            arrayList.add(InventoryUtil.mc.player.inventory.mainInventory.get(i));
        }
        if (arrayList.size() >= 1) {
            return InventoryUtil.mc.player.inventory.mainInventory.indexOf(arrayList.get(0));
        }
        return -1;
    }

    public static int findPistonBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (itemStack == ItemStack.EMPTY || !(itemStack.getItem() instanceof ItemBlock)) continue;
            if (!(((ItemBlock)itemStack.getItem()).getBlock() instanceof BlockPistonBase)) {
                continue;
            }
            return i;
        }
        return -1;
    }

    public static int findHotbarBlock(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (itemStack == ItemStack.EMPTY) {
                continue;
            }
            if (clazz.isInstance(itemStack.getItem())) {
                return i;
            }
            if (!(itemStack.getItem() instanceof ItemBlock)) continue;
            if (!clazz.isInstance(((ItemBlock)itemStack.getItem()).getBlock())) {
                continue;
            }
            return i;
        }
        return -1;
    }

    public static boolean isSlotEmpty(int n) {
        Slot slot = InventoryUtil.mc.player.inventoryContainer.inventorySlots.get(n);
        ItemStack itemStack = slot.getStack();
        return itemStack.isEmpty();
    }

    public static int findInventoryBlock(Class clazz, boolean bl) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!InventoryUtil.isBlock(entry.getValue().getItem(), clazz)) continue;
            if (entry.getKey() == 45 && !bl) {
                continue;
            }
            atomicInteger.set(entry.getKey());
            return atomicInteger.get();
        }
        return atomicInteger.get();
    }

    public static class Task {
        private final int slot;
        private final boolean update;
        private final boolean quickClick;

        public Task() {
            this.update = true;
            this.slot = -1;
            this.quickClick = false;
        }

        public Task(int n) {
            this.slot = n;
            this.quickClick = false;
            this.update = false;
        }

        public void run() {
            if (this.update) {
                Wrapper.mc.playerController.updateController();
            }
            if (this.slot != -1) {
                Wrapper.mc.playerController.windowClick(0, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, Wrapper.mc.player);
            }
        }

        public Task(int n, boolean bl) {
            this.slot = n;
            this.quickClick = bl;
            this.update = false;
        }
    }
}

