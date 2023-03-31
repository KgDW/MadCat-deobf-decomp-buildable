package me.madcat.features.modules.player;

import me.madcat.MadCat;
import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockTweaks
extends Module {
    private static BlockTweaks INSTANCE = new BlockTweaks();
    public final Setting<Boolean> noFriendAttack;
    public final Setting<Boolean> autoTool = this.register(new Setting<>("AutoTool", false));
    public final Setting<Boolean> noGhost;
    private int lastHotbarSlot = -1;
    private boolean switched = false;
    private int currentTargetSlot = -1;

    public BlockTweaks() {
        super("BlockTweaks", "Some tweaks for blocks", Module.Category.PLAYER);
        this.noFriendAttack = this.register(new Setting<>("NoFriendAttack", false));
        this.noGhost = this.register(new Setting<>("NoGlitchBlocks", false));
        this.setInstance();
    }

    public static BlockTweaks INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new BlockTweaks();
        }
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        if (this.switched) {
            this.equip(this.lastHotbarSlot, false);
        }
        this.lastHotbarSlot = -1;
        this.currentTargetSlot = -1;
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.LeftClickBlock leftClickBlock) {
        if (this.autoTool.getValue() && !Feature.fullNullCheck() && leftClickBlock.getPos() != null) {
            this.equipBestTool(BlockTweaks.mc.world.getBlockState(leftClickBlock.getPos()));
        }
    }

    private void equipBestTool(IBlockState iBlockState) {
        int n = -1;
        double d = 0.0;
        for (int i = 0; i < 9; ++i) {
            int n2;
            float f;
            float f2;
            ItemStack itemStack = BlockTweaks.mc.player.inventory.getStackInSlot(i);
            if (itemStack.isEmpty() || !((f2 = itemStack.getDestroySpeed(iBlockState)) > 1.0f) || !((double)(f = (float)((double)f2 + ((n2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, itemStack)) > 0 ? Math.pow(n2, 2.0) + 1.0 : 0.0))) > d)) continue;
            d = f;
            n = i;
        }
        this.equip(n, true);
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void equip(int n, boolean bl) {
        if (n != -1) {
            if (n != BlockTweaks.mc.player.inventory.currentItem) {
                this.lastHotbarSlot = BlockTweaks.mc.player.inventory.currentItem;
            }
            this.currentTargetSlot = n;
            BlockTweaks.mc.player.inventory.currentItem = n;
            this.switched = bl;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        Entity entity;
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.noFriendAttack.getValue() && send.getPacket() instanceof CPacketUseEntity && (entity = ((CPacketUseEntity)send.getPacket()).getEntityFromWorld(BlockTweaks.mc.world)) != null && MadCat.friendManager.isFriend(entity.getName())) {
            send.setCanceled(true);
        }
    }

    private void removeGlitchBlocks(BlockPos blockPos) {
        for (int i = -4; i <= 4; ++i) {
            for (int j = -4; j <= 4; ++j) {
                for (int k = -4; k <= 4; ++k) {
                    BlockPos blockPos2 = new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k);
                    if (!BlockTweaks.mc.world.getBlockState(blockPos2).getBlock().equals(Blocks.AIR)) continue;
                    BlockTweaks.mc.playerController.processRightClickBlock(BlockTweaks.mc.player, BlockTweaks.mc.world, blockPos2, EnumFacing.DOWN, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent breakEvent) {
        if (Feature.fullNullCheck() || !this.noGhost.getValue()) {
            return;
        }
        if (!(BlockTweaks.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            this.removeGlitchBlocks(BlockTweaks.mc.player.getPosition());
        }
    }

    @Override
    public void onUpdate() {
        if (!Feature.fullNullCheck()) {
            if (BlockTweaks.mc.player.inventory.currentItem != this.lastHotbarSlot && BlockTweaks.mc.player.inventory.currentItem != this.currentTargetSlot) {
                this.lastHotbarSlot = BlockTweaks.mc.player.inventory.currentItem;
            }
            if (!BlockTweaks.mc.gameSettings.keyBindAttack.isKeyDown() && this.switched) {
                this.equip(this.lastHotbarSlot, false);
            }
        }
    }
}

