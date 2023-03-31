package me.madcat.features.modules.combat;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AntiPiston
extends Module {
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", true));

    private IBlockState getBlock(BlockPos blockPos) {
        return AntiPiston.mc.world.getBlockState(blockPos);
    }

    public AntiPiston() {
        super("AntiPiston", "Trap self when piston kick", Module.Category.COMBAT);
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        BlockPos blockPos = new BlockPos(AntiPiston.mc.player.posX, AntiPiston.mc.player.posY, AntiPiston.mc.player.posZ);
        if (this.getBlock(blockPos.add(0, 1, 1)).getBlock() instanceof BlockPistonBase) {
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 1, -1))) {
                this.placeBlock(blockPos.add(0, 0, -1));
            }
            this.placeBlock(blockPos.add(0, 1, -1));
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 0))) {
                this.placeBlock(blockPos.add(0, 2, -1));
            }
            this.placeBlock(blockPos.add(0, 2, 0));
        }
        if (this.getBlock(blockPos.add(0, 1, -1)).getBlock() instanceof BlockPistonBase) {
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 1, 1))) {
                this.placeBlock(blockPos.add(0, 0, 1));
            }
            this.placeBlock(blockPos.add(0, 1, 1));
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 0))) {
                this.placeBlock(blockPos.add(0, 2, 1));
            }
            this.placeBlock(blockPos.add(0, 2, 0));
        }
        if (this.getBlock(blockPos.add(1, 1, 0)).getBlock() instanceof BlockPistonBase) {
            if (BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, 0))) {
                this.placeBlock(blockPos.add(-1, 0, 0));
            }
            this.placeBlock(blockPos.add(-1, 1, 0));
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 0))) {
                this.placeBlock(blockPos.add(-1, 2, 0));
            }
            this.placeBlock(blockPos.add(0, 2, 0));
        }
        if (this.getBlock(blockPos.add(-1, 1, 0)).getBlock() instanceof BlockPistonBase) {
            if (BlockUtil.cantBlockPlace2(blockPos.add(1, 1, 0))) {
                this.placeBlock(blockPos.add(1, 0, 0));
            }
            this.placeBlock(blockPos.add(1, 1, 0));
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 0))) {
                this.placeBlock(blockPos.add(1, 2, 0));
            }
            this.placeBlock(blockPos.add(0, 2, 0));
        }
    }

    private void placeBlock(BlockPos blockPos) {
        if (BlockUtil.cantBlockPlaceMine(blockPos)) {
            return;
        }
        int n = AntiPiston.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            return;
        }
        AntiPiston.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        AntiPiston.mc.playerController.updateController();
        BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
        AntiPiston.mc.player.inventory.currentItem = n;
        AntiPiston.mc.playerController.updateController();
    }
}

