package me.madcat.features.modules.legacy;

import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoCev
extends Module {
    private static AutoCev INSTANCE = new AutoCev();
    private final Setting<Integer> tick;
    public static EntityPlayer target;
    private final Setting<Float> range;
    private final Setting<Boolean> rotate;
    public final Setting<Boolean> helper;
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", true));
    int ticked;

    @Override
    public void onEnable() {
        this.ticked = 20;
    }

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    public AutoCev() {
        super("AutoCev", "Automatic overhead explosion", Module.Category.LEGACY);
        this.helper = this.register(new Setting<>("TrapHelper", true));
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));
        this.tick = this.register(new Setting<>("Tick", 15, 7, 20));
        this.rotate = this.register(new Setting<>("Rotate", true));
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static AutoCev Instance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoCev();
        }
        return INSTANCE;
    }

    @Override
    public void onTick() {
        if (Feature.fullNullCheck()) {
            return;
        }
        ++this.ticked;
        target = this.getTarget(this.range.getValue());
        if (target == null) {
            return;
        }
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            Command.sendMessage("No obsidian");
            this.disable();
            return;
        }
        if (InventoryUtil.findItemInHotbar(Items.END_CRYSTAL) == -1) {
            Command.sendMessage("No Crystal");
            this.disable();
            return;
        }
        BlockPos blockPos = new BlockPos(AutoCev.target.posX, AutoCev.target.posY, AutoCev.target.posZ);
        if (AutoCev.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.BEDROCK || AutoCev.mc.world.getBlockState(blockPos.add(0, 3, 0)).getBlock() == Blocks.BEDROCK || AutoCev.mc.world.getBlockState(blockPos.add(0, 4, 0)).getBlock() == Blocks.BEDROCK) {
            return;
        }
        if (AutoCev.mc.world.getBlockState(blockPos.add(0, 3, 0)).getBlock() != Blocks.AIR) {
            this.Mine(blockPos.add(0, 3, 0));
            return;
        }
        if (AutoCev.mc.world.getBlockState(blockPos.add(0, 4, 0)).getBlock() != Blocks.AIR) {
            this.Mine(blockPos.add(0, 4, 0));
            return;
        }
        if (AutoCev.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            this.Mine(blockPos.add(0, 2, 0));
        }
        if (this.ticked >= this.tick.getValue()) {
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 0)) && this.helper.getValue()) {
                if (!BlockUtil.cantBlockPlaceMine(blockPos.add(0, 2, 1))) {
                    this.placeBlock2(blockPos.add(0, 2, 1));
                } else if (!BlockUtil.cantBlockPlaceMine(blockPos.add(0, 2, -1))) {
                    this.placeBlock2(blockPos.add(0, 2, -1));
                } else if (!BlockUtil.cantBlockPlaceMine(blockPos.add(1, 2, 0))) {
                    this.placeBlock2(blockPos.add(1, 2, 0));
                } else if (!BlockUtil.cantBlockPlaceMine(blockPos.add(-1, 2, 0))) {
                    this.placeBlock2(blockPos.add(-1, 2, 0));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, -1)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(0, 1, -1))) {
                    this.placeBlock2(blockPos.add(0, 1, -1));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 1)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(0, 1, 1))) {
                    this.placeBlock2(blockPos.add(0, 1, 1));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 0)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(1, 1, 0))) {
                    this.placeBlock2(blockPos.add(1, 1, 0));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(-1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(-1, 1, 0))) {
                    this.placeBlock2(blockPos.add(-1, 1, 0));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, -1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 1, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 1, -1)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(0, 0, -1))) {
                    this.placeBlock2(blockPos.add(0, 0, -1));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 1, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 1, 1)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(0, 0, 1))) {
                    this.placeBlock2(blockPos.add(0, 0, 1));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(-1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(-1, 1, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, 0)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(-1, 0, 0))) {
                    this.placeBlock2(blockPos.add(-1, 0, 0));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(1, 1, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 1, 0)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(1, 0, 0))) {
                    this.placeBlock2(blockPos.add(1, 0, 0));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(1, 1, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 1, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(1, 0, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 0, 0)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(1, -1, 0))) {
                    this.placeBlock2(blockPos.add(1, -1, 0));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(-1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(-1, 1, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(-1, 0, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 0, 0)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(-1, -1, 0))) {
                    this.placeBlock2(blockPos.add(-1, -1, 0));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 1, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 1, 1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 0, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 0, 1)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(0, -1, 1))) {
                    this.placeBlock2(blockPos.add(0, -1, 1));
                } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, -1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 1, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 1, -1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 0, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 0, -1)) && !BlockUtil.cantBlockPlaceMine(blockPos.add(0, -1, -1))) {
                    this.placeBlock2(blockPos.add(0, -1, -1));
                }
                return;
            }
            this.placeBlock(blockPos.add(0, 2, 0));
            this.ticked = 0;
        }
    }

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = Math.pow(d, 2.0) + 1.0;
        for (EntityPlayer entityPlayer2 : AutoCev.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d)) continue;
            if (MadCat.speedManager.getPlayerSpeed(entityPlayer2) > 10.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = AutoCev.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (AutoCev.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = AutoCev.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    private void placeBlock2(BlockPos blockPos) {
        if (BlockUtil.cantBlockPlaceMine(blockPos)) {
            return;
        }
        int n = AutoCev.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return;
        }
        AutoCev.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        AutoCev.mc.playerController.updateController();
        BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
        AutoCev.mc.player.inventory.currentItem = n;
        AutoCev.mc.playerController.updateController();
    }

    private void Mine(BlockPos blockPos) {
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(blockPos)) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCev.target.posX, AutoCev.target.posY, AutoCev.target.posZ)) && AutoCev.mc.world.getBlockState(new BlockPos(AutoCev.target.posX, AutoCev.target.posY, AutoCev.target.posZ)).getBlock() != Blocks.AIR) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCev.mc.player.posX, AutoCev.mc.player.posY + 2.0, AutoCev.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCev.mc.player.posX, AutoCev.mc.player.posY - 1.0, AutoCev.mc.player.posZ))) {
                return;
            }
            if (AutoCev.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        AutoCev.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
    }

    private void placeBlock(BlockPos blockPos) {
        int n = AutoCev.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return;
        }
        AutoCev.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        AutoCev.mc.playerController.updateController();
        BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
        AutoCev.mc.player.inventory.currentItem = n;
        AutoCev.mc.playerController.updateController();
    }
}

