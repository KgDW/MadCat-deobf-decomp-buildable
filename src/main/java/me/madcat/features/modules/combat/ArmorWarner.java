package me.madcat.features.modules.combat;

import java.util.HashMap;
import java.util.Map;
import me.madcat.MadCat;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.DamageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ArmorWarner
extends Module {
    private final Setting<Boolean> notifySelf;
    private final Setting<Integer> armorThreshhold = this.register(new Setting<>("Armor%", 20, 1, 100));
    private final Map<EntityPlayer, Integer> entityArmorArraylist;

    public ArmorWarner() {
        super("ArmorWarner", "Message friends when their armor is low durable", Module.Category.COMBAT);
        this.notifySelf = this.register(new Setting<>("NotifySelf", true));
        this.entityArmorArraylist = new HashMap<>();
    }

    @Override
    public void onUpdate() {
        for (EntityPlayer entityPlayer : ArmorWarner.mc.world.playerEntities) {
            if (entityPlayer.isDead) continue;
            if (!MadCat.friendManager.isFriend(entityPlayer.getName())) {
                continue;
            }
            for (ItemStack itemStack : entityPlayer.inventory.armorInventory) {
                if (itemStack == ItemStack.EMPTY) {
                    continue;
                }
                int n = DamageUtil.getRoundedDamage(itemStack);
                if (n <= this.armorThreshhold.getValue() && !this.entityArmorArraylist.containsKey(entityPlayer)) {
                    if (entityPlayer == ArmorWarner.mc.player && this.notifySelf.getValue()) {
                        Command.sendMessage(entityPlayer.getName() + " watchout your " + this.getArmorPieceName(itemStack) + " low durable!");
                    } else {
                        ArmorWarner.mc.player.sendChatMessage("/msg " + entityPlayer.getName() + " " + entityPlayer.getName() + " watchout your " + this.getArmorPieceName(itemStack) + " low durable!");
                    }
                    this.entityArmorArraylist.put(entityPlayer, entityPlayer.inventory.armorInventory.indexOf(itemStack));
                }
                if (!this.entityArmorArraylist.containsKey(entityPlayer) || this.entityArmorArraylist.get(entityPlayer) != entityPlayer.inventory.armorInventory.indexOf(itemStack)) continue;
                if (n <= this.armorThreshhold.getValue()) {
                    continue;
                }
                this.entityArmorArraylist.remove(entityPlayer);
            }
            if (!this.entityArmorArraylist.containsKey(entityPlayer)) continue;
            if (entityPlayer.inventory.armorInventory.get(this.entityArmorArraylist.get(entityPlayer)) != ItemStack.EMPTY) {
                continue;
            }
            this.entityArmorArraylist.remove(entityPlayer);
        }
    }

    private String getArmorPieceName(ItemStack itemStack) {
        if (itemStack.getItem() == Items.DIAMOND_HELMET || itemStack.getItem() == Items.GOLDEN_HELMET || itemStack.getItem() == Items.IRON_HELMET || itemStack.getItem() == Items.CHAINMAIL_HELMET || itemStack.getItem() == Items.LEATHER_HELMET) {
            return "helmet is";
        }
        if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.IRON_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE) {
            return "chestplate is";
        }
        if (itemStack.getItem() == Items.DIAMOND_LEGGINGS || itemStack.getItem() == Items.GOLDEN_LEGGINGS || itemStack.getItem() == Items.IRON_LEGGINGS || itemStack.getItem() == Items.CHAINMAIL_LEGGINGS || itemStack.getItem() == Items.LEATHER_LEGGINGS) {
            return "leggings are";
        }
        return "boots are";
    }
}

