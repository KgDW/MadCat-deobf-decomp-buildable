package me.madcat.manager;

import me.madcat.features.Feature;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HoleManager
extends Feature {
    private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true));
    private List<BlockPos> holes;
    private final List<BlockPos> midSafety = new ArrayList<>();

    public void update() {
        if (!HoleManager.fullNullCheck()) {
            this.holes = this.calcHoles();
        }
    }

    public boolean isSafe(BlockPos blockPos) {
        boolean bl = true;
        for (BlockPos blockPos2 : surroundOffset) {
            Block block = HoleManager.mc.world.getBlockState(blockPos.add(blockPos2)).getBlock();
            if (block == Blocks.BEDROCK) {
                continue;
            }
            bl = false;
            break;
        }
        return bl;
    }

    public HoleManager() {
        this.holes = new ArrayList<>();
    }

    private static double getSortedHoles0(BlockPos blockPos) {
        return HoleManager.mc.player.getDistanceSq(blockPos);
    }

    public List<BlockPos> getMidSafety() {
        return this.midSafety;
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> arrayList = new ArrayList<>();
        this.midSafety.clear();
        List<BlockPos> list = BlockUtil.getSphere(EntityUtil.getPlayerPos(HoleManager.mc.player), 6.0f, 6, false, true, 0);
        for (BlockPos blockPos : list) {
            if (!HoleManager.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !HoleManager.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) continue;
            if (!HoleManager.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            boolean bl = true;
            boolean bl2 = true;
            for (BlockPos blockPos2 : surroundOffset) {
                Block block = HoleManager.mc.world.getBlockState(blockPos.add(blockPos2)).getBlock();
                if (BlockUtil.isBlockUnSolid(block)) {
                    bl2 = false;
                }
                if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST) continue;
                if (block == Blocks.ANVIL) {
                    continue;
                }
                bl = false;
            }
            if (bl) {
                arrayList.add(blockPos);
            }
            if (!bl2) {
                continue;
            }
            this.midSafety.add(blockPos);
        }
        return arrayList;
    }

    public List<BlockPos> getHoles() {
        return this.holes;
    }

    public List<BlockPos> getSortedHoles() {
        this.holes.sort(Comparator.comparingDouble(HoleManager::getSortedHoles0));
        return this.getHoles();
    }
}

