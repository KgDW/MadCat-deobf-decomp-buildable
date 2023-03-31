package me.madcat.features.modules.combat;

import me.madcat.MadCat;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoTrap
extends Module {
    private final Setting<Float> range;
    public final Setting<Boolean> packet;
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    private final Setting<Boolean> antiStep;
    private final Setting<Boolean> extend;
    public EntityPlayer target;
    public final Setting<Double> maxTargetSpeed;

    private void placeBlock(BlockPos blockPos) {
        if (BlockUtil.cantBlockPlaceMineEntity(blockPos)) {
            return;
        }
        int n = AutoTrap.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return;
        }
        AutoTrap.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        AutoTrap.mc.playerController.updateController();
        BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
        AutoTrap.mc.player.inventory.currentItem = n;
        AutoTrap.mc.playerController.updateController();
    }

    public AutoTrap() {
        super("AutoTrap", "Automatically trap the enemy", Module.Category.COMBAT);
        this.packet = this.register(new Setting<>("Packet", true));
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));
        this.antiStep = this.register(new Setting<>("AntiStep", false));
        this.extend = this.register(new Setting<>("Extend", true));
        this.maxTargetSpeed = this.register(new Setting<>("MaxTargetSpeed", 4.0, 1.0, 15.0));
    }

    @Override
    public void onTick() {
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        BlockPos blockPos = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        if (InstantMine.breakPos != null && InstantMine.breakPos.equals(new BlockPos(blockPos.add(0, 2, 0)))) {
            return;
        }
        if (this.antiStep.getValue()) {
            this.placeBlock(blockPos.add(0, 3, 0));
        }
        if (this.extend.getValue()) {
            if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, 1))) != null) {
                this.placeBlock(blockPos.add(0, 2, 1));
            }
            if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, -1))) != null) {
                this.placeBlock(blockPos.add(0, 2, -1));
            }
            if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 0))) != null) {
                this.placeBlock(blockPos.add(1, 2, 0));
            }
            if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 0))) != null) {
                this.placeBlock(blockPos.add(-1, 2, 0));
            }
        }
        if (AutoTrap.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            if (!BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 0))) {
                this.placeBlock(blockPos.add(0, 2, 0));
            } else if (!BlockUtil.cantBlockPlaceMineEntity(blockPos.add(0, 2, 1))) {
                this.placeBlock(blockPos.add(0, 2, 1));
            } else if (!BlockUtil.cantBlockPlaceMineEntity(blockPos.add(0, 2, -1))) {
                this.placeBlock(blockPos.add(0, 2, -1));
            } else if (!BlockUtil.cantBlockPlaceMineEntity(blockPos.add(1, 2, 0))) {
                this.placeBlock(blockPos.add(1, 2, 0));
            } else if (!BlockUtil.cantBlockPlaceMineEntity(blockPos.add(-1, 2, 0))) {
                this.placeBlock(blockPos.add(-1, 2, 0));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, -1)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(0, 1, -1))) {
                this.placeBlock(blockPos.add(0, 1, -1));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 1)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(0, 1, 1))) {
                this.placeBlock(blockPos.add(0, 1, 1));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 0)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(1, 1, 0))) {
                this.placeBlock(blockPos.add(1, 1, 0));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(-1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(-1, 1, 0))) {
                this.placeBlock(blockPos.add(-1, 1, 0));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, -1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 1, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 1, -1)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(0, 0, -1))) {
                this.placeBlock(blockPos.add(0, 0, -1));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 1, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 1, 1)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(0, 0, 1))) {
                this.placeBlock(blockPos.add(0, 0, 1));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(-1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(-1, 1, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, 0)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(-1, 0, 0))) {
                this.placeBlock(blockPos.add(-1, 0, 0));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(1, 1, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 1, 0)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(1, 0, 0))) {
                this.placeBlock(blockPos.add(1, 0, 0));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(1, 1, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 1, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(1, 0, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(1, 0, 0)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(1, -1, 0))) {
                this.placeBlock(blockPos.add(1, -1, 0));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(-1, 2, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(-1, 1, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, 0)) && BlockUtil.EntityAndMineCheck(blockPos.add(-1, 0, 0)) && BlockUtil.cantBlockPlace2(blockPos.add(-1, 0, 0)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(-1, -1, 0))) {
                this.placeBlock(blockPos.add(-1, -1, 0));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 1, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 1, 1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 0, 1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 0, 1)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(0, -1, 1))) {
                this.placeBlock(blockPos.add(0, -1, 1));
            } else if (BlockUtil.EntityAndMineCheck(blockPos.add(0, 2, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 2, -1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 1, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 1, -1)) && BlockUtil.EntityAndMineCheck(blockPos.add(0, 0, -1)) && BlockUtil.cantBlockPlace2(blockPos.add(0, 0, -1)) && !BlockUtil.cantBlockPlaceMineEntity(blockPos.add(0, -1, -1))) {
                this.placeBlock(blockPos.add(0, -1, -1));
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }

    Entity checkEntity(Vec3d[] vec3dArray, BlockPos blockPos) {
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray) {
            BlockPos blockPos2 = new BlockPos(blockPos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : AutoTrap.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (!(entity2 instanceof EntityPlayer)) continue;
                if (entity2 == AutoTrap.mc.player) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = d;
        for (EntityPlayer entityPlayer2 : AutoTrap.mc.world.playerEntities) {
            if (MadCat.speedManager.getPlayerSpeed(entityPlayer2) > this.maxTargetSpeed.getValue() || EntityUtil.isntValid(entityPlayer2, d) || MadCat.friendManager.isFriend(entityPlayer2.getName())) continue;
            if (AutoTrap.mc.player.posY - entityPlayer2.posY >= 5.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = AutoTrap.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (AutoTrap.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = AutoTrap.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }
}

