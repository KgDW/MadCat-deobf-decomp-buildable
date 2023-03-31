package me.madcat.features.modules.client;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.util.ResourceLocation;

public class Capes
extends Module {
    private static Capes instance;
    public final Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.Custom));
    public final Setting<String> Name = this.register(new Setting<>("CapeName", "custom", this::new0));

    public Capes() {
        super("Capes", "Renders the client's capes", Module.Category.CLIENT);
        instance = this;
    }

    public static Capes INSTANCE() {
        if (instance == null) {
            instance = new Capes();
        }
        return instance;
    }

    public ResourceLocation Cape() {
        if (this.mode.getValue() == Mode.Duck) {
            return new ResourceLocation("textures/capes/duck.png");
        }
        if (this.mode.getValue() == Mode.Lunar) {
            return new ResourceLocation("textures/capes/lunar.png");
        }
        if (this.mode.getValue() == Mode.OF) {
            return new ResourceLocation("textures/capes/of.png");
        }
        if (this.mode.getValue() == Mode.Moon) {
            return new ResourceLocation("textures/capes/moon.png");
        }
        if (this.mode.getValue() == Mode.Enderman) {
            return new ResourceLocation("textures/capes/enderman.png");
        }
        if (this.mode.getValue() == Mode.Panda) {
            return new ResourceLocation("textures/capes/panda.png");
        }
        if (this.mode.getValue() == Mode.Heart) {
            return new ResourceLocation("textures/capes/heart.png");
        }
        if (this.mode.getValue() == Mode.Purple) {
            return new ResourceLocation("textures/capes/purple.png");
        }
        if (this.mode.getValue() == Mode.Sad) {
            return new ResourceLocation("textures/capes/sad.png");
        }
        if (this.mode.getValue() == Mode.Shawchi) {
            return new ResourceLocation("textures/capes/shawchi.png");
        }
        if (this.mode.getValue() == Mode.Sad_two) {
            return new ResourceLocation("textures/capes/sad_two.png");
        }
        if (this.mode.getValue() == Mode.Custom) {
            return new ResourceLocation("textures/capes/" + this.Name.getValue() + ".png");
        }
        return null;
    }

    private boolean new0(String string) {
        return this.mode.getValue() == Mode.Custom;
    }

    public enum Mode {
        Custom,
        Duck,
        Lunar,
        OF,
        Moon,
        Enderman,
        Panda,
        Heart,
        Purple,
        Sad,
        Shawchi,
        Sad_two

    }
}

