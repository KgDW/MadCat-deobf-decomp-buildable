package me.madcat.features.modules.render;

import java.awt.Color;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class BlockHighLight
extends Module {
    private final Setting<Integer> cAlpha = this.register(new Setting<>("alpha", 255, 0, 255));
    private final Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 0, 255));
    private final Setting<Boolean> rainbow = this.register(new Setting<>("Rainbow", true));

    public BlockHighLight() {
        super("BlockOutline", "Highlights the block u look at", Module.Category.RENDER);
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        RayTraceResult rayTraceResult = BlockHighLight.mc.objectMouseOver;
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = rayTraceResult.getBlockPos();
            RenderUtil.drawBBBox(blockPos, this.rainbow.getValue() ? ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.cAlpha.getValue()));
        }
    }
}

