package me.madcat.features.modules.player;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;

public class PacketEat
extends Module {
    private static PacketEat INSTANCE = new PacketEat();
    private final Setting<Boolean> autoEat = this.register(new Setting<>("OnlyGappleAutoEAT", true));
    public final Setting<Float> health = this.register(new Setting<>("Health", 32.0f, 0.0f, 35.9f, this::new0));
    public final Setting<Float> hunger = this.register(new Setting<>("Hunger", 19.0f, 0.0f, 19.9f, this::new1));

    public PacketEat() {
        super("PacketEat", "PacketEat", Module.Category.PLAYER);
        this.setInstance();
    }

    public static PacketEat INSTANCE() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new PacketEat();
        return INSTANCE;
    }

    public static Module getInstance() {
        return null;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.autoEat.getValue()) {
            if (PacketEat.mc.player.isCreative()) {
                return;
            }
            if (PacketEat.mc.player.getHealth() + PacketEat.mc.player.getAbsorptionAmount() <= this.health.getValue() || (float)PacketEat.mc.player.getFoodStats().getFoodLevel() <= this.hunger.getValue()) {
                if (PacketEat.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.GOLDEN_APPLE) {
                    PacketEat.mc.playerController.processRightClick(PacketEat.mc.player, PacketEat.mc.world, EnumHand.MAIN_HAND);
                    return;
                }
                if (PacketEat.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.GOLDEN_APPLE) {
                    PacketEat.mc.playerController.processRightClick(PacketEat.mc.player, PacketEat.mc.world, EnumHand.OFF_HAND);
                }
            }
        }
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private boolean new1(Float f) {
        return this.autoEat.getValue();
    }

    private boolean new0(Float f) {
        return this.autoEat.getValue();
    }
}

