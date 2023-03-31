package me.madcat.features.modules.render;

import java.awt.Color;
import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Ambience
extends Module {
    public final Setting<Boolean> timeChanger;
    private final Setting<Integer> red2;
    public final Setting<Boolean> fogColor;
    private final Setting<Integer> time;
    private final Setting<Integer> blue2;
    public final Setting<Integer> blue;
    public final Setting<Boolean> lightMap = this.register(new Setting<>("LightMap", Boolean.FALSE));
    private final Setting<Integer> green2;
    public final Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255, this::new0));
    private final Setting<Boolean> rainbow;
    private static Ambience INSTANCE = new Ambience();
    public final Setting<Integer> green = this.register(new Setting<>("Green", 255, 0, 255, this::new1));

    private boolean new6(Integer n) {
        return this.fogColor.getValue();
    }

    @SubscribeEvent
    public void fog_density(EntityViewRenderEvent.FogDensity fogDensity) {
        if (!this.fogColor.getValue()) {
            return;
        }
        fogDensity.setDensity(0.0f);
        fogDensity.setCanceled(true);
    }

    @SubscribeEvent
    public void init(WorldEvent worldEvent) {
        if (this.timeChanger.getValue()) {
            worldEvent.getWorld().setWorldTime(this.time.getValue());
        }
    }

    private boolean new3(Integer n) {
        return this.timeChanger.getValue();
    }

    private boolean new4(Integer n) {
        return this.fogColor.getValue();
    }

    public static Ambience INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Ambience();
        }
        return INSTANCE;
    }

    private boolean new7(Boolean bl) {
        return this.fogColor.getValue();
    }

    private boolean new5(Integer n) {
        return this.fogColor.getValue();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private boolean new2(Integer n) {
        return this.lightMap.getValue();
    }

    public Ambience() {
        super("Ambience", "Changes the color of the world", Module.Category.RENDER);
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255, this::new2));
        this.timeChanger = this.register(new Setting<>("TimeChanger", Boolean.FALSE));
        this.fogColor = this.register(new Setting<>("FogColor", Boolean.FALSE));
        this.time = this.register(new Setting<>("Time", 0, 0, 24000, this::new3));
        this.red2 = this.register(new Setting<>("FogRed", 255, 0, 255, this::new4));
        this.green2 = this.register(new Setting<>("FogGreen", 255, 0, 255, this::new5));
        this.blue2 = this.register(new Setting<>("FogBlue", 255, 0, 255, this::new6));
        this.rainbow = this.register(new Setting<>("FogRainbow", Boolean.FALSE, this::new7));
        this.setInstance();
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive receive) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (receive.getPacket() instanceof SPacketTimeUpdate && this.timeChanger.getValue()) {
            receive.setCanceled(true);
        }
    }

    private boolean new0(Integer n) {
        return this.lightMap.getValue();
    }

    @SubscribeEvent
    public void fogColors(EntityViewRenderEvent.FogColors fogColors) {
        if (!this.fogColor.getValue()) {
            return;
        }
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], 0.8f, 0.8f);
        if (this.rainbow.getValue()) {
            fogColors.setRed((float)(n >> 16 & 0xFF) / 255.0f);
            fogColors.setGreen((float)(n >> 8 & 0xFF) / 255.0f);
            fogColors.setBlue((float)(n & 0xFF) / 255.0f);
        } else {
            fogColors.setRed((float) this.red2.getValue() / 255.0f);
            fogColors.setGreen((float) this.green2.getValue() / 255.0f);
            fogColors.setBlue((float) this.blue2.getValue() / 255.0f);
        }
    }

    private boolean new1(Integer n) {
        return this.lightMap.getValue();
    }
}

