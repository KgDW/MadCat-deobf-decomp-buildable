package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class BlockFly
extends Module {
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", false));
    private final Timer timer = new Timer();
    private final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    private final Setting<Boolean> tick = this.register(new Setting<>("TickMode", true));
    private final Setting<Boolean> down = this.register(new Setting<>("Down", true));
    private final Setting<Boolean> sameY = this.register(new Setting<>("SameY", false));
    private final Setting<Float> yCheck = this.register(new Setting<>("YCheck", 2.5f, 2.5f, 12.0f, this::new0));
    private final Setting<Boolean> airCheck = this.register(new Setting<>("AirCheck", Boolean.TRUE, this::new1));
    public final Setting<Boolean> search = this.register(new Setting<>("Search", Boolean.TRUE));
    public final Setting<Boolean> searchUp = this.register(new Setting<>("SearchUp", false));
    BlockPos PlacePos;

    public BlockFly() {
        super("BlockFly", "Places Blocks underneath you", Module.Category.MOVEMENT);
    }

    public static boolean isMoving(EntityLivingBase entityLivingBase) {
        return entityLivingBase.moveForward == 0.0f && entityLivingBase.moveStrafing == 0.0f;
    }

    @Override
    public void onEnable() {
        this.PlacePos = new BlockPos(BlockFly.mc.player.posX, BlockFly.mc.player.posY - 1.0, BlockFly.mc.player.posZ);
        this.timer.reset();
    }

    @Override
    public void onTick() {
        if (!this.tick.getValue()) {
            return;
        }
        this.doScaffold();
    }

    @Override
    public void onUpdate() {
        if (this.tick.getValue()) {
            return;
        }
        this.doScaffold();
    }

    public void doScaffold() {
        if (!BlockFly.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.timer.reset();
        }
        this.PlacePos = !this.sameY.getValue() || BlockFly.mc.player.posY - (double)this.PlacePos.getY() > (double) this.yCheck.getValue() || this.airCheck.getValue() && !BlockFly.mc.world.isAirBlock(new BlockPos(BlockFly.mc.player.posX, BlockFly.mc.player.posY - 1.0, BlockFly.mc.player.posZ)) || BlockFly.mc.gameSettings.keyBindJump.isKeyDown() && BlockFly.isMoving(BlockFly.mc.player) || new BlockPos(BlockFly.mc.player.posX, BlockFly.mc.player.posY - 1.0, BlockFly.mc.player.posZ).getY() < this.PlacePos.getY() ? new BlockPos(BlockFly.mc.player.posX, BlockFly.mc.player.posY - 1.0, BlockFly.mc.player.posZ) : new BlockPos(BlockFly.mc.player.posX, this.PlacePos.getY(), BlockFly.mc.player.posZ);
        if (this.down.getValue() && BlockFly.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.PlacePos = new BlockPos(BlockFly.mc.player.posX, BlockFly.mc.player.posY - 2.0, BlockFly.mc.player.posZ);
        }
        this.place(this.PlacePos);
    }
    
    public void place(BlockPos blockPos) {
        int n;
        if (blockPos.getDistance((int)BlockFly.mc.player.posX, (int)BlockFly.mc.player.posY, (int)BlockFly.mc.player.posZ) > 6.0) {
            return;
        }
        BlockPos blockPos2 = blockPos;
        if (BlockFly.mc.world.getBlockState(blockPos2).getBlock() != Blocks.AIR) {
            return;
        }
        if (this.search.getValue() && BlockUtil.cantBlockPlace2(blockPos2)) {
            if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 0, 1))) {
                blockPos2 = blockPos2.add(0, 0, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 0, -1))) {
                blockPos2 = blockPos2.add(0, 0, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 0, 0))) {
                blockPos2 = blockPos2.add(-1, 0, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 0, 0))) {
                blockPos2 = blockPos2.add(1, 0, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -1, 0))) {
                blockPos2 = blockPos2.add(0, -1, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 0, 2))) {
                blockPos2 = blockPos2.add(0, 0, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 0, -2))) {
                blockPos2 = blockPos2.add(0, 0, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 0, 0))) {
                blockPos2 = blockPos2.add(-2, 0, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 0, 0))) {
                blockPos2 = blockPos2.add(2, 0, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -2, 0))) {
                blockPos2 = blockPos2.add(0, -2, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 0, -1))) {
                blockPos2 = blockPos2.add(1, 0, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 0, 1))) {
                blockPos2 = blockPos2.add(-1, 0, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 0, 1))) {
                blockPos2 = blockPos2.add(1, 0, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 0, -1))) {
                blockPos2 = blockPos2.add(-1, 0, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, -1, 0))) {
                blockPos2 = blockPos2.add(1, -1, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -1, 1))) {
                blockPos2 = blockPos2.add(0, -1, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, -1, 0))) {
                blockPos2 = blockPos2.add(-1, -1, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -1, -1))) {
                blockPos2 = blockPos2.add(0, -1, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -2, 1))) {
                blockPos2 = blockPos2.add(0, -2, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, -2, 0))) {
                blockPos2 = blockPos2.add(1, -2, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -2, -1))) {
                blockPos2 = blockPos2.add(0, -2, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, -2, 0))) {
                blockPos2 = blockPos2.add(-1, -2, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 0, 3))) {
                blockPos2 = blockPos2.add(0, 0, 3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 0, -3))) {
                blockPos2 = blockPos2.add(0, 0, -3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 0, 0))) {
                blockPos2 = blockPos2.add(-3, 0, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 0, 0))) {
                blockPos2 = blockPos2.add(3, 0, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 0, 3))) {
                blockPos2 = blockPos2.add(1, 0, 3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 0, -3))) {
                blockPos2 = blockPos2.add(1, 0, -3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 0, 1))) {
                blockPos2 = blockPos2.add(-3, 0, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 0, 1))) {
                blockPos2 = blockPos2.add(3, 0, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 0, 3))) {
                blockPos2 = blockPos2.add(-1, 0, 3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 0, -3))) {
                blockPos2 = blockPos2.add(-1, 0, -3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 0, -1))) {
                blockPos2 = blockPos2.add(-3, 0, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 0, -1))) {
                blockPos2 = blockPos2.add(3, 0, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, -1, 3))) {
                blockPos2 = blockPos2.add(1, -1, 3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, -1, -3))) {
                blockPos2 = blockPos2.add(1, -1, -3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, -1, 1))) {
                blockPos2 = blockPos2.add(-3, -1, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, -1, 1))) {
                blockPos2 = blockPos2.add(3, -1, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, -1, 3))) {
                blockPos2 = blockPos2.add(-1, -1, 3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, -1, -3))) {
                blockPos2 = blockPos2.add(-1, -1, -3);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, -1, -1))) {
                blockPos2 = blockPos2.add(-3, -1, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, -1, -1))) {
                blockPos2 = blockPos2.add(3, -1, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, -1, -1))) {
                blockPos2 = blockPos2.add(2, -1, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, -1, 1))) {
                blockPos2 = blockPos2.add(-2, -1, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, -1, 1))) {
                blockPos2 = blockPos2.add(2, -1, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, -1, -1))) {
                blockPos2 = blockPos2.add(-2, -1, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, -1, -2))) {
                blockPos2 = blockPos2.add(1, -1, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, -1, 2))) {
                blockPos2 = blockPos2.add(-1, -1, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, -1, 2))) {
                blockPos2 = blockPos2.add(1, -1, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, -1, -2))) {
                blockPos2 = blockPos2.add(-1, -1, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -1, 2))) {
                blockPos2 = blockPos2.add(0, -1, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -1, -2))) {
                blockPos2 = blockPos2.add(0, -1, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, -1, 0))) {
                blockPos2 = blockPos2.add(-2, -1, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, -1, 0))) {
                blockPos2 = blockPos2.add(2, -1, 0);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 0, -1))) {
                blockPos2 = blockPos2.add(2, 0, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 0, 1))) {
                blockPos2 = blockPos2.add(-2, 0, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 0, 1))) {
                blockPos2 = blockPos2.add(2, 0, 1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 0, -1))) {
                blockPos2 = blockPos2.add(-2, 0, -1);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 0, -2))) {
                blockPos2 = blockPos2.add(1, 0, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 0, 2))) {
                blockPos2 = blockPos2.add(-1, 0, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 0, 2))) {
                blockPos2 = blockPos2.add(1, 0, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 0, -2))) {
                blockPos2 = blockPos2.add(-1, 0, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, -1, -2))) {
                blockPos2 = blockPos2.add(2, -1, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, -1, 2))) {
                blockPos2 = blockPos2.add(-2, -1, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, -1, 2))) {
                blockPos2 = blockPos2.add(2, -1, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, -1, -2))) {
                blockPos2 = blockPos2.add(-2, -1, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, -1, -2))) {
                blockPos2 = blockPos2.add(2, -1, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, -1, 2))) {
                blockPos2 = blockPos2.add(-2, -1, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, -1, 2))) {
                blockPos2 = blockPos2.add(2, -1, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, -1, -2))) {
                blockPos2 = blockPos2.add(-2, -1, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -1, 2))) {
                blockPos2 = blockPos2.add(0, -1, 2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, -1, -2))) {
                blockPos2 = blockPos2.add(0, -1, -2);
            } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, -1, 0))) {
                blockPos2 = blockPos2.add(-2, -1, 0);
            } else {
                if (!this.searchUp.getValue()) return;
                if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 1, 0))) {
                    blockPos2 = blockPos2.add(0, 1, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 1, 0))) {
                    blockPos2 = blockPos2.add(1, 1, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 1, 1))) {
                    blockPos2 = blockPos2.add(0, 1, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 1, 0))) {
                    blockPos2 = blockPos2.add(-1, 1, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 1, -1))) {
                    blockPos2 = blockPos2.add(0, 1, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 1, 3))) {
                    blockPos2 = blockPos2.add(1, 1, 3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 1, -3))) {
                    blockPos2 = blockPos2.add(1, 1, -3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 1, 1))) {
                    blockPos2 = blockPos2.add(-3, 1, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 1, 1))) {
                    blockPos2 = blockPos2.add(3, 1, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 1, 3))) {
                    blockPos2 = blockPos2.add(-1, 1, 3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 1, -3))) {
                    blockPos2 = blockPos2.add(-1, 1, -3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 1, -1))) {
                    blockPos2 = blockPos2.add(-3, 1, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 1, -1))) {
                    blockPos2 = blockPos2.add(3, 1, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 1, -1))) {
                    blockPos2 = blockPos2.add(2, 1, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 1, 1))) {
                    blockPos2 = blockPos2.add(-2, 1, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 1, 1))) {
                    blockPos2 = blockPos2.add(2, 1, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 1, -1))) {
                    blockPos2 = blockPos2.add(-2, 1, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 1, -2))) {
                    blockPos2 = blockPos2.add(1, 1, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 1, 2))) {
                    blockPos2 = blockPos2.add(-1, 1, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 1, 2))) {
                    blockPos2 = blockPos2.add(1, 1, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 1, -2))) {
                    blockPos2 = blockPos2.add(-1, 1, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 1, 2))) {
                    blockPos2 = blockPos2.add(0, 1, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 1, -2))) {
                    blockPos2 = blockPos2.add(0, 1, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 1, 0))) {
                    blockPos2 = blockPos2.add(-2, 1, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 1, 0))) {
                    blockPos2 = blockPos2.add(2, 1, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 1, -2))) {
                    blockPos2 = blockPos2.add(2, 1, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 1, 2))) {
                    blockPos2 = blockPos2.add(-2, 1, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 1, 2))) {
                    blockPos2 = blockPos2.add(2, 1, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 1, -2))) {
                    blockPos2 = blockPos2.add(-2, 1, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 1, -2))) {
                    blockPos2 = blockPos2.add(2, 1, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 1, 2))) {
                    blockPos2 = blockPos2.add(-2, 1, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 1, 2))) {
                    blockPos2 = blockPos2.add(2, 1, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 1, -2))) {
                    blockPos2 = blockPos2.add(-2, 1, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 1, 2))) {
                    blockPos2 = blockPos2.add(0, 1, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 1, -2))) {
                    blockPos2 = blockPos2.add(0, 1, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 1, 0))) {
                    blockPos2 = blockPos2.add(-2, 1, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 2, 0))) {
                    blockPos2 = blockPos2.add(0, 2, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 2, 0))) {
                    blockPos2 = blockPos2.add(1, 2, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 2, 1))) {
                    blockPos2 = blockPos2.add(0, 2, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 2, 0))) {
                    blockPos2 = blockPos2.add(-1, 2, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 2, -1))) {
                    blockPos2 = blockPos2.add(0, 2, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 2, 3))) {
                    blockPos2 = blockPos2.add(1, 2, 3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 2, -3))) {
                    blockPos2 = blockPos2.add(1, 2, -3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 2, 1))) {
                    blockPos2 = blockPos2.add(-3, 2, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 2, 1))) {
                    blockPos2 = blockPos2.add(3, 2, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 2, 3))) {
                    blockPos2 = blockPos2.add(-1, 2, 3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 2, -3))) {
                    blockPos2 = blockPos2.add(-1, 2, -3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 2, -1))) {
                    blockPos2 = blockPos2.add(-3, 2, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 2, -1))) {
                    blockPos2 = blockPos2.add(3, 2, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 2, -1))) {
                    blockPos2 = blockPos2.add(2, 2, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 2, 1))) {
                    blockPos2 = blockPos2.add(-2, 2, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 2, 1))) {
                    blockPos2 = blockPos2.add(2, 2, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 2, -1))) {
                    blockPos2 = blockPos2.add(-2, 2, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 2, -2))) {
                    blockPos2 = blockPos2.add(1, 2, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 2, 2))) {
                    blockPos2 = blockPos2.add(-1, 2, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 2, 2))) {
                    blockPos2 = blockPos2.add(1, 2, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 2, -2))) {
                    blockPos2 = blockPos2.add(-1, 2, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 2, 2))) {
                    blockPos2 = blockPos2.add(0, 2, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 2, -2))) {
                    blockPos2 = blockPos2.add(0, 2, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 2, 0))) {
                    blockPos2 = blockPos2.add(-2, 2, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 2, 0))) {
                    blockPos2 = blockPos2.add(2, 2, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 2, -2))) {
                    blockPos2 = blockPos2.add(2, 2, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 2, 2))) {
                    blockPos2 = blockPos2.add(-2, 2, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 2, 2))) {
                    blockPos2 = blockPos2.add(2, 2, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 2, -2))) {
                    blockPos2 = blockPos2.add(-2, 2, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 2, -2))) {
                    blockPos2 = blockPos2.add(2, 2, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 2, 2))) {
                    blockPos2 = blockPos2.add(-2, 2, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 2, 2))) {
                    blockPos2 = blockPos2.add(2, 2, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 2, -2))) {
                    blockPos2 = blockPos2.add(-2, 2, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 2, 2))) {
                    blockPos2 = blockPos2.add(0, 2, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 2, -2))) {
                    blockPos2 = blockPos2.add(0, 2, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 2, 0))) {
                    blockPos2 = blockPos2.add(-2, 2, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 3, 0))) {
                    blockPos2 = blockPos2.add(0, 3, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 3, 0))) {
                    blockPos2 = blockPos2.add(1, 3, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 3, 1))) {
                    blockPos2 = blockPos2.add(0, 3, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 3, 0))) {
                    blockPos2 = blockPos2.add(-1, 3, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 3, -1))) {
                    blockPos2 = blockPos2.add(0, 3, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 3, 3))) {
                    blockPos2 = blockPos2.add(1, 3, 3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 3, -3))) {
                    blockPos2 = blockPos2.add(1, 3, -3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 3, 1))) {
                    blockPos2 = blockPos2.add(-3, 3, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 3, 1))) {
                    blockPos2 = blockPos2.add(3, 3, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 3, 3))) {
                    blockPos2 = blockPos2.add(-1, 3, 3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 3, -3))) {
                    blockPos2 = blockPos2.add(-1, 3, -3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 3, -1))) {
                    blockPos2 = blockPos2.add(-3, 3, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 3, -1))) {
                    blockPos2 = blockPos2.add(3, 3, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 3, -1))) {
                    blockPos2 = blockPos2.add(2, 3, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 3, 1))) {
                    blockPos2 = blockPos2.add(-2, 3, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 3, 1))) {
                    blockPos2 = blockPos2.add(2, 3, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 3, -1))) {
                    blockPos2 = blockPos2.add(-2, 3, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 3, -2))) {
                    blockPos2 = blockPos2.add(1, 3, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 3, 2))) {
                    blockPos2 = blockPos2.add(-1, 3, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 3, 2))) {
                    blockPos2 = blockPos2.add(1, 3, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 3, -2))) {
                    blockPos2 = blockPos2.add(-1, 3, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 3, 2))) {
                    blockPos2 = blockPos2.add(0, 3, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 3, -2))) {
                    blockPos2 = blockPos2.add(0, 3, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 3, 0))) {
                    blockPos2 = blockPos2.add(-2, 3, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 3, 0))) {
                    blockPos2 = blockPos2.add(2, 3, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 3, -2))) {
                    blockPos2 = blockPos2.add(2, 3, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 3, 2))) {
                    blockPos2 = blockPos2.add(-2, 3, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 3, 2))) {
                    blockPos2 = blockPos2.add(2, 3, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 3, -2))) {
                    blockPos2 = blockPos2.add(-2, 3, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 3, -2))) {
                    blockPos2 = blockPos2.add(2, 3, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 3, 2))) {
                    blockPos2 = blockPos2.add(-2, 3, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 3, 2))) {
                    blockPos2 = blockPos2.add(2, 3, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 3, -2))) {
                    blockPos2 = blockPos2.add(-2, 3, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 3, 2))) {
                    blockPos2 = blockPos2.add(0, 3, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 3, -2))) {
                    blockPos2 = blockPos2.add(0, 3, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 3, 0))) {
                    blockPos2 = blockPos2.add(-2, 3, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 4, 0))) {
                    blockPos2 = blockPos2.add(0, 4, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 4, 0))) {
                    blockPos2 = blockPos2.add(1, 4, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 4, 1))) {
                    blockPos2 = blockPos2.add(0, 4, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 4, 0))) {
                    blockPos2 = blockPos2.add(-1, 4, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 4, -1))) {
                    blockPos2 = blockPos2.add(0, 4, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 4, 3))) {
                    blockPos2 = blockPos2.add(1, 4, 3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 4, -3))) {
                    blockPos2 = blockPos2.add(1, 4, -3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 4, 1))) {
                    blockPos2 = blockPos2.add(-3, 4, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 4, 1))) {
                    blockPos2 = blockPos2.add(3, 4, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 4, 3))) {
                    blockPos2 = blockPos2.add(-1, 4, 3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 4, -3))) {
                    blockPos2 = blockPos2.add(-1, 4, -3);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-3, 4, -1))) {
                    blockPos2 = blockPos2.add(-3, 4, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(3, 4, -1))) {
                    blockPos2 = blockPos2.add(3, 4, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 4, -1))) {
                    blockPos2 = blockPos2.add(2, 4, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 4, 1))) {
                    blockPos2 = blockPos2.add(-2, 4, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 4, 1))) {
                    blockPos2 = blockPos2.add(2, 4, 1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 4, -1))) {
                    blockPos2 = blockPos2.add(-2, 4, -1);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 4, -2))) {
                    blockPos2 = blockPos2.add(1, 4, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 4, 2))) {
                    blockPos2 = blockPos2.add(-1, 4, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(1, 4, 2))) {
                    blockPos2 = blockPos2.add(1, 4, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-1, 4, -2))) {
                    blockPos2 = blockPos2.add(-1, 4, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 4, 2))) {
                    blockPos2 = blockPos2.add(0, 4, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 4, -2))) {
                    blockPos2 = blockPos2.add(0, 4, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 4, 0))) {
                    blockPos2 = blockPos2.add(-2, 4, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 4, 0))) {
                    blockPos2 = blockPos2.add(2, 4, 0);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 4, -2))) {
                    blockPos2 = blockPos2.add(2, 4, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 4, 2))) {
                    blockPos2 = blockPos2.add(-2, 4, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 4, 2))) {
                    blockPos2 = blockPos2.add(2, 4, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 4, -2))) {
                    blockPos2 = blockPos2.add(-2, 4, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 4, -2))) {
                    blockPos2 = blockPos2.add(2, 4, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 4, 2))) {
                    blockPos2 = blockPos2.add(-2, 4, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(2, 4, 2))) {
                    blockPos2 = blockPos2.add(2, 4, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(-2, 4, -2))) {
                    blockPos2 = blockPos2.add(-2, 4, -2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 4, 2))) {
                    blockPos2 = blockPos2.add(0, 4, 2);
                } else if (!BlockUtil.cantBlockPlace(blockPos2.add(0, 4, -2))) {
                    blockPos2 = blockPos2.add(0, 4, -2);
                } else {
                    if (BlockUtil.cantBlockPlace(blockPos2.add(-2, 4, 0))) return;
                    blockPos2 = blockPos2.add(-2, 4, 0);
                }
            }
        }
        int n2 = BlockFly.mc.player.inventory.currentItem;
        int n3 = -1;
        for (n = 0; n < 9; n += 1) {
            ItemStack itemStack = BlockFly.mc.player.inventory.getStackInSlot(n);
            if (InventoryUtil.isNull(itemStack) || !(itemStack.getItem() instanceof ItemBlock) || !Block.getBlockFromItem(itemStack.getItem()).getDefaultState().isFullBlock()) continue;
            n3 = n;
            break;
        }
        if (n3 == -1) {
            return;
        }
        n = 0;
        if (!BlockFly.mc.player.isSneaking() && BlockUtil.blackList.contains(BlockFly.mc.world.getBlockState(blockPos2).getBlock())) {
            BlockFly.mc.player.connection.sendPacket(new CPacketEntityAction(BlockFly.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            n = 1;
        }
        if (!(BlockFly.mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            BlockFly.mc.player.inventory.currentItem = n3;
            BlockFly.mc.playerController.updateController();
        }
        if (BlockFly.mc.gameSettings.keyBindJump.isKeyDown() && BlockFly.isMoving(BlockFly.mc.player)) {
            BlockFly.mc.player.motionX = 0.0;
            BlockFly.mc.player.motionZ = 0.0;
            BlockFly.mc.player.jump();
            if (this.timer.passedMs(1500L)) {
                BlockFly.mc.player.motionY = -0.28;
                this.timer.reset();
            }
        }
        BlockUtil.placeBlock(blockPos2, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), n != 0);
        BlockFly.mc.player.inventory.currentItem = n2;
        BlockFly.mc.playerController.updateController();
    }

    private boolean new1(Boolean bl) {
        return this.sameY.getValue();
    }

    private boolean new0(Float f) {
        return this.sameY.getValue();
    }
}

