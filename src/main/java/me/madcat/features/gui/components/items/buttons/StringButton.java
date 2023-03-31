package me.madcat.features.gui.components.items.buttons;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.features.gui.Gui;
import me.madcat.features.gui.components.items.buttons.Button;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.setting.Setting;
import me.madcat.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ChatAllowedCharacters;

public class StringButton
extends Button {
    private final Setting setting;
    public boolean isListening;
    private CurrentString currentString = new CurrentString("");

    public StringButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    public static String removeLastChar(String string) {
        String string2 = "";
        if (string != null && string.length() > 0) {
            string2 = string.substring(0, string.length() - 1);
        }
        return string2;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(n, n2) ? MadCat.colorManager.getColorWithAlpha(ClickGui.INSTANCE().hoverAlpha.getValue()) : MadCat.colorManager.getColorWithAlpha(ClickGui.INSTANCE().alpha.getValue())) : (!this.isHovering(n, n2) ? 0x11555555 : -2007673515));
        if (this.isListening) {
            MadCat.textManager.drawStringWithShadow(this.currentString.getString() + MadCat.textManager.getIdleSign(), this.x + 2.3f, this.y - 1.7f - (float)Gui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            MadCat.textManager.drawStringWithShadow((this.setting.getName().equals("Buttons") ? "Buttons " : (this.setting.getName().equals("Prefix") ? "Prefix  " + ChatFormatting.GRAY : "")) + this.setting.getValue(), this.x + 2.3f, this.y - 1.7f - (float)Gui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        if (this.isHovering(n, n2)) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(char c, int n) {
        if (this.isListening) {
            switch (n) {
                case 1: {
                    return;
                }
                case 28: {
                    this.enterString();
                }
                case 14: {
                    this.setString(StringButton.removeLastChar(this.currentString.getString()));
                }
            }
            if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                this.setString(this.currentString.getString() + c);
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(this.setting.isVisible());
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        } else {
            this.setting.setValue(this.currentString.getString());
        }
        this.setString("");
        this.onMouseClick();
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }

    public void setString(String string) {
        this.currentString = new CurrentString(string);
    }

    public static class CurrentString {
        private final String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}

