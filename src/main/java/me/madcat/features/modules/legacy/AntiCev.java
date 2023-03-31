package me.madcat.features.modules.legacy;

import java.util.Comparator;
import java.util.stream.Collectors;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.exploit.Clip;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RotationUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AntiCev
extends Module {
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", true));
    BlockPos crystal;

    @Override
    public void onTick() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (Clip.INSTANCE.isEnabled()) {
            return;
        }
        BlockPos blockPos = new BlockPos(AntiCev.mc.player.posX, AntiCev.mc.player.posY, AntiCev.mc.player.posZ);
        Entity entity = this.getTarget();
        if (entity == null) {
            return;
        }
        this.crystal = new BlockPos(entity.posX, entity.posY, entity.posZ);
        if (this.getBlock(blockPos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && BlockUtil.MineCheck(blockPos.add(0, 2, 0))) {
            this.placeBlock(blockPos.add(0, 3, 0));
            if (BlockUtil.MineCheck(blockPos.add(0, 3, 0))) {
                this.placeBlock(blockPos.add(0, 4, 0));
                if (BlockUtil.cantBlockPlace2(blockPos.add(0, 4, 0)) && !BlockUtil.cantBlockPlace2(blockPos.add(-1, 4, 0))) {
                    this.placeBlock(blockPos.add(-1, 4, 0));
                }
            }
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 3, 0))) {
                if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 3, 0))) {
                    this.placeBlock(blockPos.add(-1, 3, 0));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0))) {
                    this.placeBlock(blockPos.add(-1, 2, 0));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, 0))) {
                    this.placeBlock(blockPos.add(-1, 1, 0));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 0, 0))) {
                    this.placeBlock(blockPos.add(-1, 0, 0));
                }
            }
            if (new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(0, 3, 0)))) {
                AntiCev.mc.player.jump();
                if (!AntiCev.mc.player.onGround) {
                    this.attackCrystal(blockPos.add(0, 3, 0));
                }
            }
        }
        if (this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(0, 2, 1))) && BlockUtil.MineCheck(blockPos.add(0, 1, 1))) {
            this.attackCrystal(blockPos.add(0, 2, 1));
            this.placeBlock(blockPos.add(0, 2, 1));
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 1))) {
                if (!BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 1))) {
                    this.placeBlock(blockPos.add(1, 2, 1));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(1, 1, 1))) {
                    this.placeBlock(blockPos.add(1, 1, 1));
                } else {
                    this.placeBlock(blockPos.add(1, 0, 1));
                }
            }
        }
        if (this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(0, 2, -1))) && BlockUtil.MineCheck(blockPos.add(0, 1, -1))) {
            this.attackCrystal(blockPos.add(0, 2, -1));
            this.placeBlock(blockPos.add(0, 2, -1));
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, -1))) {
                if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, -1))) {
                    this.placeBlock(blockPos.add(-1, 2, -1));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, -1))) {
                    this.placeBlock(blockPos.add(-1, 1, -1));
                } else {
                    this.placeBlock(blockPos.add(-1, 0, -1));
                }
            }
        }
        if (this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(1, 2, 0))) && BlockUtil.MineCheck(blockPos.add(1, 1, 0))) {
            this.attackCrystal(blockPos.add(1, 2, 0));
            this.placeBlock(blockPos.add(1, 2, 0));
            if (BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 0))) {
                if (!BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 1))) {
                    this.placeBlock(blockPos.add(1, 2, 1));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(1, 1, 1))) {
                    this.placeBlock(blockPos.add(1, 1, 1));
                } else {
                    this.placeBlock(blockPos.add(1, 0, 1));
                }
            }
        }
        if (this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(-1, 2, 0))) && BlockUtil.MineCheck(blockPos.add(-1, 1, 0))) {
            this.attackCrystal(blockPos.add(-1, 2, 0));
            this.placeBlock(blockPos.add(-1, 2, 0));
            if (BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0))) {
                if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, -1))) {
                    this.placeBlock(blockPos.add(-1, 2, -1));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, -1))) {
                    this.placeBlock(blockPos.add(-1, 1, -1));
                } else {
                    this.placeBlock(blockPos.add(-1, 0, -1));
                }
            }
        }
        if (this.getBlock(blockPos.add(0, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(0, 4, 0))) && BlockUtil.MineCheck(blockPos.add(0, 3, 0))) {
            this.placeBlock(blockPos.add(0, 2, 0));
        }
        if (this.getBlock(blockPos.add(0, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 4, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(0, 5, 0))) && BlockUtil.MineCheck(blockPos.add(0, 4, 0))) {
            this.placeBlock(blockPos.add(0, 3, 0));
        }
        if (this.getBlock(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(1, 3, 0)))) {
            if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.AIR) {
                this.attackCrystal(blockPos.add(1, 3, 0));
            }
            if (BlockUtil.MineCheck(blockPos.add(1, 2, 0))) {
                this.placeBlock(blockPos.add(1, 1, 0));
            }
        }
        if (this.getBlock(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(0, 3, 1)))) {
            if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.AIR) {
                this.attackCrystal(blockPos.add(0, 3, 1));
            }
            if (BlockUtil.MineCheck(blockPos.add(0, 2, 1))) {
                this.placeBlock(blockPos.add(0, 1, 1));
            }
        }
        if (this.getBlock(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(0, 3, -1)))) {
            if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.AIR) {
                this.attackCrystal(blockPos.add(0, 3, -1));
            }
            if (BlockUtil.MineCheck(blockPos.add(0, 2, -1))) {
                this.placeBlock(blockPos.add(0, 1, -1));
            }
        }
        if (this.getBlock(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && new BlockPos(this.crystal).equals(new BlockPos(blockPos.add(-1, 3, 0)))) {
            if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.AIR) {
                this.attackCrystal(blockPos.add(-1, 3, 0));
            }
            if (BlockUtil.MineCheck(blockPos.add(-1, 2, 0))) {
                this.placeBlock(blockPos.add(-1, 1, 0));
            }
        }
    }

    private static Float attackcrystal1(Entity entity) {
        return AntiCev.mc.player.getDistance(entity);
    }

    private static boolean attackcrystal0(Entity entity) {
        return entity instanceof EntityEnderCrystal && !entity.isDead;
    }

    private Entity getTarget() {
        Entity entity = null;
        for (Entity entity2 : AntiCev.mc.world.loadedEntityList) {
            if (!(entity2 instanceof EntityEnderCrystal)) {
                continue;
            }
            entity = entity2;
        }
        return entity;
    }

    public AntiCev() {
        super("AntiCev", "Anti straight line explosion and oblique angle explosion", Module.Category.LEGACY);
    }

    public void attackCrystal(BlockPos blockPos) {
        for (Entity entity : AntiCev.mc.world.loadedEntityList.stream().filter(AntiCev::attackcrystal0).sorted(Comparator.comparing(AntiCev::attackcrystal1)).collect(Collectors.toList())) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (!(entity.getDistanceSq(blockPos) <= 1.0)) {
                continue;
            }
            AntiCev.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            AntiCev.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            EnumFacing enumFacing = BlockUtil.getFirstFacing(blockPos);
            if (enumFacing == null) {
                break;
            }
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            EnumFacing enumFacing2 = enumFacing.getOpposite();
            Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
            if (!this.rotate.getValue()) break;
            RotationUtil.faceVector(vec3d, true);
            break;
        }
    }

    private void placeBlock(BlockPos blockPos) {
        if (BlockUtil.cantBlockPlaceMine(blockPos)) {
            return;
        }
        int n = AntiCev.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            AntiCev.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            AntiCev.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            AntiCev.mc.player.inventory.currentItem = n;
            AntiCev.mc.playerController.updateController();
        }
    }

    private IBlockState getBlock(BlockPos blockPos) {
        return AntiCev.mc.world.getBlockState(blockPos);
    }
}

