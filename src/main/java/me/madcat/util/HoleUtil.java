package me.madcat.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class HoleUtil {
    private static final Minecraft mc;
    public static final Vec3d[] cityOffsets;
    public static final List<BlockPos> holeBlocks;

    public static boolean isHard(Block block) {
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK;
    }

    public static HoleInfo isHole(BlockPos blockPos, boolean bl, boolean bl2) {
        HoleInfo holeInfo = new HoleInfo();
        HashMap<BlockOffset, BlockSafety> hashMap = HoleUtil.getUnsafeSides(blockPos);
        if (hashMap.containsKey(BlockOffset.DOWN) && hashMap.remove(BlockOffset.DOWN, (Object)BlockSafety.BREAKABLE) && !bl2) {
            holeInfo.setSafety();
            return holeInfo;
        }
        int n = hashMap.size();
        hashMap.entrySet().removeIf(HoleUtil::isHole1);
        if (hashMap.size() != n) {
            holeInfo.setSafety();
        }
        if ((n = hashMap.size()) == 0) {
            holeInfo.setType(HoleType.SINGLE);
            holeInfo.setCentre(new AxisAlignedBB(blockPos));
            return holeInfo;
        }
        if (n == 1 && !bl) {
            return HoleUtil.isDoubleHole(holeInfo, blockPos, hashMap.keySet().stream().findFirst().get());
        }
        holeInfo.setSafety();
        return holeInfo;
    }

    private static boolean isDoubleHole0(Map.Entry entry) {
        return entry.getValue() == BlockSafety.RESISTANT;
    }

    public static boolean is2securityHole(BlockPos blockPos) {
        if (HoleUtil.is2Hole(blockPos) == null) {
            return false;
        }
        BlockPos blockPos2 = HoleUtil.is2Hole(blockPos);
        int n = 0;
        for (BlockPos blockPos3 : holeBlocks) {
            if (HoleUtil.mc.world.getBlockState(blockPos.add(blockPos3)).getBlock() != Blocks.BEDROCK) continue;
            ++n;
        }
        for (BlockPos blockPos3 : holeBlocks) {
            if (HoleUtil.mc.world.getBlockState(blockPos2.add(blockPos3)).getBlock() != Blocks.BEDROCK) continue;
            ++n;
        }
        return n == 8;
    }

    private static HoleInfo isDoubleHole(HoleInfo holeInfo, BlockPos blockPos, BlockOffset blockOffset) {
        BlockPos blockPos2 = blockOffset.offset(blockPos);
        HashMap<BlockOffset, BlockSafety> hashMap = HoleUtil.getUnsafeSides(blockPos2);
        int n = hashMap.size();
        hashMap.entrySet().removeIf(HoleUtil::isDoubleHole0);
        if (hashMap.size() != n) {
            holeInfo.setSafety();
        }
        if (hashMap.containsKey(BlockOffset.DOWN)) {
            holeInfo.setType(HoleType.CUSTOM);
            hashMap.remove(BlockOffset.DOWN);
        }
        if (hashMap.size() > 1) {
            holeInfo.setType(HoleType.NONE);
            return holeInfo;
        }
        double d = Math.min(blockPos.getX(), blockPos2.getX());
        double d2 = Math.max(blockPos.getX(), blockPos2.getX()) + 1;
        double d3 = Math.min(blockPos.getZ(), blockPos2.getZ());
        double d4 = Math.max(blockPos.getZ(), blockPos2.getZ()) + 1;
        holeInfo.setCentre(new AxisAlignedBB(d, blockPos.getY(), d3, d2, blockPos.getY() + 1, d4));
        if (holeInfo.getType() != HoleType.CUSTOM) {
            holeInfo.setType(HoleType.DOUBLE);
        }
        return holeInfo;
    }

    public static HashMap<BlockOffset, BlockSafety> getUnsafeSides(BlockPos blockPos) {
        HashMap<BlockOffset, BlockSafety> hashMap = new HashMap<>();
        BlockSafety blockSafety = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.DOWN.offset(blockPos)).getBlock());
        if (blockSafety != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.DOWN, blockSafety);
        }
        if ((blockSafety = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.NORTH.offset(blockPos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.NORTH, blockSafety);
        }
        if ((blockSafety = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.SOUTH.offset(blockPos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.SOUTH, blockSafety);
        }
        if ((blockSafety = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.EAST.offset(blockPos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.EAST, blockSafety);
        }
        if ((blockSafety = HoleUtil.isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.WEST.offset(blockPos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.WEST, blockSafety);
        }
        return hashMap;
    }

    private static boolean isHole1(Map.Entry entry) {
        return entry.getValue() == BlockSafety.RESISTANT;
    }

    static {
        holeBlocks = Arrays.asList(new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1));
        mc = Minecraft.getMinecraft();
        cityOffsets = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0)};
    }

    public static BlockSafety isBlockSafe(Block block) {
        if (block == Blocks.BEDROCK) {
            return BlockSafety.UNBREAKABLE;
        }
        if (block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL) {
            return BlockSafety.RESISTANT;
        }
        return BlockSafety.BREAKABLE;
    }

    public static boolean isHole(BlockPos blockPos) {
        int n = 0;
        for (BlockPos blockPos2 : holeBlocks) {
            if (!HoleUtil.isHard(HoleUtil.mc.world.getBlockState(blockPos.add(blockPos2)).getBlock())) continue;
            ++n;
        }
        return n == 5;
    }

    public static BlockPos is2Hole(BlockPos blockPos) {
        if (HoleUtil.isHole(blockPos)) {
            return null;
        }
        BlockPos blockPos2 = null;
        int n = 0;
        int n2 = 0;
        if (HoleUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            return null;
        }
        for (BlockPos blockPos3 : holeBlocks) {
            if (HoleUtil.mc.world.getBlockState(blockPos.add(blockPos3)).getBlock() != Blocks.AIR || blockPos.add(blockPos3).equals(new BlockPos(blockPos3.getX(), blockPos3.getY() - 1, blockPos3.getZ()))) continue;
            blockPos2 = blockPos.add(blockPos3);
            ++n;
        }
        if (n == 1) {
            for (BlockPos blockPos3 : holeBlocks) {
                if (HoleUtil.mc.world.getBlockState(blockPos.add(blockPos3)).getBlock() != Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(blockPos.add(blockPos3)).getBlock() != Blocks.OBSIDIAN) continue;
                ++n2;
            }
            for (BlockPos blockPos3 : holeBlocks) {
                if (HoleUtil.mc.world.getBlockState(blockPos2.add(blockPos3)).getBlock() != Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(blockPos2.add(blockPos3)).getBlock() != Blocks.OBSIDIAN) continue;
                ++n2;
            }
        }
        if (n2 == 8) {
            return blockPos2;
        }
        return null;
    }

    public enum BlockOffset {
        DOWN(0, -1, 0),
        UP(0, 1, 0),
        NORTH(0, 0, -1),
        EAST(1, 0, 0),
        SOUTH(0, 0, 1),
        WEST(-1, 0, 0);

        private final int y;
        private final int x;
        private final int z;

        public BlockPos offset(BlockPos blockPos) {
            return blockPos.add(this.x, this.y, this.z);
        }

        BlockOffset(int n2, int n3, int n4) {
            this.x = n2;
            this.y = n3;
            this.z = n4;
        }
    }

    public enum HoleType {
        SINGLE,
        DOUBLE,
        CUSTOM,
        NONE

    }

    public enum BlockSafety {
        UNBREAKABLE,
        RESISTANT,
        BREAKABLE

    }

    public static class HoleInfo {
        private HoleType type;

        public void setType(HoleType holeType) {
            this.type = holeType;
        }

        public void setCentre(AxisAlignedBB axisAlignedBB) {
        }

        public HoleInfo() {
            this(HoleType.NONE);
        }

        public HoleInfo(HoleType holeType) {
            this.type = holeType;
        }

        public HoleType getType() {
            return this.type;
        }

        public void setSafety() {
        }
    }
}

