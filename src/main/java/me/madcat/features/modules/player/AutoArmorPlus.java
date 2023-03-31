package me.madcat.features.modules.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.gui.Gui;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.player.FastElytra;
import me.madcat.features.modules.player.XCarry;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.Setting;
import me.madcat.util.DamageUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.MathUtil;
import me.madcat.util.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class AutoArmorPlus
extends Module {
    private final Setting<Integer> delay = this.register(new Setting<>("Delay", 50, 0, 500));
    private final Setting<Boolean> mendingTakeOff = this.register(new Setting<>("AutoMend", false));
    private final Setting<Integer> closestEnemy = this.register(new Setting<>("Enemy", 8, 1, 20, this::new0));
    private final Setting<Integer> helmetThreshold = this.register(new Setting<>("Helmet%", 80, 1, 100, this::new1));
    private final Setting<Integer> chestThreshold = this.register(new Setting<>("Chest%", 80, 1, 100, this::new2));
    private final Setting<Integer> legThreshold = this.register(new Setting<>("Legs%", 80, 1, 100, this::new3));
    private final Setting<Integer> bootsThreshold = this.register(new Setting<>("Boots%", 80, 1, 100, this::new4));
    private final Setting<Boolean> curse = this.register(new Setting<>("CurseOfBinding", false));
    private final Setting<Integer> actions = this.register(new Setting<>("Actions", 3, 1, 12));
    private final Setting<Bind> elytraBind = this.register(new Setting<>("Elytra", new Bind(-1)));
    private final Setting<Boolean> tps = this.register(new Setting<>("TpsSync", true));
    private final Setting<Boolean> updateController = this.register(new Setting<>("Update", true));
    private final Setting<Boolean> shiftClick = this.register(new Setting<>("ShiftClick", false));
    private final Timer timer = new Timer();
    private final Timer elytraTimer = new Timer();
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
    private final List<Integer> doneSlots = new ArrayList<>();
    private boolean elytraOn = false;

    public AutoArmorPlus() {
        super("AutoArmor+", "Puts Armor on for you", Module.Category.PLAYER);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent keyInputEvent) {
        if (Keyboard.getEventKeyState() && !(AutoArmorPlus.mc.currentScreen instanceof Gui) && this.elytraBind.getValue().getKey() == Keyboard.getEventKey()) {
            this.elytraOn = !this.elytraOn;
        }
    }

    @Override
    public void onLogin() {
        this.timer.reset();
        this.elytraTimer.reset();
    }

    @Override
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.elytraOn = false;
    }

    @Override
    public void onLogout() {
        this.taskList.clear();
        this.doneSlots.clear();
    }

    @Override
    public void onTick() {
        if (Feature.fullNullCheck() || AutoArmorPlus.mc.currentScreen instanceof GuiContainer && !(AutoArmorPlus.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.taskList.isEmpty()) {
            int n;
            int n2;
            ItemStack itemStack;
            int n3;
            if (this.mendingTakeOff.getValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && AutoArmorPlus.mc.gameSettings.keyBindUseItem.isKeyDown() && (this.isSafe() || EntityUtil.isSafe(AutoArmorPlus.mc.player, 1, false))) {
                ItemStack itemStack2;
                ItemStack itemStack3;
                ItemStack itemStack4 = AutoArmorPlus.mc.player.inventoryContainer.getSlot(5).getStack();
                if (!itemStack4.isEmpty() && DamageUtil.getRoundedDamage(itemStack4) >= this.helmetThreshold.getValue()) {
                    this.takeOffSlot(5);
                }
                if (!FastElytra.INSTANCE().isEnabled() && !(itemStack3 = AutoArmorPlus.mc.player.inventoryContainer.getSlot(6).getStack()).isEmpty() && DamageUtil.getRoundedDamage(itemStack3) >= this.chestThreshold.getValue()) {
                    this.takeOffSlot(6);
                }
                if (!(itemStack3 = AutoArmorPlus.mc.player.inventoryContainer.getSlot(7).getStack()).isEmpty() && DamageUtil.getRoundedDamage(itemStack3) >= this.legThreshold.getValue()) {
                    this.takeOffSlot(7);
                }
                if (!(itemStack2 = AutoArmorPlus.mc.player.inventoryContainer.getSlot(8).getStack()).isEmpty() && DamageUtil.getRoundedDamage(itemStack2) >= this.bootsThreshold.getValue()) {
                    this.takeOffSlot(8);
                }
                return;
            }
            ItemStack itemStack5 = AutoArmorPlus.mc.player.inventoryContainer.getSlot(5).getStack();
            if (itemStack5.getItem() == Items.AIR && (n3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, this.curse.getValue(), XCarry.INSTANCE().isOn())) != -1) {
                this.getSlotOn(5, n3);
            }
            if ((itemStack = AutoArmorPlus.mc.player.inventoryContainer.getSlot(6).getStack()).getItem() == Items.AIR) {
                if (this.taskList.isEmpty()) {
                    if (this.elytraOn && this.elytraTimer.passedMs(500L)) {
                        n2 = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, XCarry.INSTANCE().isOn());
                        if (n2 != -1) {
                            if (n2 < 5 && n2 > 1 || !this.shiftClick.getValue()) {
                                this.taskList.add(new InventoryUtil.Task(n2));
                                this.taskList.add(new InventoryUtil.Task(6));
                            } else {
                                this.taskList.add(new InventoryUtil.Task(n2, true));
                            }
                            if (this.updateController.getValue()) {
                                this.taskList.add(new InventoryUtil.Task());
                            }
                            this.elytraTimer.reset();
                        }
                    } else if (!this.elytraOn && (n2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, this.curse.getValue(), XCarry.INSTANCE().isOn())) != -1) {
                        this.getSlotOn(6, n2);
                    }
                }
            } else if (this.elytraOn && itemStack.getItem() != Items.ELYTRA && this.elytraTimer.passedMs(500L)) {
                if (this.taskList.isEmpty()) {
                    n2 = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, XCarry.INSTANCE().isOn());
                    if (n2 != -1) {
                        this.taskList.add(new InventoryUtil.Task(n2));
                        this.taskList.add(new InventoryUtil.Task(6));
                        this.taskList.add(new InventoryUtil.Task(n2));
                        if (this.updateController.getValue()) {
                            this.taskList.add(new InventoryUtil.Task());
                        }
                    }
                    this.elytraTimer.reset();
                }
            } else if (!this.elytraOn && itemStack.getItem() == Items.ELYTRA && this.elytraTimer.passedMs(500L) && this.taskList.isEmpty()) {
                n2 = InventoryUtil.findItemInventorySlot(Items.DIAMOND_CHESTPLATE, false, XCarry.INSTANCE().isOn());
                if (n2 == -1 && (n2 = InventoryUtil.findItemInventorySlot(Items.IRON_CHESTPLATE, false, XCarry.INSTANCE().isOn())) == -1 && (n2 = InventoryUtil.findItemInventorySlot(Items.GOLDEN_CHESTPLATE, false, XCarry.INSTANCE().isOn())) == -1 && (n2 = InventoryUtil.findItemInventorySlot(Items.CHAINMAIL_CHESTPLATE, false, XCarry.INSTANCE().isOn())) == -1) {
                    n2 = InventoryUtil.findItemInventorySlot(Items.LEATHER_CHESTPLATE, false, XCarry.INSTANCE().isOn());
                }
                if (n2 != -1) {
                    this.taskList.add(new InventoryUtil.Task(n2));
                    this.taskList.add(new InventoryUtil.Task(6));
                    this.taskList.add(new InventoryUtil.Task(n2));
                    if (this.updateController.getValue()) {
                        this.taskList.add(new InventoryUtil.Task());
                    }
                }
                this.elytraTimer.reset();
            }
            if (AutoArmorPlus.mc.player.inventoryContainer.getSlot(7).getStack().getItem() == Items.AIR && (n2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, this.curse.getValue(), XCarry.INSTANCE().isOn())) != -1) {
                this.getSlotOn(7, n2);
            }
            if (AutoArmorPlus.mc.player.inventoryContainer.getSlot(8).getStack().getItem() == Items.AIR && (n = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, this.curse.getValue(), XCarry.INSTANCE().isOn())) != -1) {
                this.getSlotOn(8, n);
            }
        }
        if (this.timer.passedMs((int)((float) this.delay.getValue() * (this.tps.getValue() ? MadCat.serverManager.getTpsFactor() : 1.0f)))) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < this.actions.getValue(); ++i) {
                    InventoryUtil.Task task = this.taskList.poll();
                    if (task == null) continue;
                    task.run();
                }
            }
            this.timer.reset();
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.elytraOn) {
            return "Elytra";
        }
        return null;
    }

    private void takeOffSlot(int n) {
        if (this.taskList.isEmpty()) {
            int n2 = -1;
            for (int n3 : InventoryUtil.findEmptySlots(XCarry.INSTANCE().isOn())) {
                if (this.doneSlots.contains(n2)) continue;
                n2 = n3;
                this.doneSlots.add(n3);
            }
            if (n2 != -1) {
                if (n2 < 5 && n2 > 0 || !this.shiftClick.getValue()) {
                    this.taskList.add(new InventoryUtil.Task(n));
                    this.taskList.add(new InventoryUtil.Task(n2));
                } else {
                    this.taskList.add(new InventoryUtil.Task(n, true));
                }
                if (this.updateController.getValue()) {
                    this.taskList.add(new InventoryUtil.Task());
                }
            }
        }
    }

    private void getSlotOn(int n, int n2) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object)n2);
            if (n2 < 5 && n2 > 0 || !this.shiftClick.getValue()) {
                this.taskList.add(new InventoryUtil.Task(n2));
                this.taskList.add(new InventoryUtil.Task(n));
            } else {
                this.taskList.add(new InventoryUtil.Task(n2, true));
            }
            if (this.updateController.getValue()) {
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }

    private boolean isSafe() {
        EntityPlayer entityPlayer = EntityUtil.getClosestEnemy(this.closestEnemy.getValue());
        return entityPlayer == null || AutoArmorPlus.mc.player.getDistanceSq(entityPlayer) >= MathUtil.square(this.closestEnemy.getValue());
    }

    private boolean new4(Integer n) {
        return this.mendingTakeOff.getValue();
    }

    private boolean new3(Integer n) {
        return this.mendingTakeOff.getValue();
    }

    private boolean new2(Integer n) {
        return this.mendingTakeOff.getValue();
    }

    private boolean new1(Integer n) {
        return this.mendingTakeOff.getValue();
    }

    private boolean new0(Integer n) {
        return this.mendingTakeOff.getValue();
    }
}

