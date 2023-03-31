package me.madcat.mixin.mixins;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import me.madcat.event.events.PerspectiveEvent;
import me.madcat.features.modules.misc.NoEntityTrace;
import me.madcat.features.modules.render.Ambience;
import me.madcat.features.modules.render.CameraClip;
import me.madcat.features.modules.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class})
public class MixinEntityRenderer {
    public ItemStack itemActivationItem;
    final Minecraft mc = Minecraft.getMinecraft();

    @Redirect(method={"getMouseOver"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List getEntitiesInAABBexcluding(WorldClient worldClient, Entity entity, AxisAlignedBB axisAlignedBB, Predicate predicate) {
        if (NoEntityTrace.INSTANCE().isOn()) {
            return new ArrayList<Entity>();
        }
        return worldClient.getEntitiesInAABBexcluding(entity, axisAlignedBB, predicate);
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    public void hurtCameraEffect(float f, CallbackInfo callbackInfo) {
        if (NoRender.INSTANCE.isEnabled() && NoRender.INSTANCE().hurtCam.getValue()) {
            callbackInfo.cancel();
        }
    }

    @ModifyVariable(method={"orientCamera"}, ordinal=3, at=@At(value="STORE", ordinal=0), require=1)
    public double changeCameraDistanceHook(double d) {
        if (CameraClip.Instance().isEnabled()) {
            return CameraClip.Instance().distance.getValue();
        }
        return d;
    }

    @Inject(method={"renderItemActivation"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemActivationHook(CallbackInfo callbackInfo) {
        if (this.itemActivationItem != null && NoRender.INSTANCE().isOn() && NoRender.INSTANCE().totemPops.getValue().booleanValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            callbackInfo.cancel();
        }
    }

    @ModifyVariable(method={"orientCamera"}, ordinal=7, at=@At(value="STORE", ordinal=0), require=1)
    public double orientCameraHook(double d) {
        if (CameraClip.Instance().isEnabled()) {
            return CameraClip.Instance().distance.getValue();
        }
        return d;
    }

    @Redirect(method={"setupCameraTransform"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onSetupCameraTransform(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent((float)this.mc.displayWidth / (float)this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post((Event)perspectiveEvent);
        Project.gluPerspective((float)f, (float)perspectiveEvent.getAspect(), (float)f3, (float)f4);
    }

    @Redirect(method={"renderWorldPass"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderWorldPass(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent((float)this.mc.displayWidth / (float)this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post((Event)perspectiveEvent);
        Project.gluPerspective((float)f, (float)perspectiveEvent.getAspect(), (float)f3, (float)f4);
    }

    @Redirect(method={"renderCloudsCheck"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderCloudsCheck(float f, float f2, float f3, float f4) {
        PerspectiveEvent perspectiveEvent = new PerspectiveEvent((float)this.mc.displayWidth / (float)this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post((Event)perspectiveEvent);
        Project.gluPerspective((float)f, (float)perspectiveEvent.getAspect(), (float)f3, (float)f4);
    }

    @ModifyVariable(method={"updateLightmap"}, at=@At(value="STORE"), index=20)
    public int red(int n) {
        Ambience ambience = Ambience.INSTANCE();
        if (ambience.isOn() && ambience.lightMap.getValue()) {
            n = ambience.red.getValue();
        }
        return n;
    }

    @ModifyVariable(method={"updateLightmap"}, at=@At(value="STORE"), index=21)
    public int green(int n) {
        Ambience ambience = Ambience.INSTANCE();
        if (ambience.isOn() && ambience.lightMap.getValue()) {
            n = ambience.green.getValue();
        }
        return n;
    }

    @ModifyVariable(method={"updateLightmap"}, at=@At(value="STORE"), index=22)
    public int blue(int n) {
        Ambience ambience = Ambience.INSTANCE();
        if (ambience.isOn() && ambience.lightMap.getValue()) {
            n = ambience.blue.getValue();
        }
        return n;
    }
}
 