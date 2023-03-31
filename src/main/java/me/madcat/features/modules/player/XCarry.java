package me.madcat.features.modules.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import me.madcat.event.events.ClientEvent;
import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.gui.Gui;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.Setting;
import me.madcat.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class XCarry
extends Module {
    private static XCarry INSTANCE = new XCarry();
    private final Setting<Boolean> simpleMode = this.register(new Setting<>("Simple", false));
    private final Setting<Bind> autoStore = this.register(new Setting<>("AutoDuel", new Bind(-1)));
    private final Setting<Integer> obbySlot = this.register(new Setting<Object>("ObbySlot", 2, 1, 9, this::new0));
    private final Setting<Integer> slot1 = this.register(new Setting<Object>("Slot1", 22, 9, 44, this::new1));
    private final Setting<Integer> slot2 = this.register(new Setting<Object>("Slot2", 23, 9, 44, this::new2));
    private final Setting<Integer> slot3 = this.register(new Setting<Object>("Slot3", 24, 9, 44, this::new3));
    private final Setting<Integer> tasks = this.register(new Setting<Object>("Actions", 3, 1, 12, this::new4));
    private final Setting<Boolean> shiftClicker = this.register(new Setting<>("ShiftClick", false));
    private final Setting<Boolean> withShift = this.register(new Setting<Object>("WithShift", Boolean.TRUE, this::new5));
    private final Setting<Bind> keyBind = this.register(new Setting<Object>("ShiftBind", new Bind(-1), this::new6));
    private final AtomicBoolean guiNeedsClose = new AtomicBoolean(false);
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
    private GuiInventory openedGui = null;
    private boolean guiCloseGuard = false;
    private boolean autoDuelOn = false;
    private boolean obbySlotDone = false;
    private boolean slot1done = false;
    private boolean slot2done = false;
    private boolean slot3done = false;
    private List<Integer> doneSlots = new ArrayList<>();

    public XCarry() {
        super("XCarry", "Uses the crafting inventory for storage", Module.Category.PLAYER);
        this.setInstance();
    }

    public static XCarry INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new XCarry();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.shiftClicker.getValue() && XCarry.mc.currentScreen instanceof GuiInventory) {
            Slot slot;
            boolean bl;
            boolean bl2 = bl = this.keyBind.getValue().getKey() != -1 && Keyboard.isKeyDown(this.keyBind.getValue().getKey()) && !Keyboard.isKeyDown(42);
            if ((Keyboard.isKeyDown(42) && this.withShift.getValue() || bl) && Mouse.isButtonDown(0) && (slot = ((GuiInventory) XCarry.mc.currentScreen).getSlotUnderMouse()) != null && InventoryUtil.getEmptyXCarry() != -1) {
                int n = slot.slotNumber;
                if (n > 4 && bl) {
                    this.taskList.add(new InventoryUtil.Task(n));
                    this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
                } else if (n > 4 && this.withShift.getValue()) {
                    boolean bl3 = true;
                    boolean bl4 = true;
                    for (int n2 : InventoryUtil.findEmptySlots(false)) {
                        if (n2 > 4 && n2 < 36) {
                            bl4 = false;
                            continue;
                        }
                        if (n2 <= 35 || n2 >= 45) continue;
                        bl3 = false;
                    }
                    if (n > 35 && n < 45) {
                        if (bl4) {
                            this.taskList.add(new InventoryUtil.Task(n));
                            this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
                        }
                    } else if (bl3) {
                        this.taskList.add(new InventoryUtil.Task(n));
                        this.taskList.add(new InventoryUtil.Task(InventoryUtil.getEmptyXCarry()));
                    }
                }
            }
        }
        if (this.autoDuelOn) {
            this.doneSlots = new ArrayList<>();
            if (InventoryUtil.getEmptyXCarry() == -1 || this.obbySlotDone && this.slot1done && this.slot2done && this.slot3done) {
                this.autoDuelOn = false;
            }
            if (this.autoDuelOn) {
                if (!this.obbySlotDone && !XCarry.mc.player.inventory.getStackInSlot(this.obbySlot.getValue() - 1).isEmpty()) {
                    this.addTasks(36 + this.obbySlot.getValue() - 1);
                }
                this.obbySlotDone = true;
                if (!this.slot1done && !XCarry.mc.player.inventoryContainer.inventorySlots.get(this.slot1.getValue()).getStack().isEmpty()) {
                    this.addTasks(this.slot1.getValue());
                }
                this.slot1done = true;
                if (!this.slot2done && !XCarry.mc.player.inventoryContainer.inventorySlots.get(this.slot2.getValue()).getStack().isEmpty()) {
                    this.addTasks(this.slot2.getValue());
                }
                this.slot2done = true;
                if (!this.slot3done && !XCarry.mc.player.inventoryContainer.inventorySlots.get(this.slot3.getValue()).getStack().isEmpty()) {
                    this.addTasks(this.slot3.getValue());
                }
                this.slot3done = true;
            }
        } else {
            this.obbySlotDone = false;
            this.slot1done = false;
            this.slot2done = false;
            this.slot3done = false;
        }
        if (!this.taskList.isEmpty()) {
            for (int i = 0; i < this.tasks.getValue(); ++i) {
                InventoryUtil.Task task = this.taskList.poll();
                if (task == null) continue;
                task.run();
            }
        }
    }

    private void addTasks(int n) {
        if (InventoryUtil.getEmptyXCarry() != -1) {
            int n2 = InventoryUtil.getEmptyXCarry();
            if (!(!this.doneSlots.contains(n2) && InventoryUtil.isSlotEmpty(n2) || !this.doneSlots.contains(++n2) && InventoryUtil.isSlotEmpty(n2) || !this.doneSlots.contains(++n2) && InventoryUtil.isSlotEmpty(n2) || !this.doneSlots.contains(++n2) && InventoryUtil.isSlotEmpty(n2))) {
                return;
            }
            if (n2 > 4) {
                return;
            }
            this.doneSlots.add(n2);
            this.taskList.add(new InventoryUtil.Task(n));
            this.taskList.add(new InventoryUtil.Task(n2));
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    @Override
    public void onDisable() {
        if (!this.simpleMode.getValue()) {
            this.closeGui();
            this.close();
        } else {
            XCarry.mc.player.connection.sendPacket(new CPacketCloseWindow(XCarry.mc.player.inventoryContainer.windowId));
        }
    }

    @Override
    public void onLogout() {
        this.onDisable();
    }

    @SubscribeEvent
    public void onCloseGuiScreen(PacketEvent.Send send) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.simpleMode.getValue() && send.getPacket() instanceof CPacketCloseWindow) {
            CPacketCloseWindow cPacketCloseWindow = send.getPacket();
            send.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGuiOpen(GuiOpenEvent guiOpenEvent) {
        if (!this.simpleMode.getValue()) {
            if (this.guiCloseGuard) {
                guiOpenEvent.setCanceled(true);
            } else if (guiOpenEvent.getGui() instanceof GuiInventory) {
                guiOpenEvent.setGui(this.openedGui);
                this.guiNeedsClose.set(false);
            }
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent clientEvent) {
        if (clientEvent.getStage() == 2 && clientEvent.getSetting() != null && clientEvent.getSetting().getFeature() != null && clientEvent.getSetting().getFeature().equals(this)) {
            Setting setting = clientEvent.getSetting();
            String string = clientEvent.getSetting().getName();
            if (setting.equals(this.simpleMode) && setting.getPlannedValue() != setting.getValue()) {
                this.disable();
            } else if (string.equalsIgnoreCase("Store")) {
                clientEvent.setCanceled(true);
                this.autoDuelOn = !this.autoDuelOn;
                Command.sendMessage("<XCarry> §aAutostoring...");
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent keyInputEvent) {
        if (Keyboard.getEventKeyState() && !(XCarry.mc.currentScreen instanceof Gui) && this.autoStore.getValue().getKey() == Keyboard.getEventKey()) {
            this.autoDuelOn = !this.autoDuelOn;
            Command.sendMessage("<XCarry> §aAutostoring...");
        }
    }

    private void close() {
        this.openedGui = null;
        this.guiNeedsClose.set(false);
        this.guiCloseGuard = false;
    }

    private void closeGui() {
        if (this.guiNeedsClose.compareAndSet(true, false)) {
            this.guiCloseGuard = true;
            XCarry.mc.player.closeScreen();
            if (this.openedGui != null) {
                this.openedGui.onGuiClosed();
                this.openedGui = null;
            }
            this.guiCloseGuard = false;
        }
    }

    private boolean new6(Object object) {
        return this.shiftClicker.getValue();
    }

    private boolean new5(Object object) {
        return this.shiftClicker.getValue();
    }

    private boolean new4(Object object) {
        return this.autoStore.getValue().getKey() != -1;
    }

    private boolean new3(Object object) {
        return this.autoStore.getValue().getKey() != -1;
    }

    private boolean new2(Object object) {
        return this.autoStore.getValue().getKey() != -1;
    }

    private boolean new1(Object object) {
        return this.autoStore.getValue().getKey() != -1;
    }

    private boolean new0(Object object) {
        return this.autoStore.getValue().getKey() != -1;
    }

    static AtomicBoolean access$000(XCarry xCarry) {
        return xCarry.guiNeedsClose;
    }

    static boolean access$100(XCarry xCarry) {
        return xCarry.guiCloseGuard;
    }
}

