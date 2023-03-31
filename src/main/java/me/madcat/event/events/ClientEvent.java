package me.madcat.event.events;

import me.madcat.event.EventStage;
import me.madcat.features.Feature;
import me.madcat.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ClientEvent
extends EventStage {
    private Feature feature;
    private Setting setting;

    public ClientEvent(int n, Feature feature) {
        super(n);
        this.feature = feature;
    }

    public ClientEvent(Setting setting) {
        super(2);
        this.setting = setting;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public Setting getSetting() {
        return this.setting;
    }
}

