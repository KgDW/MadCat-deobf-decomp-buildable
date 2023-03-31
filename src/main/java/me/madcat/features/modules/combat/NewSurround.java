package me.madcat.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.combat.NewBurrow;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RotationUtil;
import me.madcat.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NewSurround
extends Module {
    public final Setting<Boolean> packet;
    public final Setting<Boolean> rotate;
    public final Setting<Integer> delay = this.register(new Setting<>("Delay", 50, 0, 500));
    public final Setting<Integer> multiPlace = this.register(new Setting<>("MultiPlace", 1, 0, 8));
    double startX = 0.0;
    double startY = 0.0;
    private final Setting<Boolean> breakCrystal;
    final Timer timer;
    private static NewSurround INSTANCE = new NewSurround();
    double startZ = 0.0;

    @Override
    public void onEnable() {
        this.startX = NewSurround.mc.player.posX;
        this.startY = NewSurround.mc.player.posY;
        this.startZ = NewSurround.mc.player.posZ;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    static boolean checkSelf(BlockPos blockPos) {
        Vec3d[] vec3dArray;
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray = EntityUtil.getVarOffsets(0, 0, 0)) {
            BlockPos blockPos2 = new BlockPos(blockPos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : NewSurround.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (entity2 != NewSurround.mc.player) continue;
                if (entity != null) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity != null;
    }

    public NewSurround() {
        super("NewSurround", "unknown", Module.Category.COMBAT);
        this.rotate = this.register(new Setting<>("Rotate", true));
        this.packet = this.register(new Setting<>("Packet", true));
        this.breakCrystal = this.add(new Setting<>("BreakCrystal", true));
        this.timer = new Timer();
        this.setInstance();
    }

    public static NewSurround INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NewSurround();
        }
        return INSTANCE;
    }

    private boolean placeBlock(BlockPos blockPos) {
        if (BlockUtil.cantBlockPlaceMine(blockPos)) {
            return false;
        }
        int n = NewSurround.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return false;
        }
        NewSurround.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        NewSurround.mc.playerController.updateController();
        BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
        NewSurround.mc.player.inventory.currentItem = n;
        NewSurround.mc.playerController.updateController();
        return true;
    }

    @Override
    public void onUpdate() {
        if (!this.timer.passedMs(this.delay.getValue())) {
            return;
        }
        this.timer.reset();
        BlockPos blockPos = new BlockPos(NewSurround.mc.player.posX, NewSurround.mc.player.posY + 0.5, NewSurround.mc.player.posZ);
        if (NewSurround.mc.player.getDistance(this.startX, this.startY, this.startZ) > 1.3 || this.startY - NewSurround.mc.player.posY > 0.5 || this.startY - NewSurround.mc.player.posY < -0.5) {
            this.disable();
            return;
        }
        if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) == -1) {
            Command.sendMessage(ChatFormatting.AQUA + "<Surround> " + ChatFormatting.GRAY + "cant find obsidian");
            this.disable();
            return;
        }
        if (this.breakCrystal.getValue()) {
            this.attackCrystal();
        }
        int n = 0;
        if (this.placeBlock(blockPos.add(0, -1, 0))) {
            ++n;
        }
        for (EnumFacing enumFacing : EnumFacing.VALUES) {
            if (enumFacing == EnumFacing.DOWN) continue;
            if (enumFacing == EnumFacing.UP) {
                continue;
            }
            if (n > this.multiPlace.getDefaultValue() - 1) {
                return;
            }
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (NewSurround.checkSelf(blockPos2)) {
                if (this.placeBlock(blockPos2)) {
                    ++n;
                }
                if (n > this.multiPlace.getDefaultValue() - 1) {
                    return;
                }
                for (EnumFacing enumFacing2 : EnumFacing.VALUES) {
                    if (enumFacing2 == EnumFacing.DOWN) continue;
                    if (enumFacing2 == EnumFacing.UP) {
                        continue;
                    }
                    if (n > this.multiPlace.getDefaultValue() - 1) {
                        return;
                    }
                    BlockPos blockPos3 = blockPos2.offset(enumFacing2);
                    if (NewSurround.checkSelf(blockPos3)) {
                        for (EnumFacing enumFacing3 : EnumFacing.VALUES) {
                            if (enumFacing3 == EnumFacing.DOWN) continue;
                            if (enumFacing3 == EnumFacing.UP) {
                                continue;
                            }
                            if (n > this.multiPlace.getDefaultValue() - 1) {
                                return;
                            }
                            if (this.placeBlock(blockPos3)) {
                                ++n;
                            }
                            if (n > this.multiPlace.getDefaultValue() - 1) {
                                return;
                            }
                            BlockPos blockPos4 = blockPos3.offset(enumFacing3);
                            if (!BlockUtil.cantBlockPlace2(blockPos4)) {
                                if (this.placeBlock(blockPos4)) {
                                    ++n;
                                }
                                continue;
                            }
                            if (!this.placeBlock(blockPos4.offset(EnumFacing.DOWN))) continue;
                            ++n;
                        }
                    }
                    if (n > this.multiPlace.getDefaultValue() - 1) {
                        return;
                    }
                    if (!BlockUtil.cantBlockPlace2(blockPos3)) {
                        if (this.placeBlock(blockPos3)) {
                            ++n;
                        }
                        continue;
                    }
                    if (!this.placeBlock(blockPos3.offset(EnumFacing.DOWN))) continue;
                    ++n;
                }
            }
            if (n > this.multiPlace.getDefaultValue() - 1) {
                return;
            }
            if (!BlockUtil.cantBlockPlace2(blockPos2)) {
                if (this.placeBlock(blockPos2)) {
                    ++n;
                }
                continue;
            }
            if (!this.placeBlock(blockPos2.offset(EnumFacing.DOWN))) continue;
            ++n;
        }
    }

    public void attackCrystal() {
        if (!NewBurrow.breakTimer.passedMs(250L)) {
            return;
        }
        NewBurrow.breakTimer.reset();
        for (Entity entity : NewSurround.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (entity.getDistance(NewSurround.mc.player.posX, NewSurround.mc.player.posY, NewSurround.mc.player.posZ) > 2.5) {
                continue;
            }
            NewSurround.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            NewSurround.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            BlockPos blockPos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            if (!this.rotate.getValue()) break;
            RotationUtil.facePos(blockPos);
            break;
        }
    }
}

