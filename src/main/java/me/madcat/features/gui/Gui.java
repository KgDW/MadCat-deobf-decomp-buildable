package me.madcat.features.gui;

import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.gui.components.Component;
import me.madcat.features.gui.components.items.DescriptionDisplay;
import me.madcat.features.gui.components.items.Item;
import me.madcat.features.gui.components.items.buttons.ModuleButton;
import me.madcat.features.gui.particle.Particle;
import me.madcat.features.gui.particle.Snow;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.util.ColorUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

public class Gui extends GuiScreen {
    private static final DescriptionDisplay descriptionDisplay;
    private static Gui INSTANCE;
    private final ArrayList<Snow> _snowList;
    private final ArrayList<Component> components;
    private final Particle.Util particles;

    public Gui() {
        this._snowList = new ArrayList<>();
        this.components = new ArrayList<>();
        this.particles = new Particle.Util(300);
        this.setInstance();
        this.load();
    }

    public static Gui INSTANCE() {
        if (Gui.INSTANCE == null) {
            Gui.INSTANCE = new Gui();
        }
        return Gui.INSTANCE;
    }

    public static Gui getClickGui() {
        return INSTANCE();
    }

    private void setInstance() {
        Gui.INSTANCE = this;
    }

    private void load() {
        int x = -84;
        final Random random = new Random();
        for (int i = 0; i < 100; ++i) {
            for (int y = 0; y < 3; ++y) {
                final Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2) + 1);
                this._snowList.add(snow);
            }
        }
        ArrayList<Component> components;
        for (final Module.Category category : MadCat.moduleManager.getCategories()) {
            components = this.components;
            final String name = category.getName();
            x += 90;
            components.add(new Component(name, x, 34, true) {

                    Module.Category valcategory;{
                    this.valcategory = category;
                }


                @Override
                public void setupItems() {
                    counter1 = new int[]{1};
                    MadCat.moduleManager.getModulesByCategory(this.valcategory).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            });
        }
        this.components.forEach(Gui::load0);
    }

    public void updateModule(final Module module) {
        final Iterator<Component> iterator = this.components.iterator();
        while (iterator.hasNext()) {
            for (final Item item : iterator.next().getItems()) {
                if (!(item instanceof ModuleButton)) {
                    continue;
                }
                final ModuleButton moduleButton = (ModuleButton)item;
                final Module module2 = moduleButton.getModule();
                if (module == null) {
                    continue;
                }
                if (!module.equals(module2)) {
                    continue;
                }
                moduleButton.initSettings();
            }
        }
    }

    public void drawScreen(final int mouseX, final int mouseY, final float n3) {
        final ClickGui instance = ClickGui.INSTANCE();
        Gui.descriptionDisplay.setDraw(false);
        this.checkMouseWheel();
        this.drawDefaultBackground();
        if (ClickGui.INSTANCE().background.getValue()) {
            RenderUtil.drawVGradientRect(0.0f, 0.0f, (float)MadCat.textManager.scaledWidth, (float)MadCat.textManager.scaledHeight, new Color(0, 0, 0, 0).getRGB(), ColorUtil.toRGBA(MadCat.colorManager.getCurrent(ClickGui.INSTANCE().backgroundAlpha.getValue())));
        }
        this.components.forEach(component -> component.onKeyTyped((char) mouseX, mouseY));
        if (Gui.descriptionDisplay.shouldDraw() && instance.moduleDescription.getValue()) {
            Gui.descriptionDisplay.drawScreen(mouseX, mouseY, n3);
        }
        final ScaledResolution res = new ScaledResolution(this.mc);
        if (!this._snowList.isEmpty() && ClickGui.INSTANCE().snowing.getValue()) {
            this._snowList.forEach(snow -> snow.Update(res));
        }
        if (ClickGui.INSTANCE().particles.getValue()) {
            this.particles.drawParticles();
        }
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int clickedButton) {
        this.components.forEach(Gui::mouseClicked3);
    }

    private static void mouseClicked3(Component component) {
    }

    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public DescriptionDisplay getDescriptionDisplay() {
        return Gui.descriptionDisplay;
    }

    public void checkMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(Gui::checkMouseWheel5);
        }
        else if (dWheel > 0) {
            this.components.forEach(Gui::checkMouseWheel6);
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public void keyTyped(final char c, final int mouseX) throws IOException {
        super.keyTyped(c, mouseX);
        this.components.forEach(component -> component.onKeyTyped(c, mouseX));
    }

    private static void checkMouseWheel6(final Component component) {
        component.setY(component.getY() + 10);
    }

    private static void checkMouseWheel5(final Component component) {
        component.setY(component.getY() - 10);
    }

    private static void load0(final Component component) {
        component.getItems().sort(Comparator.comparing((Function<? super Item, ? extends Comparable>)Feature::getName));
    }

    static {
        Gui.INSTANCE = new Gui();
        descriptionDisplay = new DescriptionDisplay("", 0.0f, 0.0f);
    }
}