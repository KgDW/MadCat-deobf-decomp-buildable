package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import me.madcat.features.modules.render.BlockHighLight;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.Setting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class GhostBlock
extends Module {
    public final Setting<Bind> bind = this.register(new Setting<>("Bind", new Bind(-1)));
    private boolean clicked = false;

    @Override
    public void onTick() {
        if (GhostBlock.mc.currentScreen == null && this.bind.getValue().isDown()) {
            RayTraceResult rayTraceResult;
            if (!this.clicked && (rayTraceResult = BlockHighLight.mc.objectMouseOver) != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = rayTraceResult.getBlockPos();
                GhostBlock.mc.world.setBlockToAir(blockPos);
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    public GhostBlock() {
        super("GhostBlock", "shady addons", Module.Category.MISC);
    }
}

