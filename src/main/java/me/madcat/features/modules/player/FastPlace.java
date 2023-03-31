package me.madcat.features.modules.player;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.item.ItemBlock;

public class FastPlace
extends Module {
    public final Setting<Boolean> eChest = this.register(new Setting<>("OnlyEnderChest", true));

    public FastPlace() {
        super("FastPlace", "Fast everything", Module.Category.PLAYER);
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.eChest.getValue()) {
            this.isHoldingEChest();
        }
    }

    public void isHoldingEChest() {
        if (!(FastPlace.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) || !(((ItemBlock) FastPlace.mc.player.getHeldItemMainhand().getItem()).getBlock() instanceof BlockEnderChest)) {
            if (FastPlace.mc.player.getHeldItemOffhand().getItem() instanceof ItemBlock) {
                ((ItemBlock) FastPlace.mc.player.getHeldItemOffhand().getItem()).getBlock();
            }
        }
    }
}

