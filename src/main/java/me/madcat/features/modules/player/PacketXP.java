package me.madcat.features.modules.player;

import me.madcat.event.events.UpdateWalkingPlayerEventTwo;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.player.TimerModule;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class PacketXP
extends Module {
    private static PacketXP INSTANCE = new PacketXP();
    public final Setting<Bind> bind = this.register(new Setting<>("PacketBind", new Bind(-1)));
    public final Setting<Boolean> pitch = this.register(new Setting<>("Pitch", true));
    int prvSlot;

    public PacketXP() {
        super("PacketXP", "Allows you to XP instantly", Module.Category.PLAYER);
        this.setInstance();
    }

    public static PacketXP INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PacketXP();
        }
        return INSTANCE;
    }

    public static int convertToMouse(int n) {
        switch (n) {
            case -2: {
                return 0;
            }
            case -3: {
                return 1;
            }
            case -4: {
                return 2;
            }
            case -5: {
                return 3;
            }
            case -6: {
                return 4;
            }
        }
        return -1;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.findExpInHotbar() == -1) {
            return;
        }
        if (this.bind.getValue().getKey() > -1) {
            if (Keyboard.isKeyDown(this.bind.getValue().getKey()) && PacketXP.mc.currentScreen == null) {
                this.ThrowXP();
            }
        } else if (this.bind.getValue().getKey() < -1 && Mouse.isButtonDown(PacketXP.convertToMouse(this.bind.getValue().getKey())) && PacketXP.mc.currentScreen == null) {
            this.ThrowXP();
        }
    }

    @SubscribeEvent
    public void MotionEvent(UpdateWalkingPlayerEventTwo updateWalkingPlayerEventTwo) {
        if (this.findExpInHotbar() == -1) {
            return;
        }
        if (TimerModule.INSTANCE().isEnabled() && TimerModule.INSTANCE().packetControl.getValue()) {
            return;
        }
        if (this.bind.getValue().getKey() > -1) {
            if (Keyboard.isKeyDown(this.bind.getValue().getKey()) && PacketXP.mc.currentScreen == null && this.pitch.getValue()) {
                updateWalkingPlayerEventTwo.setPitch(90.0f);
            }
        } else if (this.bind.getValue().getKey() < -1 && Mouse.isButtonDown(PacketXP.convertToMouse(this.bind.getValue().getKey())) && PacketXP.mc.currentScreen == null && this.pitch.getValue()) {
            updateWalkingPlayerEventTwo.setPitch(90.0f);
        }
    }

    private int findExpInHotbar() {
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            if (PacketXP.mc.player.inventory.getStackInSlot(i).getItem() != Items.EXPERIENCE_BOTTLE) continue;
            n = i;
            break;
        }
        return n;
    }

    private void ThrowXP() {
        this.prvSlot = PacketXP.mc.player.inventory.currentItem;
        PacketXP.mc.player.inventory.currentItem = this.findExpInHotbar();
        PacketXP.mc.playerController.updateController();
        PacketXP.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        PacketXP.mc.player.inventory.currentItem = this.prvSlot;
        PacketXP.mc.playerController.updateController();
    }
}

