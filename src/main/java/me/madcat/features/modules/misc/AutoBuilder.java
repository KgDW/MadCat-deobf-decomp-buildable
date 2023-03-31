package me.madcat.features.modules.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.madcat.event.events.Render3DEvent;
import me.madcat.event.events.UpdateWalkingPlayerEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.BlockUtil;
import me.madcat.util.InventoryUtil;
import me.madcat.util.RenderUtil;
import me.madcat.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoBuilder
extends Module {
    private final Setting<Settings> settings;
    private final Setting<Mode> mode;
    private final Setting<Direction> stairDirection;
    private final Setting<Integer> width;
    private final Setting<Boolean> dynamic;
    private final Setting<Boolean> setPos;
    private final Setting<Float> range;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Integer> placeDelay;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> altRotate;
    private final Setting<Boolean> ground;
    private final Setting<Boolean> noMove;
    private final Setting<Boolean> packet;
    private final Setting<Boolean> render;
    private final Setting<Boolean> box;
    private final Setting<Integer> bRed;
    private final Setting<Integer> bGreen;
    private final Setting<Integer> bBlue;
    private final Setting<Integer> bAlpha;
    private final Setting<Boolean> outline;
    private final Setting<Integer> oRed;
    private final Setting<Integer> oGreen;
    private final Setting<Integer> oBlue;
    private final Setting<Integer> oAlpha;
    private final Setting<Float> lineWidth;
    private final Setting<Boolean> keepPos;
    private final Setting<Updates> updates;
    private final Setting<Switch> switchMode;
    private final Setting<Boolean> allBlocks;
    private final Timer timer;
    private final List<BlockPos> placepositions = new ArrayList<>();
    private BlockPos startPos;
    private int blocksThisTick;
    private int lastSlot;
    private int blockSlot;

    public AutoBuilder() {
        super("AutoBuilder", "Auto Builds", Module.Category.MISC);
        this.settings = this.register(new Setting<>("Settings", Settings.PATTERN));
        this.mode = this.register(new Setting<>("Mode", Mode.FLAT, this::new0));
        this.stairDirection = this.register(new Setting<>("Direction", Direction.NORTH, this::new1));
        this.width = this.register(new Setting<>("StairWidth", 40, 1, 100, this::new2));
        this.dynamic = this.register(new Setting<>("Dynamic", Boolean.TRUE, this::new3));
        this.setPos = this.register(new Setting<>("ResetPos", Boolean.FALSE, this::new4));
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 6.0f, this::new5));
        this.blocksPerTick = this.register(new Setting<>("Blocks/Tick", 8, 1, 8, this::new6));
        this.placeDelay = this.register(new Setting<>("PlaceDelay", 0, 0, 500, this::new7));
        this.rotate = this.register(new Setting<>("Rotate", Boolean.FALSE, this::new8));
        this.altRotate = this.register(new Setting<>("AltRotate", Boolean.FALSE, this::new9));
        this.ground = this.register(new Setting<>("NoJump", Boolean.TRUE, this::new10));
        this.noMove = this.register(new Setting<>("NoMove", Boolean.TRUE, this::new11));
        this.packet = this.register(new Setting<>("Packet", Boolean.TRUE, this::new12));
        this.render = this.register(new Setting<>("Render", Boolean.TRUE, this::new13));
        this.box = this.register(new Setting<>("Box", Boolean.TRUE, this::new14));
        this.bRed = this.register(new Setting<>("BoxRed", 150, 0, 255, this::new15));
        this.bGreen = this.register(new Setting<>("BoxGreen", 0, 0, 255, this::new16));
        this.bBlue = this.register(new Setting<>("BoxBlue", 150, 0, 255, this::new17));
        this.bAlpha = this.register(new Setting<>("BoxAlpha", 40, 0, 255, this::new18));
        this.outline = this.register(new Setting<>("Outline", Boolean.TRUE, this::new19));
        this.oRed = this.register(new Setting<>("OutlineRed", 255, 0, 255, this::new20));
        this.oGreen = this.register(new Setting<>("OutlineGreen", 50, 0, 255, this::new21));
        this.oBlue = this.register(new Setting<>("OutlineBlue", 255, 0, 255, this::new22));
        this.oAlpha = this.register(new Setting<>("OutlineAlpha", 255, 0, 255, this::new23));
        this.lineWidth = this.register(new Setting<>("LineWidth", 1.5f, 0.1f, 5.0f, this::new24));
        this.keepPos = this.register(new Setting<>("KeepOldPos", Boolean.FALSE, this::new25));
        this.updates = this.register(new Setting<>("Update", Updates.TICK, this::new26));
        this.switchMode = this.register(new Setting<>("Switch", Switch.SILENT, this::new27));
        this.allBlocks = this.register(new Setting<>("AllBlocks", Boolean.TRUE, this::new28));
        this.timer = new Timer();
    }

    @Override
    public void onTick() {
        if (this.updates.getValue() == Updates.TICK) {
            this.doAutoBuilder();
        }
    }

    @Override
    public void onUpdate() {
        if (this.updates.getValue() == Updates.UPDATE) {
            this.doAutoBuilder();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        if (this.updates.getValue() == Updates.WALKING && updateWalkingPlayerEvent.getStage() != 1) {
            this.doAutoBuilder();
        }
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (this.placepositions == null || !this.render.getValue()) {
            return;
        }
        Color color = new Color(this.oRed.getValue(), this.oGreen.getValue(), this.oBlue.getValue(), this.oAlpha.getValue());
        Color color2 = new Color(this.bRed.getValue(), this.bGreen.getValue(), this.bBlue.getValue(), this.bAlpha.getValue());
        RenderUtil.prepareGL3D();
        this.placepositions.forEach(arg_0 -> this.onRender3D29(color2, color, arg_0));
        RenderUtil.releaseGL3D();
    }

    @Override
    public void onEnable() {
        this.placepositions.clear();
        if (!this.keepPos.getValue() || this.startPos == null) {
            this.startPos = new BlockPos(AutoBuilder.mc.player.posX, Math.ceil(AutoBuilder.mc.player.posY), AutoBuilder.mc.player.posZ).down();
        }
        this.blocksThisTick = 0;
        this.lastSlot = AutoBuilder.mc.player.inventory.currentItem;
        this.timer.reset();
    }

    private void doAutoBuilder() {
        if (!this.check()) {
            return;
        }
        for (BlockPos blockPos : this.placepositions) {
            if (this.blocksThisTick >= this.blocksPerTick.getValue()) {
                this.doSwitch(true);
                return;
            }
            int n = BlockUtil.isPositionPlaceable(blockPos, false, true);
            if (n == 3) {
                BlockUtil.placeBlockNotRetarded(blockPos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.altRotate.getValue());
                ++this.blocksThisTick;
            } else {
                if (n != 2 || this.mode.getValue() != Mode.STAIRS) continue;
                if (BlockUtil.isPositionPlaceable(blockPos.down(), false, true) == 3) {
                    BlockUtil.placeBlockNotRetarded(blockPos.down(), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.altRotate.getValue());
                    ++this.blocksThisTick;
                } else {
                    switch (this.stairDirection.getValue()) {
                        case SOUTH: {
                            if (BlockUtil.isPositionPlaceable(blockPos.south(), false, true) != 3) break;
                            BlockUtil.placeBlockNotRetarded(blockPos.south(), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.altRotate.getValue());
                            ++this.blocksThisTick;
                            break;
                        }
                        case WEST: {
                            if (BlockUtil.isPositionPlaceable(blockPos.west(), false, true) != 3) break;
                            BlockUtil.placeBlockNotRetarded(blockPos.west(), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.altRotate.getValue());
                            ++this.blocksThisTick;
                            break;
                        }
                        case NORTH: {
                            if (BlockUtil.isPositionPlaceable(blockPos.north(), false, true) != 3) break;
                            BlockUtil.placeBlockNotRetarded(blockPos.north(), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.altRotate.getValue());
                            ++this.blocksThisTick;
                            break;
                        }
                        case EAST: {
                            if (BlockUtil.isPositionPlaceable(blockPos.east(), false, true) != 3) break;
                            BlockUtil.placeBlockNotRetarded(blockPos.east(), EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.altRotate.getValue());
                            ++this.blocksThisTick;
                        }
                    }
                }
            }
        }
        this.doSwitch(true);
    }

    private boolean doSwitch(boolean bl) {
        Item item = AutoBuilder.mc.player.getHeldItemMainhand().getItem();
        switch (this.switchMode.getValue()) {
            case NONE: {
                return item instanceof ItemBlock && (this.allBlocks.getValue() || ((ItemBlock)item).getBlock() instanceof BlockObsidian);
            }
            case NORMAL: {
                if (bl) break;
                AutoBuilder.mc.player.inventory.currentItem = this.blockSlot;
                AutoBuilder.mc.playerController.updateController();
                break;
            }
            case SILENT: {
                if (item instanceof ItemBlock && (this.allBlocks.getValue() || ((ItemBlock)item).getBlock() instanceof BlockObsidian) || this.lastSlot == -1) break;
                if (bl) {
                    AutoBuilder.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.lastSlot));
                    break;
                }
                AutoBuilder.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.blockSlot));
            }
        }
        return true;
    }

    private boolean check() {
        if (this.setPos.getValue()) {
            this.startPos = new BlockPos(AutoBuilder.mc.player.posX, Math.ceil(AutoBuilder.mc.player.posY), AutoBuilder.mc.player.posZ).down();
            this.setPos.setValue(false);
        }
        this.getPositions();
        if (this.placepositions.isEmpty()) {
            return false;
        }
        if (!this.timer.passedMs(this.placeDelay.getValue())) {
            return false;
        }
        this.timer.reset();
        this.blocksThisTick = 0;
        this.lastSlot = AutoBuilder.mc.player.inventory.currentItem;
        this.blockSlot = this.allBlocks.getValue() ? InventoryUtil.findAnyBlock() : InventoryUtil.findHotbarBlock(BlockObsidian.class);
        return (!this.ground.getValue() || AutoBuilder.mc.player.onGround) && this.blockSlot != -1 && (!this.noMove.getValue() || AutoBuilder.mc.player.moveForward == 0.0f && AutoBuilder.mc.player.moveStrafing == 0.0f) && this.doSwitch(false);
    }

    private void getPositions() {
        if (this.startPos == null) {
            return;
        }
        this.placepositions.clear();
        for (BlockPos blockPos : BlockUtil.getSphere(new BlockPos(AutoBuilder.mc.player.posX, Math.ceil(AutoBuilder.mc.player.posY), AutoBuilder.mc.player.posZ).up(), this.range.getValue(), this.range.getValue().intValue(), false, true, 0)) {
            if (this.placepositions.contains(blockPos) || !AutoBuilder.mc.world.isAirBlock(blockPos)) continue;
            if (this.mode.getValue() == Mode.STAIRS) {
                switch (this.stairDirection.getValue()) {
                    case NORTH: {
                        if (this.startPos.getZ() - blockPos.getZ() != blockPos.getY() - this.startPos.getY() || Math.abs(blockPos.getX() - this.startPos.getX()) >= this.width.getValue() / 2)
                            break;
                        this.placepositions.add(blockPos);
                        break;
                    }
                    case EAST: {
                        if (blockPos.getX() - this.startPos.getX() != blockPos.getY() - this.startPos.getY() || Math.abs(blockPos.getZ() - this.startPos.getZ()) >= this.width.getValue() / 2)
                            break;
                        this.placepositions.add(blockPos);
                        break;
                    }
                    case SOUTH: {
                        if (blockPos.getZ() - this.startPos.getZ() != blockPos.getY() - this.startPos.getY() || Math.abs(this.startPos.getX() - blockPos.getX()) >= this.width.getValue() / 2)
                            break;
                        this.placepositions.add(blockPos);
                        break;
                    }
                    case WEST: {
                        if (this.startPos.getX() - blockPos.getX() != blockPos.getY() - this.startPos.getY() || Math.abs(this.startPos.getZ() - blockPos.getZ()) >= this.width.getValue() / 2)
                            continue;
                        this.placepositions.add(blockPos);
                    }
                }
                continue;
            }
            if (this.mode.getValue() != Mode.FLAT || (double) blockPos.getY() != (this.dynamic.getValue() ? Math.ceil(AutoBuilder.mc.player.posY) - 1.0 : (double) this.startPos.getY()))
                continue;
            this.placepositions.add(blockPos);
        }
    }

    private void onRender3D29(Color color, Color color2, BlockPos blockPos) {
        RenderUtil.drawBoxESP(blockPos, color, true, color2, this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), color.getAlpha(), true);
    }

    private boolean new28(Boolean bl) {
        return this.settings.getValue() == Settings.MISC;
    }

    private boolean new27(Switch switch_) {
        return this.settings.getValue() == Settings.MISC;
    }

    private boolean new26(Updates updates) {
        return this.settings.getValue() == Settings.MISC;
    }

    private boolean new25(Boolean bl) {
        return this.settings.getValue() == Settings.MISC;
    }

    private boolean new24(Float f) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue();
    }

    private boolean new23(Integer n) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue();
    }

    private boolean new22(Integer n) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue();
    }

    private boolean new21(Integer n) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue();
    }

    private boolean new20(Integer n) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.outline.getValue();
    }

    private boolean new19(Boolean bl) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue();
    }

    private boolean new18(Integer n) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.box.getValue();
    }

    private boolean new17(Integer n) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.box.getValue();
    }

    private boolean new16(Integer n) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.box.getValue();
    }

    private boolean new15(Integer n) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue() && this.box.getValue();
    }

    private boolean new14(Boolean bl) {
        return this.settings.getValue() == Settings.RENDER && this.render.getValue();
    }

    private boolean new13(Boolean bl) {
        return this.settings.getValue() == Settings.RENDER;
    }

    private boolean new12(Boolean bl) {
        return this.settings.getValue() == Settings.PLACE;
    }

    private boolean new11(Boolean bl) {
        return this.settings.getValue() == Settings.PLACE;
    }

    private boolean new10(Boolean bl) {
        return this.settings.getValue() == Settings.PLACE;
    }

    private boolean new9(Boolean bl) {
        return this.rotate.getValue() && this.settings.getValue() == Settings.PLACE;
    }

    private boolean new8(Boolean bl) {
        return this.settings.getValue() == Settings.PLACE;
    }

    private boolean new7(Integer n) {
        return this.settings.getValue() == Settings.PLACE;
    }

    private boolean new6(Integer n) {
        return this.settings.getValue() == Settings.PLACE;
    }

    private boolean new5(Float f) {
        return this.settings.getValue() == Settings.PLACE;
    }

    private boolean new4(Boolean bl) {
        return this.settings.getValue() == Settings.PATTERN && (this.mode.getValue() == Mode.STAIRS || this.mode.getValue() == Mode.FLAT && !this.dynamic.getValue());
    }

    private boolean new3(Boolean bl) {
        return this.mode.getValue() == Mode.FLAT && this.settings.getValue() == Settings.PATTERN;
    }

    private boolean new2(Integer n) {
        return this.mode.getValue() == Mode.STAIRS && this.settings.getValue() == Settings.PATTERN;
    }

    private boolean new1(Direction direction) {
        return this.mode.getValue() == Mode.STAIRS && this.settings.getValue() == Settings.PATTERN;
    }

    private boolean new0(Mode mode) {
        return this.settings.getValue() == Settings.PATTERN;
    }

    public enum Settings {
        MISC,
        PATTERN,
        PLACE,
        RENDER

    }

    public enum Direction {
        WEST,
        SOUTH,
        EAST,
        NORTH

    }

    public enum Updates {
        TICK,
        UPDATE,
        WALKING

    }

    public enum Switch {
        NONE,
        NORMAL,
        SILENT

    }

    public enum Mode {
        STAIRS,
        FLAT

    }
}

