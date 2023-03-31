package me.madcat.features.modules.legacy;

import me.madcat.MadCat;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class CevSelect
extends Module {
    public static EntityPlayer target;
    private final Setting<Float> range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));

    @Override
    public void onUpdate() {
        target = this.getTarget(this.range.getValue());
        this.surroundMine();
        this.disable();
    }

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = Math.pow(d, 2.0) + 1.0;
        for (EntityPlayer entityPlayer2 : CevSelect.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d)) continue;
            if (MadCat.speedManager.getPlayerSpeed(entityPlayer2) > 10.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = CevSelect.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (CevSelect.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = CevSelect.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    private IBlockState getBlock(BlockPos blockPos) {
        return CevSelect.mc.world.getBlockState(blockPos);
    }

    public CevSelect() {
        super("CevSelect", "Testing", Module.Category.LEGACY);
    }

    private void surroundMine(BlockPos blockPos) {
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(blockPos)) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(CevSelect.target.posX, CevSelect.target.posY, CevSelect.target.posZ)) && CevSelect.mc.world.getBlockState(new BlockPos(CevSelect.target.posX, CevSelect.target.posY, CevSelect.target.posZ)).getBlock() != Blocks.AIR) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(CevSelect.mc.player.posX, CevSelect.mc.player.posY + 2.0, CevSelect.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(CevSelect.mc.player.posX, CevSelect.mc.player.posY - 1.0, CevSelect.mc.player.posZ))) {
                return;
            }
            if (CevSelect.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        CevSelect.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
    }

    private void surroundMine() {
        if (target == null) {
            return;
        }
        int n = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (n == -1) {
            return;
        }
        BlockPos blockPos = new BlockPos(CevSelect.target.posX, CevSelect.target.posY, CevSelect.target.posZ);
        if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 3, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
            this.surroundMine(blockPos.add(1, 1, 0));
        } else if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 3, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
            this.surroundMine(blockPos.add(-1, 1, 0));
        } else if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 3, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
            this.surroundMine(blockPos.add(0, 1, 1));
        } else if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 3, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
            this.surroundMine(blockPos.add(0, 1, -1));
        } else {
            target = null;
        }
    }
}

