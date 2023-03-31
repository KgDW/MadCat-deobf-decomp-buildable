package me.madcat.features.modules.combat;

import java.util.Comparator;
import java.util.stream.Collectors;
import me.madcat.MadCat;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RotationUtil;
import me.madcat.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class TrapSelf
extends Module {
    private int obsidian = -1;
    private final Setting<Boolean> cev;
    private final Setting<Boolean> head;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> center;
    private final Setting<Boolean> civ;
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", true));
    private final Setting<Boolean> breakCrystal;
    final Timer breakTimer;
    private BlockPos startPos;
    private final Setting<Boolean> cev2;
    private final Setting<Boolean> button;

    private static Float attackcrystal1(Entity entity) {
        return TrapSelf.mc.player.getDistance(entity);
    }

    @Override
    public void onTick() {
        if (!this.startPos.equals(EntityUtil.getRoundedBlockPos(TrapSelf.mc.player))) {
            this.disable();
            return;
        }
        this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
        if (this.obsidian == -1) {
            this.disable();
            return;
        }
        BlockPos blockPos = new BlockPos(TrapSelf.mc.player.posX, TrapSelf.mc.player.posY, TrapSelf.mc.player.posZ);
        if (BlockUtil.cantBlockPlace2(blockPos.add(0, 0, -1))) {
            this.placeBlock(blockPos.add(0, -1, -1));
        }
        if (BlockUtil.cantBlockPlace2(blockPos.add(0, 0, 1))) {
            this.placeBlock(blockPos.add(0, -1, 1));
        }
        if (BlockUtil.cantBlockPlace2(blockPos.add(1, 0, 0))) {
            this.placeBlock(blockPos.add(1, -1, 0));
        }
        if (BlockUtil.cantBlockPlace2(blockPos.add(-1, 0, 0))) {
            this.placeBlock(blockPos.add(-1, -1, 0));
        }
        if (this.placeBlock(blockPos.add(1, 0, 0))) {
            this.placeBlock(blockPos.add(1, 1, 0));
        }
        if (this.placeBlock(blockPos.add(-1, 0, 0))) {
            this.placeBlock(blockPos.add(-1, 1, 0));
        }
        if (this.placeBlock(blockPos.add(0, 0, 1))) {
            this.placeBlock(blockPos.add(0, 1, 1));
        }
        if (this.placeBlock(blockPos.add(0, 0, -1))) {
            this.placeBlock(blockPos.add(0, 1, -1));
        }
        if (this.cev.getValue() && this.placeBlock(blockPos.add(0, 2, -1)) && this.placeBlock(blockPos.add(0, 3, -1))) {
            this.placeBlock(blockPos.add(0, 3, 0));
        }
        if (this.cev2.getValue() && this.placeBlock(blockPos.add(0, 3, -1)) && this.placeBlock(blockPos.add(0, 4, -1))) {
            this.placeBlock(blockPos.add(0, 4, 0));
        }
        if (this.head.getValue()) {
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 0))) {
                this.placeBlock(blockPos.add(0, 2, -1));
                this.placeBlock(blockPos.add(0, 2, 1));
            }
            if (this.button.getValue()) {
                this.obsidian = InventoryUtil.findHotbarBlock(Blocks.WOODEN_BUTTON);
                if (this.obsidian == -1) {
                    this.obsidian = InventoryUtil.findHotbarBlock(Blocks.STONE_BUTTON);
                    if (this.obsidian == -1) {
                        this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        if (this.obsidian == -1) {
                            return;
                        }
                    }
                }
            }
            if (this.placeBlock(blockPos.add(0, 2, 0)) && this.button.getValue()) {
                this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                if (this.obsidian == -1) {
                    return;
                }
            }
        }
        if (this.civ.getValue() && this.placeBlock(blockPos.add(0, 2, -1)) && this.placeBlock(blockPos.add(0, 2, 1)) && this.placeBlock(blockPos.add(1, 2, 0))) {
            this.placeBlock(blockPos.add(-1, 2, 0));
        }
    }

    @Override
    public void onEnable() {
        this.startPos = new BlockPos(TrapSelf.mc.player.posX, TrapSelf.mc.player.posY + 0.5, TrapSelf.mc.player.posZ);
        if (this.center.getValue()) {
            MadCat.moduleManager.enableModule("AutoCenter");
        }
    }

    private boolean placeBlock(BlockPos blockPos) {
        if (this.breakCrystal.getValue()) {
            this.attackCrystal(blockPos);
        }
        if (BlockUtil.cantBlockPlaceMine(blockPos)) {
            return true;
        }
        int n = TrapSelf.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return true;
        }
        TrapSelf.mc.player.inventory.currentItem = this.obsidian;
        TrapSelf.mc.playerController.updateController();
        BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
        TrapSelf.mc.player.inventory.currentItem = n;
        TrapSelf.mc.playerController.updateController();
        return false;
    }

    public TrapSelf() {
        super("TrapSelf", "Trap self", Module.Category.COMBAT);
        this.center = this.register(new Setting<>("TPCenter", true));
        this.breakCrystal = this.register(new Setting<>("BreakCrystal", true));
        this.head = this.register(new Setting<>("TrapHead", false));
        this.button = this.register(new Setting<>("HeadButton", false));
        this.rotate = this.register(new Setting<>("Rotate", true));
        this.cev = this.register(new Setting<>("AntiCev", false));
        this.civ = this.register(new Setting<>("AntiCiv", false));
        this.cev2 = this.register(new Setting<>("AntiCev2", false));
        this.breakTimer = new Timer();
    }

    private static boolean attackcrystal0(Entity entity) {
        return entity instanceof EntityEnderCrystal && !entity.isDead;
    }

    public void attackCrystal(BlockPos blockPos) {
        if (!this.breakTimer.passedMs(200L)) {
            return;
        }
        this.breakTimer.reset();
        for (Entity entity : TrapSelf.mc.world.loadedEntityList.stream().filter(TrapSelf::attackcrystal0).sorted(Comparator.comparing(TrapSelf::attackcrystal1)).collect(Collectors.toList())) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (!(entity.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) < 2.0)) {
                continue;
            }
            TrapSelf.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            TrapSelf.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            if (!this.rotate.getValue()) break;
            RotationUtil.facePos(new BlockPos(entity.posX, entity.posY, entity.posZ));
            break;
        }
    }
}

