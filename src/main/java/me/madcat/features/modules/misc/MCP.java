package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import me.madcat.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class MCP
extends Module {
    private boolean clicked = false;

    public MCP() {
        super("MCP", "Throws a pearl", Module.Category.MISC);
    }

    @Override
    public void onTick() {
        if (MCP.mc.currentScreen == null && Mouse.isButtonDown(2)) {
            this.clicked = true;
        } else if (this.clicked) {
            this.throwPearl();
            this.clicked = false;
        }
    }

    private void throwPearl() {
        boolean bl;
        int n = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        boolean bl2 = bl = MCP.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL;
        if (n != -1 || bl) {
            int n2 = MCP.mc.player.inventory.currentItem;
            if (!bl) {
                InventoryUtil.switchToHotbarSlot(n, false);
            }
            MCP.mc.playerController.processRightClick(MCP.mc.player, MCP.mc.world, bl ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!bl) {
                InventoryUtil.switchToHotbarSlot(n2, false);
            }
        }
    }
}

