package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.madcat.event.events.PlayerJumpEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    EntityPlayer player;
    
    public MixinEntityPlayer(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn);
    }
    
    @Inject(method = { "jump" }, at = { @At("HEAD") })
    public void onJump(final CallbackInfo ci) {
        if (Minecraft.getMinecraft().player.getName().equals(this.getName())) {
            MinecraftForge.EVENT_BUS.post(new PlayerJumpEvent(this.motionX, this.motionY));
        }
    }
}
