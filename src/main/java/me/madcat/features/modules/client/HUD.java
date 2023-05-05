package me.madcat.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.Objects;
import me.madcat.MadCat;
import me.madcat.event.events.Render2DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.exploit.Phase;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.MathUtil;
import me.madcat.util.RenderUtil;
import me.madcat.util.TextUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class HUD
extends Module {
    private static final ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static final ItemStack Crystal = new ItemStack(Items.END_CRYSTAL);
    private static final ItemStack xp = new ItemStack(Items.EXPERIENCE_BOTTLE);
    private static final ItemStack ap = new ItemStack(Items.GOLDEN_APPLE);
    private static final ItemStack obs = new ItemStack(Blocks.OBSIDIAN);
    private static final double HALF_PI = 1.5707963267948966;
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static HUD INSTANCE = new HUD();
    private final Setting<Page> page = this.register(new Setting<>("Page", Page.GLOBAL));
    final Setting<Boolean> waterMark = this.register(new Setting<>("Watermark", Boolean.FALSE, this::new0));
    public final Setting<Boolean> watermark2 = this.register(new Setting<>("SkeetMode", Boolean.FALSE, this::new1));
    public final Setting<Integer> compactX = this.register(new Setting<Object>("X", 20, 0, 1080, this::new2));
    public final Setting<Integer> compactY = this.register(new Setting<>("Y", 20, 0, 530, this::new3));
    public final Setting<Integer> alpha = this.register(new Setting<>("Alpha", 50, 0, 255, this::new4));
    public final Setting<Boolean> potionIcons = this.register(new Setting<>("NoPotionIcons", Boolean.FALSE, this::new5));
    public final Setting<Boolean> combatCount = this.register(new Setting<>("ItemsCount", Boolean.TRUE, this::new6));
    public final Setting<Integer> combatCountX = this.register(new Setting<>("ItemX", 125, 0, 300, this::new7));
    public final Setting<Integer> combatCountY = this.register(new Setting<>("ItemY", 18, 0, 500, this::new8));
    public final Setting<Boolean> armor = this.register(new Setting<>("Armor", Boolean.TRUE, this::new9));
    public final Setting<Compass> compass = this.register(new Setting<>("Compass", Compass.NONE, this::new10));
    public final Setting<Integer> compassX = this.register(new Setting<>("CompX", 472, 0, 1000, this::new11));
    public final Setting<Integer> compassY = this.register(new Setting<>("CompY", 424, 0, 1000, this::new12));
    public final Setting<Integer> scale = this.register(new Setting<>("Scale", 3, 0, 10, this::new13));
    public final Setting<Boolean> playerViewer = this.register(new Setting<>("PlayerViewer", Boolean.FALSE, this::new14));
    public final Setting<Integer> playerViewerX = this.register(new Setting<>("PlayerX", 752, 0, 1000, this::new15));
    public final Setting<Integer> playerViewerY = this.register(new Setting<>("PlayerY", 497, 0, 1000, this::new16));
    public final Setting<Float> playerScale = this.register(new Setting<>("PlayerScale", 1.0f, 0.1f, 2.0f, this::new17));
    public final Setting<Boolean> inventory = this.register(new Setting<>("Inventory", Boolean.FALSE, this::new18));
    public final Setting<Integer> invX = this.register(new Setting<>("InvX", 564, 0, 1000, this::new19));
    public final Setting<Integer> invY = this.register(new Setting<>("InvY", 467, 0, 1000, this::new20));
    public final Setting<Integer> invH = this.register(new Setting<>("InvH", 3, this::new21));
    public final Setting<Integer> fineinvX = this.register(new Setting<>("InvFineX", 0, this::new22));
    public final Setting<Integer> fineinvY = this.register(new Setting<>("InvFineY", 0, this::new23));
    public final Setting<Boolean> renderingUp = this.register(new Setting<>("RenderingUp", Boolean.TRUE, this::new24));
    public final Setting<Boolean> arrayList = this.register(new Setting<>("ActiveModules", Boolean.TRUE, this::new25));
    final Setting<Boolean> pulse = this.register(new Setting<>("Pusle", Boolean.TRUE, this::new26));
    public final Setting<Integer> index = this.register(new Setting<>("index", 40, 0, 200, this::new27));
    final Setting<Boolean> glow = this.register(new Setting<>("Glow", Boolean.TRUE, this::new28));
    public final Setting<RenderingMode> renderingMode = this.register(new Setting<>("Ordering", RenderingMode.Length, this::new29));
    public final Setting<Boolean> onlyBind = this.register(new Setting<>("onlyBind", Boolean.TRUE, this::new30));
    public final Setting<Integer> arraylisty = this.register(new Setting<>("OffsetY", 40, 0, 200, this::new31));
    public final Setting<Boolean> notifyToggles = this.register(new Setting<>("ChatNotify", Boolean.TRUE, this::new32));
    public final Setting<TextUtil.Color> bracketColor = this.register(new Setting<>("BracketColor", TextUtil.Color.WHITE, this::new33));
    public final Setting<TextUtil.Color> commandColor = this.register(new Setting<>("NameColor", TextUtil.Color.WHITE, this::new34));
    public final Setting<Boolean> grayNess = this.register(new Setting<>("Gray", Boolean.TRUE, this::new35));
    public final Setting<Boolean> coords = this.register(new Setting<>("Coords", Boolean.TRUE, this::new36));
    public final Setting<Boolean> speed = this.register(new Setting<>("Speed", Boolean.FALSE, this::new37));
    public final Setting<Boolean> server = this.register(new Setting<>("IP", Boolean.FALSE, this::new38));
    public final Setting<Boolean> ping = this.register(new Setting<>("Ping", Boolean.FALSE, this::new39));
    public final Setting<Boolean> tps = this.register(new Setting<>("TPS", Boolean.FALSE, this::new40));
    public final Setting<Boolean> fps = this.register(new Setting<>("FPS", Boolean.FALSE, this::new41));
    public final Setting<Boolean> lag = this.register(new Setting<>("LagNotifier", Boolean.TRUE, this::new42));
    public final Setting<Integer> lagTime = this.register(new Setting<>("LagTime", 1000, 0, 2000, this::new43));
    public final Setting<Boolean> burrow = this.register(new Setting<>("BurrowWarner", Boolean.FALSE, this::new44));
    public final Setting<Boolean> friendlist = this.register(new Setting<>("FriendList", Boolean.FALSE, this::new45));
    public final Setting<Integer> x = this.register(new Setting<>("ListX", 100, 0, 1920, this::new46));
    public final Setting<Integer> y = this.register(new Setting<>("ListY", 200, 0, 1080, this::new47));
    public final Setting<Integer> offset = this.register(new Setting<>("Offset", 2, 0, 10, this::new48));
    private int color;

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Module.Category.CLIENT);
        this.setInstance();
    }

    public static HUD INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new HUD();
        }
        return INSTANCE;
    }

    private static double getPosOnCompass(Direction direction) {
        double d = Math.toRadians(MathHelper.wrapDegrees(HUD.mc.player.rotationYaw));
        int n = direction.ordinal();
        return d + (double)n * 1.5707963267948966;
    }

    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
        GlStateManager.color(255.0f, 255.0f, 255.0f, 255.0f);
    }

    private static void postboxrender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
    }

    private static void postitemrender() {
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onRender2D(final Render2DEvent render2DEvent) {
        if (this.compass.getValue() != Compass.NONE) {
            this.drawCompass();
        }
        if (this.inventory.getValue()) {
            this.renderInventory();
        }
        if (this.playerViewer.getValue()) {
            this.drawPlayer();
        }
        if (this.friendlist.getValue()) {
            final int intValue = this.x.getValue();
            final int intValue2 = this.y.getValue();
            final int rgba = ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), 255);
            MadCat.textManager.drawString("Your Friend:", (float)intValue, (float)intValue2, rgba, true);
            int n = intValue2 + 10 + this.offset.getValue();
            for (final EntityPlayer entityPlayer : HUD.mc.world.playerEntities) {
                if (MadCat.friendManager.isFriend(entityPlayer)) {
                    MadCat.textManager.drawString(entityPlayer.getName(), (float)intValue, (float)n, rgba, true);
                    n = n + 10 + this.offset.getValue();
                }
            }
        }
        if (this.combatCount.getValue()) {
            final int scaledWidth = this.renderer.scaledWidth;
            final int scaledHeight = this.renderer.scaledHeight;
            int sum = HUD.mc.player.inventory.mainInventory.stream().filter(HUD::onRender2D49).mapToInt(ItemStack::getCount).sum();
            if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                sum += HUD.mc.player.getHeldItemOffhand().getCount();
            }
            GlStateManager.enableTexture2D();
            final int n2 = scaledWidth / 2;
            final int n3 = scaledHeight - this.combatCountY.getValue();
            final int n4 = n2 + this.combatCountX.getValue();
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.totem, n4, n3);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.totem, n4, n3, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(String.valueOf(sum), (float)(n4 + 19 - 2 - this.renderer.getStringWidth(String.valueOf(sum))), (float)(n3 + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            int sum2 = HUD.mc.player.inventory.mainInventory.stream().filter(HUD::onRender2D50).mapToInt(ItemStack::getCount).sum();
            if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                sum2 += HUD.mc.player.getHeldItemOffhand().getCount();
            }
            final int n5 = n4 + 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.Crystal, n5, n3);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.Crystal, n5, n3, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(String.valueOf(sum2), (float)(n5 + 19 - 2 - this.renderer.getStringWidth(String.valueOf(sum2))), (float)(n3 + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            int sum3 = HUD.mc.player.inventory.mainInventory.stream().filter(HUD::onRender2D51).mapToInt(ItemStack::getCount).sum();
            if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                sum3 += HUD.mc.player.getHeldItemOffhand().getCount();
            }
            final int n6 = n5 + 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.xp, n6, n3);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.xp, n6, n3, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(String.valueOf(sum3), (float)(n6 + 19 - 2 - this.renderer.getStringWidth(String.valueOf(sum3))), (float)(n3 + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            int sum4 = HUD.mc.player.inventory.mainInventory.stream().filter(HUD::onRender2D52).mapToInt(ItemStack::getCount).sum();
            if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
                sum4 += HUD.mc.player.getHeldItemOffhand().getCount();
            }
            final int n7 = n6 + 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.ap, n7, n3);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.ap, n7, n3, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(String.valueOf(sum4), (float)(n7 + 19 - 2 - this.renderer.getStringWidth(String.valueOf(sum4))), (float)(n3 + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
            final int sum5 = HUD.mc.player.inventory.mainInventory.stream().filter(HUD::onRender2D53).mapToInt(ItemStack::getCount).sum();
            final int n8 = n7 + 20;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.obs, n8, n3);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.obs, n8, n3, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(String.valueOf(sum5), (float)(n8 + 19 - 2 - this.renderer.getStringWidth(String.valueOf(sum5))), (float)(n3 + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        if (this.armor.getValue()) {
            final int scaledWidth2 = this.renderer.scaledWidth;
            final int scaledHeight2 = this.renderer.scaledHeight;
            GlStateManager.enableTexture2D();
            final int n9 = scaledWidth2 / 2;
            int n10 = 0;
            final int n11 = scaledHeight2 - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            for (final ItemStack itemStack : HUD.mc.player.inventory.armorInventory) {
                ++n10;
                if (itemStack.isEmpty()) {
                    continue;
                }
                final int n12 = n9 - 90 + (9 - n10) * 20 + 2;
                GlStateManager.enableDepth();
                RenderUtil.itemRender.zLevel = 200.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, n12, n11);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, itemStack, n12, n11, "");
                RenderUtil.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                final String s = (itemStack.getCount() > 1) ? (String.valueOf(itemStack.getCount())) : "";
                this.renderer.drawStringWithShadow(s, (float)(n12 + 19 - 2 - this.renderer.getStringWidth(s)), (float)(n11 + 9), 16777215);
                final float n13 = (itemStack.getMaxDamage() - (float)itemStack.getItemDamage()) / itemStack.getMaxDamage();
                final float n14 = 1.0f - n13;
                int n15 = 100 - (int)(n14 * 100.0f);
                if (n15 == -2147483547) {
                    n15 = 100;
                }
                this.renderer.drawStringWithShadow(String.valueOf(n15), (float)(n12 + 8 - this.renderer.getStringWidth(String.valueOf(n15)) / 2), (float)(n11 - 11), ColorUtil.toRGBA((int)(n14 * 255.0f), (int)(n13 * 255.0f), 0));
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        if ((Phase.collideBlockIntersects(HUD.mc.player.getEntityBoundingBox(), HUD::onRender2D54) || Phase.collideBlockIntersects(HUD.mc.player.getEntityBoundingBox(), HUD::onRender2D55)) && this.burrow.getValue()) {
            MadCat.textManager.drawString(ChatFormatting.DARK_RED + "You are in Burrow!", (float)(this.renderer.scaledWidth / 2.0) - (float)(this.renderer.getStringWidth("You are in Burrow!") / 2.0), (float)(this.renderer.scaledHeight / 2.0) - 20.0f, 200, true);
        }
        final int scaledWidth3 = this.renderer.scaledWidth;
        final int scaledHeight3 = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue());
        if (this.waterMark.getValue() && !this.watermark2.getValue()) {
            final String string = ClickGui.INSTANCE().clientName.getValueAsString() + " " + "3.0";
            if (ClickGui.INSTANCE().rainbow.getValue()) {
                if (ClickGui.INSTANCE().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, 2.0f, ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] array = { 1 };
                    final char[] charArray = string.toCharArray();
                    float n16 = 0.0f;
                    for (final char c : charArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + n16, 2.0f, ColorUtil.rainbow(array[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB(), true);
                        n16 += this.renderer.getStringWidth(String.valueOf(c));
                        final int n17 = 0;
                        ++array[n17];
                    }
                }
            }
            else {
                this.renderer.drawString(string, 2.0f, 2.0f, this.color, true);
            }
        }
        if (this.waterMark.getValue() && this.watermark2.getValue()) {
            if (this.alpha.getValue() >= 0) {
                RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 10 + this.renderer.getStringWidth(ClickGui.INSTANCE().clientName.getValueAsString() + " | FPS:" + Minecraft.getDebugFPS() + " | TPS:" + MadCat.serverManager.getTPS() + " | Ping:" + MadCat.serverManager.getPing()), 15, ColorUtil.toRGBA(20, 20, 20, this.alpha.getValue()));
            }
            final String string2 = ClickGui.INSTANCE().clientName.getValueAsString() + " | FPS:" + Minecraft.getDebugFPS() + " | TPS:" + MadCat.serverManager.getTPS() + " | Ping:" + MadCat.serverManager.getPing();
            this.color = ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue());
            if (ClickGui.INSTANCE().rainbow.getValue()) {
                RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 10 + this.renderer.getStringWidth(ClickGui.INSTANCE().clientName.getValueAsString() + " | FPS:" + Minecraft.getDebugFPS() + " | TPS:" + MadCat.serverManager.getTPS() + " | Ping:" + MadCat.serverManager.getPing()), 1, ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB());
                if (ClickGui.INSTANCE().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string2, (float)(this.compactX.getValue() + 5), (float)(this.compactY.getValue() + 4), ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] array4 = { 1 };
                    final char[] charArray2 = string2.toCharArray();
                    float n18 = 0.0f;
                    for (final char c2 : charArray2) {
                        this.renderer.drawString(String.valueOf(c2), this.compactX.getValue() + 5 + n18, (float)(this.compactY.getValue() + 4), ColorUtil.rainbow(array4[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB(), true);
                        n18 += this.renderer.getStringWidth(String.valueOf(c2));
                        final int n19 = 0;
                        ++array4[n19];
                    }
                }
            }
            else {
                this.renderer.drawString(string2, (float)(this.compactX.getValue() + 5), (float)(this.compactY.getValue() + 4), this.color, true);
                RenderUtil.drawRectangleCorrectly(this.compactX.getValue(), this.compactY.getValue(), 10 + this.renderer.getStringWidth(ClickGui.INSTANCE().clientName.getValueAsString() + " | FPS:" + Minecraft.getDebugFPS() + " | TPS:" + MadCat.serverManager.getTPS() + " | Ping:" + MadCat.serverManager.getPing()), 1, this.color);
            }
        }
        this.color = ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue());
        this.color = ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue());
        final int[] array7 = { 1 };
        int n20 = (HUD.mc.currentScreen instanceof GuiChat && !this.renderingUp.getValue()) ? 14 : 0;
        if (this.arrayList.getValue()) {
            if (this.renderingUp.getValue()) {
                if (this.renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < MadCat.moduleManager.sortedModulesABC.size(); ++k) {
                        final String s2 = MadCat.moduleManager.sortedModulesABC.get(k);
                        if (this.glow.getValue()) {
                            RenderUtil.drawGlow(scaledWidth3 - 2 - this.renderer.getStringWidth(s2), 2 + n20 * 4 + this.arraylisty.getValue() - 7, scaledWidth3, 2 + n20 * 4 + this.arraylisty.getValue() + 14, ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.toRGBA(new Color(ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRed(), ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getGreen(), ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getBlue(), 100)) : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), 100));
                        }
                        this.renderer.drawString(s2, (float)(scaledWidth3 - 2 - this.renderer.getStringWidth(s2)), (float)(2 + n20 * 4 + this.arraylisty.getValue()), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                        n20 += 3;
                        final int n21 = 0;
                        ++array7[n21];
                    }
                }
                else {
                    for (int l = 0; l < MadCat.moduleManager.sortedModules.size(); ++l) {
                        final Module module = MadCat.moduleManager.sortedModules.get(l);
                        if (module.getBind().getKey() != -1 || !this.onlyBind.getValue()) {
                            final String string3 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                            if (this.glow.getValue()) {
                                RenderUtil.drawGlow(scaledWidth3 - 2 - this.renderer.getStringWidth(string3), 2 + n20 * 4 + this.arraylisty.getValue() - 7, scaledWidth3, 2 + n20 * 4 + this.arraylisty.getValue() + 14, ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.toRGBA(new Color(ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRed(), ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getGreen(), ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getBlue(), 100)) : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), 100));
                            }
                            this.renderer.drawString(string3, (float)(scaledWidth3 - 2 - this.renderer.getStringWidth(string3)), (float)(2 + n20 * 4 + this.arraylisty.getValue()), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                            n20 += 3;
                            final int n22 = 0;
                            ++array7[n22];
                        }
                    }
                }
            }
            else if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int n23 = 0; n23 < MadCat.moduleManager.sortedModulesABC.size(); ++n23) {
                    final String s3 = MadCat.moduleManager.sortedModulesABC.get(n23);
                    n20 += 12;
                    if (this.glow.getValue()) {
                        RenderUtil.drawGlow(scaledWidth3 - 2 - this.renderer.getStringWidth(s3), scaledHeight3 - n20 - 14, scaledWidth3, scaledHeight3 - n20 + 7, ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.toRGBA(new Color(ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRed(), ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getGreen(), ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getBlue(), 100)) : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), 100));
                    }
                    this.renderer.drawString(s3, (float)(scaledWidth3 - 2 - this.renderer.getStringWidth(s3)), (float)(scaledHeight3 - n20), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n24 = 0;
                    ++array7[n24];
                }
            }
            else {
                for (int n25 = 0; n25 < MadCat.moduleManager.sortedModules.size(); ++n25) {
                    final Module module2 = MadCat.moduleManager.sortedModules.get(n25);
                    if (module2.getBind().getKey() != -1 || !this.onlyBind.getValue()) {
                        final String string4 = module2.getDisplayName() + ChatFormatting.GRAY + ((module2.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module2.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                        n20 += 12;
                        if (this.glow.getValue()) {
                            RenderUtil.drawGlow(scaledWidth3 - 2 - this.renderer.getStringWidth(string4), scaledHeight3 - n20 - 14, scaledWidth3, scaledHeight3 - n20 + 7, ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.toRGBA(new Color(ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRed(), ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getGreen(), ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getBlue(), 100)) : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), 100));
                        }
                        this.renderer.drawString(string4, (float)(scaledWidth3 - 2 - this.renderer.getStringWidth(string4)), (float)(scaledHeight3 - n20), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                        final int n26 = 0;
                        ++array7[n26];
                    }
                }
            }
        }
        final String s4 = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int n27 = (HUD.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            if (this.server.getValue()) {
                final String string5 = s4 + "IP " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
                n27 += 10;
                this.renderer.drawString(string5, (float)(scaledWidth3 - this.renderer.getStringWidth(string5) - 2), (float)(scaledHeight3 - 2 - n27), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                final int n28 = 0;
                ++array7[n28];
            }
            if (this.speed.getValue()) {
                final String string6 = s4 + "Speed " + ChatFormatting.WHITE + MadCat.speedManager.getSpeedKpH() + " km/h";
                n27 += 10;
                this.renderer.drawString(string6, (float)(scaledWidth3 - this.renderer.getStringWidth(string6) - 2), (float)(scaledHeight3 - 2 - n27), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                final int n29 = 0;
                ++array7[n29];
            }
            if (this.tps.getValue()) {
                final String string7 = s4 + "TPS " + ChatFormatting.WHITE + MadCat.serverManager.getTPS();
                n27 += 10;
                this.renderer.drawString(string7, (float)(scaledWidth3 - this.renderer.getStringWidth(string7) - 2), (float)(scaledHeight3 - 2 - n27), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                final int n30 = 0;
                ++array7[n30];
            }
            final String string8 = s4 + "FPS " + ChatFormatting.WHITE + Minecraft.getDebugFPS();
            final String string9 = s4 + "Ping " + ChatFormatting.WHITE + MadCat.serverManager.getPing();
            if (this.renderer.getStringWidth(string9) > this.renderer.getStringWidth(string8)) {
                if (this.ping.getValue()) {
                    n27 += 10;
                    this.renderer.drawString(string9, (float)(scaledWidth3 - this.renderer.getStringWidth(string9) - 2), (float)(scaledHeight3 - 2 - n27), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n31 = 0;
                    ++array7[n31];
                }
                if (this.fps.getValue()) {
                    n27 += 10;
                    this.renderer.drawString(string8, (float)(scaledWidth3 - this.renderer.getStringWidth(string8) - 2), (float)(scaledHeight3 - 2 - n27), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n32 = 0;
                    ++array7[n32];
                }
            }
            else {
                if (this.fps.getValue()) {
                    n27 += 10;
                    this.renderer.drawString(string8, (float)(scaledWidth3 - this.renderer.getStringWidth(string8) - 2), (float)(scaledHeight3 - 2 - n27), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n33 = 0;
                    ++array7[n33];
                }
                if (this.ping.getValue()) {
                    n27 += 10;
                    this.renderer.drawString(string9, (float)(scaledWidth3 - this.renderer.getStringWidth(string9) - 2), (float)(scaledHeight3 - 2 - n27), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n34 = 0;
                    ++array7[n34];
                }
            }
        }
        else {
            if (this.server.getValue()) {
                final String string10 = s4 + "IP " + ChatFormatting.WHITE + (HUD.mc.isSingleplayer() ? "SinglePlayer" : Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP);
                this.renderer.drawString(string10, (float)(scaledWidth3 - this.renderer.getStringWidth(string10) - 2), (float)(2 + n27++ * 10), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                final int n35 = 0;
                ++array7[n35];
            }
            if (this.speed.getValue()) {
                final String string11 = s4 + "Speed " + ChatFormatting.WHITE + MadCat.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(string11, (float)(scaledWidth3 - this.renderer.getStringWidth(string11) - 2), (float)(2 + n27++ * 10), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                final int n36 = 0;
                ++array7[n36];
            }
            if (this.tps.getValue()) {
                final String string12 = s4 + "TPS " + ChatFormatting.WHITE + MadCat.serverManager.getTPS();
                this.renderer.drawString(string12, (float)(scaledWidth3 - this.renderer.getStringWidth(string12) - 2), (float)(2 + n27++ * 10), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                final int n37 = 0;
                ++array7[n37];
            }
            final String string13 = s4 + "FPS " + ChatFormatting.WHITE + Minecraft.getDebugFPS();
            final String string14 = s4 + "Ping " + ChatFormatting.WHITE + MadCat.serverManager.getPing();
            if (this.renderer.getStringWidth(string14) > this.renderer.getStringWidth(string13)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(string14, (float)(scaledWidth3 - this.renderer.getStringWidth(string14) - 2), (float)(2 + n27++ * 10), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n38 = 0;
                    ++array7[n38];
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(string13, (float)(scaledWidth3 - this.renderer.getStringWidth(string13) - 2), (float)(2 + n27++ * 10), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n39 = 0;
                    ++array7[n39];
                }
            }
            else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(string13, (float)(scaledWidth3 - this.renderer.getStringWidth(string13) - 2), (float)(2 + n27++ * 10), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n40 = 0;
                    ++array7[n40];
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(string14, (float)(scaledWidth3 - this.renderer.getStringWidth(string14) - 2), (float)(2 + n27++ * 10), ClickGui.INSTANCE().rainbow.getValue() ? ((ClickGui.INSTANCE().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(array7[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB()) : (this.pulse.getValue() ? ColorUtil.pulseColor(new Color(this.color), this.index.getValue(), array7[0]).getRGB() : this.color), true);
                    final int n41 = 0;
                    ++array7[n41];
                }
            }
        }
        final boolean equals = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int n42 = (int)HUD.mc.player.posX;
        final int n43 = (int)HUD.mc.player.posY;
        final int n44 = (int)HUD.mc.player.posZ;
        final float n45 = equals ? 8.0f : 0.125f;
        final int n46 = (int)(HUD.mc.player.posX * n45);
        final int n47 = (int)(HUD.mc.player.posZ * n45);
        int n48 = (HUD.mc.currentScreen instanceof GuiChat) ? 14 : 0;
        final String string15 = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + (equals ? (n42 + ", " + n43 + ", " + n44 + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + n46 + ", " + n47 + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (n42 + ", " + n43 + ", " + n44 + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + n46 + ", " + n47 + ChatFormatting.WHITE + "]"));
        final String s5 = this.coords.getValue() ? MadCat.rotationManager.getDirection4D(false) : "";
        final String s6 = this.coords.getValue() ? string15 : "";
        n48 += 10;
        if (ClickGui.INSTANCE().rainbow.getValue()) {
            final String s7 = this.coords.getValue() ? ("XYZ " + n42 + ", " + n43 + ", " + n44 + " [" + n46 + ", " + n47 + "]") : "";
            if (ClickGui.INSTANCE().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(s5, 2.0f, (float)(scaledHeight3 - n48 - 11), ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB(), true);
                this.renderer.drawString(s7, 2.0f, (float)(scaledHeight3 - n48), ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] array26 = { 1 };
                final char[] charArray3 = s5.toCharArray();
                float n49 = 0.0f;
                for (final char c3 : charArray3) {
                    this.renderer.drawString(String.valueOf(c3), 2.0f + n49, (float)(scaledHeight3 - n48 - 11), ColorUtil.rainbow(array26[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB(), true);
                    n49 += this.renderer.getStringWidth(String.valueOf(c3));
                    final int n51 = 0;
                    ++array26[n51];
                }
                final int[] array29 = { 1 };
                final char[] charArray4 = s7.toCharArray();
                float n52 = 0.0f;
                for (final char c4 : charArray4) {
                    this.renderer.drawString(String.valueOf(c4), 2.0f + n52, (float)(scaledHeight3 - n48), ColorUtil.rainbow(array29[0] * ClickGui.INSTANCE().rainbowHue.getValue()).getRGB(), true);
                    n52 += this.renderer.getStringWidth(String.valueOf(c4));
                    final int n54 = 0;
                    ++array29[n54];
                }
            }
        }
        else {
            this.renderer.drawString(s5, 2.0f, (float)(scaledHeight3 - n48 - 11), this.color, true);
            this.renderer.drawString(s6, 2.0f, (float)(scaledHeight3 - n48), this.color, true);
        }
        if (this.lag.getValue()) {
            this.renderLag();
        }
    }

    public void renderLag() {
        int n = this.renderer.scaledWidth;
        if (MadCat.serverManager.isServerNotResponding()) {
            String string = ChatFormatting.RED + "Server not responding " + MathUtil.round((float)MadCat.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(string, (float)n / 2.0f - (float)this.renderer.getStringWidth(string) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }

    private double getX(double d) {
        return Math.sin(d) * (double)(this.scale.getValue() * 10);
    }

    private double getY(double d) {
        double d2 = MathHelper.clamp(HUD.mc.player.rotationPitch + 30.0f, -90.0f, 90.0f);
        double d3 = Math.toRadians(d2);
        return Math.cos(d) * Math.sin(d3) * (double)(this.scale.getValue() * 10);
    }

    public void drawPlayer() {
        EntityPlayerSP entityPlayerSP = HUD.mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), 50.0f);
        GlStateManager.scale(-50.0f * this.playerScale.getValue(), 50.0f * this.playerScale.getValue(), 50.0f * this.playerScale.getValue());
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-((float)Math.atan((float) this.playerViewerY.getValue() / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager renderManager = mc.getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        try {
            renderManager.renderEntity(entityPlayerSP, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception exception) {
            // empty catch block
        }
        renderManager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    public void renderInventory() {
        this.itemrender(HUD.mc.player.inventory.mainInventory, this.invX.getValue() + this.fineinvX.getValue(), this.invY.getValue() + this.fineinvY.getValue());
    }

    private void boxrender(int n, int n2) {
        HUD.preboxrender();
        HUD.mc.renderEngine.bindTexture(box);
        RenderUtil.drawTexturedRect(n, n2, 0, 0, 176, 16, 500);
        RenderUtil.drawTexturedRect(n, n2 + 16, 0, 16, 176, 54 + this.invH.getValue(), 500);
        RenderUtil.drawTexturedRect(n, n2 + 16 + 54, 0, 160, 176, 8, 500);
        HUD.postboxrender();
    }

    private void itemrender(NonNullList<ItemStack> nonNullList, int n, int n2) {
        for (int i = 0; i < nonNullList.size() - 9; ++i) {
            int n3 = n + i % 9 * 18 + 8;
            int n4 = n2 + i / 9 * 18 + 18;
            ItemStack itemStack = nonNullList.get(i + 9);
            HUD.preitemrender();
            HUD.mc.getRenderItem().zLevel = 501.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, n3, n4);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, itemStack, n3, n4, null);
            HUD.mc.getRenderItem().zLevel = 0.0f;
            HUD.postitemrender();
        }
    }

    public void drawCompass() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        if (this.compass.getValue() == Compass.LINE) {
            float f = HUD.mc.player.rotationYaw;
            float f2 = MathUtil.wrap(f);
            RenderUtil.drawRect(this.compassX.getValue(), this.compassY.getValue(), this.compassX.getValue() + 100, this.compassY.getValue() + this.renderer.getFontHeight(), 1963986960);
            RenderUtil.glScissor(this.compassX.getValue(), this.compassY.getValue(), this.compassX.getValue() + 100, this.compassY.getValue() + this.renderer.getFontHeight(), scaledResolution);
            GL11.glEnable(3089);
            float f3 = MathUtil.wrap((float)(Math.atan2(0.0 - HUD.mc.player.posZ, 0.0 - HUD.mc.player.posX) * 180.0 / Math.PI) - 90.0f);
            RenderUtil.drawLine((float) this.compassX.getValue() - f2 + 50.0f + f3, this.compassY.getValue() + 2, (float) this.compassX.getValue() - f2 + 50.0f + f3, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -61424);
            RenderUtil.drawLine((float) this.compassX.getValue() - f2 + 50.0f + 45.0f, this.compassY.getValue() + 2, (float) this.compassX.getValue() - f2 + 50.0f + 45.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float) this.compassX.getValue() - f2 + 50.0f - 45.0f, this.compassY.getValue() + 2, (float) this.compassX.getValue() - f2 + 50.0f - 45.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float) this.compassX.getValue() - f2 + 50.0f + 135.0f, this.compassY.getValue() + 2, (float) this.compassX.getValue() - f2 + 50.0f + 135.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float) this.compassX.getValue() - f2 + 50.0f - 135.0f, this.compassY.getValue() + 2, (float) this.compassX.getValue() - f2 + 50.0f - 135.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            this.renderer.drawStringWithShadow("n", (float) this.compassX.getValue() - f2 + 50.0f + 180.0f - (float)this.renderer.getStringWidth("n") / 2.0f, this.compassY.getValue(), -1);
            this.renderer.drawStringWithShadow("n", (float) this.compassX.getValue() - f2 + 50.0f - 180.0f - (float)this.renderer.getStringWidth("n") / 2.0f, this.compassY.getValue(), -1);
            this.renderer.drawStringWithShadow("e", (float) this.compassX.getValue() - f2 + 50.0f - 90.0f - (float)this.renderer.getStringWidth("e") / 2.0f, this.compassY.getValue(), -1);
            this.renderer.drawStringWithShadow("s", (float) this.compassX.getValue() - f2 + 50.0f - (float)this.renderer.getStringWidth("s") / 2.0f, this.compassY.getValue(), -1);
            this.renderer.drawStringWithShadow("w", (float) this.compassX.getValue() - f2 + 50.0f + 90.0f - (float)this.renderer.getStringWidth("w") / 2.0f, this.compassY.getValue(), -1);
            RenderUtil.drawLine(this.compassX.getValue() + 50, this.compassY.getValue() + 1, this.compassX.getValue() + 50, this.compassY.getValue() + this.renderer.getFontHeight() - 1, 2.0f, -7303024);
            GL11.glDisable(3089);
        } else {
            double d = this.compassX.getValue();
            double d2 = this.compassY.getValue();
            for (Direction direction : Direction.values()) {
                double d3 = HUD.getPosOnCompass(direction);
                this.renderer.drawStringWithShadow(direction.name(), (float)(d + this.getX(d3)), (float)(d2 + this.getY(d3)), direction == Direction.N ? -65536 : -1);
            }
        }
    }

    private static Boolean onRender2D55(Block block) {
        return block == Blocks.ENDER_CHEST;
    }

    private static Boolean onRender2D54(Block block) {
        return block == Blocks.OBSIDIAN;
    }

    private static boolean onRender2D53(ItemStack itemStack) {
        return itemStack.getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN);
    }

    private static boolean onRender2D52(ItemStack itemStack) {
        return itemStack.getItem() == Items.GOLDEN_APPLE;
    }

    private static boolean onRender2D51(ItemStack itemStack) {
        return itemStack.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    private static boolean onRender2D50(ItemStack itemStack) {
        return itemStack.getItem() == Items.END_CRYSTAL;
    }

    private static boolean onRender2D49(ItemStack itemStack) {
        return itemStack.getItem() == Items.TOTEM_OF_UNDYING;
    }

    private boolean new48(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.friendlist.getValue();
    }

    private boolean new47(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.friendlist.getValue();
    }

    private boolean new46(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.friendlist.getValue();
    }

    private boolean new45(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new44(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new43(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.lag.getValue();
    }

    private boolean new42(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new41(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new40(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new39(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new38(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new37(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new36(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new35(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new34(TextUtil.Color color) {
        return this.page.getValue() == Page.GLOBAL && this.notifyToggles.getValue();
    }

    private boolean new33(TextUtil.Color color) {
        return this.page.getValue() == Page.GLOBAL && this.notifyToggles.getValue();
    }

    private boolean new32(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new31(Integer n) {
        return this.page.getValue() == Page.GLOBAL && this.arrayList.getValue() && this.renderingUp.getValue();
    }

    private boolean new30(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL && this.arrayList.getValue() && this.renderingMode.getValue() != RenderingMode.ABC;
    }

    private boolean new29(RenderingMode renderingMode) {
        return this.page.getValue() == Page.GLOBAL && this.arrayList.getValue();
    }

    private boolean new28(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL && this.arrayList.getValue();
    }

    private boolean new27(Integer n) {
        return this.page.getValue() == Page.GLOBAL && this.arrayList.getValue() && this.pulse.getValue();
    }

    private boolean new26(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL && this.arrayList.getValue();
    }

    private boolean new25(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new24(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new23(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.inventory.getValue();
    }

    private boolean new22(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.inventory.getValue();
    }

    private boolean new21(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.inventory.getValue();
    }

    private boolean new20(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.inventory.getValue();
    }

    private boolean new19(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.inventory.getValue();
    }

    private boolean new18(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new17(Float f) {
        return this.page.getValue() == Page.ELEMENTS && this.playerViewer.getValue();
    }

    private boolean new16(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.playerViewer.getValue();
    }

    private boolean new15(Integer n) {
        return this.page.getValue() == Page.ELEMENTS && this.playerViewer.getValue();
    }

    private boolean new14(Boolean bl) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new13(Integer n) {
        return this.compass.getValue() != Compass.NONE && this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new12(Integer n) {
        return this.compass.getValue() != Compass.NONE && this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new11(Integer n) {
        return this.compass.getValue() != Compass.NONE && this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new10(Compass compass) {
        return this.page.getValue() == Page.ELEMENTS;
    }

    private boolean new9(Boolean bl) {
        return this.page.getValue() == Page.ITEM;
    }

    private boolean new8(Integer n) {
        return this.combatCount.getValue() && this.page.getValue() == Page.ITEM;
    }

    private boolean new7(Integer n) {
        return this.combatCount.getValue() && this.page.getValue() == Page.ITEM;
    }

    private boolean new6(Boolean bl) {
        return this.page.getValue() == Page.ITEM;
    }

    private boolean new5(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    private boolean new4(Integer n) {
        return this.page.getValue() == Page.GLOBAL && this.waterMark.getValue() && this.watermark2.getValue();
    }

    private boolean new3(Integer n) {
        return this.page.getValue() == Page.GLOBAL && this.waterMark.getValue() && this.watermark2.getValue();
    }

    private boolean new2(Object object) {
        return this.page.getValue() == Page.GLOBAL && this.waterMark.getValue() && this.watermark2.getValue();
    }

    private boolean new1(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL && this.waterMark.getValue();
    }

    private boolean new0(Boolean bl) {
        return this.page.getValue() == Page.GLOBAL;
    }

    public enum RenderingMode {
        Length,
        ABC

    }

    private enum Direction {
        N,
        W,
        S,
        E

    }

    public enum Compass {
        NONE,
        CIRCLE,
        LINE

    }

    private enum Page {
        ELEMENTS,
        GLOBAL,
        ITEM

    }
}

