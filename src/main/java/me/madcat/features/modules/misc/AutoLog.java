package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class AutoLog
extends Module {
    public final Setting<Integer> totems = this.register(new Setting<>("Totems", 0, 0, 10));
    private final Setting<Float> health = this.register(new Setting<>("Health", 16.0f, 0.1f, 36.0f));
    private final Setting<Boolean> logout = this.register(new Setting<>("LogoutOff", true));

    public AutoLog() {
        super("AutoLog", "Logout when in danger", Module.Category.MISC);
    }

    @Override
    public void onTick() {
        int n = AutoLog.mc.player.inventory.mainInventory.stream().filter(AutoLog::onTick0).mapToInt(ItemStack::getCount).sum();
        if (AutoLog.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            n += AutoLog.mc.player.getHeldItemOffhand().getCount();
        }
        if (AutoLog.mc.player.getHealth() <= this.health.getValue() && ((float)n <= this.totems.getValue().floatValue() || (float)n == this.totems.getValue().floatValue())) {
            Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect(new TextComponentString("Internal Exception: java.lang.NullPointerException")));
            if (this.logout.getValue()) {
                this.disable();
            }
        }
    }

    private static boolean onTick0(ItemStack itemStack) {
        return itemStack.getItem() == Items.TOTEM_OF_UNDYING;
    }
}

