package me.madcat.features.setting;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.lwjgl.input.Keyboard;

public class Bind {
    private int key;

    public Bind(int n) {
        this.key = n;
    }

    public static Bind none() {
        return new Bind(-1);
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int n) {
        this.key = n;
    }

    public boolean isEmpty() {
        return this.key < 0;
    }

    public String toString() {
        return this.isEmpty() ? "None" : (this.key < 0 ? "None" : this.capitalise(Keyboard.getKeyName(this.key)));
    }

    public boolean isDown() {
        return !this.isEmpty() && Keyboard.isKeyDown(this.getKey());
    }

    private String capitalise(String string) {
        if (string.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(string.charAt(0)) + (string.length() != 1 ? string.substring(1).toLowerCase() : "");
    }

    public static class BindConverter
            extends Converter<Bind, JsonElement> {
        public JsonElement doForward(Bind bind) {
            return new JsonPrimitive(bind.toString());
        }

        public Bind doBackward(JsonElement jsonElement) {
            String s = jsonElement.getAsString();
            if (s.equalsIgnoreCase("None")) {
                return Bind.none();
            }
            int key = -1;
            try {
                key = Keyboard.getKeyIndex(s.toUpperCase());
            } catch (Exception exception) {
                // empty catch block
            }
            if (key == 0) {
                return Bind.none();
            }
            return new Bind(key);
        }
    }
}