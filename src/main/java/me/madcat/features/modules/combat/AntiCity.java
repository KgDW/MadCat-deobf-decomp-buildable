package me.madcat.features.modules.combat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;
import me.madcat.event.events.BlockBreakEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.combat.NewSurround;
import me.madcat.features.modules.combat.Surround;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RotationUtil;
import me.madcat.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiCity
extends Module {
    public final Setting<Integer> delay;
    boolean active = false;
    public final Setting<Boolean> onlySurround = this.register(new Setting<>("Only Surround", true));
    public final HashMap<EntityPlayer, BlockPos> MineMap;
    final Timer breakTimer;
    final Timer delayTimer;

    private static Float attackCrystal1(Entity entity) {
        return AntiCity.mc.player.getDistance(entity);
    }

    @Override
    public void onUpdate() {
        if (!Surround.INSTANCE().isEnabled() && !NewSurround.INSTANCE().isOn() && this.onlySurround.getValue() || !this.delayTimer.passedMs(this.delay.getValue())) {
            return;
        }
        this.delayTimer.reset();
        BlockPos blockPos = new BlockPos(AntiCity.mc.player.posX, AntiCity.mc.player.posY + 0.5, AntiCity.mc.player.posZ);
        for (EnumFacing enumFacing : EnumFacing.VALUES) {
            if (this.getBlock(blockPos.offset(enumFacing)) != Blocks.OBSIDIAN || !this.isMine(blockPos.offset(enumFacing))) continue;
            for (EnumFacing enumFacing2 : EnumFacing.VALUES) {
                if (enumFacing2 == EnumFacing.DOWN) {
                    continue;
                }
                if (!BlockUtil.cantBlockPlace(blockPos.offset(enumFacing).offset(enumFacing2))) {
                    this.placeBlock(blockPos.offset(enumFacing).offset(enumFacing2));
                    this.active = true;
                    return;
                }
                this.attackCrystal(blockPos.offset(enumFacing).offset(enumFacing2));
            }
        }
        this.active = false;
    }

    private static boolean attackCrystal0(Entity entity) {
        return entity instanceof EntityEnderCrystal && !entity.isDead;
    }

    private void placeBlock(BlockPos blockPos) {
        int n = AntiCity.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            AntiCity.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            AntiCity.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, true, true, false);
            AntiCity.mc.player.inventory.currentItem = n;
            AntiCity.mc.playerController.updateController();
        }
    }

    public AntiCity() {
        super("AntiCity", "unknown", Module.Category.COMBAT);
        this.delay = this.register(new Setting<>("Delay", 100, 0, 1000));
        this.MineMap = new HashMap();
        this.delayTimer = new Timer();
        this.breakTimer = new Timer();
    }

    @Override
    public String getDisplayInfo() {
        return this.active ? "Active" : null;
    }

    Block getBlock(BlockPos blockPos) {
        return AntiCity.mc.world.getBlockState(blockPos).getBlock();
    }

    @SubscribeEvent
    public void BrokenBlock2(BlockBreakEvent blockBreakEvent) {
        if (blockBreakEvent.getPosition().getY() == -1) {
            return;
        }
        if (AntiCity.mc.world.getBlockState(new BlockPos(blockBreakEvent.getPosition())).getBlock() == Blocks.BEDROCK) {
            return;
        }
        this.MineMap.put((EntityPlayer)AntiCity.mc.world.getEntityByID(blockBreakEvent.getBreakerId()), blockBreakEvent.getPosition());
    }

    public void attackCrystal(BlockPos blockPos) {
        if (!this.breakTimer.passedMs(300L)) {
            return;
        }
        for (Entity entity : AntiCity.mc.world.loadedEntityList.stream().filter(AntiCity::attackCrystal0).sorted(Comparator.comparing(AntiCity::attackCrystal1)).collect(Collectors.toList())) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (!(entity.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) < 2.0)) {
                continue;
            }
            AntiCity.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            AntiCity.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
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

    public boolean isMine(BlockPos blockPos) {
        if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(blockPos))) {
            return true;
        }
        for (EntityPlayer entityPlayer : this.MineMap.keySet()) {
            if (entityPlayer == null) {
                continue;
            }
            BlockPos blockPos2 = this.MineMap.get(entityPlayer);
            if (blockPos2 == null) {
                continue;
            }
            if (!new BlockPos(blockPos2).equals(new BlockPos(blockPos))) continue;
            return true;
        }
        return false;
    }
}

