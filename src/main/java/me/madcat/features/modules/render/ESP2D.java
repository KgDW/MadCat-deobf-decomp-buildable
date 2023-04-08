package me.madcat.features.modules.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import me.madcat.MadCat;
import me.madcat.event.events.Render2DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.render.NameTags;
import me.madcat.features.setting.Setting;
import me.madcat.mixin.mixins.IEntityRenderer;
import me.madcat.util.InventoryUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public final class ESP2D
extends Module {
    private final Setting<Integer> colorGreenValue;
    private final FloatBuffer vector;
    public final Setting<Mode> boxMode;
    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final int[] DISPLAY_LISTS_2D = new int[4];
    public final Setting<Boolean> bbtt = this.register(new Setting<>("2B2T Mode", true));
    public final Setting<Boolean> tagsValue;
    private final Setting<Integer> colorBlueValue;
    public final Setting<Boolean> localPlayer;
    public final Setting<Boolean> clearNameValue;
    public final Setting<Boolean> animals;
    private final IntBuffer viewport;
    public final Setting<Boolean> outlineFont;
    public final Setting<Boolean> healthNumber;
    private static final Frustum frustrum = new Frustum();
    private final FloatBuffer projection;
    private static ESP2D INSTANCE;
    private final FloatBuffer modelview;
    public final Setting<Boolean> armorBar;
    public final Setting<Boolean> droppedItems;
    public final Setting<Boolean> hoverValue;
    public final Setting<Boolean> tagsBGValue;
    public final Setting<Boolean> healthBar;
    public final Setting<Boolean> outline = this.register(new Setting<>("Outline", true));
    public final Setting<Boolean> mobs;
    private final DecimalFormat dFormat;
    public final Setting<Mode4> hpMode;
    private final Setting<Float> fontScaleValue;
    public static final List collectedEntities;
    public final Setting<Mode2> hpBarMode;
    private final Setting<Integer> colorRedValue;
    private final int backgroundColor;
    private final int black;
    public final Setting<Mode5> colorModeValue;
    public final Setting<Boolean> friendColor;

    private Vector3d project2D(int n, double d, double d2, double d3) {
        GL11.glGetFloat(2982, this.modelview);
        GL11.glGetFloat(2983, this.projection);
        GL11.glGetInteger(2978, this.viewport);
        return GLU.gluProject((float)d, (float)d2, (float)d3, this.modelview, this.projection, this.viewport, this.vector) ? new Vector3d(this.vector.get(0) / (float)n, ((float)Display.getHeight() - this.vector.get(1)) / (float)n, this.vector.get(2)) : null;
    }

    private void drawScaledString(String string, double d, double d2, double d3) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(d, d2, d);
        GlStateManager.scale(d3, d3, d3);
        if (this.outlineFont.getValue()) {
            this.drawOutlineStringWithoutGL(string, 0.0f, 0.0f, -1, ESP2D.mc.fontRenderer);
        } else {
            ESP2D.mc.fontRenderer.drawStringWithShadow(string, 0.0f, 0.0f, -1);
        }
        GlStateManager.popMatrix();
    }

    public boolean isAnimal(Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem || entity instanceof EntityVillager || entity instanceof EntityBat;
    }

    public static int[] getFractionIndices(float[] fArray, float f) {
        int n;
        int[] nArray = new int[2];
        for (n = 0; n < fArray.length && fArray[n] <= f; ++n) {
        }
        if (n >= fArray.length) {
            n = fArray.length - 1;
        }
        nArray[0] = n - 1;
        nArray[1] = n;
        return nArray;
    }

    public static String stripColor(String string) {
        return COLOR_PATTERN.matcher(string).replaceAll("");
    }

    public Color getColor(Entity entity) {
        if (entity instanceof EntityPlayer && this.friendColor.getValue() && MadCat.friendManager.isFriend((EntityPlayer) entity)) {
            return Color.cyan;
        }
        return ESP2D.fade(new Color(this.colorRedValue.getValue(), this.colorGreenValue.getValue(), this.colorBlueValue.getValue()), 0, 100);
    }

    public static void newDrawRect(double d, double d2, double d3, double d4, int n) {
        double d5;
        if (d < d3) {
            d5 = d;
            d = d3;
            d3 = d5;
        }
        if (d2 < d4) {
            d5 = d2;
            d2 = d4;
            d4 = d5;
        }
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f2, f3, f4, f);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(d, d4, 0.0).endVertex();
        bufferBuilder.pos(d3, d4, 0.0).endVertex();
        bufferBuilder.pos(d3, d2, 0.0).endVertex();
        bufferBuilder.pos(d, d2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void setColor(Color color) {
        float f = (float)(color.getRGB() >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(color.getRGB() >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(color.getRGB() >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(color.getRGB() & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
    }

    public static void drawRect(double d, double d2, double d3, double d4, int n) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        ESP2D.glColor(n);
        GL11.glBegin(7);
        GL11.glVertex2d(d3, d2);
        GL11.glVertex2d(d, d2);
        GL11.glVertex2d(d, d4);
        GL11.glVertex2d(d3, d4);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    private void collectEntities() {
        collectedEntities.clear();
        List list = ESP2D.mc.world.loadedEntityList;
        int n = list.size();
        for (Object o : list) {
            Entity entity = (Entity) o;
            if (!this.isSelected(entity, false) && (!this.localPlayer.getValue() || !(entity instanceof EntityPlayerSP) || ESP2D.mc.gameSettings.thirdPersonView == 0) && (!this.droppedItems.getValue() || !(entity instanceof EntityItem)))
                continue;
            collectedEntities.add(entity);
        }
    }

    @Override
    public void onDisable() {
        collectedEntities.clear();
    }

    public static void render(int n, Runnable runnable) {
        GL11.glBegin(n);
        runnable.run();
        GL11.glEnd();
    }

    static {
        collectedEntities = new ArrayList();
        INSTANCE = new ESP2D();
        for (int i = 0; i < DISPLAY_LISTS_2D.length; ++i) {
            ESP2D.DISPLAY_LISTS_2D[i] = GL11.glGenLists(1);
        }
        GL11.glNewList(DISPLAY_LISTS_2D[0], 4864);
        ESP2D.quickDrawRect(-7.0f, 2.0f, -4.0f, 3.0f);
        ESP2D.quickDrawRect(4.0f, 2.0f, 7.0f, 3.0f);
        ESP2D.quickDrawRect(-7.0f, 0.5f, -6.0f, 3.0f);
        ESP2D.quickDrawRect(6.0f, 0.5f, 7.0f, 3.0f);
        GL11.glEndList();
        GL11.glNewList(DISPLAY_LISTS_2D[1], 4864);
        ESP2D.quickDrawRect(-7.0f, 3.0f, -4.0f, 3.3f);
        ESP2D.quickDrawRect(4.0f, 3.0f, 7.0f, 3.3f);
        ESP2D.quickDrawRect(-7.3f, 0.5f, -7.0f, 3.3f);
        ESP2D.quickDrawRect(7.0f, 0.5f, 7.3f, 3.3f);
        GL11.glEndList();
        GL11.glNewList(DISPLAY_LISTS_2D[2], 4864);
        ESP2D.quickDrawRect(4.0f, -20.0f, 7.0f, -19.0f);
        ESP2D.quickDrawRect(-7.0f, -20.0f, -4.0f, -19.0f);
        ESP2D.quickDrawRect(6.0f, -20.0f, 7.0f, -17.5f);
        ESP2D.quickDrawRect(-7.0f, -20.0f, -6.0f, -17.5f);
        GL11.glEndList();
        GL11.glNewList(DISPLAY_LISTS_2D[3], 4864);
        ESP2D.quickDrawRect(7.0f, -20.0f, 7.3f, -17.5f);
        ESP2D.quickDrawRect(-7.3f, -20.0f, -7.0f, -17.5f);
        ESP2D.quickDrawRect(4.0f, -20.3f, 7.3f, -20.0f);
        ESP2D.quickDrawRect(-7.3f, -20.3f, -4.0f, -20.0f);
        GL11.glEndList();
    }

    public boolean isSelected(Entity entity, boolean bl) {
        if (entity instanceof EntityLivingBase && entity.isEntityAlive() && entity != ESP2D.mc.player) {
            if (entity instanceof EntityPlayer) {
                if (bl) {
                    if (MadCat.friendManager.isFriend((EntityPlayer)entity)) {
                        return false;
                    }
                    if (((EntityPlayer)entity).isSpectator()) {
                        return false;
                    }
                    return !((EntityPlayer)entity).isPlayerSleeping();
                }
                return true;
            }
            return this.isMob(entity) && this.mobs.getValue() || this.isAnimal(entity) && this.animals.getValue();
        }
        return false;
    }

    private void renderItemStack(ItemStack itemStack, int n) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        NameTags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, n, -26);
        mc.getRenderItem().renderItemOverlays(NameTags.mc.fontRenderer, itemStack, n, -26);
        NameTags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    @Override
    @SubscribeEvent
    public void onRender2D(Render2DEvent render2DEvent) {
        GL11.glPushMatrix();
        this.collectEntities();
        float f = render2DEvent.partialTicks;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int n = scaledResolution.getScaleFactor();
        double d = (double)n / Math.pow(n, 2.0);
        GL11.glScaled(d, d, d);
        int n2 = this.black;
        RenderManager renderManager = mc.getRenderManager();
        EntityRenderer entityRenderer = ESP2D.mc.entityRenderer;
        boolean bl = this.outline.getValue();
        boolean bl2 = this.healthBar.getValue();
        int n3 = collectedEntities.size();
        for (Object collectedEntity : collectedEntities) {
            int n4;
            String string = null;
            EntityLivingBase entityLivingBase;
            boolean bl3;
            Entity entity = (Entity) collectedEntity;
            int n5 = this.getColor(entity).getRGB();
            if (!ESP2D.isInViewFrustrum(entity)) continue;
            double d2 = ESP2D.interpolate(entity.posX, entity.lastTickPosX, f);
            double d3 = ESP2D.interpolate(entity.posY, entity.lastTickPosY, f);
            double d4 = ESP2D.interpolate(entity.posZ, entity.lastTickPosZ, f);
            double d5 = (double) entity.width / 1.5;
            double d6 = (double) entity.height + (entity.isSneaking() ? -0.3 : 0.2);
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d2 - d5, d3, d4 - d5, d2 + d5, d3 + d6, d4 + d5);
            List<Vector3d> list = Arrays.asList(new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
            ((IEntityRenderer) ESP2D.mc.entityRenderer).invokeSetupCameraTransform(f, 0);
            Vector4d vector4d = null;
            for (Vector3d value : list) {
                Vector3d vector3d;
                Vector3d vector3d2 = value;
                vector3d2 = this.project2D(n, vector3d2.x - renderManager.viewerPosX, vector3d2.y - renderManager.viewerPosY, vector3d2.z - renderManager.viewerPosZ);
                if (vector3d2 == null || !(vector3d2.z >= 0.0) || !(vector3d2.z < 1.0)) continue;
                if (vector4d == null) {
                    vector4d = new Vector4d(vector3d2.x, vector3d2.y, vector3d2.z, 0.0);
                }
                vector4d.x = Math.min(vector3d2.x, vector4d.x);
                vector4d.y = Math.min(vector3d2.y, vector4d.y);
                vector4d.z = Math.max(vector3d2.x, vector4d.z);
                vector4d.w = Math.max(vector3d2.y, vector4d.w);
            }
            if (vector4d == null) continue;
            entityRenderer.setupOverlayRendering();
            double d7 = vector4d.x;
            double d8 = vector4d.y;
            double d9 = vector4d.z;
            double d10 = vector4d.w;
            if (bl) {
                if (this.boxMode.getValue() == Mode.Box) {
                    ESP2D.newDrawRect(d7 - 1.0, d8, d7 + 0.5, d10 + 0.5, n2);
                    ESP2D.newDrawRect(d7 - 1.0, d8 - 0.5, d9 + 0.5, d8 + 0.5 + 0.5, n2);
                    ESP2D.newDrawRect(d9 - 0.5 - 0.5, d8, d9 + 0.5, d10 + 0.5, n2);
                    ESP2D.newDrawRect(d7 - 1.0, d10 - 0.5 - 0.5, d9 + 0.5, d10 + 0.5, n2);
                    ESP2D.newDrawRect(d7 - 0.5, d8, d7 + 0.5 - 0.5, d10, n5);
                    ESP2D.newDrawRect(d7, d10 - 0.5, d9, d10, n5);
                    ESP2D.newDrawRect(d7 - 0.5, d8, d9, d8 + 0.5, n5);
                    ESP2D.newDrawRect(d9 - 0.5, d8, d9, d10, n5);
                } else {
                    ESP2D.newDrawRect(d7 + 0.5, d8, d7 - 1.0, d8 + (d10 - d8) / 4.0 + 0.5, n2);
                    ESP2D.newDrawRect(d7 - 1.0, d10, d7 + 0.5, d10 - (d10 - d8) / 4.0 - 0.5, n2);
                    ESP2D.newDrawRect(d7 - 1.0, d8 - 0.5, d7 + (d9 - d7) / 3.0 + 0.5, d8 + 1.0, n2);
                    ESP2D.newDrawRect(d9 - (d9 - d7) / 3.0 - 0.5, d8 - 0.5, d9, d8 + 1.0, n2);
                    ESP2D.newDrawRect(d9 - 1.0, d8, d9 + 0.5, d8 + (d10 - d8) / 4.0 + 0.5, n2);
                    ESP2D.newDrawRect(d9 - 1.0, d10, d9 + 0.5, d10 - (d10 - d8) / 4.0 - 0.5, n2);
                    ESP2D.newDrawRect(d7 - 1.0, d10 - 1.0, d7 + (d9 - d7) / 3.0 + 0.5, d10 + 0.5, n2);
                    ESP2D.newDrawRect(d9 - (d9 - d7) / 3.0 - 0.5, d10 - 1.0, d9 + 0.5, d10 + 0.5, n2);
                    ESP2D.newDrawRect(d7, d8, d7 - 0.5, d8 + (d10 - d8) / 4.0, n5);
                    ESP2D.newDrawRect(d7, d10, d7 - 0.5, d10 - (d10 - d8) / 4.0, n5);
                    ESP2D.newDrawRect(d7 - 0.5, d8, d7 + (d9 - d7) / 3.0, d8 + 0.5, n5);
                    ESP2D.newDrawRect(d9 - (d9 - d7) / 3.0, d8, d9, d8 + 0.5, n5);
                    ESP2D.newDrawRect(d9 - 0.5, d8, d9, d8 + (d10 - d8) / 4.0, n5);
                    ESP2D.newDrawRect(d9 - 0.5, d10, d9, d10 - (d10 - d8) / 4.0, n5);
                    ESP2D.newDrawRect(d7, d10 - 0.5, d7 + (d9 - d7) / 3.0, d10, n5);
                    ESP2D.newDrawRect(d9 - (d9 - d7) / 3.0, d10 - 0.5, d9 - 0.5, d10, n5);
                }
            }
            if (bl3 = entity instanceof EntityLivingBase) {
                entityLivingBase = (EntityLivingBase) entity;
                if (bl2) {
                    float f2 = entityLivingBase.getHealth();
                    float f3 = entityLivingBase.getMaxHealth();
                    if (this.bbtt.getValue() && entityLivingBase instanceof EntityPlayer) {
                        f2 = entityLivingBase.getHealth() + entityLivingBase.getAbsorptionAmount();
                        f3 = entityLivingBase.getMaxHealth() + 16.0f;
                    }
                    if (f2 > f3) {
                        f2 = f3;
                    }
                    double d11 = f2 / f3;
                    double d12 = (d10 - d8) * d11;
                    string = this.dFormat.format(entityLivingBase.getHealth() + entityLivingBase.getAbsorptionAmount()) + " \u00a7\u0063\u2764";
                    String string2 = (int) (entityLivingBase.getHealth() / f3 * 100.0f) + "%";
                    if (this.healthNumber.getValue() && (!this.hoverValue.getValue() || entity == ESP2D.mc.player || this.isHovering(d7, d9, d8, d10, scaledResolution))) {
                        this.drawScaledString(this.hpMode.getValue() == Mode4.Health ? string : string2, d7 - 4.0 - (double) ((float) this.renderer.getStringWidth(this.hpMode.getValue() == Mode4.Health ? string : string2) * this.fontScaleValue.getValue()), d10 - d12 - (double) ((float) ESP2D.mc.fontRenderer.FONT_HEIGHT / 2.0f * this.fontScaleValue.getValue()), this.fontScaleValue.getValue());
                    }
                    ESP2D.newDrawRect(d7 - 3.5, d8 - 0.5, d7 - 1.5, d10 + 0.5, this.backgroundColor);
                    if (f2 > 0.0f) {
                        n4 = ESP2D.getHealthColor(f2, f3).getRGB();
                        double d13 = d10 - d8;
                        if (this.hpBarMode.getValue() == Mode2.Dot && d13 >= 60.0) {
                            for (double d14 = 0.0; d14 < 10.0; d14 += 1.0) {
                                double d15 = MathHelper.clamp((double) f2 - d14 * ((double) f3 / 10.0), 0.0, (double) f3 / 10.0) / ((double) f3 / 10.0);
                                double d16 = (d13 / 10.0 - 0.5) * d15;
                                ESP2D.newDrawRect(d7 - 3.0, d10 - (d13 + 0.5) / 10.0 * d14, d7 - 2.0, d10 - (d13 + 0.5) / 10.0 * d14 - d16, n4);
                            }
                        } else {
                            ESP2D.newDrawRect(d7 - 3.0, d10, d7 - 2.0, d10 - d12, n4);
                        }
                    }
                }
            }
            if (bl3 && this.tagsValue.getValue()) {
                entityLivingBase = (EntityLivingBase) entity;
                String string3 = this.clearNameValue.getValue() ? entityLivingBase.getName() : (string = entityLivingBase.getDisplayName().getFormattedText());
                if (this.friendColor.getValue() && MadCat.friendManager.isFriend(entityLivingBase.getName())) {
                    string = "§b" + string;
                }
                if (this.tagsBGValue.getValue()) {
                    ESP2D.newDrawRect(d7 + (d9 - d7) / 2.0 - (double) (((float) ESP2D.mc.fontRenderer.getStringWidth(string) / 2.0f + 2.0f) * this.fontScaleValue.getValue()), d8 - 1.0 - (double) (((float) ESP2D.mc.fontRenderer.FONT_HEIGHT + 2.0f) * this.fontScaleValue.getValue()), d7 + (d9 - d7) / 2.0 + (double) (((float) ESP2D.mc.fontRenderer.getStringWidth(string) / 2.0f + 2.0f) * this.fontScaleValue.getValue()), d8 - 1.0 + (double) (2.0f * this.fontScaleValue.getValue()), -1610612736);
                }
                this.drawScaledCenteredString(string, d7 + (d9 - d7) / 2.0, d8 - 1.0 - (double) ((float) ESP2D.mc.fontRenderer.FONT_HEIGHT * this.fontScaleValue.getValue()), this.fontScaleValue.getValue());
            }
            if (!this.armorBar.getValue() || !(entity instanceof EntityPlayer) || !bl3) continue;
            entityLivingBase = (EntityLivingBase) entity;
            double d17 = (d10 - d8) / 4.0;
            for (n4 = 4; n4 > 0; --n4) {
                ItemStack itemStack = entityLivingBase.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (n4 == 3) {
                    itemStack = entityLivingBase.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                }
                if (n4 == 2) {
                    itemStack = entityLivingBase.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                }
                if (n4 == 1) {
                    itemStack = entityLivingBase.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                }
                double d18 = d17 + 0.25;
                if (itemStack == null || itemStack.getItem() == null) continue;
                ESP2D.newDrawRect(d9 + 1.5, d10 + 0.5 - d18 * (double) n4, d9 + 3.5, d10 + 0.5 - d18 * (double) (n4 - 1), new Color(0, 0, 0, 120).getRGB());
                ESP2D.newDrawRect(d9 + 2.0, d10 + 0.5 - d18 * (double) (n4 - 1) - 0.25, d9 + 3.0, d10 + 0.5 - d18 * (double) (n4 - 1) - 0.25 - (d17 - 0.25) * MathHelper.clamp((double) InventoryUtil.getItemDurability(itemStack) / (double) itemStack.getMaxDamage(), 0.0, 1.0), new Color(0, 255, 255).getRGB());
            }
        }
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
        GlStateManager.resetColor();
        entityRenderer.setupOverlayRendering();
    }

    public static ESP2D INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ESP2D();
        }
        return INSTANCE;
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return ESP2D.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public ESP2D() {
        super("ESP", "fixed", Module.Category.RENDER);
        this.boxMode = this.register(new Setting<>("Mode", Mode.Corners));
        this.healthBar = this.register(new Setting<>("Health-bar", true));
        this.hpBarMode = this.register(new Setting<>("HBar-Mode", Mode2.Dot));
        this.healthNumber = this.register(new Setting<>("HealthNumber", true));
        this.hpMode = this.register(new Setting<>("HP-Mode", Mode4.Health));
        this.hoverValue = this.register(new Setting<>("Details-HoverOnly", false));
        this.tagsValue = this.register(new Setting<>("Tags", true));
        this.tagsBGValue = this.register(new Setting<>("Tags-Background", true));
        this.armorBar = this.register(new Setting<>("ArmorBar", true));
        this.outlineFont = this.register(new Setting<>("OutlineFont", true));
        this.clearNameValue = this.register(new Setting<>("Use-Clear-Name", false));
        this.localPlayer = this.register(new Setting<>("Local-Player", true));
        this.friendColor = this.register(new Setting<>("FriendColor", true));
        this.mobs = this.register(new Setting<>("Mobs", false));
        this.animals = this.register(new Setting<>("Animasl", false));
        this.droppedItems = this.register(new Setting<>("Dropped-Items", false));
        this.colorModeValue = this.register(new Setting<>("Color", Mode5.Custom));
        this.colorRedValue = this.register(new Setting<>("Red", 255, 0, 255));
        this.colorGreenValue = this.register(new Setting<>("Green", 255, 0, 255));
        this.colorBlueValue = this.register(new Setting<>("Blue", 255, 0, 255));
        Setting<Float> saturationValue = this.register(new Setting<>("Saturation", 1.0f, 0.0f, 1.0f));
        Setting<Float> brightnessValue = this.register(new Setting<>("Brightness", 1.0f, 0.0f, 1.0f));
        Setting<Integer> mixerSecondsValue = this.register(new Setting<>("Seconds", 2, 1, 10));
        this.fontScaleValue = this.register(new Setting<>("Font-Scale", 0.5f, 0.0f, 1.0f));
        this.dFormat = new DecimalFormat("0.0");
        this.viewport = GLAllocation.createDirectIntBuffer(16);
        this.modelview = GLAllocation.createDirectFloatBuffer(16);
        this.projection = GLAllocation.createDirectFloatBuffer(16);
        this.vector = GLAllocation.createDirectFloatBuffer(4);
        this.backgroundColor = new Color(0, 0, 0, 120).getRGB();
        this.black = Color.BLACK.getRGB();
        this.setInstance();
    }

    public static Color blendColors(float[] fArray, Color[] colorArray, float f) {
        if (fArray.length == colorArray.length) {
            int[] nArray = ESP2D.getFractionIndices(fArray, f);
            float[] fArray2 = new float[]{fArray[nArray[0]], fArray[nArray[1]]};
            Color[] colorArray2 = new Color[]{colorArray[nArray[0]], colorArray[nArray[1]]};
            float f2 = fArray2[1] - fArray2[0];
            float f3 = f - fArray2[0];
            float f4 = f3 / f2;
            return ESP2D.blend(colorArray2[0], colorArray2[1], 1.0f - f4);
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    public static void quickDrawRect(float f, float f2, float f3, float f4) {
        GL11.glBegin(7);
        GL11.glVertex2d(f3, f2);
        GL11.glVertex2d(f, f2);
        GL11.glVertex2d(f, f4);
        GL11.glVertex2d(f3, f4);
        GL11.glEnd();
    }

    public void drawOutlineStringWithoutGL(String string, float f, float f2, int n, FontRenderer fontRenderer) {
        fontRenderer.drawString(ESP2D.stripColor(string), (int)(f * 2.0f - 1.0f), (int)(f2 * 2.0f), Color.BLACK.getRGB());
        fontRenderer.drawString(ESP2D.stripColor(string), (int)(f * 2.0f + 1.0f), (int)(f2 * 2.0f), Color.BLACK.getRGB());
        fontRenderer.drawString(ESP2D.stripColor(string), (int)(f * 2.0f), (int)(f2 * 2.0f - 1.0f), Color.BLACK.getRGB());
        fontRenderer.drawString(ESP2D.stripColor(string), (int)(f * 2.0f), (int)(f2 * 2.0f + 1.0f), Color.BLACK.getRGB());
        fontRenderer.drawString(string, (int)(f * 2.0f), (int)(f2 * 2.0f), n);
    }

    private static boolean isInViewFrustrum(AxisAlignedBB axisAlignedBB) {
        Entity entity = mc.getRenderViewEntity();
        frustrum.setPosition(entity.posX, entity.posY, entity.posZ);
        return frustrum.isBoundingBoxInFrustum(axisAlignedBB);
    }

    public static Color getHealthColor(float f, float f2) {
        float[] fArray = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colorArray = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
        float f3 = f / f2;
        return ESP2D.blendColors(fArray, colorArray, f3).brighter();
    }

    public static Color slowlyRainbow(long l, int n, float f, float f2) {
        Color color = new Color(Color.HSBtoRGB(((float)l + (float)n * -3000000.0f) / 2.0f / 1.0E9f, f, f2));
        return new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    private boolean isHovering(double d, double d2, double d3, double d4, ScaledResolution scaledResolution) {
        return (double)scaledResolution.getScaledWidth() / 2.0 >= d && (double)scaledResolution.getScaledWidth() / 2.0 < d2 && (double)scaledResolution.getScaledHeight() / 2.0 >= d3 && (double)scaledResolution.getScaledHeight() / 2.0 < d4;
    }

    public static void glColor(int n) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.color(f2, f3, f4, f);
    }

    public boolean isMob(Entity entity) {
        return entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityGhast || entity instanceof EntityDragon;
    }

    public static void color(double d, double d2, double d3, double d4) {
        GL11.glColor4d(d, d2, d3, d4);
    }

    public static Color blend(Color color, Color color2, double d) {
        float f = (float)d;
        float f2 = 1.0f - f;
        float[] fArray = color.getColorComponents(new float[3]);
        float[] fArray2 = color2.getColorComponents(new float[3]);
        float f3 = fArray[0] * f + fArray2[0] * f2;
        float f4 = fArray[1] * f + fArray2[1] * f2;
        float f5 = fArray[2] * f + fArray2[2] * f2;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        } else if (f3 > 255.0f) {
            f3 = 255.0f;
        }
        if (f4 < 0.0f) {
            f4 = 0.0f;
        } else if (f4 > 255.0f) {
            f4 = 255.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        } else if (f5 > 255.0f) {
            f5 = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(f3, f4, f5);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        return color3;
    }

    public static Color fade(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)n / (float)n2 * 2.0f) % 2.0f - 1.0f);
        f = 0.5f + 0.5f * f;
        fArray[2] = f % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        ESP2D.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    private void drawScaledCenteredString(String string, double d, double d2, double d3) {
        this.drawScaledString(string, d - (double)((float)ESP2D.mc.fontRenderer.getStringWidth(string) / 2.0f) * d3, d2, d3);
    }

    public static int getRainbowOpaque(int n, float f, float f2, int n2) {
        float f3 = (float)((System.currentTimeMillis() + (long)n2) % (long)(n * 1000)) / (float)(n * 1000);
        return Color.HSBtoRGB(f3, f, f2);
    }

    public static double interpolate(double d, double d2, double d3) {
        return d2 + (d - d2) * d3;
    }

    public enum Mode5 {
        Custom,
        Slowly,
        AnotherRainbow

    }

    public enum Mode2 {
        Dot,
        Line

    }

    public enum Mode4 {
        Health,
        Percent

    }

    public enum Mode {
        Box,
        Corners

    }
}

