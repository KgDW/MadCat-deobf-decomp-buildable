package me.madcat.features.modules.combat;

import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.MathUtil;
import me.madcat.util.Timer;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoWeb
extends Module {
    private final Setting<Integer> downSpeed;
    public EntityPlayer target;
    private final Setting<Integer> delay;
    private final Timer timer;
    int webSlot = -1;
    private final Setting<Boolean> predict;
    private final Setting<Integer> predictSpeed;
    private final Setting<Float> range = this.register(new Setting<>("Range", 5.0f, 1.0f, 6.0f));

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        this.webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        if (this.webSlot == -1) {
            return;
        }
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        BlockPos blockPos = EntityUtil.getPlayerPos(this.target);
        if (EntityUtil.isInHole(this.target)) {
            return;
        }
        if (AutoWeb.mc.player.onGround && this.timer.passedMs(this.delay.getValue())) {
            boolean bl = true;
            if (BlockUtil.CanPlace(blockPos.add(0, -1, 0)) && MadCat.speedManager.getPlayerSpeed(this.target) >= (double) this.downSpeed.getValue()) {
                this.webPlace(blockPos.add(0, -1, 0));
                bl = false;
            }
            if (BlockUtil.CanPlace(blockPos) && MadCat.speedManager.getPlayerSpeed(this.target) >= 13.0) {
                this.webPlace(blockPos);
                bl = false;
            }
            if (this.predict.getValue() && MadCat.speedManager.getPlayerSpeed(this.target) >= (double) this.predictSpeed.getValue() && bl) {
                if (this.check(blockPos.add(1, -1, 0))) {
                    this.webPlace(blockPos.add(1, -1, 0));
                } else if (this.check(blockPos.add(0, -1, 1))) {
                    this.webPlace(blockPos.add(0, -1, 1));
                } else if (this.check(blockPos.add(-1, -1, 0))) {
                    this.webPlace(blockPos.add(-1, -1, 0));
                } else if (this.check(blockPos.add(0, -1, -1))) {
                    this.webPlace(blockPos.add(0, -1, -1));
                }
            }
            this.timer.reset();
        }
    }

    private EntityPlayer getTarget(double d) {
        EntityPlayer entityPlayer = null;
        double d2 = d;
        for (EntityPlayer entityPlayer2 : AutoWeb.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d)) {
                continue;
            }
            if (entityPlayer2.isDead) {
                continue;
            }
            if (MadCat.speedManager.getPlayerSpeed(entityPlayer2) > 30.0) {
                continue;
            }
            if (entityPlayer != null && AutoWeb.mc.player.getDistanceSq(entityPlayer2) >= d2) {
                continue;
            }
            entityPlayer = entityPlayer2;
            d2 = AutoWeb.mc.player.getDistanceSq(entityPlayer2);
        }
        return entityPlayer;
    }

    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.getName();
        }
        return null;
    }

    public AutoWeb() {
        super("AutoWeb", "Traps other players in webs", Module.Category.COMBAT);
        this.delay = this.register(new Setting<>("Delay", 72, 0, 500));
        this.downSpeed = this.register(new Setting<>("DownSpeed", 13));
        this.predict = this.register(new Setting<>("Predict", true));
        this.predictSpeed = this.register(new Setting<>("PredictSpeed", 13, this::new0));
        this.timer = new Timer();
    }

    private void webPlace(BlockPos blockPos) {
        if (BlockUtil.CrystalCheck(blockPos)) {
            return;
        }
        int n = AutoWeb.mc.player.inventory.currentItem;
        InventoryUtil.switchToSlot(this.webSlot);
        BlockUtil.placeBlock(blockPos, EnumHand.MAIN_HAND, false, true, false);
        InventoryUtil.switchToSlot(n);
    }

    private boolean new0(Integer n) {
        return this.predict.getValue();
    }

    private boolean check(BlockPos blockPos) {
        return BlockUtil.CanPlace(blockPos) && BlockUtil.isAir(blockPos.add(0, 1, 0)) && BlockUtil.isAir(blockPos.add(0, 2, 0)) && !BlockUtil.CrystalCheck(blockPos) && AutoWeb.mc.player.getDistanceSq(blockPos) <= MathUtil.square(this.range.getValue()) && !BlockUtil.MineCheck(blockPos);
    }
}

