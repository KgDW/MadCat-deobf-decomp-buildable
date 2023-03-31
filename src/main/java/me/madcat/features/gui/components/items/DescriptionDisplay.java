package me.madcat.features.gui.components.items;

import me.madcat.MadCat;
import me.madcat.features.gui.components.items.Item;
import me.madcat.features.modules.client.FontMod;
import me.madcat.util.RenderUtil;

public class DescriptionDisplay
extends Item {
    private String description;
    private boolean draw;

    public DescriptionDisplay(String string, float f, float f2) {
        super("DescriptionDisplay");
        this.description = string;
        this.setLocation(f, f2);
        this.width = !FontMod.INSTANCE().cfont.getValue() && FontMod.INSTANCE().isEnabled() || !FontMod.INSTANCE().isEnabled() ? MadCat.textManager.getStringWidth(this.description) + 4 : MadCat.textManager.getStringCWidth(this.description) + 4;
        this.height = MadCat.textManager.getFontHeight() + 4;
        this.draw = false;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.width = !FontMod.INSTANCE().cfont.getValue() && FontMod.INSTANCE().isEnabled() || !FontMod.INSTANCE().isEnabled() ? MadCat.textManager.getStringWidth(this.description) + 4 : MadCat.textManager.getStringCWidth(this.description) + 5;
        this.height = MadCat.textManager.getFontHeight() + 4;
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, -704643072);
        if (!FontMod.INSTANCE().cfont.getValue() && FontMod.INSTANCE().isEnabled() || !FontMod.INSTANCE().isEnabled()) {
            MadCat.textManager.drawString(this.description, this.x + 2.0f, this.y + 2.0f, 0xFFFFFF, true);
        } else {
            MadCat.textManager.drawStringClickGui(this.description, this.x + 2.0f, this.y + 3.0f, 0xFFFFFF);
        }
    }

    public boolean shouldDraw() {
        return this.draw;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public void setDraw(boolean bl) {
        this.draw = bl;
    }
}

