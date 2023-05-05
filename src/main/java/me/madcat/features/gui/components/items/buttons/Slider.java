package me.madcat.features.gui.components.items.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.features.gui.Gui;
import me.madcat.features.gui.components.Component;
import me.madcat.features.gui.components.items.buttons.Button;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.client.FontMod;
import me.madcat.features.setting.Setting;
import me.madcat.util.RenderUtil;
import org.lwjgl.input.Mouse;

public class Slider
extends Button {
    public final Setting setting;
    private final Number min;
    private final Number max;
    private final int difference;

    public Slider(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = (Number)setting.getMin();
        this.max = (Number)setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 15;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.dragSetting(n, n2);
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, !this.isHovering(n, n2) ? 0x11555555 : -2007673515);
        RenderUtil.drawRect(this.x, this.y, ((Number)this.setting.getValue()).floatValue() <= this.min.floatValue() ? this.x : this.x + ((float)this.width + 7.4f) * this.partialMultiplier(), this.y + (float)this.height - 0.5f, !this.isHovering(n, n2) ? MadCat.colorManager.getColorWithAlpha(ClickGui.INSTANCE().hoverAlpha.getValue()) : MadCat.colorManager.getColorWithAlpha(ClickGui.INSTANCE().alpha.getValue()));
        if (!FontMod.INSTANCE().cfont.getValue() && FontMod.INSTANCE().isEnabled() || !FontMod.INSTANCE().isEnabled()) {
            MadCat.textManager.drawStringWithShadow(this.getName() + " " + ChatFormatting.GRAY + (this.setting.getValue() instanceof Float ? this.setting.getValue() : Double.valueOf(((Number)this.setting.getValue()).doubleValue())), this.x + 2.3f, this.y - 1.7f - (float)Gui.getClickGui().getTextOffset(), -1);
        } else {
            MadCat.textManager.drawStringClickGui(this.getName() + " " + ChatFormatting.GRAY + (this.setting.getValue() instanceof Float ? this.setting.getValue() : Double.valueOf(((Number)this.setting.getValue()).doubleValue())), this.x + 2.3f, this.y - 0.7f - (float)Gui.getClickGui().getTextOffset(), -1);
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isHovering(n, n2)) {
            this.setSettingFromX(n);
        }
    }

    @Override
    public boolean isHovering(final int n, final int n2) {
        for (Component component : Gui.getClickGui().getComponents()) {
            if (!component.drag) {
                continue;
            }
            return false;
        }
        return n >= this.getX() && n <= this.getX() + this.getWidth() + 8.0f && n2 >= this.getY() && n2 <= this.getY() + this.height;
    }

    @Override
    public void update() {
        this.setHidden(this.setting.isVisible());
    }

    private void dragSetting(int n, int n2) {
        if (this.isHovering(n, n2) && Mouse.isButtonDown(0)) {
            this.setSettingFromX(n);
        }
    }

    private void setSettingFromX(int n) {
        float f = ((float)n - this.x) / ((float)this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            double d = (Double)this.setting.getMin() + (double)((float)this.difference * f);
            this.setting.setValue((double)Math.round(10.0 * d) / 10.0);
        } else if (this.setting.getValue() instanceof Float) {
            float f2 = (Float) this.setting.getMin() + (float)this.difference * f;
            this.setting.setValue((float) Math.round(10.0f * f2) / 10.0f);
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((Integer)this.setting.getMin() + (int)((float)this.difference * f));
        }
    }

    private float middle() {
        return this.max.floatValue() - this.min.floatValue();
    }

    private float part() {
        return ((Number)this.setting.getValue()).floatValue() - this.min.floatValue();
    }

    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}

