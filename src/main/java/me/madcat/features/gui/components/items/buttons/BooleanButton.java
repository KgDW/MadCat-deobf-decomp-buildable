package me.madcat.features.gui.components.items.buttons;

import me.madcat.MadCat;
import me.madcat.features.gui.Gui;
import me.madcat.features.gui.components.items.buttons.Button;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.client.FontMod;
import me.madcat.features.setting.Setting;
import me.madcat.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class BooleanButton
extends Button {
    private final Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(n, n2) ? MadCat.colorManager.getColorWithAlpha(ClickGui.INSTANCE().hoverAlpha.getValue()) : MadCat.colorManager.getColorWithAlpha(ClickGui.INSTANCE().alpha.getValue())) : (!this.isHovering(n, n2) ? 0x11555555 : -2007673515));
        if (!FontMod.INSTANCE().cfont.getValue() && FontMod.INSTANCE().isEnabled() || !FontMod.INSTANCE().isEnabled()) {
            MadCat.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - (float)Gui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            MadCat.textManager.drawStringClickGui(this.getName(), this.x + 2.3f, this.y - 0.7f - (float)Gui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void update() {
        this.setHidden(this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isHovering(n, n2)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void toggle() {
        this.setting.setValue(!((Boolean) this.setting.getValue()));
    }

    @Override
    public boolean getState() {
        return (Boolean)this.setting.getValue();
    }
}

