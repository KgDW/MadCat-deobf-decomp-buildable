package me.madcat.features.modules.legacy;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoDupe
extends Module {
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", true));
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    int Im;

    public AutoDupe() {
        super("BoxDupe", "Automatically places Shulker", Module.Category.LEGACY);
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (InstantMine.breakPos == null) {
            return;
        }
        BlockPos blockPos = InstantMine.breakPos;
        IBlockState iBlockState = AutoDupe.mc.world.getBlockState(blockPos);
        this.Im = this.getItemShulkerBox();
        if (iBlockState.getBlock() == Blocks.AIR && this.Im != -1) {
            AutoDupe.mc.player.inventory.currentItem = this.Im;
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
        }
    }

    public int getItemShulkerBox() {
        int n = -1;
        for (int i = 0; i <= 8; ++i) {
            Item item = AutoDupe.mc.player.inventory.getStackInSlot(i).getItem();
            if (!(item instanceof ItemShulkerBox)) continue;
            n = i;
        }
        return n;
    }
}

