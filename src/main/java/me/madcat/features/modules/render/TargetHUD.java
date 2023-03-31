package me.madcat.features.modules.render;

import java.awt.Color;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import me.madcat.event.events.Render2DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class TargetHUD
        extends Module {
    private final Setting<Integer> x = this.register(new Setting<>("X", 50, 0, 2000));
    private final Setting<Integer> y = this.register(new Setting<>("Y", 50, 0, 2000));
    private final Setting<Integer> backgroundAlpha = this.register(new Setting<>("Alpha", 80, 0, 255));
    EntityLivingBase target;

    public TargetHUD() {
        super("TargetHUD", "description", Module.Category.RENDER);
        this.target = TargetHUD.mc.player;
    }

    private static double applyAsDouble(EntityLivingBase entityLivingBase) {
        return entityLivingBase.getDistance(TargetHUD.mc.player);
    }

    private static boolean checkIsNotPlayer(Entity entity) {
        return !entity.equals(TargetHUD.mc.player);
    }

    @Override
    public synchronized void onTick() {
        LinkedList linkedList = new LinkedList();
        Stream<Entity> stream = TargetHUD.mc.world.loadedEntityList.stream().filter(EntityPlayer.class::isInstance).filter(TargetHUD::checkIsNotPlayer).map(Entity.class::cast);
        EntityPlayer.class.getClass();
        Stream<Entity> stream2 = stream.filter(EntityPlayer.class::isInstance).filter(TargetHUD::checkIsNotPlayer);
        EntityLivingBase.class.getClass();
        Stream<EntityLivingBase> stream3 = stream2.map(EntityLivingBase.class::cast).sorted(Comparator.comparingDouble(TargetHUD::applyAsDouble));
        linkedList.getClass();
        stream3.forEach(linkedList::add);
        this.target = !linkedList.isEmpty() ? (EntityLivingBase)linkedList.get(0) : TargetHUD.mc.player;
        if (TargetHUD.mc.currentScreen instanceof GuiChat) {
            this.target = TargetHUD.mc.player;
        }
    }

    @Override
    public synchronized void onRender2D(Render2DEvent render2DEvent) {
        if (this.target != null && !this.target.isDead) {
            int n;
            FontRenderer fontRenderer = TargetHUD.mc.fontRenderer;
            int n2 = this.target.getHealth() / this.target.getMaxHealth() > 0.66f ? -16711936 : (this.target.getHealth() / this.target.getMaxHealth() > 0.33f ? -26368 : -65536);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            GuiInventory.drawEntityOnScreen(this.x.getValue() + 15, this.y.getValue() + 32, 15, 1.0f, 1.0f, this.target);
            LinkedList linkedList = new LinkedList();
            LinkedList linkedList2 = new LinkedList();
            this.target.getArmorInventoryList().forEach(arg_0 -> TargetHUD.onRender2D(linkedList2, arg_0));
            for (n = linkedList2.size() - 1; n >= 0; --n) {
                linkedList.add(linkedList2.get(n));
            }
            n = 0;
            switch (linkedList.size()) {
                case 0: {
                    if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 28, this.y.getValue() + 18);
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 43, this.y.getValue() + 18);
                        n += 45;
                        break;
                    }
                    if (this.target.getHeldItemMainhand().isEmpty() && this.target.getHeldItemOffhand().isEmpty()) break;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 28, this.y.getValue() + 18);
                    n += 30;
                    break;
                }
                case 1: {
                    n = 15;
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(0), this.x.getValue() + 28, this.y.getValue() + 18);
                    if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 43, this.y.getValue() + 18);
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 58, this.y.getValue() + 18);
                        n += 45;
                        break;
                    }
                    if (this.target.getHeldItemMainhand().isEmpty() && this.target.getHeldItemOffhand().isEmpty()) break;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 43, this.y.getValue() + 18);
                    n += 30;
                    break;
                }
                case 2: {
                    n = 30;
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(0), this.x.getValue() + 28, this.y.getValue() + 18);
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(1), this.x.getValue() + 43, this.y.getValue() + 18);
                    if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 58, this.y.getValue() + 18);
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 73, this.y.getValue() + 18);
                        n += 45;
                        break;
                    }
                    if (this.target.getHeldItemMainhand().isEmpty() && this.target.getHeldItemOffhand().isEmpty()) break;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 58, this.y.getValue() + 18);
                    n += 30;
                    break;
                }
                case 3: {
                    n = 45;
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(0), this.x.getValue() + 28, this.y.getValue() + 18);
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(1), this.x.getValue() + 43, this.y.getValue() + 18);
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(2), this.x.getValue() + 58, this.y.getValue() + 18);
                    if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 73, this.y.getValue() + 18);
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 98, this.y.getValue() + 18);
                        n += 45;
                        break;
                    }
                    if (this.target.getHeldItemMainhand().isEmpty() && this.target.getHeldItemOffhand().isEmpty()) break;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 73, this.y.getValue() + 18);
                    n += 30;
                    break;
                }
                case 4: {
                    n = 60;
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(0), this.x.getValue() + 28, this.y.getValue() + 18);
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(1), this.x.getValue() + 43, this.y.getValue() + 18);
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(2), this.x.getValue() + 58, this.y.getValue() + 18);
                    mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)linkedList.get(3), this.x.getValue() + 73, this.y.getValue() + 18);
                    if (!this.target.getHeldItemMainhand().isEmpty() && !this.target.getHeldItemOffhand().isEmpty()) {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand(), this.x.getValue() + 98, this.y.getValue() + 18);
                        mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemOffhand(), this.x.getValue() + 113, this.y.getValue() + 18);
                        n += 45;
                        break;
                    }
                    if (this.target.getHeldItemMainhand().isEmpty() && this.target.getHeldItemOffhand().isEmpty()) break;
                    mc.getRenderItem().renderItemAndEffectIntoGUI(this.target.getHeldItemMainhand().isEmpty() ? this.target.getHeldItemOffhand() : this.target.getHeldItemMainhand(), this.x.getValue() + 98, this.y.getValue() + 18);
                    n += 30;
                }
            }
            int n3 = this.y.getValue() + 35;
            int n4 = fontRenderer.getStringWidth(this.target.getName()) + 30;
            int n5 = fontRenderer.getStringWidth(this.target.getName()) > n ? this.x.getValue() + n4 : this.x.getValue() + n + 30;
            Gui.drawRect(this.x.getValue() - 2, this.y.getValue(), n5 += 5, n3 += 5, new Color(0, 0, 0, this.backgroundAlpha.getValue()).getRGB());
            int n6 = (int)(this.target.getHealth() / this.target.getMaxHealth() * (float)(n5 - this.x.getValue()));
            Gui.drawRect(this.x.getValue() - 2, n3 - 2, this.x.getValue() + n6, n3, n2);
            fontRenderer.drawString(this.target.getName(), this.x.getValue() + 30, this.y.getValue() + 8, new Color(255, 255, 255).getRGB(), true);
        }
    }

    private static void onRender2D(List list, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            list.add(itemStack);
        }
    }
}
 