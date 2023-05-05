package me.madcat.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.HashMap;
import me.madcat.event.events.BlockBreakEvent;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.FadeUtils;
import me.madcat.util.RenderUtil;
import me.madcat.util.Timer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreakESP
extends Module {
    private final Setting<Boolean> renderName;
    private final Setting<Integer> blue;
    private final Setting<Boolean> renderUnknown;
    private final Setting<Integer> doubleGreen;
    static final HashMap<EntityPlayer, MinePosition> MineMap;
    private final Setting<Integer> red;
    static final boolean $assertionsDisabled;
    private final Setting<Double> range;
    private final Setting<Boolean> renderSelf;
    private final Setting<Integer> doubleRed;
    private final Setting<Integer> doubleAlpha;
    private final Setting<Boolean> doubleRender;
    private final Setting<Boolean> renderProgress;
    private final Setting<Integer> green;
    private final Setting<Integer> doubleBlue;
    private final Setting<Integer> alpha;
    private final Setting<Boolean> renderAir = this.register(new Setting<>("RenderAir", true));

    @SubscribeEvent
    public void BlockBreak(BlockBreakEvent blockBreakEvent) {
        if (InstantMine.godBlocks.contains(BreakESP.mc.world.getBlockState(blockBreakEvent.getPosition()).getBlock())) {
            return;
        }
        if (blockBreakEvent.getPosition().getY() == -1) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)BreakESP.mc.world.getEntityByID(blockBreakEvent.getBreakerId());
        if (!MineMap.containsKey(entityPlayer)) {
            MineMap.put(entityPlayer, new MinePosition(entityPlayer));
        }
        MineMap.get(entityPlayer).update(blockBreakEvent.getPosition());
    }

    public void draw(BlockPos blockPos, double d, Color color, int n) {
        AxisAlignedBB axisAlignedBB = BreakESP.mc.world.getBlockState(blockPos).getSelectedBoundingBox(BreakESP.mc.world, blockPos);
        double d2 = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
        double d3 = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
        double d4 = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
        double d5 = d * (axisAlignedBB.maxX - d2);
        double d6 = d * (axisAlignedBB.maxY - d3);
        double d7 = d * (axisAlignedBB.maxZ - d4);
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(d2 - d5, d3 - d6, d4 - d7, d2 + d5, d3 + d6, d4 + d7);
        RenderUtil.drawBBBox(axisAlignedBB2, color, n);
        RenderUtil.drawBBFill(axisAlignedBB2, color, n);
    }

    static {
        $assertionsDisabled = !BreakESP.class.desiredAssertionStatus();
        MineMap = new HashMap();
    }

    private void onRender3D5(double[] dArray, Render3DEvent render3DEvent, double[] dArray2, MinePosition minePosition) {
        double d;
        double d2;
        double d3;
        if (!this.renderAir.getValue() && BreakESP.mc.world.isAirBlock(minePosition.first)) {
            minePosition.finishFirst();
        }
        if ((!minePosition.firstFinished || this.renderAir.getValue()) && (minePosition.miner != BreakESP.mc.player || this.renderSelf.getValue()) && minePosition.first.getDistance((int)BreakESP.mc.player.posX, (int)BreakESP.mc.player.posY, (int)BreakESP.mc.player.posZ) < this.range.getValue()) {
            dArray[0] = minePosition.firstFade.easeOutQuad();
            this.draw(minePosition.first, dArray[0], new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.alpha.getValue());
            if (this.renderName.getValue() && (minePosition.miner != null || this.renderUnknown.getValue())) {
                d3 = this.interpolate(minePosition.first.getX(), minePosition.first.getX(), render3DEvent.getPartialTicks()) - BreakESP.mc.getRenderManager().viewerPosX + 0.5;
                d2 = this.interpolate(minePosition.first.getY(), minePosition.first.getY(), render3DEvent.getPartialTicks()) - BreakESP.mc.getRenderManager().viewerPosY - 1.0;
                d = this.interpolate(minePosition.first.getZ(), minePosition.first.getZ(), render3DEvent.getPartialTicks()) - BreakESP.mc.getRenderManager().viewerPosZ + 0.5;
                this.renderNameTag(ChatFormatting.GRAY + (minePosition.miner == null ? "Unknown" : minePosition.miner.getName()), d3, d2, d, render3DEvent.getPartialTicks());
                if (!this.renderProgress.getValue()) {
                    this.renderNameTag(ChatFormatting.GREEN + "Breaking", d3, d2 - 0.3, d, render3DEvent.getPartialTicks());
                } else if ((int)minePosition.breakTime.getPassedTimeMs() < 1500) {
                    double d4 = (double)minePosition.breakTime.getPassedTimeMs() / 15.0;
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    this.renderNameTag(ChatFormatting.GREEN + decimalFormat.format(d4) + "%", d3, d2 - 0.3, d, render3DEvent.getPartialTicks());
                } else {
                    this.renderNameTag(ChatFormatting.GREEN + "100.0%", d3, d2 - 0.3, d, render3DEvent.getPartialTicks());
                }
            }
        }
        if ((minePosition.miner != BreakESP.mc.player || this.renderSelf.getValue()) && !minePosition.secondFinished && minePosition.second != null) {
            if (BreakESP.mc.world.isAirBlock(minePosition.second)) {
                minePosition.finishSecond();
            } else if (!minePosition.second.equals(minePosition.first) && minePosition.second.getDistance((int)BreakESP.mc.player.posX, (int)BreakESP.mc.player.posY, (int)BreakESP.mc.player.posZ) < this.range.getValue() && this.doubleRender.getValue()) {
                dArray2[0] = minePosition.secondFade.easeOutQuad();
                this.draw(minePosition.second, dArray2[0], new Color(this.doubleRed.getValue(), this.doubleGreen.getValue(), this.doubleBlue.getValue(), this.doubleAlpha.getValue()), this.doubleAlpha.getValue());
                if (this.renderName.getValue() && (minePosition.miner != null || this.renderUnknown.getValue())) {
                    d3 = this.interpolate(minePosition.second.getX(), minePosition.second.getX(), render3DEvent.getPartialTicks()) - BreakESP.mc.getRenderManager().viewerPosX + 0.5;
                    d2 = this.interpolate(minePosition.second.getY(), minePosition.second.getY(), render3DEvent.getPartialTicks()) - BreakESP.mc.getRenderManager().viewerPosY - 1.0;
                    d = this.interpolate(minePosition.second.getZ(), minePosition.second.getZ(), render3DEvent.getPartialTicks()) - BreakESP.mc.getRenderManager().viewerPosZ + 0.5;
                    this.renderNameTag(ChatFormatting.GRAY + (minePosition.miner == null ? "Unknown" : minePosition.miner.getName()), d3, d2, d, render3DEvent.getPartialTicks());
                    this.renderNameTag(ChatFormatting.GOLD + "Double", d3, d2 - 0.3, d, render3DEvent.getPartialTicks());
                }
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        EntityPlayer[] entityPlayerArray;
        for (EntityPlayer entityPlayer : entityPlayerArray = MineMap.keySet().toArray(new EntityPlayer[0])) {
            if (entityPlayer == null || entityPlayer.isEntityAlive()) continue;
            MineMap.remove(entityPlayer);
        }
        double[] dArray = new double[1];
        double[] dArray2 = new double[1];
        MineMap.values().forEach(arg_0 -> this.onRender3D5(dArray, render3DEvent, dArray2, arg_0));
    }

    private boolean new0(Boolean bl) {
        return this.renderName.getValue();
    }

    public BreakESP() {
        super("BreakESP", "Show instant mine postion", Module.Category.RENDER);
        this.renderSelf = this.register(new Setting<>("RenderSelf", true));
        this.renderUnknown = this.register(new Setting<>("RenderUnknown", true));
        this.range = this.register(new Setting<>("Range", 15.0, 0.0, 50.0));
        this.renderName = this.register(new Setting<>("PlayerName", true));
        this.renderProgress = this.register(new Setting<>("Progress", Boolean.TRUE, this::new0));
        this.red = this.register(new Setting<>("Red", 255, 0, 255));
        this.green = this.register(new Setting<>("Green", 255, 0, 255));
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255));
        this.alpha = this.register(new Setting<>("Alpha", 100, 20, 255));
        this.doubleRender = this.register(new Setting<>("DoubleRender", true));
        this.doubleRed = this.register(new Setting<>("DoubleRed", 255, 0, 255, this::new1));
        this.doubleGreen = this.register(new Setting<>("DoubleGreen", 255, 0, 255, this::new2));
        this.doubleBlue = this.register(new Setting<>("DoubleBlue", 255, 0, 255, this::new3));
        this.doubleAlpha = this.register(new Setting<>("DoubleAlpha", 100, 20, 255, this::new4));
    }

    private void renderNameTag(String string, double d, double d2, double d3, float f) {
        Entity entity = mc.getRenderViewEntity();
        if (!$assertionsDisabled && entity == null) {
            throw new AssertionError();
        }
        double d4 = entity.posX;
        double d5 = entity.posY;
        double d6 = entity.posZ;
        entity.posX = this.interpolate(entity.prevPosX, entity.posX, f);
        entity.posY = this.interpolate(entity.prevPosY, entity.posY, f);
        entity.posZ = this.interpolate(entity.prevPosZ, entity.posZ, f);
        int n = this.renderer.getStringWidth(string) / 2;
        double d7 = 0.027999999999999997;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)d, (float)d2 + 1.4f, (float)d3);
        GlStateManager.rotate(-BreakESP.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(BreakESP.mc.getRenderManager().playerViewX, BreakESP.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-d7, -d7, d7);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        GlStateManager.disableBlend();
        this.renderer.drawStringWithShadow(string, -n, -(this.renderer.getFontHeight() - 1), ColorUtil.toRGBA(new Color(255, 255, 255, 255)));
        entity.posX = d4;
        entity.posY = d5;
        entity.posZ = d6;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private boolean new3(Integer n) {
        return this.doubleRender.getValue();
    }

    private boolean new2(Integer n) {
        return this.doubleRender.getValue();
    }

    private boolean new4(Integer n) {
        return this.doubleRender.getValue();
    }

    private boolean new1(Integer n) {
        return this.doubleRender.getValue();
    }

    private double interpolate(double d, double d2, float f) {
        return d + (d2 - d) * (double)f;
    }

    @Override
    public void onDisable() {
        MineMap.clear();
    }

    private static class MinePosition {
        public BlockPos first;
        public final FadeUtils firstFade = new FadeUtils(2000L);
        public final EntityPlayer miner;
        public final FadeUtils secondFade = new FadeUtils(2000L);
        public final Timer breakTime = new Timer();
        public boolean firstFinished;
        public BlockPos second;
        public boolean secondFinished;

        public void finishFirst() {
            this.firstFinished = true;
        }

        public MinePosition(EntityPlayer entityPlayer) {
            this.miner = entityPlayer;
            this.secondFinished = true;
        }

        public void finishSecond() {
            this.secondFinished = true;
        }

        public void update(BlockPos blockPos) {
            if (this.first != null && this.first.equals(blockPos)) {
                return;
            }
            if (this.secondFinished || this.second == null) {
                this.second = blockPos;
                this.secondFinished = false;
                this.secondFade.reset();
            }
            this.firstFinished = false;
            this.breakTime.reset();
            this.firstFade.reset();
            this.first = blockPos;
            if (this.second != null && this.second.equals(this.first)) {
                this.secondFade.reset();
            }
        }
    }
}

