package me.madcat.features.modules.legacy;

import me.madcat.MadCat;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.combat.Flatten;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoCiv
extends Module {
    private static AutoCiv INSTANCE = new AutoCiv();
    private final Setting<Integer> tick;
    private final Setting<Float> range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));
    public static EntityPlayer target;
    int ticked;

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (AutoCiv.fullNullCheck()) {
            return;
        }
        target = this.getTarget(this.range.getValue());
        this.surroundMine();
    }

    public static AutoCiv Instance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoCiv();
        }
        return INSTANCE;
    }

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = Math.pow(d, 2.0) + 1.0;
        for (EntityPlayer entityPlayer2 : AutoCiv.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d)) continue;
            if (MadCat.speedManager.getPlayerSpeed(entityPlayer2) > 10.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = AutoCiv.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (AutoCiv.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = AutoCiv.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    private IBlockState getBlock(BlockPos blockPos) {
        return AutoCiv.mc.world.getBlockState(blockPos);
    }

    private void surroundMine(BlockPos blockPos) {
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(blockPos)) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCiv.target.posX, AutoCiv.target.posY, AutoCiv.target.posZ)) && AutoCiv.mc.world.getBlockState(new BlockPos(AutoCiv.target.posX, AutoCiv.target.posY, AutoCiv.target.posZ)).getBlock() != Blocks.AIR) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCiv.mc.player.posX, AutoCiv.mc.player.posY + 2.0, AutoCiv.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCiv.mc.player.posX, AutoCiv.mc.player.posY - 1.0, AutoCiv.mc.player.posZ))) {
                return;
            }
            if (AutoCiv.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        AutoCiv.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
    }

    @Override
    public void onEnable() {
        this.ticked = 20;
    }

    private void surroundMine() {
        if (target == null) {
            return;
        }
        int n = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (n == -1) {
            return;
        }
        int n2 = Flatten.mc.player.inventory.currentItem;
        BlockPos blockPos = new BlockPos(AutoCiv.target.posX, AutoCiv.target.posY, AutoCiv.target.posZ);
        ++this.ticked;
        if (this.ticked >= this.tick.getValue()) {
            if (this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 3, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 4, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.switchToSlot(n);
                BlockUtil.placeBlock(blockPos.add(1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(n2);
                if (this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.AIR) {
                    this.surroundMine(blockPos.add(1, 1, 0));
                }
            } else if (this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 3, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 4, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.switchToSlot(n);
                BlockUtil.placeBlock(blockPos.add(-1, 1, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(n2);
                if (this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.AIR) {
                    this.surroundMine(blockPos.add(-1, 1, 0));
                }
            } else if (this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 3, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 4, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                this.switchToSlot(n);
                BlockUtil.placeBlock(blockPos.add(0, 1, 1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(n2);
                if (this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.AIR) {
                    this.surroundMine(blockPos.add(0, 1, 1));
                }
            } else if (this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 3, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 4, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                this.switchToSlot(n);
                BlockUtil.placeBlock(blockPos.add(0, 1, -1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(n2);
                if (this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.AIR) {
                    this.surroundMine(blockPos.add(0, 1, -1));
                }
            } else if (InstantMine.breakPos2 != null) {
                if (this.getBlock(blockPos.add(0, 1, 1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 3, 1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR || this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.BEDROCK || !InstantMine.breakPos2.equals(blockPos.add(0, 2, 1))) {
                    this.surroundMine2(blockPos.add(0, 2, 1));
                } else if (this.getBlock(blockPos.add(0, 1, 1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 3, 1)).getBlock() == Blocks.AIR || this.getBlock(blockPos.add(0, 3, 1)).getBlock() == Blocks.BEDROCK || !InstantMine.breakPos2.equals(blockPos.add(0, 3, 1))) {
                    this.surroundMine2(blockPos.add(0, 3, 1));
                } else if (this.getBlock(blockPos.add(0, 1, -1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 3, -1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 2, 1)).getBlock() == Blocks.AIR || this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.BEDROCK || !InstantMine.breakPos2.equals(blockPos.add(0, 2, -1))) {
                    this.surroundMine2(blockPos.add(0, 2, -1));
                } else if (this.getBlock(blockPos.add(0, 1, -1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 2, -1)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(0, 3, 1)).getBlock() == Blocks.AIR || this.getBlock(blockPos.add(0, 3, -1)).getBlock() == Blocks.BEDROCK || !InstantMine.breakPos2.equals(blockPos.add(0, 3, -1))) {
                    this.surroundMine2(blockPos.add(0, 3, -1));
                } else if (this.getBlock(blockPos.add(1, 1, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(1, 3, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.AIR || this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.BEDROCK || !InstantMine.breakPos2.equals(blockPos.add(1, 2, 0))) {
                    this.surroundMine2(blockPos.add(1, 2, 0));
                } else if (this.getBlock(blockPos.add(1, 1, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(1, 2, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(1, 3, 0)).getBlock() == Blocks.AIR || this.getBlock(blockPos.add(1, 3, 0)).getBlock() == Blocks.BEDROCK || !InstantMine.breakPos2.equals(blockPos.add(1, 3, 0))) {
                    this.surroundMine2(blockPos.add(1, 3, 0));
                } else if (this.getBlock(blockPos.add(-1, 1, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 3, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.AIR || this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.BEDROCK || !InstantMine.breakPos2.equals(blockPos.add(-1, 2, 0))) {
                    this.surroundMine2(blockPos.add(-1, 2, 0));
                } else if (this.getBlock(blockPos.add(-1, 1, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 2, 0)).getBlock() == Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 3, 0)).getBlock() == Blocks.AIR || this.getBlock(blockPos.add(-1, 3, 0)).getBlock() == Blocks.BEDROCK || !InstantMine.breakPos2.equals(blockPos.add(-1, 3, 0))) {
                    this.surroundMine2(blockPos.add(-1, 3, 0));
                }
            } else if (this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 3, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine2(blockPos.add(0, 2, 1));
            } else if (this.getBlock(blockPos.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 3, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 3, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine2(blockPos.add(0, 3, 1));
            } else if (this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 3, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine2(blockPos.add(0, 2, -1));
            } else if (this.getBlock(blockPos.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 2, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 3, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 3, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine2(blockPos.add(0, 3, -1));
            } else if (this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine2(blockPos.add(1, 2, 0));
            } else if (this.getBlock(blockPos.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(1, 3, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 3, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine2(blockPos.add(1, 3, 0));
            } else if (this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 3, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine2(blockPos.add(-1, 2, 0));
            } else if (this.getBlock(blockPos.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 2, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-1, 3, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 3, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine2(blockPos.add(-1, 3, 0));
            }
            this.ticked = 0;
        }
    }

    public AutoCiv() {
        super("AutoCiv", "Automatic oblique angle explosion", Module.Category.LEGACY);
        this.tick = this.register(new Setting<>("Tick", 15, 7, 20));
        this.setInstance();
    }

    private void switchToSlot(int n) {
        Flatten.mc.player.inventory.currentItem = n;
        Flatten.mc.playerController.updateController();
    }

    private void surroundMine2(BlockPos blockPos) {
        if (InstantMine.breakPos2 != null && InstantMine.breakPos2.equals(blockPos)) {
            return;
        }
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(blockPos)) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCiv.target.posX, AutoCiv.target.posY, AutoCiv.target.posZ)) && AutoCiv.mc.world.getBlockState(new BlockPos(AutoCiv.target.posX, AutoCiv.target.posY, AutoCiv.target.posZ)).getBlock() != Blocks.AIR) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCiv.mc.player.posX, AutoCiv.mc.player.posY + 2.0, AutoCiv.mc.player.posZ))) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCiv.mc.player.posX, AutoCiv.mc.player.posY - 1.0, AutoCiv.mc.player.posZ))) {
                return;
            }
            if (AutoCiv.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
        }
        AutoCiv.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
    }

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }
        return null;
    }
}

