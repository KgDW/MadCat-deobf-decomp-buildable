package me.madcat.features.modules.misc;

import me.madcat.event.events.Render2DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.util.ColorUtil;
import me.madcat.util.RenderUtil;
import me.madcat.util.Timer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShulkerViewer extends Module
{
    private static final ResourceLocation SHULKER_GUI_TEXTURE;
    private static ShulkerViewer INSTANCE;
    public final Map<EntityPlayer, ItemStack> spiedPlayers;
    public final Map<EntityPlayer, Timer> playerTimers;

    public ShulkerViewer() {
        super("ShulkerViewer", "Several tweaks for Peek", Category.MISC);
        this.spiedPlayers = new ConcurrentHashMap<>();
        this.playerTimers = new ConcurrentHashMap<>();
        this.setInstance();
    }

    public static ShulkerViewer INSTANCE() {
        if (ShulkerViewer.INSTANCE == null) {
            ShulkerViewer.INSTANCE = new ShulkerViewer();
        }
        return ShulkerViewer.INSTANCE;
    }

    public static void drawShulkerGui(final ItemStack stack, final String name) {
        try {
            final Item item = stack.getItem();
            final TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            final ItemShulkerBox shulker = (ItemShulkerBox)item;
            entityBox.setWorld(ShulkerViewer.mc.world);
            entityBox.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));
            entityBox.setCustomName((name == null) ? stack.getDisplayName() : name);
            new Thread(() -> {
                try {
                    Thread.sleep(200L);
                }
                catch (final InterruptedException ignored) {}
                ShulkerViewer.mc.player.displayGUIChest(entityBox);
            }).start();
        }
        catch (final Exception ignored) {}
    }

    private void setInstance() {
        ShulkerViewer.INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        for (final EntityPlayer player : ShulkerViewer.mc.world.playerEntities) {
            if (player != null && player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox) {
                if (ShulkerViewer.mc.player == player) {
                    continue;
                }
                final ItemStack stack = player.getHeldItemMainhand();
                this.spiedPlayers.put(player, stack);
            }
        }
    }

    @Override
    public void onRender2D(final Render2DEvent event) {
        final int x = -3;
        final int y = 124;
        for (final EntityPlayer player : ShulkerViewer.mc.world.playerEntities) {
            if (this.spiedPlayers.get(player) == null) {
                continue;
            }
            player.getHeldItemMainhand();
            if (!(player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox)) {
                final Timer playerTimer = this.playerTimers.get(player);
                if (playerTimer == null) {
                    final Timer timer = new Timer();
                    timer.reset();
                    this.playerTimers.put(player, timer);
                }
                else if (playerTimer.passedS(3.0)) {
                    continue;
                }
            }
            else {
                final Timer playerTimer;
                if (player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox && (playerTimer = this.playerTimers.get(player)) != null) {
                    playerTimer.reset();
                    this.playerTimers.put(player, playerTimer);
                }
            }
            final ItemStack stack = this.spiedPlayers.get(player);
            this.renderShulkerToolTip(stack, x, y, player.getName());
        }
    }

    public void renderShulkerToolTip(final ItemStack stack, final int x, final int y, final String name) {
        final NBTTagCompound tagCompound = stack.getTagCompound();
        final NBTTagCompound blockEntityTag;
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            ShulkerViewer.mc.getTextureManager().bindTexture(ShulkerViewer.SHULKER_GUI_TEXTURE);
            RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
            RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 57, 500);
            RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
            GlStateManager.disableDepth();
            final Color color = new Color(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue(), 200);
            this.renderer.drawStringWithShadow((name == null) ? stack.getDisplayName() : name, (float)(x + 8), (float)(y + 6), ColorUtil.toRGBA(color));
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            final NonNullList nonnulllist = NonNullList.withSize(27, (Object)ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(blockEntityTag, nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final int iX = x + i % 9 * 18 + 8;
                final int iY = y + i / 9 * 18 + 18;
                final ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(ShulkerViewer.mc.fontRenderer, itemStack, iX, iY, null);
            }
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    static {
        SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
        ShulkerViewer.INSTANCE = new ShulkerViewer();
    }
}
 