package me.madcat.mixin.mixins;

import net.minecraft.block.material.Material;
import net.minecraft.block.BlockLiquid;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.block.Block;

@Mixin({ BlockLiquid.class })
public class MixinBlockLiquid extends Block
{
    protected MixinBlockLiquid(final Material materialIn) {
        super(materialIn);
    }
}
