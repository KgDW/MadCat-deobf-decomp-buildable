package me.madcat.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.GraphicsEnvironment;
import me.madcat.MadCat;
import me.madcat.event.events.ClientEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FontMod
extends Module {
    private static FontMod INSTANCE = new FontMod();
    public final Setting<Boolean> cfont = this.register(new Setting<>("ClickGuiFont", Boolean.TRUE));
    public final Setting<Boolean> clientFont = this.register(new Setting<>("ClientFont", Boolean.TRUE, "test."));
    public final Setting<String> fontName = this.register(new Setting<>("FontName", "Arial", this::new0));
    public final Setting<Boolean> antiAlias = this.register(new Setting<>("AntiAlias", Boolean.TRUE, this::new1));
    public final Setting<Boolean> fractionalMetrics = this.register(new Setting<>("Metrics", Boolean.TRUE, this::new2));
    public final Setting<Integer> fontSize = this.register(new Setting<>("Size", 18, 12, 30, this::new3));
    public final Setting<Integer> fontStyle = this.register(new Setting<>("Style", 0, 0, 3, this::new4));
    private boolean reloadFont = false;

    public FontMod() {
        super("CustomFont", "Modify the font of client text", Module.Category.CLIENT);
        this.setInstance();
    }

    public static FontMod INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new FontMod();
        }
        return INSTANCE;
    }

    public static boolean checkFont(String string, boolean bl) {
        for (String string2 : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!bl && string2.equals(string)) {
                return true;
            }
            if (!bl) continue;
            Command.sendMessage(string2);
        }
        return false;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent clientEvent) {
        Setting setting;
        if (!this.clientFont.getValue()) {
            return;
        }
        if (clientEvent.getStage() == 2 && (setting = clientEvent.getSetting()) != null && setting.getFeature().equals(this)) {
            if (setting.getName().equals("FontName") && !FontMod.checkFont(setting.getPlannedValue().toString(), false)) {
                Command.sendMessage(ChatFormatting.RED + "That font doesnt exist.");
                clientEvent.setCanceled(true);
                return;
            }
            this.reloadFont = true;
        }
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (!this.clientFont.getValue()) {
            return;
        }
        if (this.reloadFont) {
            MadCat.textManager.init();
            this.reloadFont = false;
        }
    }

    private boolean new4(Integer n) {
        return this.clientFont.getValue();
    }

    private boolean new3(Integer n) {
        return this.clientFont.getValue();
    }

    private boolean new2(Boolean bl) {
        return this.clientFont.getValue();
    }

    private boolean new1(Boolean bl) {
        return this.clientFont.getValue();
    }

    private boolean new0(String string) {
        return this.clientFont.getValue();
    }
}

