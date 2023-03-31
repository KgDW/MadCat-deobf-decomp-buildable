package me.madcat.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.madcat.MadCat;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.modules.Module;
import me.madcat.util.ColorUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Trajectories
extends Module {
    public Trajectories() {
        super("Trajectories", "Draws trajectories", Module.Category.RENDER);
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        Vec3d vec3d = null;
        if (Trajectories.mc.player == null || Trajectories.mc.world == null || Trajectories.mc.gameSettings.thirdPersonView == 2) {
            return;
        }
        if (!(Trajectories.mc.player.getHeldItemMainhand() != ItemStack.EMPTY && Trajectories.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow || Trajectories.mc.player.getHeldItemMainhand() != ItemStack.EMPTY && this.isThrowable(Trajectories.mc.player.getHeldItemMainhand().getItem()) || Trajectories.mc.player.getHeldItemOffhand() != ItemStack.EMPTY && this.isThrowable(Trajectories.mc.player.getHeldItemOffhand().getItem()) || Mouse.isButtonDown(2))) {
            return;
        }
        double d = Trajectories.mc.getRenderManager().viewerPosX;
        double d2 = Trajectories.mc.getRenderManager().viewerPosY;
        double d3 = Trajectories.mc.getRenderManager().viewerPosZ;
        Item item = null;
        if (Trajectories.mc.player.getHeldItemMainhand() != ItemStack.EMPTY && (Trajectories.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow || this.isThrowable(Trajectories.mc.player.getHeldItemMainhand().getItem()))) {
            item = Trajectories.mc.player.getHeldItemMainhand().getItem();
        } else if (Trajectories.mc.player.getHeldItemOffhand() != ItemStack.EMPTY && this.isThrowable(Trajectories.mc.player.getHeldItemOffhand().getItem())) {
            item = Trajectories.mc.player.getHeldItemOffhand().getItem();
        }
        if (item == null && Mouse.isButtonDown(2)) {
            item = Items.ENDER_PEARL;
        } else if (item == null) {
            return;
        }
        GL11.glPushAttrib(1048575);
        GL11.glPushMatrix();
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4353);
        GL11.glDisable(2896);
        double d4 = d - (double)(MathHelper.cos(Trajectories.mc.player.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        double d5 = d2 + (double)Trajectories.mc.player.getEyeHeight() - 0.1000000014901161;
        double d6 = d3 - (double)(MathHelper.sin(Trajectories.mc.player.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        float f = this.getDistance(item);
        double d7 = -MathHelper.sin(Trajectories.mc.player.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(Trajectories.mc.player.rotationPitch / 180.0f * (float)Math.PI) * f;
        double d8 = -MathHelper.sin((Trajectories.mc.player.rotationPitch - (float)this.getPitch(item)) / 180.0f * 3.141593f) * f;
        double d9 = MathHelper.cos(Trajectories.mc.player.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(Trajectories.mc.player.rotationPitch / 180.0f * (float)Math.PI) * f;
        int n = 72000 - Trajectories.mc.player.getItemInUseCount();
        float f2 = (float)n / 20.0f;
        f2 = (f2 * f2 + f2 * 2.0f) / 3.0f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        float f3 = MathHelper.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
        d7 /= f3;
        d8 /= f3;
        d9 /= f3;
        float f4 = (item instanceof ItemBow ? f2 * 2.0f : 1.0f) * this.getVelocity(item);
        d7 *= f4;
        d8 *= f4;
        d9 *= f4;
        if (!Trajectories.mc.player.onGround) {
            d8 += Trajectories.mc.player.motionY;
        }
        RenderUtil.glColor(MadCat.colorManager.getCurrent());
        GL11.glEnable(2848);
        float f5 = (float)(item instanceof ItemBow ? 0.3 : 0.25);
        boolean bl = false;
        Entity entity = null;
        RayTraceResult rayTraceResult = null;
        GL11.glBegin(3);
        while (!bl && d5 > 0.0) {
            Vec3d vec3d2 = new Vec3d(d4, d5, d6);
            vec3d = new Vec3d(d4 + d7, d5 + d8, d6 + d9);
            RayTraceResult rayTraceResult2 = Trajectories.mc.world.rayTraceBlocks(vec3d2, vec3d, false, true, false);
            if (rayTraceResult2 != null && rayTraceResult2.typeOfHit != RayTraceResult.Type.MISS) {
                rayTraceResult = rayTraceResult2;
                bl = true;
            }
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d4 - (double)f5, d5 - (double)f5, d6 - (double)f5, d4 + (double)f5, d5 + (double)f5, d6 + (double)f5);
            List<Entity> list = this.getEntitiesWithinAABB(axisAlignedBB.offset(d7, d8, d9).expand(1.0, 1.0, 1.0));
            for (Entity entity2 : list) {
                if (!entity2.canBeCollidedWith() || entity2 == Trajectories.mc.player) continue;
                float f6 = 0.3f;
                AxisAlignedBB axisAlignedBB2 = entity2.getEntityBoundingBox().expand(f6, f6, f6);
                RayTraceResult rayTraceResult3 = axisAlignedBB2.calculateIntercept(vec3d2, vec3d);
                if (rayTraceResult3 == null) continue;
                bl = true;
                entity = entity2;
                rayTraceResult = rayTraceResult3;
            }
            d4 += d7;
            d5 += d8;
            d6 += d9;
            float f7 = 0.99f;
            d7 *= 0.99f;
            d8 *= 0.99f;
            d9 *= 0.99f;
            d8 -= this.getGravity(item);
            this.drawTracer(d4 - d, d5 - d2, d6 - d3);
        }
        GL11.glEnd();
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            GlStateManager.translate(d4 - d, d5 - d2, d6 - d3);
            int n2 = rayTraceResult.sideHit.getIndex();
            if (n2 == 2) {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            } else if (n2 == 3) {
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            } else if (n2 == 4) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            } else if (n2 == 5) {
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            }
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            vec3d.scale(100011);
            vec3d.scale(100012);
            RenderUtil.glColor(ColorUtil.injectAlpha(MadCat.colorManager.getCurrent(), 100));
        }
        GL11.glEnable(2896);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        if (entity != null) {
            RenderUtil.drawEntityBoxESP(entity, MadCat.colorManager.getCurrent(), false, new Color(-1), 1.0f, false, true, 100);
        }
    }

    public void drawTracer(double d, double d2, double d3) {
        GL11.glVertex3d(d, d2, d3);
    }

    private boolean isThrowable(Item item) {
        return item instanceof ItemEnderPearl || item instanceof ItemExpBottle || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemSplashPotion || item instanceof ItemLingeringPotion;
    }

    private float getDistance(Item item) {
        return item instanceof ItemBow ? 1.0f : 0.4f;
    }

    private float getVelocity(Item item) {
        if (item instanceof ItemSplashPotion || item instanceof ItemLingeringPotion) {
            return 0.5f;
        }
        if (item instanceof ItemExpBottle) {
            return 0.59f;
        }
        return 1.5f;
    }

    private int getPitch(Item item) {
        if (item instanceof ItemSplashPotion || item instanceof ItemLingeringPotion || item instanceof ItemExpBottle) {
            return 20;
        }
        return 0;
    }

    private float getGravity(Item item) {
        if (item instanceof ItemBow || item instanceof ItemSplashPotion || item instanceof ItemLingeringPotion || item instanceof ItemExpBottle) {
            return 0.05f;
        }
        return 0.03f;
    }

    private List<Entity> getEntitiesWithinAABB(AxisAlignedBB axisAlignedBB) {
        ArrayList<Entity> arrayList = new ArrayList<>();
        int n = MathHelper.floor((axisAlignedBB.minX - 2.0) / 16.0);
        int n2 = MathHelper.floor((axisAlignedBB.maxX + 2.0) / 16.0);
        int n3 = MathHelper.floor((axisAlignedBB.minZ - 2.0) / 16.0);
        int n4 = MathHelper.floor((axisAlignedBB.maxZ + 2.0) / 16.0);
        for (int i = n; i <= n2; ++i) {
            for (int j = n3; j <= n4; ++j) {
                if (Trajectories.mc.world.getChunkProvider().getLoadedChunk(i, j) == null) continue;
                Trajectories.mc.world.getChunk(i, j).getEntitiesWithinAABBForEntity(Trajectories.mc.player, axisAlignedBB, arrayList, EntitySelectors.NOT_SPECTATING);
            }
        }
        return arrayList;
    }
}

