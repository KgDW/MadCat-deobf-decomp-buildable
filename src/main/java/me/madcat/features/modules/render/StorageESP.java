package me.madcat.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.MathUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;

public class StorageESP
extends Module {
    private final Setting<Float> range = this.register(new Setting<>("Range", 50.0f, 1.0f, 300.0f));
    private final Setting<Boolean> chest = this.register(new Setting<>("Chest", true));
    private final Setting<Boolean> dispenser = this.register(new Setting<>("Dispenser", false));
    private final Setting<Boolean> shulker = this.register(new Setting<>("Shulker", true));
    private final Setting<Boolean> echest = this.register(new Setting<>("Ender Chest", true));
    private final Setting<Boolean> furnace = this.register(new Setting<>("Furnace", false));
    private final Setting<Boolean> hopper = this.register(new Setting<>("Hopper", false));
    private final Setting<Boolean> cart = this.register(new Setting<>("Minecart", false));
    private final Setting<Boolean> frame = this.register(new Setting<>("Item Frame", false));
    private final Setting<Boolean> box = this.register(new Setting<>("Box", false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", 125, 0, 255, this::new0));
    private final Setting<Boolean> outline = this.register(new Setting<>("Outline", true));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", 1.0f, 0.1f, 5.0f, this::new1));

    public StorageESP() {
        super("StorageESP", "Highlights Containers", Module.Category.RENDER);
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        int n;
        BlockPos blockPos;
        HashMap<BlockPos, Integer> hashMap = new HashMap<>();
        for (TileEntity object : StorageESP.mc.world.loadedTileEntityList) {
            if (!((object instanceof TileEntityChest && this.chest.getValue() || object instanceof TileEntityDispenser && this.dispenser.getValue() || object instanceof TileEntityShulkerBox && this.shulker.getValue() || object instanceof TileEntityEnderChest && this.echest.getValue() || object instanceof TileEntityFurnace && this.furnace.getValue() || object instanceof TileEntityHopper && this.hopper.getValue()) && StorageESP.mc.player.getDistanceSq(blockPos = object.getPos()) <= MathUtil.square(this.range.getValue()) && (n = this.getTileEntityColor(object)) != -1)) continue;
            hashMap.put(blockPos, n);
        }
        for (Entity entity : StorageESP.mc.world.loadedEntityList) {
            if (!((entity instanceof EntityItemFrame && this.frame.getValue() || entity instanceof EntityMinecartChest && this.cart.getValue()) && StorageESP.mc.player.getDistanceSq(blockPos = entity.getPosition()) <= MathUtil.square(this.range.getValue()) && (n = this.getEntityColor(entity)) != -1)) continue;
            hashMap.put(blockPos, n);
        }
        for (Map.Entry entry : hashMap.entrySet()) {
            BlockPos blockPos2 = (BlockPos)entry.getKey();
            n = (Integer)entry.getValue();
            RenderUtil.drawBoxESP(blockPos2, new Color(n), false, new Color(n), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }

    private int getTileEntityColor(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            return ColorUtil.Colors.BLUE;
        }
        if (tileEntity instanceof TileEntityShulkerBox) {
            return ColorUtil.Colors.RED;
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return ColorUtil.Colors.PURPLE;
        }
        if (tileEntity instanceof TileEntityFurnace) {
            return ColorUtil.Colors.GRAY;
        }
        if (tileEntity instanceof TileEntityHopper) {
            return ColorUtil.Colors.DARK_RED;
        }
        if (tileEntity instanceof TileEntityDispenser) {
            return ColorUtil.Colors.ORANGE;
        }
        return -1;
    }

    private int getEntityColor(Entity entity) {
        if (entity instanceof EntityMinecartChest) {
            return ColorUtil.Colors.ORANGE;
        }
        if (entity instanceof EntityItemFrame && ((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox) {
            return ColorUtil.Colors.YELLOW;
        }
        if (entity instanceof EntityItemFrame && !(((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox)) {
            return ColorUtil.Colors.ORANGE;
        }
        return -1;
    }

    private boolean new1(Object object) {
        return this.outline.getValue();
    }

    private boolean new0(Object object) {
        return this.box.getValue();
    }
}

