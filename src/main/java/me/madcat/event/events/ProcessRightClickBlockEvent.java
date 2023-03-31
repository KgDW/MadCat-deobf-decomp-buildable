package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ProcessRightClickBlockEvent
extends EventStage {
    public final BlockPos pos;
    public final EnumHand hand;
    public final ItemStack stack;

    public ProcessRightClickBlockEvent(BlockPos blockPos, EnumHand enumHand, ItemStack itemStack) {
        this.pos = blockPos;
        this.hand = enumHand;
        this.stack = itemStack;
    }
}

