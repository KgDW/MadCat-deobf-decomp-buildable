package me.madcat.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.event.events.ClientEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.gui.Gui;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.HUD;
import me.madcat.features.setting.Setting;
import me.madcat.util.TextUtil;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    private final Setting<Settings> setting = this.register(new Setting<>("Settings", Settings.Gui));
    public final Setting<String> prefix = this.register(new Setting<Object>("Prefix", ".", this::new0));
    public final Setting<String> clientName = this.register(new Setting<>("ClientName", "MadCat", this::new1));
    public final Setting<Integer> alphaBox = this.register(new Setting<Object>("AlphaBox", 0, 0, 255, this::new2));
    public final Setting<Boolean> outline = this.register(new Setting<>("Outline", Boolean.TRUE, this::new3));
    public final Setting<Boolean> moduleDescription = this.register(new Setting<>("Description", Boolean.TRUE, this::new4));
    public final Setting<Boolean> moduleIcon = this.register(new Setting<>("Icon", Boolean.TRUE, this::new5));
    public final Setting<Integer> iconmode2 = this.register(new Setting<>("ArrowIcon", 0, 0, 2, this::new6));
    public final Setting<Boolean> snowing = this.register(new Setting<>("Snowing", Boolean.TRUE, this::new7));
    public final Setting<Boolean> particles = this.register(new Setting<>("Particles", Boolean.TRUE, this::new8));
    public final Setting<Boolean> button = this.register(new Setting<>("BottonGround", Boolean.TRUE, this::new9));
    public final Setting<Integer> iconmode = this.register(new Setting<>("SettingIcon", 0, 0, 6, this::new10));
    public final Setting<Integer> moduleWidth = this.register(new Setting<>("ModuleWidth", 14, 0, 30, this::new11));
    public final Setting<Integer> moduleDistance = this.register(new Setting<>("ModuleDistance", 16, 0, 30, this::new12));
    public final Setting<Boolean> rainbowg = this.register(new Setting<Object>("GradientRainbow", Boolean.FALSE, this::new13));
    public final Setting<Boolean> rainbow = this.register(new Setting<Object>("Rainbow", Boolean.FALSE, this::new14));
    public final Setting<Integer> red = this.register(new Setting<Object>("Red", 140, 0, 255, this::new15));
    public final Setting<Integer> green = this.register(new Setting<Object>("Green", 140, 0, 255, this::new16));
    public final Setting<Integer> blue = this.register(new Setting<Object>("Blue", 250, 0, 255, this::new17));
    public final Setting<Integer> hoverAlpha = this.register(new Setting<Object>("Alpha", 225, 0, 255, this::new18));
    public final Setting<Integer> alpha = this.register(new Setting<Object>("HoverAlpha", 240, 0, 255, this::new19));
    public final Setting<rainbowMode> rainbowModeHud = this.register(new Setting<>("HUD", rainbowMode.Static, this::new20));
    public final Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<>("ArrayList", rainbowModeArray.Up, this::new21));
    public final Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", 600, 0, 600, this::new22));
    public final Setting<Float> rainbowBrightness = this.register(new Setting<Object>("Brightness ", 255.0f, 1.0f, 255.0f, this::new23));
    public final Setting<Float> rainbowSaturation = this.register(new Setting<Object>("Saturation", 255.0f, 1.0f, 255.0f, this::new24));
    public final Setting<Boolean> background = this.register(new Setting<>("BackGround", Boolean.FALSE, this::new25));
    public final Setting<Integer> backgroundAlpha = this.register(new Setting<>("BAlpha", 100, 0, 255, this::new26));
    public final Setting<Boolean> blur = this.register(new Setting<>("Blur", Boolean.TRUE, this::new27));
    public final Setting<Integer> g_red = this.register(new Setting<Object>("RedL", 105, 0, 255, this::new28));
    public final Setting<Integer> g_green = this.register(new Setting<Object>("GreenL", 162, 0, 255, this::new29));
    public final Setting<Integer> g_blue = this.register(new Setting<Object>("BlueL", 255, 0, 255, this::new30));
    public final Setting<Integer> g_red1 = this.register(new Setting<Object>("RedR", 143, 0, 255, this::new31));
    public final Setting<Integer> g_green1 = this.register(new Setting<Object>("GreenR", 140, 0, 255, this::new32));
    public final Setting<Integer> g_blue1 = this.register(new Setting<Object>("BlueR", 213, 0, 255, this::new33));
    public final Setting<Integer> g_alpha = this.register(new Setting<Object>("AlphaL", 0, 0, 255, this::new34));
    public final Setting<Integer> g_alpha1 = this.register(new Setting<Object>("AlphaR", 0, 0, 255, this::new35));

    public ClickGui() {
        super("ClickGui", "Module interface", Module.Category.CLIENT);
        this.setInstance();
    }

    public static ClickGui INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent clientEvent) {
        if (clientEvent.getStage() == 2 && clientEvent.getSetting().getFeature().equals(this)) {
            if (clientEvent.getSetting().equals(this.prefix)) {
                MadCat.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + MadCat.commandManager.getPrefix());
            }
            MadCat.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    public String getCommandMessage() {
        return TextUtil.coloredString("[", HUD.INSTANCE().bracketColor.getPlannedValue()) + TextUtil.coloredString(ClickGui.INSTANCE().clientName.getValueAsString(), HUD.INSTANCE().commandColor.getPlannedValue()) + TextUtil.coloredString("]", HUD.INSTANCE().bracketColor.getPlannedValue());
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Gui.getClickGui());
    }

    @Override
    public void onLoad() {
        MadCat.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        MadCat.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.blur.getValue()) {
            if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (ClickGui.mc.entityRenderer.getShaderGroup() != null) {
                    ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
                try {
                    ClickGui.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (ClickGui.mc.entityRenderer.getShaderGroup() != null && ClickGui.mc.currentScreen == null) {
                ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        } else if (ClickGui.mc.entityRenderer.getShaderGroup() != null) {
            ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public void onTick() {
        MadCat.commandManager.setClientMessage(this.getCommandMessage());
        if (!(ClickGui.mc.currentScreen instanceof Gui)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        if (ClickGui.mc.currentScreen instanceof Gui) {
            mc.displayGuiScreen(null);
        }
        if (ClickGui.mc.entityRenderer.getShaderGroup() != null) {
            ClickGui.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    private boolean new35(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new34(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new33(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new32(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new31(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new30(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new29(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new28(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new27(Boolean bl) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new26(Integer n) {
        return this.setting.getValue() == Settings.Gui && this.background.getValue();
    }

    private boolean new25(Boolean bl) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new24(Object object) {
        return this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new23(Object object) {
        return this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new22(Object object) {
        return this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new21(Object object) {
        return this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new20(Object object) {
        return this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new19(Object object) {
        return !this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new18(Object object) {
        return !this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new17(Object object) {
        return !this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new16(Object object) {
        return !this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new15(Object object) {
        return !this.rainbow.getValue() && this.setting.getValue() == Settings.Color;
    }

    private boolean new14(Object object) {
        return this.setting.getValue() == Settings.Color;
    }

    private boolean new13(Object object) {
        return this.setting.getValue() == Settings.Gradient;
    }

    private boolean new12(Integer n) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new11(Integer n) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new10(Integer n) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new9(Boolean bl) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new8(Boolean bl) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new7(Boolean bl) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new6(Integer n) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new5(Boolean bl) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new4(Boolean bl) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new3(Boolean bl) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new2(Object object) {
        return this.setting.getValue() == Settings.Color;
    }

    private boolean new1(String string) {
        return this.setting.getValue() == Settings.Gui;
    }

    private boolean new0(Object object) {
        return this.setting.getValue() == Settings.Gui;
    }

    public enum Settings {
        Gui,
        Color,
        Gradient

    }

    public enum rainbowMode {
        Static,
        Sideway

    }

    public enum rainbowModeArray {
        Static,
        Up

    }
}

