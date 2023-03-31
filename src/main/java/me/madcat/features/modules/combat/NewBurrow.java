package me.madcat.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RotationUtil;
import me.madcat.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NewBurrow
extends Module {
    public final Setting<Boolean> packet;
    public final Setting<Boolean> onlyGround;
    private final Setting<Boolean> breakCrystal;
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    private final Setting<Boolean> multiPlace;
    public final Setting<Boolean> debug;
    static final Timer breakTimer = new Timer();

    public void attackCrystal() {
        if (!breakTimer.passedMs(250L)) {
            return;
        }
        breakTimer.reset();
        for (Entity entity : NewBurrow.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (entity.getDistance(NewBurrow.mc.player.posX, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ) > 2.5) {
                continue;
            }
            NewBurrow.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
            NewBurrow.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            BlockPos blockPos = new BlockPos(entity.posX, entity.posY, entity.posZ);
            if (!this.rotate.getValue()) break;
            RotationUtil.facePos(blockPos);
            break;
        }
    }

    @Override
    public void onTick() {
        int n = NewBurrow.mc.player.inventory.currentItem;
        BlockPos blockPos = new BlockPos(NewBurrow.mc.player.posX, NewBurrow.mc.player.posY + 0.5, NewBurrow.mc.player.posZ);
        if (InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1 && InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)) == -1) {
            Command.sendMessage(ChatFormatting.RED + "Obsidian/Ender Chest ?");
            this.disable();
            return;
        }
        if (this.breakCrystal.getValue()) {
            this.attackCrystal();
        }
        if (this.onlyGround.getValue() && !NewBurrow.mc.player.onGround) {
            return;
        }
        if (!NewBurrow.mc.world.getBlockState(blockPos.offset(EnumFacing.UP, 2)).getBlock().equals(Blocks.AIR) || NewBurrow.mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock() != Blocks.AIR && NewBurrow.checkSelf(blockPos.add(1, 0, 0)) != null || NewBurrow.mc.world.getBlockState(blockPos.add(-1, 2, 0)).getBlock() != Blocks.AIR && NewBurrow.checkSelf(blockPos.add(-1, 0, 0)) != null || NewBurrow.mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock() != Blocks.AIR && NewBurrow.checkSelf(blockPos.add(0, 0, 1)) != null || NewBurrow.mc.world.getBlockState(blockPos.add(0, 2, -1)).getBlock() != Blocks.AIR && NewBurrow.checkSelf(blockPos.add(0, 0, -1)) != null || NewBurrow.mc.world.getBlockState(blockPos.add(1, 2, 1)).getBlock() != Blocks.AIR && NewBurrow.checkSelf(blockPos.add(1, 0, 1)) != null || NewBurrow.mc.world.getBlockState(blockPos.add(1, 2, -1)).getBlock() != Blocks.AIR && NewBurrow.checkSelf(blockPos.add(1, 0, -1)) != null || NewBurrow.mc.world.getBlockState(blockPos.add(-1, 2, 1)).getBlock() != Blocks.AIR && NewBurrow.checkSelf(blockPos.add(-1, 0, 1)) != null || NewBurrow.mc.world.getBlockState(blockPos.add(-1, 2, -1)).getBlock() != Blocks.AIR && NewBurrow.checkSelf(blockPos.add(-1, 0, -1)) != null) {
            boolean bl = false;
            boolean bl2 = false;
            BlockPos blockPos2 = blockPos;
            if (NewBurrow.checkSelf(blockPos2) != null && !NewBurrow.isAir(blockPos2)) {
                NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX) / 2.0, NewBurrow.mc.player.posY + 0.2, NewBurrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ) / 2.0, false));
                NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX), NewBurrow.mc.player.posY + 0.2, NewBurrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ), false));
                if (this.debug.getValue()) {
                    Command.sendMessage("autochthonous " + ((double) blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX) + " " + ((double) blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ));
                }
            } else {
                for (EnumFacing enumFacing : EnumFacing.VALUES) {
                    if (enumFacing == EnumFacing.UP) continue;
                    if (enumFacing == EnumFacing.DOWN) {
                        continue;
                    }
                    blockPos2 = blockPos.offset(enumFacing);
                    if (NewBurrow.checkSelf(blockPos2) == null || NewBurrow.isAir(blockPos2)) continue;
                    NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX) / 2.0, NewBurrow.mc.player.posY + 0.2, NewBurrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ) / 2.0, false));
                    NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX), NewBurrow.mc.player.posY + 0.2, NewBurrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ), false));
                    bl = true;
                    if (!this.debug.getValue()) break;
                    Command.sendMessage("no air " + ((double) blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX) + " " + ((double) blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ));
                    break;
                }
                if (!bl) {
                    for (EnumFacing enumFacing : EnumFacing.VALUES) {
                        if (enumFacing == EnumFacing.UP) continue;
                        if (enumFacing == EnumFacing.DOWN) {
                            continue;
                        }
                        blockPos2 = blockPos.offset(enumFacing);
                        if (NewBurrow.checkSelf(blockPos2) == null) continue;
                        NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX) / 2.0, NewBurrow.mc.player.posY + 0.2, NewBurrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ) / 2.0, false));
                        NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX), NewBurrow.mc.player.posY + 0.2, NewBurrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ), false));
                        bl2 = true;
                        if (!this.debug.getValue()) break;
                        Command.sendMessage("entity " + ((double) blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX) + " " + ((double) blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ));
                        break;
                    }
                    if (!bl2) {
                        for (EnumFacing enumFacing : EnumFacing.VALUES) {
                            if (enumFacing == EnumFacing.UP) continue;
                            if (enumFacing == EnumFacing.DOWN) {
                                continue;
                            }
                            blockPos2 = blockPos.offset(enumFacing);
                            if (!NewBurrow.isAir(blockPos2) || !NewBurrow.isAir(blockPos2.offset(EnumFacing.UP))) continue;
                            NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX) / 2.0, NewBurrow.mc.player.posY + 0.2, NewBurrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ) / 2.0, false));
                            NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX), NewBurrow.mc.player.posY + 0.2, NewBurrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ), false));
                            if (!this.debug.getValue()) break;
                            Command.sendMessage("air " + ((double) blockPos2.getX() + 0.5 - NewBurrow.mc.player.posX) + " " + ((double) blockPos2.getZ() + 0.5 - NewBurrow.mc.player.posZ));
                            break;
                        }
                    }
                }
            }
        } else {
            if (this.debug.getValue()) {
                Command.sendMessage("fake jump");
            }
            NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX, NewBurrow.mc.player.posY + 0.4199999868869781, NewBurrow.mc.player.posZ, false));
            NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX, NewBurrow.mc.player.posY + 0.7531999805212017, NewBurrow.mc.player.posZ, false));
            NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX, NewBurrow.mc.player.posY + 0.9999957640154541, NewBurrow.mc.player.posZ, false));
            NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX, NewBurrow.mc.player.posY + 1.1661092609382138, NewBurrow.mc.player.posZ, false));
        }
        NewBurrow.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) != -1 ? InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) : InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST));
        NewBurrow.mc.playerController.updateController();
        if (this.multiPlace.getValue()) {
            if (NewBurrow.isAir(new BlockPos(NewBurrow.mc.player.posX + 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ + 0.3))) {
                try {
                    BlockUtil.placeBlock(new BlockPos(NewBurrow.mc.player.posX + 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ + 0.3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (NewBurrow.isAir(new BlockPos(NewBurrow.mc.player.posX + 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ - 0.3))) {
                try {
                    BlockUtil.placeBlock(new BlockPos(NewBurrow.mc.player.posX + 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ - 0.3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (NewBurrow.isAir(new BlockPos(NewBurrow.mc.player.posX - 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ + 0.3))) {
                try {
                    BlockUtil.placeBlock(new BlockPos(NewBurrow.mc.player.posX - 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ + 0.3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (NewBurrow.isAir(new BlockPos(NewBurrow.mc.player.posX - 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ - 0.3))) {
                try {
                    BlockUtil.placeBlock(new BlockPos(NewBurrow.mc.player.posX - 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ - 0.3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (NewBurrow.isAir(blockPos)) {
            try {
                BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (NewBurrow.isAir(new BlockPos(NewBurrow.mc.player.posX + 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ + 0.3))) {
            try {
                BlockUtil.placeBlock(new BlockPos(NewBurrow.mc.player.posX + 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ + 0.3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (NewBurrow.isAir(new BlockPos(NewBurrow.mc.player.posX + 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ - 0.3))) {
            try {
                BlockUtil.placeBlock(new BlockPos(NewBurrow.mc.player.posX + 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ - 0.3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (NewBurrow.isAir(new BlockPos(NewBurrow.mc.player.posX - 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ + 0.3))) {
            try {
                BlockUtil.placeBlock(new BlockPos(NewBurrow.mc.player.posX - 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ + 0.3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (NewBurrow.isAir(new BlockPos(NewBurrow.mc.player.posX - 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ - 0.3))) {
            try {
                BlockUtil.placeBlock(new BlockPos(NewBurrow.mc.player.posX - 0.3, NewBurrow.mc.player.posY, NewBurrow.mc.player.posZ - 0.3), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        NewBurrow.mc.player.inventory.currentItem = n;
        NewBurrow.mc.playerController.updateController();
        NewBurrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(NewBurrow.mc.player.posX, -7.0, NewBurrow.mc.player.posZ, false));
        this.disable();
    }

    static Entity checkSelf(BlockPos blockPos) {
        Vec3d[] vec3dArray;
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray = EntityUtil.getVarOffsets(0, 0, 0)) {
            BlockPos blockPos2 = new BlockPos(blockPos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : NewBurrow.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (entity2 != NewBurrow.mc.player) continue;
                if (entity != null) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    public NewBurrow() {
        super("NewBurrow", "unknown", Module.Category.COMBAT);
        this.packet = this.register(new Setting<>("Packet", true));
        this.onlyGround = this.register(new Setting<>("onlyGround", true));
        this.debug = this.register(new Setting<>("Debug", false));
        this.breakCrystal = this.add(new Setting<>("BreakCrystal", true));
        this.multiPlace = this.add(new Setting<>("MultiPlace", true));
    }

    public static boolean isAir(BlockPos blockPos) {
        Block block = NewBurrow.mc.world.getBlockState(blockPos).getBlock();
        return block instanceof BlockAir;
    }
}

