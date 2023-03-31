package me.madcat.features.modules.render;

import java.awt.Color;
import me.madcat.MadCat;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.EntityUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CityESP
extends Module {
    private final Setting<Float> range = this.register(new Setting<>("Range", 7.0f, 1.0f, 12.0f));
    public EntityPlayer target;

    private void surroundRender1() {
        if (this.target == null) {
            return;
        }
        Vec3d vec3d = this.target.getPositionVector();
        BlockPos blockPos = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        if (this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(blockPos.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(vec3d, -1.0, 0.0, 0.0, true);
            } else if (this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundRender1(vec3d, -1.0, 0.0, 0.0, false);
            }
        }
        if (this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(blockPos.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundRender1(vec3d, 1.0, 0.0, 0.0, true);
            } else if (this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundRender1(vec3d, 1.0, 0.0, 0.0, false);
            }
        }
        if (this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK) {
            this.surroundRender1(vec3d, -2.0, 0.0, 0.0, this.getBlock(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() == Blocks.AIR);
        }
        if (this.getBlock(blockPos.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(-2, 1, 0)).getBlock() != Blocks.BEDROCK) {
            this.surroundRender1(vec3d, -2.0, 1.0, 0.0, this.getBlock(blockPos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(-2, 0, 0)).getBlock() == Blocks.AIR);
        }
        if (this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK) {
            this.surroundRender1(vec3d, 2.0, 0.0, 0.0, this.getBlock(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(2, 1, 0)).getBlock() == Blocks.AIR);
        }
        if (this.getBlock(blockPos.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(2, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(2, 1, 0)).getBlock() != Blocks.BEDROCK) {
            this.surroundRender1(vec3d, 2.0, 1.0, 0.0, this.getBlock(blockPos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(2, 0, 0)).getBlock() == Blocks.AIR);
        }
        if (this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(blockPos.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(vec3d, 0.0, 0.0, 1.0, true);
            } else if (this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
                this.surroundRender1(vec3d, 0.0, 0.0, 1.0, false);
            }
        }
        if (this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK) {
            if (this.getBlock(blockPos.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                this.surroundRender1(vec3d, 0.0, 0.0, -1.0, true);
            } else if (this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
                this.surroundRender1(vec3d, 0.0, 0.0, -1.0, false);
            }
        }
        if (this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK) {
            this.surroundRender1(vec3d, 0.0, 0.0, 2.0, this.getBlock(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 2)).getBlock() == Blocks.AIR);
        }
        if (this.getBlock(blockPos.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, 2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, 2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 2)).getBlock() != Blocks.BEDROCK) {
            this.surroundRender1(vec3d, 0.0, 1.0, 2.0, this.getBlock(blockPos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, 2)).getBlock() == Blocks.AIR);
        }
        if (this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK) {
            this.surroundRender1(vec3d, 0.0, 0.0, -2.0, this.getBlock(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 1, -2)).getBlock() == Blocks.AIR);
        }
        if (this.getBlock(blockPos.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 0, -2)).getBlock() != Blocks.BEDROCK && this.getBlock(blockPos.add(0, 1, -2)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -2)).getBlock() != Blocks.BEDROCK) {
            this.surroundRender1(vec3d, 0.0, 1.0, -2.0, this.getBlock(blockPos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(blockPos.add(0, 0, -2)).getBlock() == Blocks.AIR);
        }
        if (this.getBlock(blockPos.add(0, 0, 0)).getBlock() != Blocks.AIR && this.getBlock(blockPos.add(0, 0, 0)).getBlock() != Blocks.BEDROCK) {
            AxisAlignedBB axisAlignedBB = CityESP.mc.world.getBlockState(new BlockPos(vec3d)).getSelectedBoundingBox(CityESP.mc.world, new BlockPos(vec3d));
            RenderUtil.drawBBBox(axisAlignedBB, new Color(255, 255, 255), 120);
            RenderUtil.drawBoxESP(new BlockPos(vec3d), new Color(255, 255, 255), false, new Color(255, 255, 255), 1.0f, false, true, 42, true);
        }
    }

    private void surroundRender1(Vec3d vec3d, double d, double d2, double d3, boolean bl) {
        BlockPos blockPos = new BlockPos(vec3d).add(d, d2, d3);
        if (CityESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
            return;
        }
        if (CityESP.mc.world.getBlockState(blockPos).getBlock() == Blocks.FIRE) {
            return;
        }
        AxisAlignedBB axisAlignedBB = CityESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox(CityESP.mc.world, blockPos);
        if (bl) {
            RenderUtil.drawBBBox(axisAlignedBB, new Color(255, 147, 147), 120);
            RenderUtil.drawBoxESP(blockPos, new Color(255, 147, 147), false, new Color(255, 147, 147), 1.0f, false, true, 80, true);
            return;
        }
        RenderUtil.drawBBBox(axisAlignedBB, new Color(118, 118, 255), 120);
        RenderUtil.drawBoxESP(blockPos, new Color(118, 118, 255), false, new Color(118, 118, 255), 1.0f, false, true, 40, true);
    }

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = d;
        for (EntityPlayer entityPlayer2 : CityESP.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d) || MadCat.friendManager.isFriend(entityPlayer2.getName())) continue;
            if (CityESP.mc.player.posY - entityPlayer2.posY >= 5.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = CityESP.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (CityESP.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = CityESP.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    public CityESP() {
        super("CityESP", "CityESP", Module.Category.RENDER);
    }

    private IBlockState getBlock(BlockPos blockPos) {
        return CityESP.mc.world.getBlockState(blockPos);
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        this.target = this.getTarget(this.range.getValue());
        this.surroundRender1();
    }
}

