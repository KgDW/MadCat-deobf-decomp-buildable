package me.madcat.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.madcat.MadCat;
import me.madcat.features.modules.legacy.BreakCheck;
import me.madcat.features.modules.legacy.InstantMine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BlockUtil
implements Wrapper {
    public static final List<Block> unSolidBlocks;
    public static final List<Block> emptyBlocks;
    public static final List<Block> unSafeBlocks;
    public static final List<Block> noPlaceBlock;
    public static final List<Block> shulkerList;
    private static final Timer attackTimer;
    public static final List<Block> blackList;
    public static final List<Block> rightclickableBlocks;

    public static boolean canReplace(BlockPos blockPos) {
        return BlockUtil.getState(blockPos).getMaterial().isReplaceable();
    }

    static Entity checkEntity(Vec3d[] vec3dArray, BlockPos blockPos) {
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray) {
            BlockPos blockPos2 = new BlockPos(blockPos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (!(entity2 instanceof EntityPlayer)) continue;
                if (entity != null) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    public static boolean isBlockUnSafe(Block block) {
        return !unSafeBlocks.contains(block);
    }

    public static boolean PowerCheck(BlockPos blockPos) {
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.AIR) {
            return true;
        }
        if (BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.REDSTONE_BLOCK) {
            return true;
        }
        return BlockUtil.checkEntityCrystal(EntityUtil.getVarOffsets(0, 0, 0), blockPos) != null;
    }

    public static void placeCrystalOnBlock(BlockPos blockPos, EnumHand enumHand, boolean bl, boolean bl2, boolean bl3) {
        RayTraceResult rayTraceResult = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + (double)BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() - 0.5, (double)blockPos.getZ() + 0.5));
        EnumFacing enumFacing = rayTraceResult == null || rayTraceResult.sideHit == null ? EnumFacing.UP : rayTraceResult.sideHit;
        int n = BlockUtil.mc.player.inventory.currentItem;
        int n2 = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
        if (enumHand == EnumHand.MAIN_HAND && bl3 && n2 != -1 && n2 != BlockUtil.mc.player.inventory.currentItem) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(n2));
        }
        BlockUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, 0.0f, 0.0f, 0.0f));
        if (enumHand == EnumHand.MAIN_HAND && bl3 && n2 != -1 && n2 != BlockUtil.mc.player.inventory.currentItem) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketHeldItemChange(n));
        }
        if (bl) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketAnimation(bl2 ? enumHand : EnumHand.MAIN_HAND));
        }
    }

    private static IBlockState getState(BlockPos blockPos) {
        return BlockUtil.mc.world.getBlockState(blockPos);
    }

    public static EnumFacing getFirstFacing(BlockPos blockPos) {
        Iterator<EnumFacing> iterator = BlockUtil.getPossibleSides(blockPos).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    static {
        blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
        unSafeBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.ENDER_CHEST, Blocks.ANVIL);
        unSolidBlocks = Arrays.asList(Blocks.FLOWING_LAVA, Blocks.FLOWER_POT, Blocks.SNOW, Blocks.CARPET, Blocks.END_ROD, Blocks.SKULL, Blocks.FLOWER_POT, Blocks.TRIPWIRE, Blocks.TRIPWIRE_HOOK, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.STONE_BUTTON, Blocks.LADDER, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.UNLIT_REDSTONE_TORCH, Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WIRE, Blocks.AIR, Blocks.PORTAL, Blocks.END_PORTAL, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.SAPLING, Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS, Blocks.REEDS, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.WATERLILY, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.TALLGRASS, Blocks.DEADBUSH, Blocks.VINE, Blocks.FIRE, Blocks.RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.TORCH);
        noPlaceBlock = Arrays.asList(Blocks.AIR, Blocks.WATER, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.FLOWING_LAVA, Blocks.FIRE);
        attackTimer = new Timer();
        emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
        rightclickableBlocks = Arrays.asList(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.UNPOWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.BEACON, Blocks.BED, Blocks.FURNACE, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
    }

    public static void placeBlock(BlockPos blockPos, EnumHand enumHand, boolean bl, boolean bl2) throws Exception {
        EnumFacing enumFacing = BlockUtil.getFirstFacing(blockPos);
        if (enumFacing == null) {
            return;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        Block block = BlockUtil.mc.world.getBlockState(blockPos2).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (blackList.contains(block) || shulkerList.contains(block))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
        }
        if (bl) {
            RotationUtil.faceVector(vec3d, true);
        }
        BlockUtil.rightClickBlock(blockPos2, vec3d, enumHand, enumFacing2, bl2);
        // Use reflection to set rightClickDelayTimer
        Field field = Minecraft.class.getDeclaredField("rightClickDelayTimer");
        field.setAccessible(true);
        field.set(BlockUtil.mc, 4);
    }


    public static EnumFacing getRayTraceFacing(BlockPos blockPos) {
        RayTraceResult rayTraceResult = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + (double)BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getX() - 0.5, (double)blockPos.getX() + 0.5));
        if (rayTraceResult == null || rayTraceResult.sideHit == null) {
            return EnumFacing.UP;
        }
        return rayTraceResult.sideHit;
    }

    public static void placeBlock(BlockPos blockPos, EnumFacing enumFacing, EnumHand enumHand) {
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        BlockUtil.rightClickBlock(blockPos2, vec3d, enumHand, enumFacing2, true);
    }

    public static boolean EntityAndMineCheck(BlockPos blockPos) {
        if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos))) {
            return false;
        }
        for (EntityPlayer entityPlayer : BreakCheck.Instance().MineMap.keySet()) {
            if (entityPlayer == null) {
                continue;
            }
            BlockPos blockPos2 = BreakCheck.Instance().MineMap.get(entityPlayer);
            if (blockPos2 == null) {
                continue;
            }
            if (!new BlockPos(blockPos2).equals(new BlockPos(blockPos))) continue;
            return false;
        }
        return BlockUtil.checkEntityCrystal(EntityUtil.getVarOffsets(0, 0, 0), blockPos) == null;
    }

    public static BlockPos[] getHorizontalOffsets(BlockPos blockPos) {
        return new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down()};
    }

    public static boolean CrystalCheck(BlockPos blockPos) {
        for (Entity entity : BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos))) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            return true;
        }
        return false;
    }

    private static Vec3d getEyesPos() {
        return new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + (double)BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ);
    }

    public static boolean MineCheck(BlockPos blockPos) {
        if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos))) {
            return true;
        }
        for (EntityPlayer entityPlayer : BreakCheck.Instance().MineMap.keySet()) {
            if (entityPlayer == null) {
                continue;
            }
            BlockPos blockPos2 = BreakCheck.Instance().MineMap.get(entityPlayer);
            if (blockPos2 == null) {
                continue;
            }
            if (!new BlockPos(blockPos2).equals(new BlockPos(blockPos))) continue;
            return true;
        }
        return false;
    }

    public static void rotatePacket(double d, double d2, double d3) {
        double d4 = d - BlockUtil.mc.player.posX;
        double d5 = d2 - (BlockUtil.mc.player.posY + (double)BlockUtil.mc.player.getEyeHeight());
        double d6 = d3 - BlockUtil.mc.player.posZ;
        double d7 = Math.sqrt(d4 * d4 + d6 * d6);
        float f = (float)Math.toDegrees(Math.atan2(d6, d4)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d5, d7)));
        BlockUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(f, f2, BlockUtil.mc.player.onGround));
    }

    public static void sneak(BlockPos blockPos) {
        if (!BlockUtil.mc.player.isSneaking() && (blackList.contains(blockPos) || shulkerList.contains(blockPos))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
        }
    }

    public static boolean isBlockSolid(BlockPos blockPos) {
        return !BlockUtil.isBlockUnSolid(blockPos);
    }

    public static NonNullList possiblePlacePositions(float f, boolean bl, boolean bl2) {
        NonNullList nonNullList = NonNullList.create();
        nonNullList.addAll(BlockUtil.getSphere(EntityUtil.getPlayerPos(BlockUtil.mc.player), f, (int)f, false, true, 0).stream().filter(arg_0 -> BlockUtil.possiblePlacePositions1(bl, bl2, arg_0)).collect(Collectors.toList()));
        return nonNullList;
    }

    public static NonNullList getBlockSphere(float f, Class clazz) {
        NonNullList nonNullList = NonNullList.create();
        nonNullList.addAll(BlockUtil.getSphere(EntityUtil.getPlayerPos(BlockUtil.mc.player), f, (int)f, false, true, 0).stream().filter(arg_0 -> BlockUtil.getBlockSphere0(clazz, arg_0)).collect(Collectors.toList()));
        return nonNullList;
    }

    public static boolean isAir(BlockPos blockPos) {
        return BlockUtil.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR);
    }

    public static double distanceToXZ(double d, double d2) {
        double d3 = BlockUtil.mc.player.posX - d;
        double d4 = BlockUtil.mc.player.posZ - d2;
        return Math.sqrt(d3 * d3 + d4 * d4);
    }

    public static boolean isScaffoldPos(BlockPos blockPos) {
        return BlockUtil.mc.world.isAirBlock(blockPos) || BlockUtil.mc.world.getBlockState(blockPos).getBlock() == Blocks.SNOW_LAYER || BlockUtil.mc.world.getBlockState(blockPos).getBlock() == Blocks.TALLGRASS || BlockUtil.mc.world.getBlockState(blockPos).getBlock() instanceof BlockLiquid;
    }

    public static BlockPos getFlooredPosition(Entity entity) {
        return new BlockPos(Math.floor(entity.posX), (double)Math.round(entity.posY), Math.floor(entity.posZ));
    }

    public static BlockPos[] toBlockPos(Vec3d[] vec3dArray) {
        BlockPos[] blockPosArray = new BlockPos[vec3dArray.length];
        for (int i = 0; i < vec3dArray.length; ++i) {
            blockPosArray[i] = new BlockPos(vec3dArray[i]);
        }
        return blockPosArray;
    }

    public static BlockPos vec3toBlockPos(Vec3d vec3d) {
        return new BlockPos(Math.floor(vec3d.x), (double)Math.round(vec3d.y), Math.floor(vec3d.z));
    }

    public static boolean isAir(double d, double d2, double d3) {
        Vec3d vec3d = new Vec3d(d, d2, d3);
        return BlockUtil.mc.world.getBlockState(BlockUtil.vec3toBlockPos(vec3d, true)).getBlock().equals(Blocks.AIR);
    }

    static Entity checkEntityCrystal(Vec3d[] vec3dArray, BlockPos blockPos) {
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray) {
            BlockPos blockPos2 = new BlockPos(blockPos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (!(entity2 instanceof EntityPlayer) && !(entity2 instanceof EntityEnderCrystal)) continue;
                if (entity != null) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    public static boolean PistonCheck(BlockPos blockPos) {
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.AIR) {
            return true;
        }
        if (BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            return true;
        }
        return BlockUtil.checkEntityCrystal(EntityUtil.getVarOffsets(0, 0, 0), blockPos) != null;
    }

    public static boolean isReplaceable(BlockPos blockPos) {
        return BlockUtil.getState(blockPos).getMaterial().isReplaceable();
    }

    public static boolean placeBlock(BlockPos blockPos, EnumHand enumHand, boolean bl) throws Exception {
        boolean bl2 = false;
        EnumFacing enumFacing = null;
        Iterator<EnumFacing> iterator = BlockUtil.getPossibleSides(blockPos).iterator();
        if (iterator.hasNext()) {
            enumFacing = iterator.next();
        }
        if (enumFacing == null) {
            return bl;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        Block block = BlockUtil.mc.world.getBlockState(blockPos2).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (blackList.contains(block) || shulkerList.contains(block))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
            bl2 = true;
        }
        BlockUtil.rightClickBlock(blockPos2, vec3d, enumHand, enumFacing2);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        Field field = Minecraft.class.getDeclaredField("rightClickDelayTimer");
        field.setAccessible(true);
        field.set(BlockUtil.mc, 4);
        return bl2 || bl;
    }


    public static boolean CanPlace(BlockPos blockPos) {
        for (EnumFacing enumFacing : EnumFacing.VALUES) {
            if (!BlockUtil.isReplaceable(blockPos) || noPlaceBlock.contains(BlockUtil.getBlock(blockPos.offset(enumFacing))) || !(BlockUtil.mc.player.getDistanceSq(blockPos) <= MathUtil.square(5.0))) continue;
            return true;
        }
        return false;
    }

    public static void attackCrystals(BlockPos blockPos, boolean bl) {
        boolean bl2 = BlockUtil.mc.player.isSprinting();
        int n = MadCat.serverManager.getPing();
        for (EntityEnderCrystal entityEnderCrystal : BlockUtil.mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(blockPos))) {
            long l = n <= 50 ? 75L : 100L;
            if (!attackTimer.passedMs(l)) continue;
            if (bl) {
                MadCat.rotationManager.lookAtVec3dPacket(entityEnderCrystal.getPositionVector(), false, true);
            }
            if (bl2) {
                BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            BlockUtil.mc.player.connection.sendPacket(new CPacketUseEntity(entityEnderCrystal));
            BlockUtil.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            if (bl2) {
                BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            }
            attackTimer.reset();
            break;
        }
    }

    public static boolean fakeBBoxCheck(EntityPlayer entityPlayer, Vec3d vec3d, boolean bl) {
        Vec3d vec3d2 = entityPlayer.getPositionVector().add(vec3d);
        if (bl) {
            Vec3d vec3d3 = entityPlayer.getPositionVector();
            return BlockUtil.isAir(vec3d2.add(0.3, 0.0, 0.3)) && BlockUtil.isAir(vec3d2.add(-0.3, 0.0, 0.3)) && BlockUtil.isAir(vec3d2.add(0.3, 0.0, -0.3)) && BlockUtil.isAir(vec3d2.add(-0.3, 0.0, 0.3)) && BlockUtil.isAir(vec3d2.add(0.3, 1.8, 0.3)) && BlockUtil.isAir(vec3d2.add(-0.3, 1.8, 0.3)) && BlockUtil.isAir(vec3d2.add(0.3, 1.8, -0.3)) && BlockUtil.isAir(vec3d2.add(-0.3, 1.8, 0.3)) && BlockUtil.isAir(vec3d3.add(0.3, 2.8, 0.3)) && BlockUtil.isAir(vec3d3.add(-0.3, 2.8, 0.3)) && BlockUtil.isAir(vec3d3.add(-0.3, 2.8, -0.3)) && BlockUtil.isAir(vec3d3.add(0.3, 2.8, -0.3));
        }
        return BlockUtil.isAir(vec3d2.add(0.3, 0.0, 0.3)) && BlockUtil.isAir(vec3d2.add(-0.3, 0.0, 0.3)) && BlockUtil.isAir(vec3d2.add(0.3, 0.0, -0.3)) && BlockUtil.isAir(vec3d2.add(-0.3, 0.0, 0.3)) && BlockUtil.isAir(vec3d2.add(0.3, 1.8, 0.3)) && BlockUtil.isAir(vec3d2.add(-0.3, 1.8, 0.3)) && BlockUtil.isAir(vec3d2.add(0.3, 1.8, -0.3)) && BlockUtil.isAir(vec3d2.add(-0.3, 1.8, 0.3));
    }

    public static boolean isBlockUnSolid(BlockPos blockPos) {
        return BlockUtil.isBlockUnSolid(BlockUtil.mc.world.getBlockState(blockPos).getBlock());
    }
    
    public static boolean canPlaceCrystal(BlockPos blockPos, boolean bl, boolean bl2) {
        BlockPos blockPos2 = blockPos.add(0, 1, 0);
        BlockPos blockPos3 = blockPos.add(0, 2, 0);
        try {
            if (BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if (!bl2 && BlockUtil.mc.world.getBlockState(blockPos3).getBlock() != Blocks.AIR || BlockUtil.mc.world.getBlockState(blockPos2).getBlock() != Blocks.AIR) {
                return false;
            }
            for (Entity entity : BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (entity.isDead) continue;
                if (!bl || !(entity instanceof EntityEnderCrystal)) return false;
            }
            if (bl2) return true;
            for (Entity entity : BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos3))) {
                if (entity.isDead) continue;
                if (!bl || !(entity instanceof EntityEnderCrystal)) return false;
            }
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean canBeClicked(BlockPos blockPos) {
        return BlockUtil.getBlock(blockPos).canCollideCheck(BlockUtil.getState(blockPos), false);
    }

    public static double getMineDistance(BlockPos blockPos) {
        return BlockUtil.getMineDistance(BlockUtil.mc.player, blockPos);
    }

    public static boolean isBlockUnSolid(Block block) {
        return unSolidBlocks.contains(block);
    }

    public static ArrayList<BlockPos> haveNeighborBlock(BlockPos blockPos, Block block) {
        ArrayList<BlockPos> arrayList = new ArrayList<>();
        if (BlockUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals(block)) {
            arrayList.add(blockPos.add(1, 0, 0));
        }
        if (BlockUtil.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock().equals(block)) {
            arrayList.add(blockPos.add(-1, 0, 0));
        }
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(block)) {
            arrayList.add(blockPos.add(0, 1, 0));
        }
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock().equals(block)) {
            arrayList.add(blockPos.add(0, -1, 0));
        }
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals(block)) {
            arrayList.add(blockPos.add(0, 0, 1));
        }
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock().equals(block)) {
            arrayList.add(blockPos.add(0, 0, -1));
        }
        return arrayList;
    }

    public void attackEntity(Entity entity, boolean bl, boolean bl2) {
        if (bl) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        } else {
            BlockUtil.mc.playerController.attackEntity(BlockUtil.mc.player, entity);
        }
        if (bl2) {
            BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static boolean isValidBlock(BlockPos blockPos) {
        Block block = BlockUtil.mc.world.getBlockState(blockPos).getBlock();
        return !(block instanceof BlockLiquid) && block.getMaterial(null) != Material.AIR;
    }

    public static int isPositionPlaceable(BlockPos blockPos, boolean bl) {
        return BlockUtil.isPositionPlaceable(blockPos, bl, true);
    }

    public static void placeBlock(BlockPos blockPos, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (BlockUtil.canReplace(blockPos)) {
            Optional<ClickLocation> optional;
            if (bl3) {
                BlockUtil.attackCrystals(blockPos, bl);
            }
            if ((optional = BlockUtil.getClickLocation(blockPos, bl4, false, bl3)).isPresent()) {
                BlockPos blockPos2 = optional.get().neighbour;
                EnumFacing enumFacing = optional.get().opposite;
                boolean bl5 = BlockUtil.shouldShiftClick(blockPos2);
                if (bl5) {
                    BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
                Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing.getDirectionVec()).scale(0.5));
                if (bl) {
                    MadCat.rotationManager.lookAtVec3dPacket(vec3d, false, true);
                }
                if (bl2) {
                    Vec3d vec3d2 = new Vec3d(blockPos2).add(0.5, 0.5, 0.5);
                    float f = (float)(vec3d2.x - (double)blockPos2.getX());
                    float f2 = (float)(vec3d2.y - (double)blockPos2.getY());
                    float f3 = (float)(vec3d2.z - (double)blockPos2.getZ());
                    BlockUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos2, enumFacing, EnumHand.MAIN_HAND, f, f2, f3));
                } else {
                    BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, blockPos2, enumFacing, vec3d, EnumHand.MAIN_HAND);
                }
                BlockUtil.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                if (bl5) {
                    BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }
            }
        }
    }

    public static Optional<ClickLocation> getClickLocation(BlockPos blockPos, boolean bl, boolean bl2, boolean bl3) {
        Block block = BlockUtil.mc.world.getBlockState(blockPos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return Optional.empty();
        }
        if (!bl) {
            for (Entity entity : BlockUtil.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockPos))) {
                if (bl3 && entity instanceof EntityEnderCrystal) {
                    continue;
                }
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow) continue;
                return Optional.empty();
            }
        }
        EnumFacing enumFacing = null;
        for (EnumFacing enumFacing2 : EnumFacing.values()) {
            BlockPos blockPos2 = blockPos.offset(enumFacing2);
            if (bl2 && BlockUtil.mc.world.getBlockState(blockPos2).getBlock() == Blocks.PISTON) {
                continue;
            }
            if (!BlockUtil.mc.world.getBlockState(blockPos2).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(blockPos2), false)) {
                continue;
            }
            IBlockState iBlockState = BlockUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getMaterial().isReplaceable()) continue;
            enumFacing = enumFacing2;
            break;
        }
        if (enumFacing == null) {
            return Optional.empty();
        }
        BlockPos entity2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing3 = enumFacing.getOpposite();
        if (!BlockUtil.mc.world.getBlockState(entity2).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(entity2), false)) {
            return Optional.empty();
        }
        return Optional.of(new ClickLocation(entity2, enumFacing3));
    }

    public static boolean cantPistonPlace(BlockPos blockPos) {
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            return true;
        }
        return BlockUtil.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos) != null;
    }

    public static boolean isAir(Vec3d vec3d) {
        return BlockUtil.mc.world.getBlockState(BlockUtil.vec3toBlockPos(vec3d, true)).getBlock().equals(Blocks.AIR);
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.add(0, 1, 0);
        BlockPos blockPos3 = blockPos.add(0, 2, 0);
        try {
            return (BlockUtil.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || BlockUtil.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && BlockUtil.mc.world.getBlockState(blockPos2).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos3).getBlock() == Blocks.AIR && BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2)).isEmpty() && BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos3)).isEmpty();
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean canBreak(BlockPos blockPos) {
        IBlockState iBlockState = BlockUtil.mc.world.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        return block.getBlockHardness(iBlockState, BlockUtil.mc.world, blockPos) != -1.0f;
    }

    public static double getMineDistance(Entity entity, BlockPos blockPos) {
        double d = entity.posX - ((double)blockPos.getX() + 0.5);
        double d2 = entity.posY - ((double)blockPos.getY() + 0.5) + 1.5;
        double d3 = entity.posZ - ((double)blockPos.getZ() + 0.5);
        return d * d + d2 * d2 + d3 * d3;
    }

    public static double getPushDistance(EntityPlayer entityPlayer, double d, double d2) {
        double d3 = entityPlayer.posX - d;
        double d4 = entityPlayer.posZ - d2;
        return Math.sqrt(d3 * d3 + d4 * d4);
    }

    public static void placeCrystalOnBlock(BlockPos blockPos, EnumHand enumHand, boolean bl, boolean bl2) {
        RayTraceResult rayTraceResult = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + (double)BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() - 0.5, (double)blockPos.getZ() + 0.5));
        EnumFacing enumFacing = rayTraceResult == null || rayTraceResult.sideHit == null ? EnumFacing.UP : rayTraceResult.sideHit;
        BlockUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, 0.0f, 0.0f, 0.0f));
        if (bl) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketAnimation(bl2 ? enumHand : EnumHand.MAIN_HAND));
        }
    }

    public static int isPositionPlaceable(BlockPos blockPos, boolean bl, boolean bl2) {
        Block block = BlockUtil.mc.world.getBlockState(blockPos).getBlock();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return 0;
        }
        if (BlockUtil.rayTracePlaceCheck(blockPos, bl, 0.0f)) {
            return -1;
        }
        if (bl2) {
            for (Entity enumFacing : BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos))) {
                if (enumFacing instanceof EntityItem) continue;
                if (enumFacing instanceof EntityXPOrb) {
                    continue;
                }
                return 1;
            }
        }
        for (EnumFacing enumFacing : BlockUtil.getPossibleSides(blockPos)) {
            if (!BlockUtil.canBeClicked(blockPos.offset(enumFacing))) {
                continue;
            }
            return 3;
        }
        return 2;
    }

    public static boolean rayTracePlaceCheck(BlockPos blockPos, boolean bl, float f) {
        return bl && BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + (double)BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d(blockPos.getX(), (float)blockPos.getY() + f, blockPos.getZ()), false, true, false) != null;
    }

    public static Block getBlock(BlockPos blockPos) {
        return BlockUtil.getState(blockPos).getBlock();
    }

    public static BlockPos vec3toBlockPos(Vec3d vec3d, boolean bl) {
        if (bl) {
            return new BlockPos(Math.floor(vec3d.x), Math.floor(vec3d.y), Math.floor(vec3d.z));
        }
        return new BlockPos(Math.floor(vec3d.x), (double)Math.round(vec3d.y), Math.floor(vec3d.z));
    }

    public static Boolean isPosInFov(BlockPos blockPos) {
        int n = RotationUtil.getDirection4D();
        if (n == 0 && (double)blockPos.getZ() - BlockUtil.mc.player.getPositionVector().z < 0.0) {
            return false;
        }
        if (n == 1 && (double)blockPos.getX() - BlockUtil.mc.player.getPositionVector().x > 0.0) {
            return false;
        }
        if (n == 2 && (double)blockPos.getZ() - BlockUtil.mc.player.getPositionVector().z > 0.0) {
            return false;
        }
        return n != 3 || (double)blockPos.getX() - BlockUtil.mc.player.getPositionVector().x >= 0.0;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos blockPos) {
        ArrayList<EnumFacing> arrayList = new ArrayList<>();
        for (EnumFacing enumFacing : EnumFacing.values()) {
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (!BlockUtil.mc.world.getBlockState(blockPos2).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(blockPos2), false) || BlockUtil.mc.world.getBlockState(blockPos2).getMaterial().isReplaceable()) continue;
            arrayList.add(enumFacing);
        }
        return arrayList;
    }

    public static boolean placeBlock(BlockPos blockPos, EnumHand enumHand, boolean bl, boolean bl2, boolean bl3) {
        boolean bl4 = false;
        EnumFacing enumFacing = BlockUtil.getFirstFacing(blockPos);
        if (enumFacing == null) {
            return bl3;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        Block block = BlockUtil.mc.world.getBlockState(blockPos2).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (blackList.contains(block) || shulkerList.contains(block))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
            bl4 = true;
        }
        if (bl) {
            RotationUtil.faceVector(vec3d, true);
        }
        BlockUtil.rightClickBlock(blockPos2, vec3d, enumHand, enumFacing2, bl2);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        return bl4 || bl3;
    }

    public static boolean cantBlockPlaceMineEntity(BlockPos blockPos) {
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.AIR) {
            return true;
        }
        if (BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            return true;
        }
        if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos))) {
            return true;
        }
        for (EntityPlayer entityPlayer : BreakCheck.Instance().MineMap.keySet()) {
            if (entityPlayer == null) {
                continue;
            }
            BlockPos blockPos2 = BreakCheck.Instance().MineMap.get(entityPlayer);
            if (blockPos2 == null) {
                continue;
            }
            if (!new BlockPos(blockPos2).equals(new BlockPos(blockPos))) continue;
            return true;
        }
        return BlockUtil.checkEntityCrystal(EntityUtil.getVarOffsets(0, 0, 0), blockPos) != null;
    }

    public static boolean cantBlockPlace(BlockPos blockPos) {
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.AIR) {
            return true;
        }
        if (BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            return true;
        }
        return BlockUtil.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos) != null;
    }

    public static boolean isHole(BlockPos blockPos) {
        for (BlockPos blockPos2 : BlockUtil.getHorizontalOffsets(blockPos)) {
            if (BlockUtil.getBlock(blockPos2) != Blocks.AIR) {
                if (BlockUtil.getBlock(blockPos2) == Blocks.BEDROCK || BlockUtil.getBlock(blockPos2) == Blocks.OBSIDIAN) continue;
                if (BlockUtil.getBlock(blockPos2) == Blocks.ENDER_CHEST) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean shouldShiftClick(BlockPos blockPos) {
        Block block = BlockUtil.mc.world.getBlockState(blockPos).getBlock();
        TileEntity tileEntity = null;
        for (TileEntity tileEntity2 : BlockUtil.mc.world.loadedTileEntityList) {
            if (!tileEntity2.getPos().equals(blockPos)) {
                continue;
            }
            tileEntity = tileEntity2;
            break;
        }
        return tileEntity != null || block instanceof BlockBed || block instanceof BlockContainer || block instanceof BlockDoor || block instanceof BlockTrapDoor || block instanceof BlockFenceGate || block instanceof BlockButton || block instanceof BlockAnvil || block instanceof BlockWorkbench || block instanceof BlockCake || block instanceof BlockRedstoneDiode;
    }

    private static float[] getLegitRotations(Vec3d vec3d) {
        Vec3d vec3d2 = BlockUtil.getEyesPos();
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d2, d4)));
        return new float[]{BlockUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(f - BlockUtil.mc.player.rotationYaw), BlockUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(f2 - BlockUtil.mc.player.rotationPitch)};
    }

    public static void placeBlock(Vec3d vec3d, EnumHand enumHand, boolean bl, boolean bl2) {
        BlockPos blockPos = BlockUtil.vec3toBlockPos(vec3d);
        EnumFacing enumFacing = BlockUtil.getFirstFacing(blockPos);
        if (enumFacing == null) {
            return;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        Vec3d vec3d2 = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        Block block = BlockUtil.mc.world.getBlockState(blockPos2).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (blackList.contains(block) || shulkerList.contains(block))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
        }
        if (bl) {
            RotationUtil.faceVector(vec3d2, true);
        }
        BlockUtil.rightClickBlock(blockPos2, vec3d2, enumHand, enumFacing2, bl2);
    }

    public static void placeBlockNotRetarded(BlockPos blockPos, EnumHand enumHand, boolean bl, boolean bl2, boolean bl3) {
        EnumFacing enumFacing = BlockUtil.getFirstFacing(blockPos);
        if (enumFacing == null) {
            return;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        Block block = BlockUtil.mc.world.getBlockState(blockPos2).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (blackList.contains(block) || shulkerList.contains(block))) {
            BlockUtil.mc.player.connection.sendPacket(new CPacketEntityAction(BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
        }
        if (bl) {
            RotationUtil.faceVector(bl3 ? new Vec3d(blockPos) : vec3d, true);
        }
        BlockUtil.rightClickBlock(blockPos2, vec3d, enumHand, enumFacing2, bl2);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static List<BlockPos> getSphere(BlockPos blockPos, float f, int n, boolean bl, boolean bl2, int n2) {
        ArrayList<BlockPos> arrayList = new ArrayList<>();
        int n3 = blockPos.getX();
        int n4 = blockPos.getY();
        int n5 = blockPos.getZ();
        int n6 = n3 - (int)f;
        while ((float)n6 <= (float)n3 + f) {
            int n7 = n5 - (int)f;
            block1: while ((float)n7 <= (float)n5 + f) {
                int n8 = bl2 ? n4 - (int)f : n4;
                while (true) {
                    float f2 = 0;
                    float f3 = n8;
                    float f4 = bl2 ? (float)n4 + f : (f2 = (float)(n4 + n));
                    if (!(f3 < f2)) {
                        ++n7;
                        continue block1;
                    }
                    double d = (n3 - n6) * (n3 - n6) + (n5 - n7) * (n5 - n7) + (bl2 ? (n4 - n8) * (n4 - n8) : 0);
                    if (d < (double)(f * f) && (!bl || d >= (double)((f - 1.0f) * (f - 1.0f)))) {
                        BlockPos blockPos2 = new BlockPos(n6, n8 + n2, n7);
                        arrayList.add(blockPos2);
                    }
                    ++n8;
                }
            }
            ++n6;
        }
        return arrayList;
    }

    public static boolean PlayerCheck(BlockPos blockPos) {
        for (Entity entity : BlockUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos))) {
            if (!(entity instanceof EntityPlayer)) continue;
            return true;
        }
        return false;
    }

    public static void rightClickBlock(BlockPos blockPos, Vec3d vec3d, EnumHand enumHand, EnumFacing enumFacing, boolean bl) {
        if (bl) {
            float f = (float)(vec3d.x - (double)blockPos.getX());
            float f2 = (float)(vec3d.y - (double)blockPos.getY());
            float f3 = (float)(vec3d.z - (double)blockPos.getZ());
            BlockUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, f, f2, f3));
        } else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, blockPos, enumFacing, vec3d, enumHand);
        }
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static void faceVectorPacketInstant(Vec3d vec3d) {
        float[] fArray = BlockUtil.getLegitRotations(vec3d);
        BlockUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0], fArray[1], BlockUtil.mc.player.onGround));
    }

    public static void rightClickBlock(BlockPos blockPos, Vec3d vec3d, EnumHand enumHand, EnumFacing enumFacing) {
        float f = (float)(vec3d.x - (double)blockPos.getX());
        float f2 = (float)(vec3d.y - (double)blockPos.getY());
        float f3 = (float)(vec3d.z - (double)blockPos.getZ());
        BlockUtil.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, f, f2, f3));
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static Vec3d[] getHelpingBlocks(Vec3d vec3d) {
        Vec3d[] vec3dArray = new Vec3d[5];
        vec3dArray[0] = new Vec3d(vec3d.x, vec3d.y - 1.0, vec3d.z);
        vec3dArray[1] = new Vec3d(vec3d.x != 0.0 ? vec3d.x * 2.0 : vec3d.x, vec3d.y, vec3d.x != 0.0 ? vec3d.z : vec3d.z * 2.0);
        vec3dArray[2] = new Vec3d(vec3d.x == 0.0 ? vec3d.x + 1.0 : vec3d.x, vec3d.y, vec3d.x == 0.0 ? vec3d.z : vec3d.z + 1.0);
        vec3dArray[3] = new Vec3d(vec3d.x == 0.0 ? vec3d.x - 1.0 : vec3d.x, vec3d.y, vec3d.x == 0.0 ? vec3d.z : vec3d.z - 1.0);
        vec3dArray[4] = new Vec3d(vec3d.x, vec3d.y + 1.0, vec3d.z);
        return vec3dArray;
    }

    public static boolean isBlockEmpty(BlockPos blockPos) {
        block3: {
            try {
                if (!emptyBlocks.contains(BlockUtil.mc.world.getBlockState(blockPos).getBlock())) break block3;
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos);
                for (Entity entity : BlockUtil.mc.world.loadedEntityList) {
                    if (!(entity instanceof EntityLivingBase) || !axisAlignedBB.intersects(entity.getEntityBoundingBox())) continue;
                    return false;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            return true;
        }
        return false;
    }

    public static boolean placeBlock(BlockPos blockPos, int n, boolean bl, boolean bl2) {
        if (BlockUtil.isBlockEmpty(blockPos)) {
            EnumFacing[] enumFacingArray = new EnumFacing[0];
            int n2 = -1;
            if (n != BlockUtil.mc.player.inventory.currentItem) {
                n2 = BlockUtil.mc.player.inventory.currentItem;
                BlockUtil.mc.player.inventory.currentItem = n;
            }
            if (n2 != -1) {
                BlockUtil.mc.player.inventory.currentItem = n2;
            }
        }
        return false;
    }

    private static boolean getBlockSphere0(Class clazz, BlockPos blockPos) {
        return clazz.isInstance(BlockUtil.mc.world.getBlockState(blockPos).getBlock());
    }

    public static boolean cantBlockPlaceMine(BlockPos blockPos) {
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.AIR) {
            return true;
        }
        if (BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            return true;
        }
        if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos))) {
            return true;
        }
        for (EntityPlayer entityPlayer : BreakCheck.Instance().MineMap.keySet()) {
            if (entityPlayer == null) {
                continue;
            }
            BlockPos blockPos2 = BreakCheck.Instance().MineMap.get(entityPlayer);
            if (blockPos2 == null) {
                continue;
            }
            if (!new BlockPos(blockPos2).equals(new BlockPos(blockPos))) continue;
            return true;
        }
        return BlockUtil.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos) != null;
    }

    public static void placeBlock(BlockPos blockPos, boolean bl, boolean bl2, boolean bl3) {
        BlockUtil.placeBlock(blockPos, bl, bl2, bl3, false);
    }

    public static NonNullList possiblePlacePositions(float f) {
        NonNullList nonNullList = NonNullList.create();
        nonNullList.addAll(BlockUtil.getSphere(EntityUtil.getPlayerPos(BlockUtil.mc.player), f, (int)f, false, true, 0).stream().filter(BlockUtil::canPlaceCrystal).collect(Collectors.toList()));
        return nonNullList;
    }

    private static boolean possiblePlacePositions1(boolean bl, boolean bl2, BlockPos blockPos) {
        return BlockUtil.canPlaceCrystal(blockPos, bl, bl2);
    }

    public static boolean isBlockBelowEntitySolid(Entity entity) {
        if (entity != null) {
            BlockPos blockPos = new BlockPos(entity.posX, entity.posY - 1.0, entity.posZ);
            return BlockUtil.isBlockSolid(blockPos);
        }
        return false;
    }

    public static boolean cantBlockPlace2(BlockPos blockPos) {
        if (BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR) {
            return BlockUtil.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.AIR;
        }
        return false;
    }

    public static class ClickLocation {
        public final BlockPos neighbour;
        public final EnumFacing opposite;

        public ClickLocation(BlockPos blockPos, EnumFacing enumFacing) {
            this.neighbour = blockPos;
            this.opposite = enumFacing;
        }
    }
}

