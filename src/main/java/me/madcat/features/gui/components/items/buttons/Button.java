package me.madcat.features.gui.components.items.buttons;

import me.madcat.MadCat;
import me.madcat.features.gui.Gui;
import me.madcat.features.gui.components.Component;
import me.madcat.features.gui.components.items.Item;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.client.FontMod;
import me.madcat.util.ColorUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class Button
extends Item {
    private boolean state;

    public Button(String string) {
        super(string);
        this.height = 15;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(n, n2) ? MadCat.colorManager.getColorWithAlpha(ClickGui.INSTANCE().hoverAlpha.getValue()) : MadCat.colorManager.getColorWithAlpha(ClickGui.INSTANCE().alpha.getValue())) : (!this.isHovering(n, n2) ? 0x11555555 : ColorUtil.toRGBA(185, 185, 185, ClickGui.INSTANCE().alpha.getValue())));
        if (ClickGui.INSTANCE().button.getValue()) {
            RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height - 0.5f, ColorUtil.toRGBA(200, 200, 200, 30));
        }
        if (!FontMod.INSTANCE().cfont.getValue() && FontMod.INSTANCE().isEnabled() || !FontMod.INSTANCE().isEnabled()) {
            MadCat.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 2.0f - (float)Gui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            MadCat.textManager.drawStringClickGui(this.getName(), this.x + 2.3f, this.y - 1.0f - (float)Gui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (n3 == 0 && this.isHovering(n, n2)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    public boolean isHovering(final int n, final int n2) {
        for (Component component : Gui.getClickGui().getComponents()) {
            if (!component.drag) {
                continue;
            }
            return false;
        }
        return n >= this.getX() && n <= this.getX() + this.getWidth() && n2 >= this.getY() && n2 <= this.getY() + this.height;
    }
}

