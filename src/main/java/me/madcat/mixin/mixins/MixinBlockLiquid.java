package me.madcat.mixin.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={BlockLiquid.class})
public class MixinBlockLiquid
        extends Block {
    protected MixinBlockLiquid(Material materialIn) {
        super(materialIn);
    }
}
 