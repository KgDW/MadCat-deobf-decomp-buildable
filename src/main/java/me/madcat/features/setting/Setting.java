package me.madcat.features.setting;

import java.util.function.Predicate;
import me.madcat.event.events.ClientEvent;
import me.madcat.features.Feature;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.EnumConverter;
import net.minecraftforge.common.MinecraftForge;

public class Setting<T> {
    private final String name;
    private final T defaultValue;
    private T value;
    private T plannedValue;
    private T min;
    private T max;
    private boolean hasRestriction;
    private Predicate<T> visibility;
    private String description;
    private Feature feature;

    public Setting(String string, T t) {
        this.name = string;
        this.defaultValue = t;
        this.value = t;
        this.plannedValue = t;
        this.description = "";
    }

    public Setting(String string, T t, String string2) {
        this.name = string;
        this.defaultValue = t;
        this.value = t;
        this.plannedValue = t;
        this.description = string2;
    }

    public Setting(String string, T t, T t2, T t3, String string2) {
        this.name = string;
        this.defaultValue = t;
        this.value = t;
        this.min = t2;
        this.max = t3;
        this.plannedValue = t;
        this.description = string2;
        this.hasRestriction = true;
    }

    public Setting(String string, T t, T t2, T t3) {
        this.name = string;
        this.defaultValue = t;
        this.value = t;
        this.min = t2;
        this.max = t3;
        this.plannedValue = t;
        this.description = "";
        this.hasRestriction = true;
    }

    public Setting(String string, T t, T t2, T t3, Predicate<T> predicate, String string2) {
        this.name = string;
        this.defaultValue = t;
        this.value = t;
        this.min = t2;
        this.max = t3;
        this.plannedValue = t;
        this.visibility = predicate;
        this.description = string2;
        this.hasRestriction = true;
    }

    public Setting(String string, T t, T t2, T t3, Predicate<T> predicate) {
        this.name = string;
        this.defaultValue = t;
        this.value = t;
        this.min = t2;
        this.max = t3;
        this.plannedValue = t;
        this.visibility = predicate;
        this.description = "";
        this.hasRestriction = true;
    }

    public Setting(String string, T t, Predicate<T> predicate) {
        this.name = string;
        this.defaultValue = t;
        this.value = t;
        this.visibility = predicate;
        this.plannedValue = t;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T t) {
        this.setPlannedValue(t);
        if (this.hasRestriction) {
            if (((Number)this.min).floatValue() > ((Number)t).floatValue()) {
                this.setPlannedValue(this.min);
            }
            if (((Number)this.max).floatValue() < ((Number)t).floatValue()) {
                this.setPlannedValue(this.max);
            }
        }
        ClientEvent clientEvent = new ClientEvent(this);
        MinecraftForge.EVENT_BUS.post(clientEvent);
        if (!clientEvent.isCanceled()) {
            this.value = this.plannedValue;
        } else {
            this.plannedValue = this.value;
        }
    }

    public T getPlannedValue() {
        return this.plannedValue;
    }

    public void setPlannedValue(T t) {
        this.plannedValue = t;
    }

    public T getMin() {
        return this.min;
    }

    public void setMin(T t) {
        this.min = t;
    }

    public T getMax() {
        return this.max;
    }

    public void setMax(T t) {
        this.max = t;
    }

    public void setValueNoEvent(T t) {
        this.setPlannedValue(t);
        if (this.hasRestriction) {
            if (((Number)this.min).floatValue() > ((Number)t).floatValue()) {
                this.setPlannedValue(this.min);
            }
            if (((Number)this.max).floatValue() < ((Number)t).floatValue()) {
                this.setPlannedValue(this.max);
            }
        }
        this.value = this.plannedValue;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public int getEnum(String string) {
        for (int i = 0; i < this.value.getClass().getEnumConstants().length; ++i) {
            Enum enum_ = (Enum)this.value.getClass().getEnumConstants()[i];
            if (!enum_.name().equalsIgnoreCase(string)) {
                continue;
            }
            return i;
        }
        return -1;
    }

    public void setEnumValue(String string) {
        for (Enum enum_ : ((Enum)this.value).getClass().getEnumConstants()) {
            if (!enum_.name().equalsIgnoreCase(string)) continue;
            this.value = (T) enum_;
        }
    }

    public String currentEnumName() {
        return EnumConverter.getProperName((Enum)this.value);
    }

    public int currentEnum() {
        return EnumConverter.currentEnum((Enum)this.value);
    }

    public void increaseEnum() {
        this.plannedValue = (T) EnumConverter.increaseEnum((Enum)this.value);
        ClientEvent clientEvent = new ClientEvent(this);
        MinecraftForge.EVENT_BUS.post(clientEvent);
        if (!clientEvent.isCanceled()) {
            this.value = this.plannedValue;
        } else {
            this.plannedValue = this.value;
        }
    }

    public void increaseEnumNoEvent() {
        this.value = (T) EnumConverter.increaseEnum((Enum)this.value);
    }

    public String getType() {
        if (this.isEnumSetting()) {
            return "Enum";
        }
        return this.getClassName(this.defaultValue);
    }

    public <T> String getClassName(T t) {
        return t.getClass().getSimpleName();
    }

    public String getDescription() {
        if (this.description == null) {
            return "";
        }
        return this.description;
    }

    public boolean isNumberSetting() {
        return this.value instanceof Double || this.value instanceof Integer || this.value instanceof Short || this.value instanceof Long || this.value instanceof Float;
    }

    public boolean isEnumSetting() {
        return !this.isNumberSetting() && !(this.value instanceof String) && !(this.value instanceof Bind) && !(this.value instanceof Character) && !(this.value instanceof Boolean);
    }

    public boolean isStringSetting() {
        return this.value instanceof String;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public boolean hasRestriction() {
        return this.hasRestriction;
    }

    public void setVisibility(Predicate<T> predicate) {
        this.visibility = predicate;
    }

    public boolean isVisible() {
        if (this.visibility == null) {
            return false;
        }
        return !this.visibility.test(this.getValue());
    }
}

