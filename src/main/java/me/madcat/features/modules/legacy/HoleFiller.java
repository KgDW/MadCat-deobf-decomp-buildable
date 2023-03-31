package me.madcat.features.modules.legacy;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.madcat.MadCat;
import me.madcat.event.events.PacketEvent;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.combat.AutoTrap;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.ColorUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HoleFiller
extends Module {
    private static boolean isSpoofingAngles;
    private static HoleFiller INSTANCE;
    private final Setting<Double> range = this.register(new Setting<>("Range", 4.5, 0.1, 6.0));
    private final Setting<Double> smartRange = this.register(new Setting<>("HoleRange", 3.0, 0.1, 6.0));
    private final Setting<Boolean> web = this.register(new Setting<>("WEB", false));
    private final Setting<Boolean> rainbow = this.register(new Setting<>("Rainbow", false));
    private final Setting<Integer> red = this.register(new Setting<>("Red", 0, 0, 255, this::new0));
    private final Setting<Integer> green = this.register(new Setting<>("Green", 255, 0, 255, this::new1));
    private final Setting<Integer> blue = this.register(new Setting<>("Blue", 0, 0, 255, this::new2));
    private final Setting<Integer> alpha = this.register(new Setting<>("Alpha", 0, 0, 255, this::new3));
    private final Setting<Integer> outlineAlpha = this.register(new Setting<>("OL-Alpha", 0, 0, 255, this::new4));
    private BlockPos render;
    private EntityPlayer closestTarget;

    public HoleFiller() {
        super("HoleFiller", "Fills holes around enemy", Module.Category.LEGACY);
        this.setInstance();
    }

    public static HoleFiller INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new HoleFiller();
        }
        return INSTANCE;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(HoleFiller.mc.player.posX), Math.floor(HoleFiller.mc.player.posY), Math.floor(HoleFiller.mc.player.posZ));
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            double yaw = HoleFiller.mc.player.rotationYaw;
            double pitch = HoleFiller.mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.closestTarget != null) {
            return this.closestTarget.getName();
        }
        return null;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        if (Feature.fullNullCheck()) {
            return;
        }
        Object t = send.getPacket();
    }

    @Override
    public void onUpdate() {
        int n;
        int n2;
        if (Feature.fullNullCheck()) {
            return;
        }
        if (HoleFiller.mc.world == null) {
            return;
        }
        this.findClosestTarget();
        List<BlockPos> list = this.findCrystalBlocks();
        BlockPos blockPos = null;
        int n3 = n2 = HoleFiller.mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? HoleFiller.mc.player.inventory.currentItem : -1;
        if (n2 == -1) {
            for (n = 0; n < 9; ++n) {
                if (HoleFiller.mc.player.inventory.getStackInSlot(n).getItem() != Item.getItemFromBlock(Blocks.OBSIDIAN)) continue;
                n2 = n;
                break;
            }
        }
        if (this.web.getValue()) {
            int n4 = n2 = HoleFiller.mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.WEB) ? HoleFiller.mc.player.inventory.currentItem : -1;
            if (n2 == -1) {
                for (n = 0; n < 9; ++n) {
                    if (HoleFiller.mc.player.inventory.getStackInSlot(n).getItem() != Item.getItemFromBlock(Blocks.WEB)) continue;
                    n2 = n;
                    break;
                }
            }
        }
        if (n2 == -1) {
            if (this.web.getValue()) {
                n2 = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                if (n2 == -1) {
                    return;
                }
            } else {
                return;
            }
        }
        for (BlockPos blockPos2 : list) {
            if (!HoleFiller.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2)).isEmpty()) continue;
            blockPos = blockPos2;
        }
        this.render = blockPos;
        if (blockPos != null && HoleFiller.mc.player.onGround) {
            if (BlockUtil.cantBlockPlaceMine(this.render)) {
                return;
            }
            int n5 = HoleFiller.mc.player.inventory.currentItem;
            this.switchToSlot(n2);
            BlockUtil.placeBlock(this.render, EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(n5);
        }
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (this.render != null) {
            RenderUtil.drawBoxESP(this.render, this.rainbow.getValue() ? ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.outlineAlpha.getValue()), 3.5f, true, true, this.alpha.getValue());
        }
    }

    private void switchToSlot(int n) {
        AutoTrap.mc.player.inventory.currentItem = n;
        AutoTrap.mc.playerController.updateController();
    }

    public boolean IsHole(BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.add(0, 1, 0);
        BlockPos blockPos3 = blockPos.add(0, 0, 0);
        BlockPos blockPos4 = blockPos.add(0, 0, -1);
        BlockPos blockPos5 = blockPos.add(1, 0, 0);
        BlockPos blockPos6 = blockPos.add(-1, 0, 0);
        BlockPos blockPos7 = blockPos.add(0, 0, 1);
        BlockPos blockPos8 = blockPos.add(0, 2, 0);
        BlockPos blockPos9 = blockPos.add(0.5, 0.5, 0.5);
        BlockPos blockPos10 = blockPos.add(0, -1, 0);
        return !(HoleFiller.mc.world.getBlockState(blockPos2).getBlock() != Blocks.AIR || HoleFiller.mc.world.getBlockState(blockPos3).getBlock() != Blocks.AIR || HoleFiller.mc.world.getBlockState(blockPos8).getBlock() != Blocks.AIR || HoleFiller.mc.world.getBlockState(blockPos4).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(blockPos4).getBlock() != Blocks.BEDROCK || HoleFiller.mc.world.getBlockState(blockPos5).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(blockPos5).getBlock() != Blocks.BEDROCK || HoleFiller.mc.world.getBlockState(blockPos6).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(blockPos6).getBlock() != Blocks.BEDROCK || HoleFiller.mc.world.getBlockState(blockPos7).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(blockPos7).getBlock() != Blocks.BEDROCK || HoleFiller.mc.world.getBlockState(blockPos9).getBlock() != Blocks.AIR || HoleFiller.mc.world.getBlockState(blockPos10).getBlock() != Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(blockPos10).getBlock() != Blocks.BEDROCK);
    }

    public BlockPos getClosestTargetPos() {
        if (this.closestTarget != null) {
            return new BlockPos(Math.floor(this.closestTarget.posX), Math.floor(this.closestTarget.posY), Math.floor(this.closestTarget.posZ));
        }
        return null;
    }

    private void findClosestTarget() {
        List<EntityPlayer> players = HoleFiller.mc.world.playerEntities;

        for (EntityPlayer player : players) {
            if (!EntityUtil.isLiving(player) || player.getHealth() <= 0f || MadCat.friendManager.isFriend(player.getName())) continue;

            if (this.closestTarget == null || mc.player.getDistance(player) < mc.player.getDistance(this.closestTarget)) {
                this.closestTarget = player;
            }
        }
    }

    private boolean isInRange(BlockPos blockPos) {
        NonNullList nonNullList = NonNullList.create();
        nonNullList.addAll(this.getSphere(HoleFiller.getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).collect(Collectors.toList()));
        return nonNullList.contains(blockPos);
    }

    private NonNullList findCrystalBlocks() {
        NonNullList nonNullList = NonNullList.create();
        if (this.closestTarget != null) {
            nonNullList.addAll(this.getSphere(this.getClosestTargetPos(), this.smartRange.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).filter(this::isInRange).collect(Collectors.toList()));
        }
        return nonNullList;
    }

    public List<BlockPos> getSphere(BlockPos blockPos, float f, int n, boolean bl, boolean bl2, int n2) {
        ArrayList<BlockPos> arrayList = new ArrayList<>();
        int n3 = blockPos.getX();
        int n4 = blockPos.getY();
        int n5 = blockPos.getZ();
        int n6 = n3 - (int)f;
        while ((float)n6 <= (float)n3 + f) {
            int n7 = n5 - (int)f;
            block1: while ((float)n7 <= (float)n5 + f) {
                int n8 = bl2 ? n4 - (int)f : n4;
                while (true) {
                    float f2;
                    float f3 = n8;
                    float f4 = f2 = bl2 ? (float)n4 + f : (float)(n4 + n);
                    if (f3 >= f2) {
                        ++n7;
                        continue block1;
                    }
                    double d = (n3 - n6) * (n3 - n6) + (n5 - n7) * (n5 - n7) + (bl2 ? (n4 - n8) * (n4 - n8) : 0);
                    if (d < (double)(f * f) && (!bl || d >= (double)((f - 1.0f) * (f - 1.0f)))) {
                        BlockPos blockPos2 = new BlockPos(n6, n8 + n2, n7);
                        arrayList.add(blockPos2);
                    }
                    ++n8;
                }
            }
            ++n6;
        }
        return arrayList;
    }

    @Override
    public void onDisable() {
        this.closestTarget = null;
        this.render = null;
        HoleFiller.resetRotation();
    }

    private boolean new4(Integer n) {
        return !this.rainbow.getValue();
    }

    private boolean new3(Integer n) {
        return !this.rainbow.getValue();
    }

    private boolean new2(Integer n) {
        return !this.rainbow.getValue();
    }

    private boolean new1(Integer n) {
        return !this.rainbow.getValue();
    }

    private boolean new0(Integer n) {
        return !this.rainbow.getValue();
    }

    static {
        INSTANCE = new HoleFiller();
    }
}

