package me.madcat.features.modules.legacy;

import java.util.HashMap;
import me.madcat.event.events.BlockBreakEvent;
import me.madcat.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreakCheck
extends Module {
    public final HashMap<EntityPlayer, BlockPos> MineMap = new HashMap();
    private static BreakCheck INSTANCE = new BreakCheck();

    @SubscribeEvent
    public void BrokenBlock2(BlockBreakEvent blockBreakEvent) {
        if (blockBreakEvent.getBreakerId() == BreakCheck.mc.player.getEntityId()) {
            return;
        }
        if (blockBreakEvent.getPosition().getY() == -1) {
            return;
        }
        if (BreakCheck.mc.world.getBlockState(new BlockPos(blockBreakEvent.getPosition())).getBlock() == Blocks.BEDROCK) {
            return;
        }
        this.MineMap.put((EntityPlayer)BreakCheck.mc.world.getEntityByID(blockBreakEvent.getBreakerId()), blockBreakEvent.getPosition());
    }

    @Override
    public void onDisable() {
        this.MineMap.clear();
    }

    public static BreakCheck Instance() {
        if (INSTANCE == null) {
            INSTANCE = new BreakCheck();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public BreakCheck() {
        super("BreakCheck", "Check instant mine", Module.Category.LEGACY);
        this.setInstance();
    }
}

