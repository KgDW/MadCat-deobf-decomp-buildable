package me.madcat.features;

import java.util.ArrayList;
import java.util.List;
import me.madcat.MadCat;
import me.madcat.features.gui.Gui;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.manager.TextManager;
import me.madcat.util.Wrapper;

public class Feature
implements Wrapper {
    public final TextManager renderer = MadCat.textManager;
    public List<Setting> settings = new ArrayList<>();
    private String name;

    public Feature() {
    }

    public Feature(String string) {
        this.name = string;
    }

    public static boolean nullCheck() {
        return Feature.mc.player == null;
    }

    public static boolean fullNullCheck() {
        return Feature.mc.player == null || Feature.mc.world == null;
    }

    public String getName() {
        return this.name;
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }

    public boolean isEnabled() {
        if (this instanceof Module) {
            return ((Module)this).isOn();
        }
        return false;
    }

    public boolean isDisabled() {
        return !this.isEnabled();
    }

    public Setting register(Setting setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        if (this instanceof Module && Feature.mc.currentScreen instanceof Gui) {
            Gui.getInstance().updateModule((Module)this);
        }
        return setting;
    }

    public Setting add(Setting setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        if (this instanceof Module && Feature.mc.currentScreen instanceof Gui) {
            Gui.getInstance().updateModule((Module)this);
        }
        return setting;
    }

    public void unregister(Setting setting) {
        ArrayList<Setting> arrayList = new ArrayList<>();
        for (Setting setting2 : this.settings) {
            if (!setting2.equals(setting)) continue;
            arrayList.add(setting2);
        }
        if (!arrayList.isEmpty()) {
            this.settings.removeAll(arrayList);
        }
        if (this instanceof Module && Feature.mc.currentScreen instanceof Gui) {
            Gui.getInstance().updateModule((Module)this);
        }
    }

    public Setting getSettingByName(String string) {
        for (Setting setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(string)) continue;
            return setting;
        }
        return null;
    }

    public void reset() {
        for (Setting setting : this.settings) {
            setting.setValue(setting.getDefaultValue());
        }
    }

    public void clearSettings() {
        this.settings = new ArrayList<>();
    }
}

