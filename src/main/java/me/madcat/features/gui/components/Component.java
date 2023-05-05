package me.madcat.features.gui.components;

import java.awt.Color;
import java.util.ArrayList;
import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.gui.Gui;
import me.madcat.features.gui.components.items.Item;
import me.madcat.features.gui.components.items.buttons.Button;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.client.FontMod;
import me.madcat.util.ColorUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Component
extends Feature {
    public static int[] counter1 = new int[]{1};
    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final ArrayList<Item> items = new ArrayList();
    private final int barHeight;
    public boolean drag;
    private int old;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private int angle;
    private boolean hidden = false;
    private int startcolor;

    public Component(String string, int n, int n2, boolean bl) {
        super(string);
        this.x = n;
        this.y = n2;
        this.width = 88 + ClickGui.INSTANCE().moduleWidth.getValue();
        this.height = 18;
        this.barHeight = 15;
        this.angle = 180;
        this.open = bl;
        this.setupItems();
    }

    public static void drawModalRect(int n, int n2, float f, float f2, int n3, int n4, int n5, int n6, float f3, float f4) {
        net.minecraft.client.gui.Gui.drawScaledCustomSizeModalRect(n, n2, f, f2, n3, n4, n5, n6, f3, f4);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static float calculateRotation(float f) {
        float f2 = 0.0f;
        f %= 360.0f;
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }

    public void setupItems() {
    }

    private void drag(int n, int n2) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + n;
        this.y = this.y2 + n2;
    }

    public void drawScreen(int n, int n2, float f) {
        int n3;
        float f2;
        this.old = this.x - ClickGui.INSTANCE().moduleDistance.getValue();
        if (this.getName().equals("Combat")) {
            this.old += ClickGui.INSTANCE().moduleDistance.getValue();
        }
        if (this.getName().equals("Misc")) {
            this.old += ClickGui.INSTANCE().moduleDistance.getValue() * 2;
        }
        if (this.getName().equals("Render")) {
            this.old += ClickGui.INSTANCE().moduleDistance.getValue() * 3;
        }
        if (this.getName().equals("Movement")) {
            this.old += ClickGui.INSTANCE().moduleDistance.getValue() * 4;
        }
        if (this.getName().equals("Player")) {
            this.old += ClickGui.INSTANCE().moduleDistance.getValue() * 5;
        }
        if (this.getName().equals("Exploit")) {
            this.old += ClickGui.INSTANCE().moduleDistance.getValue() * 6;
        }
        if (this.getName().equals("Client")) {
            this.old += ClickGui.INSTANCE().moduleDistance.getValue() * 7;
        }
        if (this.getName().equals("Legacy")) {
            this.old += ClickGui.INSTANCE().moduleDistance.getValue() * 8;
        }
        this.width = 88 + ClickGui.INSTANCE().moduleWidth.getValue();
        this.drag(n, n2);
        counter1 = new int[]{1};
        float f3 = f2 = this.open ? this.getTotalItemHeight() - 2.0f : 0.0f;
        if (ClickGui.INSTANCE().rainbowg.getValue()) {
            if (ClickGui.INSTANCE().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.startcolor = ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB();
                n3 = ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRGB();
            }
        } else {
            this.startcolor = ColorUtil.toRGBA(ClickGui.INSTANCE().g_red.getValue(), ClickGui.INSTANCE().g_green.getValue(), ClickGui.INSTANCE().g_blue.getValue(), ClickGui.INSTANCE().g_alpha.getValue());
        }
        n3 = ColorUtil.toRGBA(ClickGui.INSTANCE().g_red1.getValue(), ClickGui.INSTANCE().g_green1.getValue(), ClickGui.INSTANCE().g_blue1.getValue(), ClickGui.INSTANCE().g_alpha1.getValue());
        RenderUtil.drawRect(this.old, this.y, this.old + this.width, this.y + this.height - 5, ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), 255));
        RenderUtil.drawGradientSideways(this.old - 1, this.y, this.old + this.width + 1, (float)(this.y + this.barHeight) - 2.0f, this.startcolor, n3);
        if (this.open) {
            RenderUtil.drawGradientSideways(this.old - 1, (float)this.y + 13.2f, this.old + this.width + 1, (float)this.y + f2 + 19.0f, this.startcolor, n3);
            RenderUtil.drawRect(this.old, (float)this.y + 13.2f, this.old + this.width, (float)(this.y + this.height) + f2, ColorUtil.toRGBA(0, 0, 0, ClickGui.INSTANCE().alphaBox.getValue()));
        }
        if (ClickGui.INSTANCE().moduleIcon.getValue()) {
            if (!FontMod.INSTANCE().cfont.getValue() && FontMod.INSTANCE().isEnabled() || !FontMod.INSTANCE().isEnabled()) {
                MadCat.textManager.drawStringWithShadow(this.getName(), (float)this.old + 17.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            } else {
                MadCat.textManager.drawStringClickGui(this.getName(), (float)this.old + 17.0f, (float)this.y - 3.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
        } else if (!FontMod.INSTANCE().cfont.getValue() && FontMod.INSTANCE().isEnabled() || !FontMod.INSTANCE().isEnabled()) {
            MadCat.textManager.drawStringWithShadow(this.getName(), (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
        } else {
            MadCat.textManager.drawStringClickGui(this.getName(), (float)this.old + 3.0f, (float)this.y - 3.0f - (float)Gui.getClickGui().getTextOffset(), -1);
        }
        if (ClickGui.INSTANCE().iconmode2.getValue() == 1) {
            MadCat.textManager.drawStringlogo("7", (float)this.old + 75.0f + (float) ClickGui.INSTANCE().moduleWidth.getValue(), (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
        }
        if (ClickGui.INSTANCE().moduleIcon.getValue()) {
            if (this.getName().equals("Combat")) {
                MadCat.textManager.drawStringlogo("b", (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName().equals("Misc")) {
                MadCat.textManager.drawStringlogo("[", (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName().equals("Render")) {
                MadCat.textManager.drawStringlogo("a", (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName().equals("Movement")) {
                MadCat.textManager.drawStringlogo("8", (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName().equals("Player")) {
                MadCat.textManager.drawStringlogo("5", (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName().equals("Exploit")) {
                MadCat.textManager.drawStringlogo("!", (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName().equals("Client")) {
                MadCat.textManager.drawStringlogo("3", (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
            if (this.getName().equals("Legacy")) {
                MadCat.textManager.drawStringlogo("9", (float)this.old + 3.0f, (float)this.y - 4.0f - (float)Gui.getClickGui().getTextOffset(), -1);
            }
        }
        if (!this.open) {
            if (this.angle > 0) {
                this.angle -= 6;
            }
        } else if (this.angle < 180) {
            this.angle += 6;
        }
        if (ClickGui.INSTANCE().iconmode2.getValue() == 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            Component.glColor(new Color(255, 255, 255, 255));
            this.minecraft.getTextureManager().bindTexture(new ResourceLocation("textures/arrow.png"));
            GlStateManager.translate((float)(this.getX() + this.getWidth() - 7), (float)(this.getY() + 6) - 0.3f, 0.0f);
            GlStateManager.rotate(Component.calculateRotation(this.angle), 0.0f, 0.0f, 1.0f);
            Component.drawModalRect(-5, -5, 0.0f, 0.0f, 10, 10, 10, 10, 10.0f, 10.0f);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
        if (this.open) {
            RenderUtil.drawRect(this.old, (float)this.y + 12.5f, this.old + this.width, (float)(this.y + this.height) + f2, 0x77000000);
            if (ClickGui.INSTANCE().outline.getValue()) {
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.shadeModel(7425);
                GL11.glBegin(2);
                GL11.glColor4f((float) ClickGui.INSTANCE().red.getValue() / 255.0f, (float) ClickGui.INSTANCE().green.getValue() / 255.0f, (float) ClickGui.INSTANCE().blue.getValue() / 255.0f, 255.0f);
                GL11.glVertex3f((float)this.old, (float)this.y - 0.5f, 0.0f);
                GL11.glVertex3f((float)(this.old + this.width), (float)this.y - 0.5f, 0.0f);
                GL11.glVertex3f((float)(this.old + this.width), (float)(this.y + this.height) + f2, 0.0f);
                GL11.glVertex3f((float)this.old, (float)(this.y + this.height) + f2, 0.0f);
                GL11.glEnd();
                GlStateManager.shadeModel(7424);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
            }
        }
        if (this.open) {
            float f4 = (float)(this.getY() + this.getHeight()) - 3.0f;
            for (Item item : this.getItems()) {
                Component.counter1[0] = counter1[0] + 1;
                if (item.isHidden()) continue;
                item.setLocation((float)this.old + 2.0f, f4);
                item.setWidth(this.getWidth() - 4);
                item.drawScreen(n, n2, f);
                f4 += (float)item.getHeight() + 1.5f;
            }
        }
    }

    public void mouseClicked(int n, int n2, int n3) {
        if (n3 == 0 && this.isHovering(n, n2)) {
            this.x2 = this.old - n;
            this.y2 = this.y - n2;
            if (this.getName().equals("Combat")) {
                this.x2 -= ClickGui.INSTANCE().moduleDistance.getValue();
            }
            if (this.getName().equals("Misc")) {
                this.x2 -= ClickGui.INSTANCE().moduleDistance.getValue() * 2;
            }
            if (this.getName().equals("Render")) {
                this.x2 -= ClickGui.INSTANCE().moduleDistance.getValue() * 3;
            }
            if (this.getName().equals("Movement")) {
                this.x2 -= ClickGui.INSTANCE().moduleDistance.getValue() * 4;
            }
            if (this.getName().equals("Player")) {
                this.x2 -= ClickGui.INSTANCE().moduleDistance.getValue() * 5;
            }
            if (this.getName().equals("Exploit")) {
                this.x2 -= ClickGui.INSTANCE().moduleDistance.getValue() * 6;
            }
            if (this.getName().equals("Client")) {
                this.x2 -= ClickGui.INSTANCE().moduleDistance.getValue() * 7;
            }
            if (this.getName().equals("Legacy")) {
                this.x2 -= ClickGui.INSTANCE().moduleDistance.getValue() * 8;
            }
            this.drag = true;
            return;
        }
        if (n3 == 1 && this.isHovering(n, n2)) {
            this.open = !this.open;
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(arg_0 -> Component.Clicked(n, n2, n3, arg_0));
    }

    public void mouseReleased(int n, int n2, int n3) {
        if (n3 == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(arg_0 -> Component.mouseReleased(n, n2, n3, arg_0));
    }

    public void onKeyTyped(char c, int n) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(arg_0 -> Component.onKeyTyped3(c, n, arg_0));
    }

    public void addButton(Button button) {
        this.items.add(button);
    }

    public int getX() {
        return this.old;
    }

    public void setX(int n) {
        this.x = n;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int n) {
        this.y = n;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int n) {
        this.width = n;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int n) {
        this.height = n;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean bl) {
        this.hidden = bl;
    }

    public boolean isOpen() {
        return this.open;
    }

    public final ArrayList<Item> getItems() {
        return this.items;
    }

    private boolean isHovering(int n, int n2) {
        return n >= this.getX() && n <= this.getX() + this.getWidth() && n2 >= this.getY() && n2 <= this.getY() + this.getHeight() - (this.open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float f = 0.0f;
        for (Item item : this.getItems()) {
            f += (float)item.getHeight() + 1.5f;
        }
        return f;
    }

    private static void onKeyTyped3(char c, int n, Item item) {
        item.onKeyTyped(c, n);
    }

    private static void mouseReleased(int n, int n2, int n3, Item item) {
        item.mouseReleased(n, n2, n3);
    }

    private static void Clicked(int n, int n2, int n3, Item item) {
        item.mouseClicked(n, n2, n3);
    }

    private static void mouseClicked0(Component component) {
        if (component.drag) {
            component.drag = false;
        }
    }
}

