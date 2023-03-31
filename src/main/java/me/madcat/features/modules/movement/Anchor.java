package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.EntityUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Anchor
extends Module {
    public static boolean Anchoring;
    private final Setting<Boolean> disable = this.register(new Setting<>("Toggle", true));
    private final Setting<Boolean> pull = this.register(new Setting<>("Pull", true));
    int holeblocks;

    public Anchor() {
        super("Anchor", "Automatically makes u go into holes", Module.Category.MOVEMENT);
    }

    public boolean isBlockHole(BlockPos blockPos) {
        this.holeblocks = 0;
        if (Anchor.mc.world.getBlockState(blockPos.add(0, 3, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockPos.add(0, 0, 0)).getBlock() == Blocks.AIR) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockPos.add(0, -1, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockPos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        if (Anchor.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN || Anchor.mc.world.getBlockState(blockPos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK) {
            ++this.holeblocks;
        }
        return this.holeblocks >= 9;
    }

    public Vec3d GetCenter(double d, double d2, double d3) {
        double d4 = Math.floor(d) + 0.5;
        double d5 = Math.floor(d2);
        double d6 = Math.floor(d3) + 0.5;
        return new Vec3d(d4, d5, d6);
    }

    @Override
    public void onUpdate() {
        if (Anchor.fullNullCheck()) {
            return;
        }
        if (Anchor.mc.world == null) {
            return;
        }
        if (this.isBlockHole(this.getPlayerPos().down(1)) || this.isBlockHole(this.getPlayerPos().down(2)) || this.isBlockHole(this.getPlayerPos().down(3)) || this.isBlockHole(this.getPlayerPos().down(4))) {
            Anchoring = true;
            if (!this.pull.getValue()) {
                Anchor.mc.player.motionX = 0.0;
                Anchor.mc.player.motionZ = 0.0;
            } else {
                Vec3d vec3d = this.GetCenter(Anchor.mc.player.posX, Anchor.mc.player.posY, Anchor.mc.player.posZ);
                double d = Math.abs(vec3d.x - Anchor.mc.player.posX);
                double d2 = Math.abs(vec3d.z - Anchor.mc.player.posZ);
                if (d > 0.1 || d2 > 0.1) {
                    double d3 = vec3d.x - Anchor.mc.player.posX;
                    double d4 = vec3d.z - Anchor.mc.player.posZ;
                    Anchor.mc.player.motionX = d3 / 2.0;
                    Anchor.mc.player.motionZ = d4 / 2.0;
                }
            }
        } else {
            Anchoring = false;
        }
        if (this.disable.getValue() && EntityUtil.isSafe(Anchor.mc.player)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        Anchoring = false;
        this.holeblocks = 0;
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Anchor.mc.player.posX), Math.floor(Anchor.mc.player.posY), Math.floor(Anchor.mc.player.posZ));
    }
}

