package me.madcat.features.modules.render;

import java.awt.Color;
import java.util.Objects;
import me.madcat.MadCat;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.DamageUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.TextUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class NameTags
extends Module {
    private static NameTags INSTANCE;
    private final Setting<Boolean> rect = this.register(new Setting<>("Rectangle", true));
    private final Setting<Boolean> armor = this.register(new Setting<>("Armor", true));
    private final Setting<Boolean> reversed = this.register(new Setting<Object>("ArmorReversed", Boolean.FALSE, this::new0));
    private final Setting<Boolean> health = this.register(new Setting<>("Health", true));
    private final Setting<Boolean> ping = this.register(new Setting<>("Ping", true));
    private final Setting<Boolean> gamemode = this.register(new Setting<>("Gamemode", true));
    private final Setting<Boolean> entityID = this.register(new Setting<>("EntityID", false));
    private final Setting<Boolean> heldStackName = this.register(new Setting<>("StackName", true));
    private final Setting<Boolean> max = this.register(new Setting<>("Max", true));
    private final Setting<Boolean> maxText = this.register(new Setting<Object>("NoMaxText", Boolean.TRUE, this::new1));
    private final Setting<Integer> Mred = this.register(new Setting<Object>("Max-Red", 178, 0, 255, this::new2));
    private final Setting<Integer> Mgreen = this.register(new Setting<Object>("Max-Green", 52, 0, 255, this::new3));
    private final Setting<Integer> Mblue = this.register(new Setting<Object>("Max-Blue", 57, 0, 255, this::new4));
    private final Setting<Float> size = this.register(new Setting<>("Size", 2.5f, 0.1f, 15.0f));
    private final Setting<Boolean> scaleing = this.register(new Setting<>("Scale", true));
    private final Setting<Boolean> smartScale = this.register(new Setting<Object>("SmartScale", Boolean.TRUE, this::new5));
    private final Setting<Float> factor = this.register(new Setting<Object>("Factor", 0.3f, 0.1f, 1.0f, this::new6));
    private final Setting<Integer> textAlpha = this.register(new Setting<>("Text-Alpha", 255, 0, 255));
    private final Setting<Boolean> NCRainbow = this.register(new Setting<>("Text-Rainbow", false));
    private final Setting<Integer> NCred = this.register(new Setting<>("Text-Red", 255, 0, 255));
    private final Setting<Integer> NCgreen = this.register(new Setting<>("Text-Green", 255, 0, 255));
    private final Setting<Integer> NCblue = this.register(new Setting<>("Text-Blue", 255, 0, 255));
    private final Setting<Boolean> outline = this.register(new Setting<>("Outline", false));
    private final Setting<Boolean> ORainbow = this.register(new Setting<Object>("Outline-Rainbow", Boolean.FALSE, this::new7));
    private final Setting<Float> Owidth = this.register(new Setting<Object>("Outline-Width", 1.3f, 0.0f, 5.0f, this::new8));
    private final Setting<Integer> Ored = this.register(new Setting<Object>("Outline-Red", 255, 0, 255, this::new9));
    private final Setting<Integer> Ogreen = this.register(new Setting<Object>("Outline-Green", 255, 0, 255, this::new10));
    private final Setting<Integer> Oblue = this.register(new Setting<Object>("Outline-Blue", 255, 0, 255, this::new11));
    private final Setting<Boolean> friendcolor = this.register(new Setting<>("FriendColor", true));
    private final Setting<Boolean> FCRainbow = this.register(new Setting<Object>("Friend-Rainbow", Boolean.FALSE, this::new12));
    private final Setting<Integer> FCred = this.register(new Setting<Object>("Friend-Red", 0, 0, 255, this::new13));
    private final Setting<Integer> FCgreen = this.register(new Setting<Object>("Friend-Green", 213, 0, 255, this::new14));
    private final Setting<Integer> FCblue = this.register(new Setting<Object>("Friend-Blue", 255, 0, 255, this::new15));
    private final Setting<Boolean> FORainbow = this.register(new Setting<Object>("FriendOutline-Rainbow", Boolean.FALSE, this::new16));
    private final Setting<Integer> FOred = this.register(new Setting<Object>("FriendOutline-Red", 0, 0, 255, this::new17));
    private final Setting<Integer> FOgreen = this.register(new Setting<Object>("FriendOutline-Green", 213, 0, 255, this::new18));
    private final Setting<Integer> FOblue = this.register(new Setting<Object>("FriendOutline-Blue", 255, 0, 255, this::new19));
    private final Setting<Boolean> sneak = this.register(new Setting<>("Sneak", Boolean.TRUE));
    private final Setting<Boolean> SCRainbow = this.register(new Setting<Object>("Sneak-Rainbow", Boolean.FALSE, this::new20));
    private final Setting<Integer> SCred = this.register(new Setting<Object>("Sneak-Red", 245, 0, 255, this::new21));
    private final Setting<Integer> SCgreen = this.register(new Setting<Object>("Sneak-Green", 0, 0, 255, this::new22));
    private final Setting<Integer> SCblue = this.register(new Setting<Object>("Sneak-Blue", 122, 0, 255, this::new23));
    private final Setting<Boolean> SORainbow = this.register(new Setting<Object>("SneakOutline-Rainbow", Boolean.FALSE, this::new24));
    private final Setting<Integer> SOred = this.register(new Setting<Object>("SneakOutline-Red", 245, 0, 255, this::new25));
    private final Setting<Integer> SOgreen = this.register(new Setting<Object>("SneakOutline-Green", 0, 0, 255, this::new26));
    private final Setting<Integer> SOblue = this.register(new Setting<Object>("SneakOutline-Blue", 122, 0, 255, this::new27));
    private final Setting<Boolean> invisibles = this.register(new Setting<>("Invisibles", Boolean.TRUE));
    private final Setting<Boolean> ICRainbow = this.register(new Setting<Object>("Invisible-Rainbow", Boolean.FALSE, this::new28));
    private final Setting<Integer> ICred = this.register(new Setting<Object>("Invisible-Red", 148, 0, 255, this::new29));
    private final Setting<Integer> ICgreen = this.register(new Setting<Object>("Invisible-Green", 148, 0, 255, this::new30));
    private final Setting<Integer> ICblue = this.register(new Setting<Object>("Invisible-Blue", 148, 0, 255, this::new31));
    private final Setting<Boolean> IORainbow = this.register(new Setting<Object>("InvisibleOutline-Rainbow", Boolean.FALSE, this::new32));
    private final Setting<Integer> IOred = this.register(new Setting<Object>("InvisibleOutline-Red", 148, 0, 255, this::new33));
    private final Setting<Integer> IOgreen = this.register(new Setting<Object>("InvisibleOutline-Green", 148, 0, 255, this::new34));
    private final Setting<Integer> IOblue = this.register(new Setting<Object>("InvisibleOutline-Blue", 148, 0, 255, this::new35));
    static final boolean $assertionsDisabled;

    public NameTags() {
        super("NameTags", "Renders info about the player on a NameTag", Module.Category.RENDER);
    }

    public static NameTags INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NameTags();
        }
        return INSTANCE;
    }

    public static int toHex(int n, int n2, int n3) {
        return 0xFF000000 | (n & 0xFF) << 16 | (n2 & 0xFF) << 8 | n3 & 0xFF;
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        for (EntityPlayer entityPlayer : NameTags.mc.world.playerEntities) {
            if (entityPlayer == null || entityPlayer.equals(NameTags.mc.player) || !entityPlayer.isEntityAlive() || entityPlayer.isInvisible() && !this.invisibles.getValue()) continue;
            double d = this.interpolate(entityPlayer.lastTickPosX, entityPlayer.posX, render3DEvent.getPartialTicks()) - NameTags.mc.getRenderManager().viewerPosX;
            double d2 = this.interpolate(entityPlayer.lastTickPosY, entityPlayer.posY, render3DEvent.getPartialTicks()) - NameTags.mc.getRenderManager().viewerPosY;
            double d3 = this.interpolate(entityPlayer.lastTickPosZ, entityPlayer.posZ, render3DEvent.getPartialTicks()) - NameTags.mc.getRenderManager().viewerPosZ;
            this.renderNameTag(entityPlayer, d, d2, d3, render3DEvent.getPartialTicks());
        }
    }

    private void renderNameTag(EntityPlayer entityPlayer, double d, double d2, double d3, float f) {
        double d4 = d2;
        d4 += entityPlayer.isSneaking() ? 0.5 : 0.7;
        Entity entity = mc.getRenderViewEntity();
        if (!$assertionsDisabled && entity == null) {
            throw new AssertionError();
        }
        double d5 = entity.posX;
        double d6 = entity.posY;
        double d7 = entity.posZ;
        entity.posX = this.interpolate(entity.prevPosX, entity.posX, f);
        entity.posY = this.interpolate(entity.prevPosY, entity.posY, f);
        entity.posZ = this.interpolate(entity.prevPosZ, entity.posZ, f);
        String string = this.getDisplayTag(entityPlayer);
        double d8 = entity.getDistance(d + NameTags.mc.getRenderManager().viewerPosX, d2 + NameTags.mc.getRenderManager().viewerPosY, d3 + NameTags.mc.getRenderManager().viewerPosZ);
        int n = this.renderer.getStringWidth(string) / 2;
        double d9 = (0.0018 + (double) this.size.getValue() * (d8 * (double) this.factor.getValue())) / 1000.0;
        if (d8 <= 6.0 && this.smartScale.getValue()) {
            d9 = (0.0018 + (double)(this.size.getValue() + 2.0f) * (d8 * (double) this.factor.getValue())) / 1000.0;
        }
        if (d8 <= 4.0 && this.smartScale.getValue()) {
            d9 = (0.0018 + (double)(this.size.getValue() + 4.0f) * (d8 * (double) this.factor.getValue())) / 1000.0;
        }
        if (!this.scaleing.getValue()) {
            d9 = (double) this.size.getValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)d, (float)d4 + 1.4f, (float)d3);
        GlStateManager.rotate(-NameTags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(NameTags.mc.getRenderManager().playerViewX, NameTags.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-d9, -d9, d9);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.getValue()) {
            this.drawRect(-n - 2, -(NameTags.mc.fontRenderer.FONT_HEIGHT + 1), (float)n + 2.0f, 1.5f, 0x55000000);
        } else if (!this.outline.getValue()) {
            this.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0x55000000);
        }
        if (this.outline.getValue()) {
            this.drawOutlineRect(-n - 2, -(NameTags.mc.fontRenderer.FONT_HEIGHT + 1), (float)n + 2.0f, 1.5f, this.getOutlineColor(entityPlayer));
        }
        GlStateManager.disableBlend();
        ItemStack itemStack = entityPlayer.getHeldItemMainhand().copy();
        if (itemStack.hasEffect()) {
            if (!(itemStack.getItem() instanceof ItemTool)) {
                itemStack.getItem();
            }
        }
        if (this.heldStackName.getValue() && !itemStack.isEmpty() && itemStack.getItem() != Items.AIR) {
            String string2 = itemStack.getDisplayName();
            int n2 = this.renderer.getStringWidth(string2) / 2;
            GL11.glPushMatrix();
            GL11.glScalef(0.75f, 0.75f, 0.0f);
            this.renderer.drawStringWithShadow(string2, -n2, -(this.getBiggestArmorTag(entityPlayer) + 20.0f), -1);
            GL11.glScalef(1.5f, 1.5f, 1.0f);
            GL11.glPopMatrix();
        }
        if (this.armor.getValue()) {
            GlStateManager.pushMatrix();
            int n3 = -6;
            for (ItemStack itemStack2 : entityPlayer.inventory.armorInventory) {
                if (itemStack2 == null) continue;
                n3 -= 8;
            }
            n3 -= 8;
            ItemStack iterator = entityPlayer.getHeldItemOffhand().copy();
            if (iterator.hasEffect()) {
                if (!(iterator.getItem() instanceof ItemTool)) {
                    iterator.getItem();
                }
            }
            this.renderItemStack(iterator, n3);
            n3 += 16;
            if (this.reversed.getValue()) {
                for (int i = 0; i <= 3; ++i) {
                    ItemStack itemStack3 = entityPlayer.inventory.armorInventory.get(i);
                    if (itemStack3.getItem() == Items.AIR) continue;
                    itemStack3.copy();
                    this.renderItemStack(itemStack3, n3);
                    n3 += 16;
                }
            } else {
                for (int i = 3; i >= 0; --i) {
                    ItemStack itemStack4 = entityPlayer.inventory.armorInventory.get(i);
                    if (itemStack4.getItem() == Items.AIR) continue;
                    itemStack4.copy();
                    this.renderItemStack(itemStack4, n3);
                    n3 += 16;
                }
            }
            this.renderItemStack(itemStack, n3);
            GlStateManager.popMatrix();
        }
        this.renderer.drawStringWithShadow(string, -n, -(this.renderer.getFontHeight() - 1), this.getDisplayColor(entityPlayer));
        entity.posX = d5;
        entity.posY = d6;
        entity.posZ = d7;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private int getDisplayColor(EntityPlayer entityPlayer) {
        int n = ColorUtil.toRGBA(this.NCred.getValue(), this.NCgreen.getValue(), this.NCblue.getValue(), this.textAlpha.getValue());
        if (MadCat.friendManager.isFriend(entityPlayer) && this.friendcolor.getValue()) {
            return ColorUtil.toRGBA(this.FCred.getValue(), this.FCgreen.getValue(), this.FCblue.getValue(), this.textAlpha.getValue());
        }
        if (entityPlayer.isInvisible() && this.invisibles.getValue()) {
            n = ColorUtil.toRGBA(this.ICred.getValue(), this.ICgreen.getValue(), this.ICblue.getValue(), this.textAlpha.getValue());
        } else if (entityPlayer.isSneaking() && this.sneak.getValue()) {
            n = ColorUtil.toRGBA(this.SCred.getValue(), this.SCgreen.getValue(), this.SCblue.getValue(), this.textAlpha.getValue());
        }
        return n;
    }

    private int getOutlineColor(EntityPlayer entityPlayer) {
        int n = NameTags.toHex(this.Ored.getValue(), this.Ogreen.getValue(), this.Oblue.getValue());
        if (MadCat.friendManager.isFriend(entityPlayer)) {
            n = NameTags.toHex(this.FOred.getValue(), this.FOgreen.getValue(), this.FOblue.getValue());
        } else if (entityPlayer.isInvisible() && this.invisibles.getValue()) {
            n = NameTags.toHex(this.IOred.getValue(), this.IOgreen.getValue(), this.IOblue.getValue());
        } else if (entityPlayer.isSneaking() && this.sneak.getValue()) {
            n = NameTags.toHex(this.SOred.getValue(), this.SOgreen.getValue(), this.SOblue.getValue());
        }
        return n;
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
        this.renderEnchantmentText(itemStack, n);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(ItemStack itemStack, int n) {
        Object object;
        int n2;
        NBTTagList nBTTagList;
        int n3 = -34;
        if (itemStack.getItem() == Items.GOLDEN_APPLE && itemStack.hasEffect()) {
            this.renderer.drawStringWithShadow("god", n * 2, n3, -3977919);
            n3 -= 8;
        }
        if ((nBTTagList = itemStack.getEnchantmentTagList()).tagCount() > 2 && this.max.getValue()) {
            if (this.maxText.getValue()) {
                this.renderer.drawStringWithShadow("", n * 2, n3, NameTags.toHex(this.Mred.getValue(), this.Mgreen.getValue(), this.Mblue.getValue()));
            } else {
                this.renderer.drawStringWithShadow("max", n * 2, n3, NameTags.toHex(this.Mred.getValue(), this.Mgreen.getValue(), this.Mblue.getValue()));
            }
            n3 -= 8;
        } else {
            for (n2 = 0; n2 < nBTTagList.tagCount(); ++n2) {
                short s = nBTTagList.getCompoundTagAt(n2).getShort("id");
                short s2 = nBTTagList.getCompoundTagAt(n2).getShort("lvl");
                object = Enchantment.getEnchantmentByID(s);
                if (object == null) continue;
                String string = ((Enchantment) object).isCurse() ? TextFormatting.RED + ((Enchantment) object).getTranslatedName(s2).substring(0, 4).toLowerCase() : ((Enchantment) object).getTranslatedName(s2).substring(0, 2).toLowerCase();
                string = string + s2;
                this.renderer.drawStringWithShadow(string, n * 2, n3, -1);
                n3 -= 8;
            }
        }
        if (DamageUtil.hasDurability(itemStack)) {
            float f = ((float)itemStack.getMaxDamage() - (float)itemStack.getItemDamage()) / (float)itemStack.getMaxDamage();
            float f2 = 1.0f - f;
            n2 = 100 - (int)(f2 * 100.0f);
            object = n2 >= 60 ? TextUtil.GREEN : (n2 >= 25 ? TextUtil.YELLOW : TextUtil.RED);
            this.renderer.drawStringWithShadow((String)object + n2 + "%", n * 2, n3, -1);
        }
    }

    private float getBiggestArmorTag(EntityPlayer entityPlayer) {
        ItemStack itemStack;
        Enchantment enchantment;
        short s;
        int n;
        float f = 0.0f;
        boolean bl = false;
        for (ItemStack itemStack2 : entityPlayer.inventory.armorInventory) {
            float f2 = 0.0f;
            if (itemStack2 != null) {
                NBTTagList nBTTagList = itemStack2.getEnchantmentTagList();
                for (n = 0; n < nBTTagList.tagCount(); ++n) {
                    s = nBTTagList.getCompoundTagAt(n).getShort("id");
                    enchantment = Enchantment.getEnchantmentByID(s);
                    if (enchantment == null) continue;
                    f2 += 8.0f;
                    bl = true;
                }
            }
            if (!(f2 > f)) continue;
            f = f2;
        }
        if ((itemStack = entityPlayer.getHeldItemOffhand().copy()).hasEffect()) {
            float f4 = 0.0f;
            NBTTagList nBTTagList = itemStack.getEnchantmentTagList();
            for (n = 0; n < nBTTagList.tagCount(); ++n) {
                short s2 = nBTTagList.getCompoundTagAt(n).getShort("id");
                enchantment = Enchantment.getEnchantmentByID(s2);
                if (enchantment == null) continue;
                f4 += 8.0f;
                bl = true;
            }
            if (f4 > f) {
                f = f4;
            }
        }
        return (float)(bl ? 0 : 20) + f;
    }

    private String getDisplayTag(EntityPlayer entityPlayer) {
        float f;
        String string = entityPlayer.getDisplayName().getFormattedText();
        if (string.contains(mc.getSession().getUsername())) {
            string = "You";
        }
        String string2 = (f = EntityUtil.getHealth(entityPlayer)) > 18.0f ? TextUtil.GREEN : (f > 16.0f ? TextUtil.DARK_GREEN : (f > 12.0f ? TextUtil.YELLOW : (f > 8.0f ? TextUtil.RED : TextUtil.DARK_RED)));
        String string3 = "";
        if (this.ping.getValue()) {
            try {
                int n = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime();
                string3 = string3 + n + "ms ";
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        String string4 = "";
        if (this.entityID.getValue()) {
            string4 = string4 + "ID: " + entityPlayer.getEntityId() + " ";
        }
        String string5 = "";
        if (this.gamemode.getValue()) {
            String string6 = entityPlayer.isCreative() ? string5 + "[C] " : (string5 = entityPlayer.isSpectator() || entityPlayer.isInvisible() ? string5 + "[I] " : string5 + "[S] ");
        }
        if (this.health.getValue()) {
            string = Math.floor(f) == (double)f ? string + string2 + " " + (f > 0.0f ? Integer.valueOf((int)Math.floor(f)) : "dead") : string + string2 + " " + (f > 0.0f ? Integer.valueOf((int)f) : "dead");
        }
        return " " + string3 + string4 + string5 + string + " ";
    }

    private double interpolate(double d, double d2, float f) {
        return d + (d2 - d) * (double)f;
    }

    public void drawOutlineRect(float f, float f2, float f3, float f4, int n) {
        float f5 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(this.Owidth.getValue());
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferBuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(f, f4, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f3, f4, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f3, f2, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f, f2, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void drawRect(float f, float f2, float f3, float f4, int n) {
        float f5 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(this.Owidth.getValue());
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(f, f4, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f3, f4, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f3, f2, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f, f2, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.ORainbow.getValue()) {
            this.OutlineRainbow();
        }
        if (this.NCRainbow.getValue()) {
            this.TextRainbow();
        }
        if (this.FCRainbow.getValue()) {
            this.FriendRainbow();
        }
        if (this.SCRainbow.getValue()) {
            this.SneakColorRainbow();
        }
        if (this.ICRainbow.getValue()) {
            this.InvisibleRainbow();
        }
        if (this.FORainbow.getValue()) {
            this.FriendOutlineRainbow();
        }
        if (this.IORainbow.getValue()) {
            this.InvisibleOutlineRainbow();
        }
        if (this.SORainbow.getValue()) {
            this.SneakOutlineRainbow();
        }
    }

    public void OutlineRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.Ored.setValue(n >> 16 & 0xFF);
        this.Ogreen.setValue(n >> 8 & 0xFF);
        this.Oblue.setValue(n & 0xFF);
    }

    public void TextRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.NCred.setValue(n >> 16 & 0xFF);
        this.NCgreen.setValue(n >> 8 & 0xFF);
        this.NCblue.setValue(n & 0xFF);
    }

    public void FriendRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.FCred.setValue(n >> 16 & 0xFF);
        this.FCgreen.setValue(n >> 8 & 0xFF);
        this.FCblue.setValue(n & 0xFF);
    }

    public void SneakColorRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.SCred.setValue(n >> 16 & 0xFF);
        this.SCgreen.setValue(n >> 8 & 0xFF);
        this.SCblue.setValue(n & 0xFF);
    }

    public void InvisibleRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.ICred.setValue(n >> 16 & 0xFF);
        this.ICgreen.setValue(n >> 8 & 0xFF);
        this.ICblue.setValue(n & 0xFF);
    }

    public void InvisibleOutlineRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.IOred.setValue(n >> 16 & 0xFF);
        this.IOgreen.setValue(n >> 8 & 0xFF);
        this.IOblue.setValue(n & 0xFF);
    }

    public void FriendOutlineRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.FOred.setValue(n >> 16 & 0xFF);
        this.FOgreen.setValue(n >> 8 & 0xFF);
        this.FOblue.setValue(n & 0xFF);
    }

    public void SneakOutlineRainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        this.SOred.setValue(n >> 16 & 0xFF);
        this.SOgreen.setValue(n >> 8 & 0xFF);
        this.SOblue.setValue(n & 0xFF);
    }

    private boolean new35(Object object) {
        return this.outline.getValue() && this.invisibles.getValue();
    }

    private boolean new34(Object object) {
        return this.outline.getValue() && this.invisibles.getValue();
    }

    private boolean new33(Object object) {
        return this.outline.getValue() && this.invisibles.getValue();
    }

    private boolean new32(Object object) {
        return this.outline.getValue() && this.invisibles.getValue();
    }

    private boolean new31(Object object) {
        return this.invisibles.getValue();
    }

    private boolean new30(Object object) {
        return this.invisibles.getValue();
    }

    private boolean new29(Object object) {
        return this.invisibles.getValue();
    }

    private boolean new28(Object object) {
        return this.invisibles.getValue();
    }

    private boolean new27(Object object) {
        return this.outline.getValue() && this.sneak.getValue();
    }

    private boolean new26(Object object) {
        return this.outline.getValue() && this.sneak.getValue();
    }

    private boolean new25(Object object) {
        return this.outline.getValue() && this.sneak.getValue();
    }

    private boolean new24(Object object) {
        return this.outline.getValue() && this.sneak.getValue();
    }

    private boolean new23(Object object) {
        return this.sneak.getValue();
    }

    private boolean new22(Object object) {
        return this.sneak.getValue();
    }

    private boolean new21(Object object) {
        return this.sneak.getValue();
    }

    private boolean new20(Object object) {
        return this.sneak.getValue();
    }

    private boolean new19(Object object) {
        return this.outline.getValue() && this.friendcolor.getValue();
    }

    private boolean new18(Object object) {
        return this.outline.getValue() && this.friendcolor.getValue();
    }

    private boolean new17(Object object) {
        return this.outline.getValue() && this.friendcolor.getValue();
    }

    private boolean new16(Object object) {
        return this.outline.getValue() && this.friendcolor.getValue();
    }

    private boolean new15(Object object) {
        return this.friendcolor.getValue();
    }

    private boolean new14(Object object) {
        return this.friendcolor.getValue();
    }

    private boolean new13(Object object) {
        return this.friendcolor.getValue();
    }

    private boolean new12(Object object) {
        return this.friendcolor.getValue();
    }

    private boolean new11(Object object) {
        return this.outline.getValue();
    }

    private boolean new10(Object object) {
        return this.outline.getValue();
    }

    private boolean new9(Object object) {
        return this.outline.getValue();
    }

    private boolean new8(Object object) {
        return this.outline.getValue();
    }

    private boolean new7(Object object) {
        return this.outline.getValue();
    }

    private boolean new6(Object object) {
        return this.scaleing.getValue();
    }

    private boolean new5(Object object) {
        return this.scaleing.getValue();
    }

    private boolean new4(Object object) {
        return this.max.getValue() && !this.maxText.getValue();
    }

    private boolean new3(Object object) {
        return this.max.getValue() && !this.maxText.getValue();
    }

    private boolean new2(Object object) {
        return this.max.getValue() && !this.maxText.getValue();
    }

    private boolean new1(Object object) {
        return this.max.getValue();
    }

    private boolean new0(Object object) {
        return this.armor.getValue();
    }

    static {
        $assertionsDisabled = !NameTags.class.desiredAssertionStatus();
        INSTANCE = new NameTags();
    }
}

