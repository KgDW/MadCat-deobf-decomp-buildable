package me.madcat.features.modules.movement;

import java.util.Objects;
import me.madcat.features.modules.Module;
import net.minecraft.potion.Potion;

public class AntiLevitate
extends Module {
    public AntiLevitate() {
        super("AntiLevitate", "Removes shulker levitation effect", Module.Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (AntiLevitate.mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionFromResourceLocation("levitation")))) {
            AntiLevitate.mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
        }
    }
}

