package me.madcat.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Comparator;
import java.util.stream.Collectors;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;

public class Burrow
extends Module {
    private final Timer timer;
    Vec3d posCheck = null;
    public final Setting<Boolean> noToggle = this.add(new Setting<>("noDisable", false));
    private final Setting<Boolean> smartOffset;
    private final Setting<Boolean> onlyOnGround;
    private final Setting<Boolean> rotate;
    private final Setting<Integer> delay;
    private final Setting<Boolean> breakCrystal;
    private final Setting<Boolean> fristObby;
    private final Setting<Double> offsetZ;
    private final Setting<Boolean> bypass;
    private final Setting<Double> offsetX;
    private final Setting<Boolean> multiPlace;
    private static Burrow INSTANCE = new Burrow();
    private final Setting<Boolean> wait = this.add(new Setting<>("WaitPlace", true));
    private final Setting<Double> offsetY;
    public final Setting<Boolean> tpcenter;

    private void setInstance() {
        INSTANCE = this;
    }

    public static boolean isAir(BlockPos blockPos) {
        Block block = Burrow.mc.world.getBlockState(blockPos).getBlock();
        return block instanceof BlockAir;
    }

    private static Double onTick2(Entity entity) {
        return Burrow.mc.player.getDistanceSq(entity);
    }

    public static boolean fakeBBoxCheck(EntityPlayer entityPlayer, Vec3d vec3d, boolean bl) {
        Vec3d vec3d2 = entityPlayer.getPositionVector().add(vec3d);
        if (bl) {
            Vec3d vec3d3 = entityPlayer.getPositionVector();
            return Burrow.isAir(vec3d2.add(0.3, 0.0, 0.3)) && Burrow.isAir(vec3d2.add(-0.3, 0.0, 0.3)) && Burrow.isAir(vec3d2.add(0.3, 0.0, -0.3)) && Burrow.isAir(vec3d2.add(-0.3, 0.0, 0.3)) && Burrow.isAir(vec3d2.add(0.3, 1.8, 0.3)) && Burrow.isAir(vec3d2.add(-0.3, 1.8, 0.3)) && Burrow.isAir(vec3d2.add(0.3, 1.8, -0.3)) && Burrow.isAir(vec3d2.add(-0.3, 1.8, 0.3)) && Burrow.isAir(vec3d3.add(0.3, 2.8, 0.3)) && Burrow.isAir(vec3d3.add(-0.3, 2.8, 0.3)) && Burrow.isAir(vec3d3.add(-0.3, 2.8, -0.3)) && Burrow.isAir(vec3d3.add(0.3, 2.8, -0.3));
        }
        return Burrow.isAir(vec3d2.add(0.3, 0.0, 0.3)) && Burrow.isAir(vec3d2.add(-0.3, 0.0, 0.3)) && Burrow.isAir(vec3d2.add(0.3, 0.0, -0.3)) && Burrow.isAir(vec3d2.add(-0.3, 0.0, 0.3)) && Burrow.isAir(vec3d2.add(0.3, 1.8, 0.3)) && Burrow.isAir(vec3d2.add(-0.3, 1.8, 0.3)) && Burrow.isAir(vec3d2.add(0.3, 1.8, -0.3)) && Burrow.isAir(vec3d2.add(-0.3, 1.8, 0.3));
    }

    public static BlockPos vec3toBlockPos(Vec3d vec3d) {
        return new BlockPos(Math.floor(vec3d.x), (double)Math.round(vec3d.y), Math.floor(vec3d.z));
    }

    public Burrow() {
        super("Burrow", "Rubberbands you into a block.", Module.Category.COMBAT);
        this.delay = this.add(new Setting<>("Delay", 3, 1, 20, this::new0));
        this.bypass = this.add(new Setting<>("Bypass", true));
        this.timer = new Timer();
        this.breakCrystal = this.add(new Setting<>("BreakCrystal", true));
        this.onlyOnGround = this.add(new Setting<>("OnlyOnGround", true));
        this.multiPlace = this.add(new Setting<>("MultiPlace", true));
        this.rotate = this.add(new Setting<>("Rotate", true));
        this.smartOffset = this.add(new Setting<>("SmartOffset", true));
        this.tpcenter = this.add(new Setting<>("TPCenter", false));
        this.offsetX = this.add(new Setting<>("OffsetX", -7.0, -10.0, 10.0));
        this.offsetY = this.add(new Setting<>("OffsetY", -7.0, -10.0, 10.0));
        this.offsetZ = this.add(new Setting<>("OffsetZ", -7.0, -10.0, 10.0));
        this.fristObby = this.add(new Setting<>("FristObby", true));
        this.setInstance();
    }

    public BlockPos getOffsetBlock(EntityPlayer entityPlayer) {
        Vec3d vec3d = new Vec3d(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        RayTraceResult result = entityPlayer.world.rayTraceBlocks(vec3d, vec3d.add(new Vec3d(0, -1, 0)));
        if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && canBur(result.getBlockPos())) {
            return result.getBlockPos();
        }
        return null;
    }

    private boolean canBur(BlockPos blockPos) {
        return false;
    }


    @Override
    public void onTick() {
        if (this.posCheck != null) {
            if (Burrow.mc.player.getDistance(this.posCheck.x, this.posCheck.y, this.posCheck.z) > 1.0) {
                this.disable();
                return;
            }
        } else if (this.noToggle.getValue()) {
            this.disable();
            return;
        }
        BlockPos blockPos = new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.5, Burrow.mc.player.posZ);
        if (!(Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, 0))) != null && Burrow.isAir(blockPos.add(0, 0, 0)) || Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, 1))) != null && Burrow.isAir(blockPos.add(0, 0, 1)) || Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, -1))) != null && Burrow.isAir(blockPos.add(0, 0, -1)) || Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 0))) != null && Burrow.isAir(blockPos.add(1, 0, 0)) || Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 0))) != null && Burrow.isAir(blockPos.add(-1, 0, 0)) || Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 1))) != null && Burrow.isAir(blockPos.add(1, 0, 1)) || Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, -1))) != null && Burrow.isAir(blockPos.add(-1, 0, -1)) || Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, -1))) != null && Burrow.isAir(blockPos.add(1, 0, -1)) || Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 1))) != null && Burrow.isAir(blockPos.add(-1, 0, 1)))) {
            if (!this.noToggle.getValue()) {
                this.disable();
            }
            return;
        }
        if (this.wait.getValue()) {
            if (!this.timer.passedMs(this.delay.getValue() * 50)) {
                return;
            }
            this.timer.reset();
        }
        if (this.breakCrystal.getValue()) {
            for (Entity entity : Burrow.mc.world.loadedEntityList.stream().filter(Burrow::onTick1).sorted(Comparator.comparing(Burrow::onTick2)).collect(Collectors.toList())) {
                if (!(entity instanceof EntityEnderCrystal) || !((double)entity.getDistance(Burrow.mc.player) < 2.5)) continue;
                Burrow.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                Burrow.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
                break;
            }
        }
        boolean bl = this.onlyOnGround.getValue();
        boolean bl2 = !Burrow.mc.player.onGround;
        if (bl & (bl2 | Burrow.mc.world.getBlockState(Burrow.getFlooredPosition(Burrow.mc.player).offset(EnumFacing.DOWN)).getBlock().equals(Blocks.AIR))) {
            return;
        }
        if (!Burrow.mc.world.isBlockLoaded(Burrow.mc.player.getPosition())) {
            return;
        }
        if (InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1 && InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)) == -1) {
            Command.sendMessage(ChatFormatting.RED + "Obsidian/Ender Chest ?");
            this.disable();
            return;
        }
        if (!Burrow.fakeBBoxCheck(Burrow.mc.player, new Vec3d(0.0, 0.0, 0.0), true)) {
            BlockPos blockPos2;
            boolean bl3 = false;
            boolean bl4 = false;
            if (!Burrow.mc.world.getBlockState(Burrow.getFlooredPosition(Burrow.mc.player).offset(EnumFacing.UP, 2)).getBlock().equals(Blocks.AIR) || Burrow.mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock() != Blocks.AIR && Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos.add(1, 0, 0)) != null || Burrow.mc.world.getBlockState(blockPos.add(-1, 2, 0)).getBlock() != Blocks.AIR && Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos.add(-1, 0, 0)) != null || Burrow.mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock() != Blocks.AIR && Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos.add(0, 0, 1)) != null || Burrow.mc.world.getBlockState(blockPos.add(0, 2, -1)).getBlock() != Blocks.AIR && Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos.add(0, 0, -1)) != null || Burrow.mc.world.getBlockState(blockPos.add(1, 2, 1)).getBlock() != Blocks.AIR && Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos.add(1, 0, 1)) != null || Burrow.mc.world.getBlockState(blockPos.add(1, 2, -1)).getBlock() != Blocks.AIR && Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos.add(1, 0, -1)) != null || Burrow.mc.world.getBlockState(blockPos.add(-1, 2, 1)).getBlock() != Blocks.AIR && Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos.add(-1, 0, 1)) != null || Burrow.mc.world.getBlockState(blockPos.add(-1, 2, -1)).getBlock() != Blocks.AIR && Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos.add(-1, 0, -1)) != null) {
                blockPos2 = Burrow.getFlooredPosition(Burrow.mc.player);
                if (!(Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos2) == null || Burrow.isAir(blockPos2) || Burrow.isAir(blockPos2.offset(EnumFacing.DOWN)) && this.bypass.getValue())) {
                    Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX) / 2.0, Burrow.mc.player.posY + 0.2, Burrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ) / 2.0, false));
                    Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX), Burrow.mc.player.posY + 0.2, Burrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ), false));
                } else {
                    for (EnumFacing enumFacing : EnumFacing.VALUES) {
                        if (enumFacing == EnumFacing.UP) continue;
                        if (enumFacing == EnumFacing.DOWN) {
                            continue;
                        }
                        blockPos2 = Burrow.getFlooredPosition(Burrow.mc.player).offset(enumFacing);
                        if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos2) == null || Burrow.isAir(blockPos2) || Burrow.isAir(blockPos2.offset(EnumFacing.DOWN)) && this.bypass.getValue()) continue;
                        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX) / 2.0, Burrow.mc.player.posY + 0.2, Burrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ) / 2.0, false));
                        Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX), Burrow.mc.player.posY + 0.2, Burrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ), false));
                        bl3 = true;
                        break;
                    }
                    if (!bl3) {
                        for (EnumFacing enumFacing : EnumFacing.VALUES) {
                            if (enumFacing == EnumFacing.UP) continue;
                            if (enumFacing == EnumFacing.DOWN) {
                                continue;
                            }
                            blockPos2 = Burrow.getFlooredPosition(Burrow.mc.player).offset(enumFacing);
                            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos2) == null || Burrow.isAir(blockPos2.offset(EnumFacing.DOWN)) && this.bypass.getValue()) continue;
                            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX) / 2.0, Burrow.mc.player.posY + 0.2, Burrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ) / 2.0, false));
                            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX), Burrow.mc.player.posY + 0.2, Burrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ), false));
                            bl4 = true;
                            break;
                        }
                        if (!bl4) {
                            for (EnumFacing enumFacing : EnumFacing.VALUES) {
                                if (enumFacing == EnumFacing.UP) continue;
                                if (enumFacing == EnumFacing.DOWN) {
                                    continue;
                                }
                                blockPos2 = Burrow.getFlooredPosition(Burrow.mc.player).offset(enumFacing);
                                if (!Burrow.isAir(blockPos2) || !Burrow.isAir(blockPos2.offset(EnumFacing.UP)) || Burrow.isAir(blockPos2.offset(EnumFacing.DOWN)) && this.bypass.getValue()) continue;
                                Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX) / 2.0, Burrow.mc.player.posY + 0.2, Burrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ) / 2.0, false));
                                Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + ((double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX), Burrow.mc.player.posY + 0.2, Burrow.mc.player.posZ + ((double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ), false));
                                break;
                            }
                        }
                    }
                }
            } else {
                blockPos2 = this.getOffsetBlock(Burrow.mc.player);
                if (blockPos2 == null) {
                    if (!this.noToggle.getValue()) {
                        this.disable();
                    }
                    return;
                }
                double d = (double)blockPos2.getX() + 0.5 - Burrow.mc.player.posX;
                double d2 = (double)blockPos2.getZ() + 0.5 - Burrow.mc.player.posZ;
                Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + d * 0.25, Burrow.mc.player.posY + 0.419999986886978, Burrow.mc.player.posZ + d2 * 0.25, false));
                Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + d * 0.5, Burrow.mc.player.posY + 0.7531999805212015, Burrow.mc.player.posZ + d2 * 0.5, false));
                Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + d * 0.75, Burrow.mc.player.posY + 1.001335979112147, Burrow.mc.player.posZ + d2 * 0.75, false));
                Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position((double)blockPos2.getX() + 0.5, Burrow.mc.player.posY + 1.166109260938214, (double)blockPos2.getZ() + 0.5, false));
            }
        } else {
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.419999986886978, Burrow.mc.player.posZ, false));
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.7531999805212015, Burrow.mc.player.posZ, false));
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.001335979112147, Burrow.mc.player.posZ, false));
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX, Burrow.mc.player.posY + 1.166109260938214, Burrow.mc.player.posZ, false));
        }
        int n = Burrow.mc.player.inventory.currentItem;
        if (this.fristObby.getValue()) {
            if (InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) != -1) {
                InventoryUtil.switchToHotbarSlot(InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)), false);
            } else if (InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)) != -1) {
                InventoryUtil.switchToHotbarSlot(InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)), false);
            }
        } else if (InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)) != -1) {
            InventoryUtil.switchToHotbarSlot(InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)), false);
        } else if (InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) != -1) {
            InventoryUtil.switchToHotbarSlot(InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)), false);
        }
        if (!this.multiPlace.getValue()) {
            if (Burrow.isAir(blockPos)) {
                BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, 1))) != null && Burrow.isAir(blockPos.add(0, 0, 1))) {
                BlockUtil.placeBlock(blockPos.add(0, 0, 1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, -1))) != null && Burrow.isAir(blockPos.add(0, 0, -1))) {
                BlockUtil.placeBlock(blockPos.add(0, 0, -1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 0))) != null && Burrow.isAir(blockPos.add(1, 0, 0))) {
                BlockUtil.placeBlock(blockPos.add(1, 0, 0), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 0))) != null && Burrow.isAir(blockPos.add(-1, 0, 0))) {
                BlockUtil.placeBlock(blockPos.add(-1, 0, 0), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 1))) != null && Burrow.isAir(blockPos.add(1, 0, 1))) {
                BlockUtil.placeBlock(blockPos.add(1, 0, 1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, -1))) != null && Burrow.isAir(blockPos.add(-1, 0, -1))) {
                BlockUtil.placeBlock(blockPos.add(-1, 0, -1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, -1))) != null && Burrow.isAir(blockPos.add(1, 0, -1))) {
                BlockUtil.placeBlock(blockPos.add(1, 0, -1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 1))) != null && Burrow.isAir(blockPos.add(-1, 0, 1))) {
                BlockUtil.placeBlock(blockPos.add(-1, 0, 1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
        } else {
            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, 1))) != null) {
                BlockUtil.placeBlock(blockPos.add(0, 0, 1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(0, 0, -1))) != null) {
                BlockUtil.placeBlock(blockPos.add(0, 0, -1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 0))) != null) {
                BlockUtil.placeBlock(blockPos.add(1, 0, 0), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 0))) != null) {
                BlockUtil.placeBlock(blockPos.add(-1, 0, 0), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, 1))) != null) {
                BlockUtil.placeBlock(blockPos.add(1, 0, 1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, -1))) != null) {
                BlockUtil.placeBlock(blockPos.add(-1, 0, -1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(1, 0, -1))) != null) {
                BlockUtil.placeBlock(blockPos.add(1, 0, -1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            if (Burrow.checkEntity(EntityUtil.getVarOffsets(0, 0, 0), new BlockPos(blockPos.add(-1, 0, 1))) != null) {
                BlockUtil.placeBlock(blockPos.add(-1, 0, 1), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            }
            BlockUtil.placeBlock(blockPos.add(0, 0, 0), EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        }
        Burrow.mc.player.inventory.currentItem = n;
        Burrow.mc.playerController.updateController();
        Burrow.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY - 1.0, Burrow.mc.player.posZ), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        if (this.smartOffset.getValue()) {
            boolean bl5 = true;
            if (Burrow.mc.player.posY >= 3.0) {
                for (int i = -10; i < 10; ++i) {
                    if (i == -1) {
                        i = 4;
                    }
                    if (!Burrow.mc.world.getBlockState(Burrow.getFlooredPosition(Burrow.mc.player).add(0, i, 0)).getBlock().equals(Blocks.AIR) || !Burrow.mc.world.getBlockState(Burrow.getFlooredPosition(Burrow.mc.player).add(0, i + 1, 0)).getBlock().equals(Blocks.AIR)) continue;
                    BlockPos blockPos3 = Burrow.getFlooredPosition(Burrow.mc.player).add(0, i, 0);
                    Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position((double)blockPos3.getX() + 0.3, blockPos3.getY(), (double)blockPos3.getZ() + 0.3, false));
                    bl5 = false;
                    break;
                }
            }
            if (bl5) {
                Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + this.offsetX.getValue(), Burrow.mc.player.posY + this.offsetY.getValue(), Burrow.mc.player.posZ + this.offsetZ.getValue(), false));
            }
        } else {
            Burrow.mc.player.connection.sendPacket(new CPacketPlayer.Position(Burrow.mc.player.posX + this.offsetX.getValue(), Burrow.mc.player.posY + this.offsetY.getValue(), Burrow.mc.player.posZ + this.offsetZ.getValue(), false));
        }
        if (!this.wait.getValue()) {
            this.disable();
        } else if (!this.noToggle.getValue() && Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.5, Burrow.mc.player.posZ)).getBlock() != Blocks.AIR) {
            this.disable();
        }
    }

    public static Burrow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Burrow();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        this.posCheck = new Vec3d(Burrow.mc.player.posX, Burrow.mc.player.posY + 0.5, Burrow.mc.player.posZ);
    }

    public static BlockPos getPlayerPosFixY(EntityPlayer entityPlayer) {
        return new BlockPos(Math.floor(entityPlayer.posX), (double)Math.round(entityPlayer.posY), Math.floor(entityPlayer.posZ));
    }

    private static boolean onTick1(Entity entity) {
        return entity instanceof EntityEnderCrystal && !entity.isDead;
    }

    static Entity checkEntity(Vec3d[] vec3dArray, BlockPos blockPos) {
        Entity entity = null;
        for (Vec3d vec3d : vec3dArray) {
            BlockPos blockPos2 = new BlockPos(blockPos).add(vec3d.x, vec3d.y, vec3d.z);
            for (Entity entity2 : Burrow.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2))) {
                if (entity2 != Burrow.mc.player) continue;
                if (entity != null) {
                    continue;
                }
                entity = entity2;
            }
        }
        return entity;
    }

    public static BlockPos vec3toBlockPos(Vec3d vec3d, boolean bl) {
        if (bl) {
            return new BlockPos(Math.floor(vec3d.x), Math.floor(vec3d.y), Math.floor(vec3d.z));
        }
        return new BlockPos(Math.floor(vec3d.x), (double)Math.round(vec3d.y), Math.floor(vec3d.z));
    }

    public static IBlockState getBlock(Vec3d vec3d) {
        return Burrow.mc.world.getBlockState(Burrow.vec3toBlockPos(vec3d));
    }

    public static boolean isAir(Vec3d vec3d) {
        return Burrow.mc.world.getBlockState(Burrow.vec3toBlockPos(vec3d, true)).getBlock().equals(Blocks.AIR);
    }

    public boolean canBur(Vec3d vec3d) {
        BlockPos blockPos = Burrow.vec3toBlockPos(vec3d);
        return Burrow.isAir(blockPos) && Burrow.isAir(blockPos.offset(EnumFacing.UP)) && Burrow.isAir(blockPos.offset(EnumFacing.UP, 2));
    }

    public static BlockPos getFlooredPosition(Entity entity) {
        return new BlockPos(Math.floor(entity.posX), (double)Math.round(entity.posY), Math.floor(entity.posZ));
    }

    private boolean new0(Integer n) {
        return this.wait.getValue();
    }
}

