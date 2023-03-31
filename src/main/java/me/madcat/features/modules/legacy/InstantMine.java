package me.madcat.features.modules.legacy;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.madcat.event.events.PacketEvent;
import me.madcat.event.events.PlayerDamageBlockEvent;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.combat.AntiBurrow;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.FadeUtils;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RenderUtil;
import me.madcat.util.RotationUtil;
import me.madcat.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InstantMine
extends Module {
    private final Setting<Integer> pos1FillAlpha;
    private boolean empty = false;
    private final Setting<Boolean> render2;
    int slotMain2;
    boolean switched = false;
    private final Setting<Boolean> render;
    public final Setting<Float> health;
    public static BlockPos breakPos2;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> ghostHand;
    public final FadeUtils secondFade;
    public final FadeUtils firstFade;
    public static BlockPos breakPos;
    static int ticked;
    private static InstantMine INSTANCE;
    private final Setting<Boolean> crystalOnBreak;
    public final Setting<Bind> bind;
    private final Timer breakSuccess;
    private final Setting<Integer> alpha;
    public static final List<Block> godBlocks;
    private final Setting<Integer> green;
    final Timer retryTime;
    private EnumFacing facing;
    public final Setting<Boolean> db;
    private final Setting<Integer> blue;
    private boolean cancelStart = false;
    private final Setting<Boolean> placeCrystal;
    private final Setting<Boolean> instant;
    private final Setting<Integer> alpha2;
    public final Setting<Boolean> attackCrystal = this.register(new Setting<>("Attack Crystal", Boolean.TRUE));
    private final Setting<Integer> red;
    private final Setting<Integer> pos1BoxAlpha;
    private final Setting<Boolean> retry;

    @Override
    public void onUpdate() {
        int n;
        if (Feature.fullNullCheck()) {
            return;
        }
        if (InstantMine.mc.player.isCreative()) {
            return;
        }
        this.slotMain2 = InstantMine.mc.player.inventory.currentItem;
        if (ticked == 0) {
            breakPos2 = null;
        }
        if (breakPos2 != null) {
            if (ticked < 60 && ticked > 1) {
                ++ticked;
            }
            if (ticked > 30 || ticked > 13 && InstantMine.mc.world.getBlockState(breakPos2).getBlock() == Blocks.ENDER_CHEST || ticked >= 3 && InstantMine.mc.world.getBlockState(breakPos2).getBlock() == Blocks.WEB) {
                if (InstantMine.mc.player.isHandActive()) {
                    this.resetMine();
                } else {
                    this.switchMine();
                }
            }
            if (InstantMine.mc.world.isAirBlock(breakPos2)) {
                this.resetMine();
                breakPos2 = null;
                ticked = 0;
            }
        } else {
            ticked = 0;
        }
        if (ticked > 80) {
            this.resetMine();
            breakPos2 = null;
            ticked = 0;
        }
        if (breakPos == null) {
            return;
        }
        if (!this.instant.getValue() && InstantMine.mc.world.isAirBlock(breakPos)) {
            breakPos = null;
            return;
        }
        if (!this.cancelStart) {
            return;
        }
        if (InstantMine.mc.player.getDistance(breakPos.getX(), breakPos.getY(), breakPos.getZ()) > 6.0) {
            return;
        }
        if (this.retry.getValue() && this.retryTime.passedMs(1000L) && !this.instant.getValue()) {
            this.retryTime.reset();
            InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, breakPos, this.facing));
            this.cancelStart = true;
            InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
        }
        if (this.attackCrystal.getValue() && InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) {
            InstantMine.attackcrystal(this.rotate.getValue());
        }
        if (this.bind.getValue().isDown() && this.placeCrystal.getValue() && InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1 && InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) {
            n = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int n2 = InstantMine.mc.player.inventory.currentItem;
            this.switchToSlot(n);
            BlockUtil.placeBlock(breakPos, EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(n2);
        }
        if (InventoryUtil.getItemHotbar(Items.END_CRYSTAL) != -1 && this.placeCrystal.getValue() && InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN && !breakPos.equals(AntiBurrow.pos)) {
            if (this.empty) {
                BlockUtil.placeCrystalOnBlock(breakPos, EnumHand.MAIN_HAND, true, false, true);
            } else if (!this.crystalOnBreak.getValue()) {
                BlockUtil.placeCrystalOnBlock(breakPos, EnumHand.MAIN_HAND, true, false, true);
            }
        }
        if (godBlocks.contains(InstantMine.mc.world.getBlockState(breakPos).getBlock())) {
            return;
        }
        if (this.rotate.getValue()) {
            RotationUtil.facePos(breakPos);
        }
        if (InstantMine.mc.world.getBlockState(breakPos).getBlock() != Blocks.WEB) {
            if (this.ghostHand.getValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1 && InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE) != -1) {
                n = InstantMine.mc.player.inventory.currentItem;
                if (InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN) {
                    if (!this.breakSuccess.passedMs(1400L)) {
                        return;
                    }
                    InstantMine.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                    InstantMine.mc.playerController.updateController();
                    InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
                    InstantMine.mc.player.inventory.currentItem = n;
                    InstantMine.mc.playerController.updateController();
                    return;
                }
                InstantMine.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                InstantMine.mc.playerController.updateController();
                InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
                InstantMine.mc.player.inventory.currentItem = n;
                InstantMine.mc.playerController.updateController();
                return;
            }
        } else if (this.ghostHand.getValue() && InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD) != -1 && InventoryUtil.getItemHotbars(Items.DIAMOND_SWORD) != -1) {
            n = InstantMine.mc.player.inventory.currentItem;
            InstantMine.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
            InstantMine.mc.playerController.updateController();
            InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
            InstantMine.mc.player.inventory.currentItem = n;
            InstantMine.mc.playerController.updateController();
            return;
        }
        InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
    }

    private boolean new1(Boolean bl) {
        return !this.instant.getValue();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private boolean new3(Integer n) {
        return this.render2.getValue();
    }

    private void switchMine() {
        if (InstantMine.mc.player.getHealth() + InstantMine.mc.player.getAbsorptionAmount() >= this.health.getValue()) {
            if (this.db.getValue()) {
                if (InstantMine.mc.world.getBlockState(breakPos2).getBlock() == Blocks.WEB) {
                    if (InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD) != -1) {
                        InstantMine.mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtil.getItemHotbars(Items.DIAMOND_SWORD)));
                        this.switched = true;
                        ++ticked;
                    }
                } else if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) {
                    InstantMine.mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE)));
                    this.switched = true;
                    ++ticked;
                }
            }
        } else if (this.switched) {
            InstantMine.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.slotMain2));
            this.switched = false;
        }
    }

    public static InstantMine INSTANCE() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new InstantMine();
        return INSTANCE;
    }

    public InstantMine() {
        super("InstantMine", "legacy", Module.Category.LEGACY);
        this.db = this.register(new Setting<>("Silent Double", Boolean.TRUE));
        this.health = this.register(new Setting<>("Health", 18.0f, 0.0f, 35.9f, this::new0));
        this.breakSuccess = new Timer();
        this.instant = this.register(new Setting<>("Instant", true));
        this.retry = this.register(new Setting<>("Retry", Boolean.TRUE, this::new1));
        this.rotate = this.register(new Setting<>("Rotate", true));
        this.ghostHand = this.register(new Setting<>("GhostHand", Boolean.TRUE));
        this.render = this.register(new Setting<>("Pos1Fill", true));
        this.pos1FillAlpha = this.register(new Setting<>("Pos1FillAlpha", 30, 0, 255, this::new2));
        this.render2 = this.register(new Setting<>("Pos1Box", true));
        this.pos1BoxAlpha = this.register(new Setting<>("Pos1BoxAlpha", 100, 0, 255, this::new3));
        this.placeCrystal = this.register(new Setting<>("Place Crystal", Boolean.TRUE));
        this.bind = this.register(new Setting<Object>("ObsidianBind", new Bind(-1), this::new4));
        this.crystalOnBreak = this.register(new Setting<>("Crystal on Break", Boolean.TRUE, this::new5));
        this.red = this.register(new Setting<>("Pos2Red", 255, 0, 255));
        this.green = this.register(new Setting<>("Pos2Green", 255, 0, 255));
        this.blue = this.register(new Setting<>("Pos2Blue", 255, 0, 255));
        this.alpha = this.register(new Setting<>("Pos2BoxAlpha", 150, 0, 255));
        this.alpha2 = this.register(new Setting<>("Pos2FillAlpha", 70, 0, 255));
        this.firstFade = new FadeUtils(1500L);
        this.secondFade = new FadeUtils(1500L);
        this.retryTime = new Timer();
        this.setInstance();
    }

    private void switchToSlot(int n) {
        InstantMine.mc.player.inventory.currentItem = n;
        InstantMine.mc.playerController.updateController();
    }

    @SubscribeEvent
    public void onBlockEvent(PlayerDamageBlockEvent playerDamageBlockEvent) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (InstantMine.mc.player.isCreative()) {
            return;
        }
        if (!BlockUtil.canBreak(playerDamageBlockEvent.pos)) {
            return;
        }
        if (breakPos != null) {
            if (breakPos.getX() == playerDamageBlockEvent.pos.getX() && breakPos.getY() == playerDamageBlockEvent.pos.getY() && breakPos.getZ() == playerDamageBlockEvent.pos.getZ()) {
                return;
            }
            if (breakPos.equals(breakPos2)) {
                this.secondFade.reset();
            }
        }
        this.firstFade.reset();
        this.empty = false;
        this.cancelStart = false;
        breakPos = playerDamageBlockEvent.pos;
        this.breakSuccess.reset();
        this.facing = playerDamageBlockEvent.facing;
        if (breakPos == null) {
            return;
        }
        InstantMine.mc.player.swingArm(EnumHand.MAIN_HAND);
        InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, breakPos, this.facing));
        this.cancelStart = true;
        InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
        playerDamageBlockEvent.setCanceled(true);
        if (breakPos2 == null) {
            ticked = 1;
            breakPos2 = playerDamageBlockEvent.pos;
            this.secondFade.reset();
        }
    }

    public static void attackcrystal(boolean bl) {
        for (Entity entity : InstantMine.mc.world.loadedEntityList.stream().filter(InstantMine::attackcrystal).sorted(Comparator.comparing(InstantMine::attackcrystal7)).collect(Collectors.toList())) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (!(entity.getDistanceSq(breakPos) <= 2.0)) {
                continue;
            }
            InstantMine.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            InstantMine.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            if (!bl) continue;
            RotationUtil.facePos(new BlockPos(entity.posX, entity.posY + 0.5, entity.posZ));
        }
    }

    private boolean new2(Integer n) {
        return this.render.getValue();
    }

    private boolean new4(Object object) {
        return this.placeCrystal.getValue();
    }

    private static boolean attackcrystal(Entity entity) {
        return entity instanceof EntityEnderCrystal && !entity.isDead;
    }

    static {
        godBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.BEDROCK);
        breakPos2 = null;
        ticked = 0;
        INSTANCE = new InstantMine();
    }

    private static Float attackcrystal7(Entity entity) {
        return InstantMine.mc.player.getDistance(entity);
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (!InstantMine.mc.player.isCreative()) {
            AxisAlignedBB axisAlignedBB;
            double d;
            double d2;
            double d3;
            double d4;
            double d5;
            double d6;
            AxisAlignedBB axisAlignedBB2;
            if (breakPos2 != null) {
                axisAlignedBB2 = InstantMine.mc.world.getBlockState(breakPos2).getSelectedBoundingBox(InstantMine.mc.world, breakPos2);
                d6 = axisAlignedBB2.minX + (axisAlignedBB2.maxX - axisAlignedBB2.minX) / 2.0;
                d5 = axisAlignedBB2.minY + (axisAlignedBB2.maxY - axisAlignedBB2.minY) / 2.0;
                d4 = axisAlignedBB2.minZ + (axisAlignedBB2.maxZ - axisAlignedBB2.minZ) / 2.0;
                d3 = this.secondFade.easeOutQuad() * (axisAlignedBB2.maxX - d6);
                d2 = this.secondFade.easeOutQuad() * (axisAlignedBB2.maxY - d5);
                d = this.secondFade.easeOutQuad() * (axisAlignedBB2.maxZ - d4);
                axisAlignedBB = new AxisAlignedBB(d6 - d3, d5 - d2, d4 - d, d6 + d3, d5 + d2, d4 + d);
                if (breakPos != null) {
                    if (!breakPos2.equals(breakPos)) {
                        RenderUtil.drawBBBox(axisAlignedBB, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.alpha.getValue());
                        RenderUtil.drawBBFill(axisAlignedBB, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha2.getValue()), this.alpha2.getValue());
                    }
                } else {
                    RenderUtil.drawBBBox(axisAlignedBB, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.alpha.getValue());
                    RenderUtil.drawBBFill(axisAlignedBB, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha2.getValue()), this.alpha2.getValue());
                }
            }
            if (this.cancelStart && breakPos != null) {
                if (godBlocks.contains(InstantMine.mc.world.getBlockState(breakPos).getBlock())) {
                    this.empty = true;
                }
                axisAlignedBB2 = InstantMine.mc.world.getBlockState(breakPos).getSelectedBoundingBox(InstantMine.mc.world, breakPos);
                d6 = axisAlignedBB2.minX + (axisAlignedBB2.maxX - axisAlignedBB2.minX) / 2.0;
                d5 = axisAlignedBB2.minY + (axisAlignedBB2.maxY - axisAlignedBB2.minY) / 2.0;
                d4 = axisAlignedBB2.minZ + (axisAlignedBB2.maxZ - axisAlignedBB2.minZ) / 2.0;
                d3 = this.firstFade.easeOutQuad() * (axisAlignedBB2.maxX - d6);
                d2 = this.firstFade.easeOutQuad() * (axisAlignedBB2.maxY - d5);
                d = this.firstFade.easeOutQuad() * (axisAlignedBB2.maxZ - d4);
                axisAlignedBB = new AxisAlignedBB(d6 - d3, d5 - d2, d4 - d, d6 + d3, d5 + d2, d4 + d);
                if (this.render.getValue()) {
                    RenderUtil.drawBBFill(axisAlignedBB, new Color(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255), this.pos1FillAlpha.getValue());
                }
                if (this.render2.getValue()) {
                    RenderUtil.drawBBBox(axisAlignedBB, new Color(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255), this.pos1BoxAlpha.getValue());
                }
            }
        }
    }

    private boolean new0(Float f) {
        return this.db.getValue();
    }

    private void resetMine() {
        if (this.switched) {
            InstantMine.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.slotMain2));
            this.switched = false;
        }
    }

    private boolean new5(Boolean bl) {
        return this.placeCrystal.getValue();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (InstantMine.mc.player.isCreative()) {
            return;
        }
        if (!(send.getPacket() instanceof CPacketPlayerDigging)) {
            return;
        }
        CPacketPlayerDigging cPacketPlayerDigging = send.getPacket();
        if (cPacketPlayerDigging.getAction() != CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            return;
        }
        send.setCanceled(this.cancelStart);
    }
}

