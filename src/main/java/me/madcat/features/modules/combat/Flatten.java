package me.madcat.features.modules.combat;

import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Flatten
extends Module {
    public final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    public EntityPlayer target;
    private final Setting<Boolean> air;
    public final Setting<Boolean> holeCheck;
    int progress = 0;
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", true));
    private final Setting<Integer> blocks;
    private final Setting<Boolean> negative;
    public final Setting<Boolean> anyBlock;
    public final Setting<Integer> multiPlace;
    private final Setting<Float> range;

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        this.progress = 0;
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        if (this.air.getValue() && !Flatten.mc.player.onGround) {
            return;
        }
        BlockPos blockPos = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        if (this.placeBlock(blockPos.add(0, -1, 0))) {
            ++this.progress;
        }
        if (this.placeBlock(blockPos.add(1, -1, 0))) {
            ++this.progress;
        }
        if (this.placeBlock(blockPos.add(-1, -1, 0))) {
            ++this.progress;
        }
        if (this.placeBlock(blockPos.add(0, -1, 1))) {
            ++this.progress;
        }
        if (this.placeBlock(blockPos.add(0, -1, -1))) {
            ++this.progress;
        }
        if (this.holeCheck.getValue()) {
            if (this.placeBlock(blockPos.add(0, -2, 0))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(1, -2, 0))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(-1, -2, 0))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(0, -2, 1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(0, -2, -1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(2, -1, 0))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(-2, -1, 0))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(0, -1, 2))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(0, -1, -2))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(1, -1, 1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(-1, -1, 1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(-1, -1, -1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(1, -1, -1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(2, -2, 0))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(-2, -2, 0))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(0, -2, 2))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(0, -2, -2))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(1, -2, 1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(-1, -2, 1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(-1, -2, -1))) {
                ++this.progress;
            }
            if (this.placeBlock(blockPos.add(1, -2, -1))) {
                ++this.progress;
            }
        }
    }

    private boolean isHole(BlockPos blockPos, boolean bl) {
        if (bl) {
            return this.getBlock(blockPos) == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 0)) == Blocks.AIR && this.getBlock(blockPos.add(0, -1, 0)) != Blocks.AIR && (this.getBlock(blockPos.add(0, 0, 1)) != Blocks.AIR || this.getBlock(blockPos.add(0, 0, 2)) != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)) != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 1)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.AIR) && (this.getBlock(blockPos.add(0, 0, -1)) != Blocks.AIR || this.getBlock(blockPos.add(0, 0, -2)) != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)) != Blocks.AIR && this.getBlock(blockPos.add(1, 0, -1)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.AIR) && (this.getBlock(blockPos.add(1, 0, 0)) != Blocks.AIR || this.getBlock(blockPos.add(2, 0, 0)) != Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)) != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 1)) != Blocks.AIR && this.getBlock(blockPos.add(1, 0, -1)) != Blocks.AIR) && (this.getBlock(blockPos.add(-1, 0, 0)) != Blocks.AIR || this.getBlock(blockPos.add(-2, 0, 0)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.AIR);
        }
        return !(this.getBlock(blockPos) != Blocks.AIR || this.getBlock(blockPos.add(0, 1, 0)) != Blocks.AIR || this.getBlock(blockPos.add(0, -1, 0)) == Blocks.AIR || this.getBlock(blockPos.add(0, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 0, 1)) != Blocks.BEDROCK && (this.getBlock(blockPos.add(0, 0, 2)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 0, 2)) != Blocks.BEDROCK || this.getBlock(blockPos.add(0, 1, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 1, 1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, 1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.BEDROCK) || this.getBlock(blockPos.add(0, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 0, -1)) != Blocks.BEDROCK && (this.getBlock(blockPos.add(0, 0, -2)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 0, -2)) != Blocks.BEDROCK || this.getBlock(blockPos.add(0, 1, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 1, -1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, -1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.BEDROCK) || this.getBlock(blockPos.add(1, 0, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, 0)) != Blocks.BEDROCK && (this.getBlock(blockPos.add(2, 0, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(2, 0, 0)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 1, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 1, 0)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, 1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, -1)) != Blocks.BEDROCK) || this.getBlock(blockPos.add(-1, 0, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, 0)) != Blocks.BEDROCK && (this.getBlock(blockPos.add(-2, 0, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-2, 0, 0)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 1, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 1, 0)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.BEDROCK));
    }

    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }

    public Block getBlock(BlockPos blockPos) {
        return Flatten.mc.world.getBlockState(blockPos).getBlock();
    }

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = d;
        for (EntityPlayer entityPlayer2 : Flatten.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d) || MadCat.friendManager.isFriend(entityPlayer2.getName())) continue;
            if (Flatten.mc.player.posY - entityPlayer2.posY >= 5.0) {
                continue;
            }
            if (entityPlayer == null) {
                entityPlayer = entityPlayer2;
                d2 = Flatten.mc.player.getDistanceSq(entityPlayer2);
                continue;
            }
            if (Flatten.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = Flatten.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    private boolean isHoleNew(BlockPos blockPos, boolean bl, int n) {
        int n2 = 0;
        if (bl) {
            if (this.getBlock(blockPos.add(0, 0, 1)) != Blocks.AIR || this.getBlock(blockPos.add(0, 0, 2)) != Blocks.AIR && this.getBlock(blockPos.add(0, 1, 1)) != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 1)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.AIR) {
                ++n2;
            }
            if (this.getBlock(blockPos.add(0, 0, -1)) != Blocks.AIR || this.getBlock(blockPos.add(0, 0, -2)) != Blocks.AIR && this.getBlock(blockPos.add(0, 1, -1)) != Blocks.AIR && this.getBlock(blockPos.add(1, 0, -1)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.AIR) {
                ++n2;
            }
            if (this.getBlock(blockPos.add(1, 0, 0)) != Blocks.AIR || this.getBlock(blockPos.add(2, 0, 0)) != Blocks.AIR && this.getBlock(blockPos.add(1, 1, 0)) != Blocks.AIR && this.getBlock(blockPos.add(1, 0, 1)) != Blocks.AIR && this.getBlock(blockPos.add(1, 0, -1)) != Blocks.AIR) {
                ++n2;
            }
            if (this.getBlock(blockPos.add(-1, 0, 0)) != Blocks.AIR || this.getBlock(blockPos.add(-2, 0, 0)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 1, 0)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.AIR && this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.AIR) {
                ++n2;
            }
        } else {
            if (!(this.getBlock(blockPos.add(0, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 0, 1)) != Blocks.BEDROCK && (this.getBlock(blockPos.add(0, 0, 2)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 0, 2)) != Blocks.BEDROCK || this.getBlock(blockPos.add(0, 1, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 1, 1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, 1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.BEDROCK))) {
                ++n2;
            }
            if (!(this.getBlock(blockPos.add(0, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 0, -1)) != Blocks.BEDROCK && (this.getBlock(blockPos.add(0, 0, -2)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 0, -2)) != Blocks.BEDROCK || this.getBlock(blockPos.add(0, 1, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(0, 1, -1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, -1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.BEDROCK))) {
                ++n2;
            }
            if (!(this.getBlock(blockPos.add(1, 0, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, 0)) != Blocks.BEDROCK && (this.getBlock(blockPos.add(2, 0, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(2, 0, 0)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 1, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 1, 0)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, 1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(1, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(1, 0, -1)) != Blocks.BEDROCK))) {
                ++n2;
            }
            if (!(this.getBlock(blockPos.add(-1, 0, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, 0)) != Blocks.BEDROCK && (this.getBlock(blockPos.add(-2, 0, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-2, 0, 0)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 1, 0)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 1, 0)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, 1)) != Blocks.BEDROCK || this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.OBSIDIAN && this.getBlock(blockPos.add(-1, 0, -1)) != Blocks.BEDROCK))) {
                ++n2;
            }
        }
        return this.getBlock(blockPos) == Blocks.AIR && this.getBlock(blockPos.add(0, 1, 0)) == Blocks.AIR && this.getBlock(blockPos.add(0, -1, 0)) != Blocks.AIR && this.getBlock(blockPos.add(0, 2, 0)) == Blocks.AIR && n2 > n - 1;
    }

    public Flatten() {
        super("Flatten", "Automatically pave the road for the enemy", Module.Category.COMBAT);
        this.holeCheck = this.register(new Setting<>("HoleCheck", true));
        this.multiPlace = this.register(new Setting<>("MultiPlace", 1, 0, 8));
        this.anyBlock = this.add(new Setting<>("anyBlock", Boolean.TRUE, this::new0));
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 6.0f));
        this.negative = this.register(new Setting<>("Chest Place", false));
        this.air = this.register(new Setting<>("SelfGround", false));
        this.blocks = this.register(new Setting<>("Blocks", 3, 2, 4));
    }

    private boolean placeBlock(BlockPos blockPos) {
        if (this.progress > this.multiPlace.getValue() - 1) {
            return false;
        }
        if (this.holeCheck.getValue() && !this.isHoleNew(blockPos, this.anyBlock.getValue(), this.blocks.getValue())) {
            return false;
        }
        if (BlockUtil.cantBlockPlaceMineEntity(blockPos)) {
            return false;
        }
        int n = Flatten.mc.player.inventory.currentItem;
        if (this.negative.getValue() && InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
            Flatten.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            Flatten.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            Flatten.mc.player.inventory.currentItem = n;
            Flatten.mc.playerController.updateController();
        } else if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
            Flatten.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            Flatten.mc.playerController.updateController();
            BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), false);
            Flatten.mc.player.inventory.currentItem = n;
            Flatten.mc.playerController.updateController();
        }
        return true;
    }

    private boolean new0(Boolean bl) {
        return this.holeCheck.getValue();
    }
}

