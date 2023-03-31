package me.madcat.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import me.madcat.MadCat;
import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.legacy.AntiCev;
import me.madcat.features.modules.legacy.BreakCheck;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RotationUtil;
import me.madcat.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Surround
extends Module {
    private static Surround INSTANCE;
    public static boolean isPlacing;
    private int extenders = 1;
    private int lastHotbarSlot;
    private final Setting<Boolean> center;
    private final Timer timer;
    private int isSafe;
    public final Setting<Boolean> extend;
    private final Setting<Boolean> rotate;
    private final Map<BlockPos, Integer> retries;
    private boolean isSneaking;
    private BlockPos startPos;
    private final Setting<Boolean> noGhost;
    private final Setting<Boolean> antiCity = this.register(new Setting<>("AntiCity", false));
    private final Setting<Integer> blocksPerTick;
    private final Setting<Double> safeHealth;
    private final Set<Vec3d> extendingBlocks;
    private final Setting<Integer> delay;
    private boolean rotating = false;
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", Boolean.FALSE, this::new0));
    static final Timer breakTimer;
    private final Timer retryTimer;
    private int placements = 0;
    private boolean didPlace = false;
    private final Setting<Boolean> breakCrystal;
    private boolean offHand = false;

    private boolean new2(Double d) {
        return this.breakCrystal.getValue();
    }

    private boolean placeBlocks(Vec3d vec3d, Vec3d[] vec3dArray, boolean bl, boolean bl2, boolean bl3) {
        for (Vec3d vec3d2 : vec3dArray) {
            boolean bl4 = true;
            BlockPos blockPos = new BlockPos(vec3d).add(vec3d2.x, vec3d2.y, vec3d2.z);
            switch (BlockUtil.isPositionPlaceable(blockPos, false)) {
                case 1: {
                    if (this.retries.get(blockPos) == null || this.retries.get(blockPos) < 4) {
                        this.placeBlock(blockPos);
                        this.retries.put(blockPos, this.retries.get(blockPos) == null ? 1 : this.retries.get(blockPos) + 1);
                        this.retryTimer.reset();
                        break;
                    }
                    if (MadCat.speedManager.getSpeedKpH() != 0.0 || bl3) break;
                    if (this.extenders >= 1) {
                        break;
                    }
                    this.placeBlocks(Surround.mc.player.getPositionVector().add(vec3d2), EntityUtil.getUnsafeBlockArrayFromVec3d(Surround.mc.player.getPositionVector().add(vec3d2), 0, true), bl, false, true);
                    this.extendingBlocks.add(vec3d2);
                    ++this.extenders;
                    break;
                }
                case 2: {
                    if (!bl) {
                        break;
                    }
                    bl4 = this.placeBlocks(vec3d, BlockUtil.getHelpingBlocks(vec3d2), false, true, true);
                }
                case 3: {
                    if (bl4) {
                        this.placeBlock(blockPos);
                    }
                    if (!bl2) {
                        break;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean new0(Boolean bl) {
        return this.antiCity.getValue();
    }

    public static void attackCrystal() {
        if (!breakTimer.passedMs(200L)) {
            return;
        }
        breakTimer.reset();
        for (Entity entity : Surround.mc.world.loadedEntityList.stream().filter(Surround::attackCrystal5).sorted(Comparator.comparing(Surround::attackCrystal6)).collect(Collectors.toList())) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (!(entity.getDistance(Surround.mc.player.posX, Surround.mc.player.posY, Surround.mc.player.posZ) <= 5.0)) {
                continue;
            }
            Surround.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            Surround.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            BlockPos blockPos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            EnumFacing enumFacing = BlockUtil.getFirstFacing(blockPos);
            if (enumFacing == null) {
                break;
            }
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            EnumFacing enumFacing2 = enumFacing.getOpposite();
            Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
            RotationUtil.faceVector(vec3d, true);
            break;
        }
    }

    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Module.Category.COMBAT);
        this.extend = this.register(new Setting<>("Extend", Boolean.TRUE, this::new1));
        this.blocksPerTick = this.register(new Setting<>("BlocksPerTick", 12, 1, 20));
        this.delay = this.register(new Setting<>("Delay", 0, 0, 250));
        this.noGhost = this.register(new Setting<>("PacketPlace", true));
        this.breakCrystal = this.register(new Setting<>("BreakCrystal", true));
        this.safeHealth = this.register(new Setting<>("Safe Health", 12.5, 1.0, 36.0, this::new2));
        this.center = this.register(new Setting<>("Center", true));
        this.rotate = this.register(new Setting<>("Rotate", true));
        this.timer = new Timer();
        this.retryTimer = new Timer();
        this.extendingBlocks = new HashSet<>();
        this.retries = new HashMap<>();
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        if (EntityUtil.isSafe(Surround.mc.player, 0, true)) {
            this.isSafe = 0;
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray(Surround.mc.player, 0, true), true, false, false);
        } else if (EntityUtil.isSafe(Surround.mc.player, -1, false)) {
            this.isSafe = 1;
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray(Surround.mc.player, -1, false), false, false, true);
        } else {
            this.isSafe = 2;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
        if (this.antiCity.getValue()) {
            BlockPos blockPos = new BlockPos(Surround.mc.player.posX, Surround.mc.player.posY, Surround.mc.player.posZ);
            if (this.breakCrystal.getValue()) {
                Surround.attackCrystal();
            }
            if (this.extend.getValue()) {
                if ((this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, 2))) != null || this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, 1))) != null) && Surround.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && Surround.mc.world.getBlockState(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                    if (this.placeBlock2(blockPos.add(0, 0, 2)) && this.placeBlock2(blockPos.add(0, 1, 2)) && this.placeBlock2(blockPos.add(-1, 0, 1))) {
                        this.placeBlock(blockPos.add(1, 0, 1));
                    }
                    if (this.placeBlock2(blockPos.add(0, 0, 3)) && this.placeBlock2(blockPos.add(0, 1, 3))) {
                        this.placeBlock(blockPos.add(0, -1, 3));
                    }
                }
                if ((this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, -2))) != null || this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, -1))) != null) && Surround.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && Surround.mc.world.getBlockState(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                    if (this.placeBlock2(blockPos.add(0, 0, -2)) && this.placeBlock2(blockPos.add(0, 1, -2)) && this.placeBlock2(blockPos.add(-1, 0, -1))) {
                        this.placeBlock(blockPos.add(1, 0, -1));
                    }
                    if (this.placeBlock2(blockPos.add(0, 0, -3)) && this.placeBlock2(blockPos.add(0, 1, -3))) {
                        this.placeBlock(blockPos.add(0, -1, -3));
                    }
                }
                if ((this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(2, 0, 0))) != null || this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 0))) != null) && Surround.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && Surround.mc.world.getBlockState(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    if (this.placeBlock2(blockPos.add(2, 0, 0)) && this.placeBlock2(blockPos.add(2, 1, 0)) && this.placeBlock2(blockPos.add(1, 0, -1))) {
                        this.placeBlock(blockPos.add(1, 0, 1));
                    }
                    if (this.placeBlock2(blockPos.add(3, 0, 0)) && this.placeBlock2(blockPos.add(3, -1, 0))) {
                        this.placeBlock(blockPos.add(3, 1, 0));
                    }
                }
                if ((this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-2, 0, 0))) != null || this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 0))) != null) && Surround.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && Surround.mc.world.getBlockState(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    if (this.placeBlock2(blockPos.add(-2, 0, 0)) && this.placeBlock2(blockPos.add(-2, 1, 0)) && this.placeBlock2(blockPos.add(-1, 0, 1))) {
                        this.placeBlock(blockPos.add(-1, 0, -1));
                    }
                    if (this.placeBlock2(blockPos.add(-3, 0, 0)) && this.placeBlock2(blockPos.add(-3, -1, 0))) {
                        this.placeBlock(blockPos.add(-3, 1, 0));
                    }
                }
            }
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos.add(0, 0, 1))) || BlockUtil.MineCheck(new BlockPos(blockPos.add(0, 0, 1)))) {
                if (this.checkCrystal(EntityUtil.getVarOffsets(0, 0, 1)) != null) {
                    this.rotateTo(this.checkCrystal(EntityUtil.getVarOffsets(0, 0, 1)));
                    EntityUtil.attackEntity(this.checkCrystal(EntityUtil.getVarOffsets(0, 0, 1)), this.packet.getValue());
                }
                if (this.placeBlock2(blockPos.add(0, 0, 2)) && Surround.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
                    this.placeBlock(blockPos.add(0, 1, 2));
                }
                if (this.placeBlock2(blockPos.add(0, 1, 1)) && Surround.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(blockPos.add(0, 0, 2)).getBlock() == Blocks.AIR) {
                    this.placeBlock(blockPos.add(0, -1, 2));
                }
                if (this.extend.getValue()) {
                    if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 1))) != null && this.placeBlock2(blockPos.add(1, 0, 2))) {
                        this.placeBlock(blockPos.add(2, 0, 1));
                    }
                    if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 1))) != null && this.placeBlock2(blockPos.add(-1, 0, 2))) {
                        this.placeBlock(blockPos.add(-2, 0, 1));
                    }
                }
                if (this.placeBlock2(blockPos.add(1, 0, 1))) {
                    this.placeBlock(blockPos.add(-1, 0, 1));
                }
            }
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos.add(0, 0, -1))) || BlockUtil.MineCheck(new BlockPos(blockPos.add(0, 0, -1)))) {
                if (this.checkCrystal(EntityUtil.getVarOffsets(0, 0, -1)) != null) {
                    this.rotateTo(this.checkCrystal(EntityUtil.getVarOffsets(0, 0, -1)));
                    EntityUtil.attackEntity(this.checkCrystal(EntityUtil.getVarOffsets(0, 0, -1)), this.packet.getValue());
                }
                if (this.placeBlock2(blockPos.add(0, 0, -2)) && Surround.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(blockPos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
                    this.placeBlock(blockPos.add(0, 1, -2));
                }
                if (this.placeBlock2(blockPos.add(0, 1, -1)) && Surround.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(blockPos.add(0, 0, -2)).getBlock() == Blocks.AIR) {
                    this.placeBlock(blockPos.add(0, -1, -2));
                }
                if (this.extend.getValue()) {
                    if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, -1))) != null && this.placeBlock2(blockPos.add(1, 0, -2))) {
                        this.placeBlock(blockPos.add(2, 0, -1));
                    }
                    if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, -1))) != null && this.placeBlock2(blockPos.add(-1, 0, -2))) {
                        this.placeBlock(blockPos.add(-2, 0, -1));
                    }
                }
                if (this.placeBlock2(blockPos.add(1, 0, -1))) {
                    this.placeBlock(blockPos.add(-1, 0, -1));
                }
            }
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos.add(1, 0, 0))) || BlockUtil.MineCheck(new BlockPos(blockPos.add(1, 0, 0)))) {
                if (this.checkCrystal(EntityUtil.getVarOffsets(1, 0, 0)) != null) {
                    this.rotateTo(this.checkCrystal(EntityUtil.getVarOffsets(1, 0, 0)));
                    EntityUtil.attackEntity(this.checkCrystal(EntityUtil.getVarOffsets(1, 0, 0)), this.packet.getValue());
                }
                this.placeBlock(blockPos.add(2, 0, 0));
                if (Surround.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
                    this.placeBlock(blockPos.add(2, 1, 0));
                }
                if (Surround.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(blockPos.add(2, 0, 0)).getBlock() == Blocks.AIR) {
                    this.placeBlock(blockPos.add(2, -1, 0));
                }
                this.placeBlock(blockPos.add(1, 1, 0));
                if (this.extend.getValue()) {
                    if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 1))) != null && this.placeBlock2(blockPos.add(1, 0, 2))) {
                        this.placeBlock(blockPos.add(2, 0, 1));
                    }
                    if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, -1))) != null && this.placeBlock2(blockPos.add(1, 0, -2))) {
                        this.placeBlock(blockPos.add(2, 0, -1));
                    }
                }
                if (this.placeBlock2(blockPos.add(1, 0, -1))) {
                    this.placeBlock(blockPos.add(1, 0, 1));
                }
            }
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos.add(-1, 0, 0))) || BlockUtil.MineCheck(new BlockPos(blockPos.add(-1, 0, 0)))) {
                if (this.checkCrystal(EntityUtil.getVarOffsets(-1, 0, 0)) != null) {
                    this.rotateTo(this.checkCrystal(EntityUtil.getVarOffsets(-1, 0, 0)));
                    EntityUtil.attackEntity(this.checkCrystal(EntityUtil.getVarOffsets(-1, 0, 0)), this.packet.getValue());
                }
                if (this.placeBlock2(blockPos.add(-2, 0, 0)) && Surround.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(blockPos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
                    this.placeBlock(blockPos.add(-2, 1, 0));
                }
                if (Surround.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && Surround.mc.world.getBlockState(blockPos.add(-2, 0, 0)).getBlock() == Blocks.AIR) {
                    if (this.placeBlock2(blockPos.add(-2, -1, 0))) {
                        this.placeBlock(blockPos.add(-1, 1, 0));
                    }
                } else {
                    this.placeBlock(blockPos.add(-1, 1, 0));
                }
                if (this.extend.getValue()) {
                    if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 1))) != null && this.placeBlock2(blockPos.add(-1, 0, 2))) {
                        this.placeBlock(blockPos.add(-2, 0, 1));
                    }
                    if (this.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, -1))) != null && this.placeBlock2(blockPos.add(-1, 0, -2))) {
                        this.placeBlock(blockPos.add(-2, 0, -1));
                    }
                }
                if (this.placeBlock2(blockPos.add(-1, 0, -1))) {
                    this.placeBlock(blockPos.add(-1, 0, 1));
                }
            }
        }
    }

    public static void breakCrystal() {
        for (Entity entity : Surround.mc.world.loadedEntityList.stream().filter(Surround::breakCrystal3).sorted(Comparator.comparing(Surround::breakCrystal4)).collect(Collectors.toList())) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (!(Surround.mc.player.getDistance(entity) <= 4.0f)) {
                continue;
            }
            Surround.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            Surround.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
        }
    }

    @Override
    public void onDisable() {
        isPlacing = false;
        this.rotating = false;
    }

    private boolean placeBlock2(BlockPos blockPos) {
        if (BlockUtil.cantBlockPlaceMine(blockPos)) {
            return true;
        }
        int n = Surround.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return true;
        }
        Surround.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        Surround.mc.playerController.updateController();
        BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
        Surround.mc.player.inventory.currentItem = n;
        Surround.mc.playerController.updateController();
        return this.checkEntityCrystal(EntityUtil.getVarOffsets(0, 0, 0), blockPos) != null;
    }

    @Override
    public void onTick() {
        if (this.startPos == null) {
            this.disable();
            return;
        }
        if (!this.startPos.equals(EntityUtil.getRoundedBlockPos(Surround.mc.player))) {
            this.disable();
            return;
        }
        if (this.breakCrystal.getValue() && (double)Surround.mc.player.getHealth() >= this.safeHealth.getValue() && this.isSafe == 0) {
            Surround.breakCrystal();
        }
        this.doFeetPlace();
    }

    Entity checkEntityCrystal(Vec3d[] vec3dArray, BlockPos blockPos) {
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray) {
            BlockPos blockPos2 = new BlockPos(blockPos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : Surround.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (!(entity2 instanceof EntityEnderCrystal)) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    private Vec3d areClose(Vec3d[] vec3dArray) {
        int n = 0;
        for (Vec3d vec3d : vec3dArray) {
            for (Vec3d vec3d2 : EntityUtil.getUnsafeBlockArray(Surround.mc.player, 0, true)) {
                if (!vec3d.equals(vec3d2)) {
                    continue;
                }
                ++n;
            }
        }
        if (n == 2) {
            return Surround.mc.player.getPositionVector().add(vec3dArray[0].add(vec3dArray[1]));
        }
        return null;
    }

    public static Surround INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Surround();
        }
        return INSTANCE;
    }

    static {
        isPlacing = false;
        breakTimer = new Timer();
        INSTANCE = new Surround();
    }

    private boolean new1(Boolean bl) {
        return this.antiCity.getValue();
    }

    Entity checkCrystal(Vec3d[] vec3dArray) {
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray) {
            BlockPos blockPos = new BlockPos(Surround.mc.player.getPositionVector()).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : Surround.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos))) {
                if (!(entity2 instanceof EntityEnderCrystal)) continue;
                if (entity != null) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    public float[] calcAngle(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = (vec3d2.y - vec3d.y) * -1.0;
        double d3 = vec3d2.z - vec3d.z;
        double d4 = MathHelper.sqrt(d * d + d3 * d3);
        return new float[]{(float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(d3, d)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(d2, d4)))};
    }

    private boolean check() {
        int n = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int n2 = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (n == -1 && n2 == -1) {
            this.disable();
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        n = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int n3 = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (n == -1 && !this.offHand && n3 == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No Obsidian in hotbar disabling...");
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.player.inventory.currentItem != this.lastHotbarSlot && Surround.mc.player.inventory.currentItem != n && Surround.mc.player.inventory.currentItem != n3) {
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!this.startPos.equals(EntityUtil.getRoundedBlockPos(Surround.mc.player))) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue());
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (send.getStage() == 0 && this.rotate.getValue() && this.rotating && send.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer cPacketPlayer = send.getPacket();
            this.rotating = false;
        }
    }

    Entity checkEntity(Vec3d[] vec3dArray, BlockPos blockPos) {
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray) {
            BlockPos blockPos2 = new BlockPos(blockPos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : Surround.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (!(entity2 instanceof EntityPlayer)) continue;
                if (entity2 == Surround.mc.player) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    @Override
    public String getDisplayInfo() {
        switch (this.isSafe) {
            case 0: {
                return ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return ChatFormatting.YELLOW + "Safe";
            }
        }
        return ChatFormatting.GREEN + "Safe";
    }

    private static Float attackCrystal6(Entity entity) {
        return Surround.mc.player.getDistance(entity);
    }

    private void rotateTo(Entity entity) {
        if (this.rotate.getValue()) {
            float[] fArray = this.calcAngle(AntiCev.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            float yaw = fArray[0];
            float pitch = fArray[1];
            this.rotating = true;
        }
    }

    private static boolean attackCrystal5(Entity entity) {
        return entity instanceof EntityEnderCrystal && !entity.isDead;
    }

    private static Float breakCrystal4(Entity entity) {
        return Surround.mc.player.getDistance(entity);
    }

    private static boolean breakCrystal3(Entity entity) {
        return entity instanceof EntityEnderCrystal && !entity.isDead;
    }

    private void placeBlock(BlockPos blockPos) {
        if (this.placements < this.blocksPerTick.getValue()) {
            if (this.antiCity.getValue()) {
                if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos))) {
                    return;
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
                    return;
                }
            }
            int n = Surround.mc.player.inventory.currentItem;
            int n2 = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int n3 = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (n2 == -1 && n3 == -1) {
                this.disable();
            }
            isPlacing = true;
            Surround.mc.player.inventory.currentItem = n2 == -1 ? n3 : n2;
            Surround.mc.playerController.updateController();
            this.isSneaking = BlockUtil.placeBlock(blockPos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            Surround.mc.player.inventory.currentItem = n;
            Surround.mc.playerController.updateController();
            this.didPlace = true;
            ++this.placements;
        }
    }

    @Override
    public void onEnable() {
        this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.startPos = new BlockPos(Surround.mc.player.posX, Surround.mc.player.posY + 0.5, Surround.mc.player.posZ);
        if (this.center.getValue()) {
            MadCat.moduleManager.enableModule("AutoCenter");
        }
        this.retries.clear();
        this.retryTimer.reset();
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < 1) {
            Vec3d[] vec3dArray = new Vec3d[2];
            int n = 0;
            for (Vec3d extendingBlock : this.extendingBlocks) {
                Vec3d vec3d;
                vec3dArray[n] = extendingBlock;
                ++n;
            }
            int n2 = this.placements;
            if (this.areClose(vec3dArray) != null) {
                this.placeBlocks(this.areClose(vec3dArray), EntityUtil.getUnsafeBlockArrayFromVec3d(this.areClose(vec3dArray), 0, true), true, false, true);
            }
            if (n2 < this.placements) {
                this.extendingBlocks.clear();
            }
        } else if (this.extendingBlocks.size() > 2 || this.extenders >= 1) {
            this.extendingBlocks.clear();
        }
    }
}

