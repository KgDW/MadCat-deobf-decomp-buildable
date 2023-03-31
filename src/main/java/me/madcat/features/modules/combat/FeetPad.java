package me.madcat.features.modules.combat;

import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FeetPad
extends Module {
    private final Setting<Boolean> button;
    private final Setting<Boolean> web;
    private final Setting<Boolean> onlyGround;
    private final Setting<Boolean> feetExtend;
    public EntityPlayer target;
    public final Setting<Boolean> packet;
    private final Setting<Float> range;
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = d;
        for (EntityPlayer entityPlayer2 : FeetPad.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d) || MadCat.friendManager.isFriend(entityPlayer2.getName())) continue;
            if (FeetPad.mc.player.posY - entityPlayer2.posY >= 5.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = FeetPad.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (FeetPad.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = FeetPad.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        if (this.onlyGround.getValue() && !FeetPad.mc.player.onGround) {
            return;
        }
        BlockPos blockPos = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        this.placeBlock(blockPos.add(0, 0, 0));
        if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, 1))) != null) {
            this.placeBlock(blockPos.add(0, 0, 1));
        }
        if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, -1))) != null) {
            this.placeBlock(blockPos.add(0, 0, -1));
        }
        if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 0))) != null) {
            this.placeBlock(blockPos.add(1, 0, 0));
        }
        if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 0))) != null) {
            this.placeBlock(blockPos.add(-1, 0, 0));
        }
        if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 1))) != null) {
            this.placeBlock(blockPos.add(1, 0, 1));
        }
        if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, -1))) != null) {
            this.placeBlock(blockPos.add(-1, 0, -1));
        }
        if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, -1))) != null) {
            this.placeBlock(blockPos.add(1, 0, -1));
        }
        if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 1))) != null) {
            this.placeBlock(blockPos.add(-1, 0, 1));
        }
        if (!this.feetExtend.getValue()) {
            return;
        }
        this.placeBlock(blockPos.add(1, 0, 0));
        this.placeBlock(blockPos.add(-1, 0, 0));
        this.placeBlock(blockPos.add(0, 0, 1));
        this.placeBlock(blockPos.add(0, 0, -1));
    }

    private void placeBlock(BlockPos blockPos) {
        if (BlockUtil.cantBlockPlaceMine(blockPos)) {
            return;
        }
        int n = FeetPad.mc.player.inventory.currentItem;
        if (this.web.getValue() && InventoryUtil.findHotbarBlock(BlockWeb.class) != -1) {
            FeetPad.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockWeb.class);
            FeetPad.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            FeetPad.mc.player.inventory.currentItem = n;
            FeetPad.mc.playerController.updateController();
        } else if (this.button.getValue() && (InventoryUtil.findHotbarBlock(Blocks.STONE_BUTTON) != -1 || InventoryUtil.findHotbarBlock(Blocks.WOODEN_BUTTON) != -1)) {
            FeetPad.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.STONE_BUTTON) != -1 ? InventoryUtil.findHotbarBlock(Blocks.STONE_BUTTON) : InventoryUtil.findHotbarBlock(Blocks.WOODEN_BUTTON);
            FeetPad.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            FeetPad.mc.player.inventory.currentItem = n;
            FeetPad.mc.playerController.updateController();
        } else if (InventoryUtil.findItemInHotbar(Items.REDSTONE) != -1) {
            FeetPad.mc.player.inventory.currentItem = InventoryUtil.findItemInHotbar(Items.REDSTONE);
            FeetPad.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            FeetPad.mc.player.inventory.currentItem = n;
            FeetPad.mc.playerController.updateController();
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
            for (Entity entity2 : FeetPad.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (!(entity2 instanceof EntityPlayer)) continue;
                if (entity2 == FeetPad.mc.player) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    public FeetPad() {
        super("FeetPad", "Automatically put red stones on the enemy's feet", Module.Category.COMBAT);
        this.packet = this.register(new Setting<>("Packet", true));
        this.onlyGround = this.register(new Setting<>("SelfGround", true));
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 6.0f));
        this.web = this.register(new Setting<>("Web", false));
        this.button = this.register(new Setting<>("Button", true));
        this.feetExtend = this.register(new Setting<>("PlaceExtend", false));
    }
}

