package me.madcat.features.modules.misc;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketTabComplete;
import java.util.UUID;
import net.minecraft.util.EnumHand;
import me.madcat.features.setting.Setting;
import java.util.Random;
import me.madcat.features.modules.Module;

public class AntiAFK extends Module
{
    private final Random random;
    private final Setting<Boolean> swing;
    private final Setting<Boolean> turn;
    private final Setting<Boolean> jump;
    private final Setting<Boolean> sneak;
    private final Setting<Boolean> interact;
    private final Setting<Boolean> tabcomplete;
    private final Setting<Boolean> msgs;
    private final Setting<Boolean> stats;
    private final Setting<Boolean> window;
    private final Setting<Boolean> swap;
    private final Setting<Boolean> dig;
    private final Setting<Boolean> move;

    public AntiAFK() {
        super("AntiAFK", "Attempts to stop the server from kicking u when ur afk", Category.MISC);
        this.swing = this.register(new Setting("Swing", true));
        this.turn = this.register(new Setting("Turn", true));
        this.jump = this.register(new Setting("Jump", true));
        this.sneak = this.register(new Setting("Sneak", true));
        this.interact = this.register(new Setting("InteractBlock", false));
        this.tabcomplete = this.register(new Setting("TabComplete", true));
        this.msgs = this.register(new Setting("ChatMsgs", true));
        this.stats = this.register(new Setting("Stats", true));
        this.window = this.register(new Setting("WindowClick", true));
        this.swap = this.register(new Setting("ItemSwap", true));
        this.dig = this.register(new Setting("HitBlock", true));
        this.move = this.register(new Setting("Move", true));
        this.random = new Random();
    }

    @Override
    public void onUpdate() {
        if (AntiAFK.mc.player.ticksExisted % 45 == 0 && this.swing.getValue()) {
            AntiAFK.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        if (AntiAFK.mc.player.ticksExisted % 20 == 0 && this.turn.getValue()) {
            AntiAFK.mc.player.rotationYaw = (float)(this.random.nextInt(360) - 180);
        }
        if (AntiAFK.mc.player.ticksExisted % 60 == 0 && this.jump.getValue() && AntiAFK.mc.player.onGround) {
            AntiAFK.mc.player.jump();
        }
        if (AntiAFK.mc.player.ticksExisted % 50 == 0 && this.sneak.getValue() && !AntiAFK.mc.player.isSneaking()) {
            AntiAFK.mc.player.movementInput.sneak = true;
        }
        if (AntiAFK.mc.player.ticksExisted % 52.5 == 0.0 && this.sneak.getValue() && AntiAFK.mc.player.isSneaking()) {
            AntiAFK.mc.player.movementInput.sneak = false;
        }
        if (AntiAFK.mc.player.ticksExisted % 30 == 0 && this.interact.getValue()) {
            final WorldClient world = AntiAFK.mc.world;
            final BlockPos blockPos = AntiAFK.mc.objectMouseOver.getBlockPos();
            if (!world.isAirBlock(blockPos)) {
                AntiAFK.mc.playerController.clickBlock(blockPos, AntiAFK.mc.objectMouseOver.sideHit);
            }
        }
        if (AntiAFK.mc.player.ticksExisted % 80 == 0 && this.tabcomplete.getValue() && !AntiAFK.mc.player.isDead) {
            AntiAFK.mc.player.connection.sendPacket(new CPacketTabComplete("/" + UUID.randomUUID().toString().replace('-', 'v'), AntiAFK.mc.player.getPosition(), false));
        }
        if (AntiAFK.mc.player.ticksExisted % 200 == 0 && this.msgs.getValue() && !AntiAFK.mc.player.isDead) {
            AntiAFK.mc.player.sendChatMessage("Anti Disconnect " + this.random.nextInt());
        }
        if (AntiAFK.mc.player.ticksExisted % 300 == 0 && this.stats.getValue() && !AntiAFK.mc.player.isDead) {
            AntiAFK.mc.player.sendChatMessage("/stats");
        }
        if (AntiAFK.mc.player.ticksExisted % 125 == 0 && this.window.getValue() && !AntiAFK.mc.player.isDead) {
            AntiAFK.mc.player.connection.sendPacket(new CPacketClickWindow(1, 1, 1, ClickType.CLONE, new ItemStack(Blocks.OBSIDIAN), (short)1));
        }
        if (AntiAFK.mc.player.ticksExisted % 70 == 0 && this.swap.getValue() && !AntiAFK.mc.player.isDead) {
            AntiAFK.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, AntiAFK.mc.player.getPosition(), EnumFacing.DOWN));
        }
        if (AntiAFK.mc.player.ticksExisted % 50 == 0 && this.dig.getValue()) {
            AntiAFK.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, AntiAFK.mc.player.getPosition(), EnumFacing.DOWN));
        }
        if (AntiAFK.mc.player.ticksExisted % 150 == 0 && this.move.getValue()) {
            KeyBinding.setKeyBindState(AntiAFK.mc.gameSettings.keyBindForward.getKeyCode(), true);
            KeyBinding.setKeyBindState(AntiAFK.mc.gameSettings.keyBindBack.getKeyCode(), true);
            KeyBinding.setKeyBindState(AntiAFK.mc.gameSettings.keyBindRight.getKeyCode(), true);
            KeyBinding.setKeyBindState(AntiAFK.mc.gameSettings.keyBindLeft.getKeyCode(), true);
        }
    }
}
 