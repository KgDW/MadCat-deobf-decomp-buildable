package me.madcat.features.modules.combat;

import me.madcat.MadCat;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class AutoCity
extends Module {
    private final Setting<Float> range;
    public final Setting<Boolean> db = this.register(new Setting<>("Double Mode", false));
    private final Setting<Boolean> toggle;
    public static EntityPlayer target;

    private IBlockState getBlock(BlockPos blockPos) {
        return AutoCity.mc.world.getBlockState(blockPos);
    }

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = Math.pow(d, 2.0) + 1.0;
        for (EntityPlayer entityPlayer2 : AutoCity.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d)) continue;
            if (MadCat.speedManager.getPlayerSpeed(entityPlayer2) > 10.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = AutoCity.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (AutoCity.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = AutoCity.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    private boolean detection(EntityPlayer entityPlayer) {
        boolean bl = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX + 1.2, entityPlayer.posY, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl2 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX + 1.2, entityPlayer.posY + 1.0, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl3 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX - 1.2, entityPlayer.posY, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl4 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX - 1.2, entityPlayer.posY + 1.0, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl5 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ + 1.2)).getBlock() == Blocks.AIR;
        boolean bl6 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY + 1.0, entityPlayer.posZ + 1.2)).getBlock() == Blocks.AIR;
        boolean bl7 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ - 1.2)).getBlock() == Blocks.AIR;
        boolean bl8 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY + 1.0, entityPlayer.posZ - 1.2)).getBlock() == Blocks.AIR;
        boolean bl9 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX + 2.2, entityPlayer.posY + 1.0, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl10 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX + 2.2, entityPlayer.posY, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl11 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX + 1.2, entityPlayer.posY, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl12 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX - 2.2, entityPlayer.posY + 1.0, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl13 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX - 2.2, entityPlayer.posY, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl14 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX - 1.2, entityPlayer.posY, entityPlayer.posZ)).getBlock() == Blocks.AIR;
        boolean bl15 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY + 1.0, entityPlayer.posZ + 2.2)).getBlock() == Blocks.AIR;
        boolean bl16 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ + 2.2)).getBlock() == Blocks.AIR;
        boolean bl17 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ + 1.2)).getBlock() == Blocks.AIR;
        boolean bl18 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY + 1.0, entityPlayer.posZ - 2.2)).getBlock() == Blocks.AIR;
        boolean bl19 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ - 2.2)).getBlock() == Blocks.AIR;
        boolean bl20 = AutoCity.mc.world.getBlockState(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ - 1.2)).getBlock() == Blocks.AIR;
        return false;
    }

    @Override
    public void onUpdate() {
        target = this.getTarget(this.range.getValue());
        if (target == null) {
            return;
        }
        BlockPos blockPos = new BlockPos(AutoCity.target.posX, AutoCity.target.posY, AutoCity.target.posZ);
        if (!this.detection(target)) {
            if (this.db.getValue()) {
                if (this.getBlock(blockPos.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 0, 1));
                } else if (this.getBlock(blockPos.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 0, -1));
                } else if (this.getBlock(blockPos.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(1, 0, 0));
                } else if (this.getBlock(blockPos.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(-1, 0, 0));
                } else if (this.getBlock(blockPos.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(2, 0, 0));
                } else if (this.getBlock(blockPos.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(-2, 0, 0));
                } else if (this.getBlock(blockPos.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 0, -2));
                } else if (this.getBlock(blockPos.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 0, 2));
                } else if (this.getBlock(blockPos.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(2, 0, 0));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(blockPos.add(1, 0, 0));
                    }
                } else if (this.getBlock(blockPos.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(-2, 0, 0));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(blockPos.add(-1, 0, 0));
                    }
                } else if (this.getBlock(blockPos.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 0, -2));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(blockPos.add(0, 0, -1));
                    }
                } else if (this.getBlock(blockPos.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 0, 2));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(blockPos.add(0, 0, 1));
                    }
                } else if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 1, 1));
                } else if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 0, 1));
                } else if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 0, -1));
                } else if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(1, 0, 0));
                } else if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(-1, 0, 0));
                } else if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(1, 1, 0));
                } else if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(-1, 1, 0));
                } else if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 1, -1));
                } else if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(1, 1, 0));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(blockPos.add(1, 0, 0));
                    }
                } else if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(-1, 1, 0));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(blockPos.add(-1, 0, 0));
                    }
                } else if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 1, -1));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(blockPos.add(0, 0, -1));
                    }
                } else if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 1, 1));
                    if (InstantMine.breakPos2 == null) {
                        this.surroundMine(blockPos.add(0, 0, 1));
                    }
                } else if (this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(-2, 1, 0));
                } else if (this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(2, 1, 0));
                } else if (this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 1, 2));
                } else if (this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 1, -2));
                } else if (this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(-1, 2, 0));
                } else if (this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(1, 2, 0));
                } else if (this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 2, 1));
                } else if (this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK) {
                    this.surroundMine(blockPos.add(0, 2, -1));
                }
            } else if (this.getBlock(blockPos.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 0, 1));
            } else if (this.getBlock(blockPos.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 0, -1));
            } else if (this.getBlock(blockPos.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(1, 0, 0));
            } else if (this.getBlock(blockPos.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(-1, 0, 0));
            } else if (this.getBlock(blockPos.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(2, 0, 0));
            } else if (this.getBlock(blockPos.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(-2, 0, 0));
            } else if (this.getBlock(blockPos.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 0, -2));
            } else if (this.getBlock(blockPos.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 0, 2));
            } else if (this.getBlock(blockPos.add(2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(2, 0, 0));
            } else if (this.getBlock(blockPos.add(-2, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(-2, 0, 0));
            } else if (this.getBlock(blockPos.add(0, 1, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 0, -2));
            } else if (this.getBlock(blockPos.add(0, 1, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 0, 2));
            } else if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 1, 1));
            } else if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 0, 1));
            } else if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 0, -1));
            } else if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(1, 0, 0));
            } else if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(-1, 0, 0));
            } else if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(1, 1, 0));
            } else if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(-1, 1, 0));
            } else if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 1, -1));
            } else if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(1, 1, 0));
            } else if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(-1, 1, 0));
            } else if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 1, -1));
            } else if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 1, 1));
            } else if (this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(-2, 1, 0));
            } else if (this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(2, 1, 0));
            } else if (this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 1, 2));
            } else if (this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 1, -2));
            } else if (this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(-1, 2, 0));
            } else if (this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(1, 2, 0));
            } else if (this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 2, 1));
            } else if (this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(blockPos.add(0, 2, -1));
            }
        }
        if (this.toggle.getValue()) {
            this.disable();
        }
    }

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    private void surroundMine(BlockPos blockPos) {
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(blockPos)) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCity.target.posX, AutoCity.target.posY, AutoCity.target.posZ)) && AutoCity.mc.world.getBlockState(new BlockPos(AutoCity.target.posX, AutoCity.target.posY, AutoCity.target.posZ)).getBlock() != Blocks.AIR) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCity.mc.player.posX, AutoCity.mc.player.posY + 2.0, AutoCity.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCity.mc.player.posX, AutoCity.mc.player.posY - 1.0, AutoCity.mc.player.posZ))) {
                return;
            }
            if (AutoCity.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        AutoCity.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
    }

    public AutoCity() {
        super("AutoCity", "Automatically dig the enemy's hole", Module.Category.COMBAT);
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));
        this.toggle = this.register(new Setting<>("Toggle", false));
    }
}

