package me.madcat.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.madcat.features.Feature;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionManager
extends Feature {
    private final Map<EntityPlayer, PotionList> potions = new ConcurrentHashMap<>();

    public List<PotionEffect> getPlayerPotions(EntityPlayer entityPlayer) {
        PotionList potionList = this.potions.get(entityPlayer);
        List<PotionEffect> list = new ArrayList<>();
        if (potionList != null) {
            list = potionList.getEffects();
        }
        return list;
    }

    public PotionEffect[] getImportantPotions(EntityPlayer entityPlayer) {
        PotionEffect[] potionEffectArray = new PotionEffect[3];
        for (PotionEffect potionEffect : this.getPlayerPotions(entityPlayer)) {
            Potion potion = potionEffect.getPotion();
            switch (I18n.format(potion.getName()).toLowerCase()) {
                case "strength": {
                    potionEffectArray[0] = potionEffect;
                }
                case "weakness": {
                    potionEffectArray[1] = potionEffect;
                }
                case "speed": {
                    potionEffectArray[2] = potionEffect;
                }
            }
        }
        return potionEffectArray;
    }

    public String getColoredPotionString(PotionEffect potionEffect) {
        return this.getPotionString(potionEffect);
    }

    public List<PotionEffect> getOwnPotions() {
        return this.getPlayerPotions(PotionManager.mc.player);
    }

    public String getPotionString(PotionEffect potionEffect) {
        Potion potion = potionEffect.getPotion();
        return I18n.format(potion.getName(), new Object[0]) + " " + (potionEffect.getAmplifier() + 1) + " " + ChatFormatting.WHITE + Potion.getPotionDurationString(potionEffect, 1.0f);
    }

    public static class PotionList {
        private final List<PotionEffect> effects = new ArrayList<>();

        public List<PotionEffect> getEffects() {
            return this.effects;
        }

        public void addEffect(PotionEffect potionEffect) {
            if (potionEffect != null) {
                this.effects.add(potionEffect);
            }
        }
    }
}

