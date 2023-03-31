package me.madcat.features.modules.combat;

import me.madcat.MadCat;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AnvilAura
extends Module {
    public final Setting<Boolean> packet;
    public final Setting<Boolean> rotate;
    public final Setting<Float> range = this.register(new Setting<>("Range", 6.0f, 0.0f, 10.0f));

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = Math.pow(d, 2.0) + 1.0;
        for (EntityPlayer entityPlayer2 : AnvilAura.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d)) continue;
            if (MadCat.speedManager.getPlayerSpeed(entityPlayer2) > 10.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = AnvilAura.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (AnvilAura.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = AnvilAura.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    public void doAnvilAura() {
        EntityPlayer entityPlayer = this.getTarget(this.range.getValue());
        if (entityPlayer == null) {
            return;
        }
        BlockPos blockPos = new BlockPos(entityPlayer.posX, entityPlayer.posY + 0.5, entityPlayer.posZ);
        if (AnvilAura.mc.world.isAirBlock(blockPos) && AnvilAura.mc.world.isAirBlock(blockPos.add(0, 1, 0)) && AnvilAura.mc.world.isAirBlock(blockPos.add(0, 2, 0))) {
            if (InventoryUtil.findHotbarBlock(Blocks.ANVIL) == -1) {
                return;
            }
            int n = AnvilAura.mc.player.inventory.currentItem;
            if (BlockUtil.cantBlockPlace2(blockPos.add(0, 2, 0))) {
                if (!BlockUtil.cantBlockPlace2(blockPos.add(1, 2, 0))) {
                    this.placeBlock(blockPos.add(1, 2, 0));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(1, 1, 0))) {
                    this.placeBlock(blockPos.add(1, 1, 0));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(1, 0, 0))) {
                    this.placeBlock(blockPos.add(1, 0, 0));
                }
                if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 2, 0))) {
                    this.placeBlock(blockPos.add(-1, 2, 0));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 1, 0))) {
                    this.placeBlock(blockPos.add(-1, 1, 0));
                } else if (!BlockUtil.cantBlockPlace2(blockPos.add(-1, 0, 0))) {
                    this.placeBlock(blockPos.add(-1, 0, 0));
                }
            }
            if (BlockUtil.cantBlockPlace(blockPos.add(0, 2, 0))) {
                return;
            }
            AnvilAura.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.ANVIL);
            AnvilAura.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos.add(0, 2, 0), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            AnvilAura.mc.player.inventory.currentItem = n;
            AnvilAura.mc.playerController.updateController();
        }
    }

    public AnvilAura() {
        super("AnvilAura", "Useless", Module.Category.COMBAT);
        this.rotate = this.register(new Setting<>("Rotate", true));
        this.packet = this.register(new Setting<>("Packet", true));
    }

    private void placeBlock(BlockPos blockPos) {
        if (BlockUtil.cantBlockPlaceMine(blockPos)) {
            return;
        }
        int n = AnvilAura.mc.player.inventory.currentItem;
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            AnvilAura.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            AnvilAura.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            AnvilAura.mc.player.inventory.currentItem = n;
            AnvilAura.mc.playerController.updateController();
        }
    }

    @Override
    public void onTick() {
        this.doAnvilAura();
    }
}

