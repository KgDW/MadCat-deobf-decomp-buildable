package me.madcat.features.gui.components.items.buttons;

import java.util.ArrayList;
import java.util.List;
import me.madcat.MadCat;
import me.madcat.features.gui.Gui;
import me.madcat.features.gui.components.Component;
import me.madcat.features.gui.components.items.DescriptionDisplay;
import me.madcat.features.gui.components.items.Item;
import me.madcat.features.gui.components.items.buttons.BindButton;
import me.madcat.features.gui.components.items.buttons.BooleanButton;
import me.madcat.features.gui.components.items.buttons.Button;
import me.madcat.features.gui.components.items.buttons.EnumButton;
import me.madcat.features.gui.components.items.buttons.Slider;
import me.madcat.features.gui.components.items.buttons.StringButton;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ModuleButton
extends Button {
    private final Module module;
    private int logs;
    private List<Item> items = new ArrayList<>();
    private boolean subOpen;

    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;
        this.initSettings();
    }

    public static void drawCompleteImage(float f, float f2, int n, int n2) {
        GL11.glPushMatrix();
        GL11.glTranslatef(f, f2, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, (float)n2, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f((float)n, (float)n2, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f((float)n, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static float fa(float f) {
        if ((f %= 360.0f) < -180.0f) {
            f += 360.0f;
        }
        return f;
    }

    public static void drawModalRect(int n, int n2, float f, float f2, int n3, int n4, int n5, int n6, float f3, float f4) {
        net.minecraft.client.gui.Gui.drawScaledCustomSizeModalRect(n, n2, f, f2, n3, n4, n5, n6, f3, f4);
    }

    public void initSettings() {
        ArrayList<Item> arrayList = new ArrayList<>();
        if (!this.module.getSettings().isEmpty()) {
            for (Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    arrayList.add(new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    arrayList.add(new BindButton(setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    arrayList.add(new StringButton(setting));
                }
                if (setting.isNumberSetting() && setting.hasRestriction()) {
                    arrayList.add(new Slider(setting));
                    continue;
                }
                if (!setting.isEnumSetting()) continue;
                arrayList.add(new EnumButton(setting));
            }
        }
        arrayList.add(new BindButton(this.module.getSettingByName("Keybind")));
        this.items = arrayList;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (this.isHovering(n, n2)) {
            DescriptionDisplay descriptionDisplay = Gui.INSTANCE().getDescriptionDisplay();
            descriptionDisplay.setDescription(this.module.getDescription());
            descriptionDisplay.setLocation(n + 2, n2 + 1);
            descriptionDisplay.setDraw(true);
        }
        super.drawScreen(n, n2, f);
        if (!this.items.isEmpty()) {
            if (ClickGui.INSTANCE().iconmode.getValue() == 2 || ClickGui.INSTANCE().iconmode.getValue() == 4 || ClickGui.INSTANCE().iconmode.getValue() == 5) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                if (ClickGui.INSTANCE().iconmode.getValue() == 2) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/setting.png"));
                }
                if (ClickGui.INSTANCE().iconmode.getValue() == 5) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/setting3.png"));
                }
                if (ClickGui.INSTANCE().iconmode.getValue() == 4) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/setting4.png"));
                }
                GlStateManager.translate(this.getX() + (float)this.getWidth() - 6.7f, this.getY() + 7.7f - 0.3f, 0.0f);
                GlStateManager.rotate(ModuleButton.fa(this.logs), 0.0f, 0.0f, 1.0f);
                ModuleButton.drawModalRect(-5, -5, 0.0f, 0.0f, 10, 10, 10, 10, 10.0f, 10.0f);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (this.subOpen) {
                    float f2 = 1.0f;
                    this.logs += 5;
                    for (Item item : this.items) {
                        Component.counter1[0] = Component.counter1[0] + 1;
                        if (!item.isHidden()) {
                            item.setLocation(this.x + 1.0f, this.y + (f2 += 15.0f));
                            item.setHeight(15);
                            item.setWidth(this.width - 9);
                            item.drawScreen(n, n2, f);
                        }
                        item.update();
                    }
                } else {
                    this.logs = 0;
                }
            } else if (ClickGui.INSTANCE().iconmode.getValue() == 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                MadCat.textManager.drawStringlogo("A", this.x - 4.5f + (float)this.width - 7.4f, this.y - 2.2f - (float)Gui.getClickGui().getTextOffset(), -1);
                this.icon(n, n2, f);
            } else if (ClickGui.INSTANCE().iconmode.getValue() == 1) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/setting2.png"));
                ModuleButton.drawCompleteImage(this.x - 1.5f + (float)this.width - 7.4f, this.y - 2.2f - (float)Gui.getClickGui().getTextOffset(), 8, 8);
                this.icon(n, n2, f);
            } else if (ClickGui.INSTANCE().iconmode.getValue() == 3) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                if (this.subOpen) {
                    MadCat.textManager.drawString("-", this.x + (float)this.width - 7.4f, this.y - 2.2f - (float)Gui.getClickGui().getTextOffset(), -1, true);
                } else {
                    MadCat.textManager.drawString("+", this.x + (float)this.width - 7.4f, this.y - 2.2f - (float)Gui.getClickGui().getTextOffset(), -1, true);
                }
                this.icon(n, n2, f);
            } else if (ClickGui.INSTANCE().iconmode.getValue() == 6) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                this.icon(n, n2, f);
            }
        }
    }

    private void icon(int n, int n2, float f) {
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        if (this.subOpen) {
            float f2 = 1.0f;
            for (Item item : this.items) {
                Component.counter1[0] = Component.counter1[0] + 1;
                if (!item.isHidden()) {
                    item.setLocation(this.x + 1.0f, this.y + (f2 += 15.0f));
                    item.setHeight(15);
                    item.setWidth(this.width - 9);
                    item.drawScreen(n, n2, f);
                }
                item.update();
            }
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (!this.items.isEmpty()) {
            if (n3 == 1 && this.isHovering(n, n2)) {
                this.subOpen = !this.subOpen;
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            if (this.subOpen) {
                for (Item item : this.items) {
                    if (item.isHidden()) continue;
                    item.mouseClicked(n, n2, n3);
                }
            }
        }
    }

    @Override
    public void onKeyTyped(char c, int n) {
        super.onKeyTyped(c, n);
        if (!this.items.isEmpty() && this.subOpen) {
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                item.onKeyTyped(c, n);
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.subOpen) {
            int n = 14;
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                n += item.getHeight() + 1;
            }
            return n + 2;
        }
        return 14;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}

