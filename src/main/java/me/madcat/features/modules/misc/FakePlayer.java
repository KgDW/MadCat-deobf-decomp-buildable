package me.madcat.features.modules.misc;

import com.mojang.authlib.GameProfile;
import java.util.Random;
import java.util.UUID;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MoverType;
import net.minecraft.world.GameType;

public class FakePlayer
extends Module {
    public final Setting<Boolean> move;
    EntityOtherPlayerMP player = null;
    public final Setting<String> fakePlayer = this.register(new Setting<>("ClientName", "HuShuYu"));
    private final Setting<Integer> setHealth = this.register(new Setting<>("SetHealth", 20, 1, 36));

    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing", Module.Category.MISC);
        this.move = this.add(new Setting<>("Move", false));
    }

    @Override
    public String getDisplayInfo() {
        return this.fakePlayer.getValue();
    }

    @Override
    public void onTick() {
        if (this.player != null) {
            Random random = new Random();
            this.player.moveForward = FakePlayer.mc.player.moveForward + (float)random.nextInt(5) / 10.0f;
            this.player.moveStrafing = FakePlayer.mc.player.moveStrafing + (float)random.nextInt(5) / 10.0f;
            if (this.move.getValue()) {
                this.travel(this.player.moveStrafing, this.player.moveVertical, this.player.moveForward);
            }
        }
    }

    @Override
    public void onLogout() {
        if (this.isEnabled()) {
            this.disable();
        }
    }

    @Override
    public void onEnable() {
        Command.sendMessage("Spawned a fakeplayer with the name " + this.fakePlayer.getValue() + ".");
        this.player = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.fromString("0f75a81d-70e5-43c5-b892-f33c524284f2"), this.fakePlayer.getValue()));
        this.player.copyLocationAndAnglesFrom(FakePlayer.mc.player);
        this.player.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
        this.player.rotationYaw = FakePlayer.mc.player.rotationYaw;
        this.player.rotationPitch = FakePlayer.mc.player.rotationPitch;
        this.player.setGameType(GameType.SURVIVAL);
        this.player.inventory.copyInventory(FakePlayer.mc.player.inventory);
        this.player.setHealth((float) this.setHealth.getValue());
        FakePlayer.mc.world.addEntityToWorld(-12345, this.player);
        this.player.onLivingUpdate();
    }

    public void travel(float f, float f2, float f3) {
        double d = this.player.posY;
        float f4 = 0.8f;
        float f5 = 0.02f;
        float f6 = EnchantmentHelper.getDepthStriderModifier(this.player);
        if (f6 > 3.0f) {
            f6 = 3.0f;
        }
        if (!this.player.onGround) {
            f6 *= 0.5f;
        }
        if (f6 > 0.0f) {
            f4 += (0.54600006f - f4) * f6 / 3.0f;
            f5 += (this.player.getAIMoveSpeed() - f5) * f6 / 4.0f;
        }
        this.player.moveRelative(f, f2, f3, f5);
        this.player.move(MoverType.SELF, this.player.motionX, this.player.motionY, this.player.motionZ);
        this.player.motionX *= f4;
        this.player.motionY *= 0.8f;
        this.player.motionZ *= f4;
        if (!this.player.hasNoGravity()) {
            this.player.motionY -= 0.02;
        }
        if (this.player.collidedHorizontally && this.player.isOffsetPositionInLiquid(this.player.motionX, this.player.motionY + (double)0.6f - this.player.posY + d, this.player.motionZ)) {
            this.player.motionY = 0.3f;
        }
    }

    @Override
    public void onDisable() {
        if (FakePlayer.mc.world != null) {
            FakePlayer.mc.world.removeEntityFromWorld(-12345);
        }
    }
}

