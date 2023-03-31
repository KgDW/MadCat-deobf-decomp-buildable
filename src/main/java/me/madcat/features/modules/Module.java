package me.madcat.features.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.event.events.ClientEvent;
import me.madcat.event.events.Render2DEvent;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.modules.client.HUD;
import me.madcat.features.modules.client.Notifications;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.Setting;
import me.madcat.manager.ModuleManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

public class Module
extends Feature {
    public final Setting<Boolean> enabled = this.add(new Setting<>("Enabled", this.getName().equalsIgnoreCase("HUD")));
    public final Setting<Boolean> drawn = this.add(new Setting<>("Drawn", true));
    public final Setting<Bind> bind = this.add(new Setting<>("Keybind", this.getName().equalsIgnoreCase("ClickGui") ? new Bind(21) : new Bind(-1)));
    public final Setting<String> displayName;
    public final boolean hasListener;
    public final boolean alwaysListening;
    public final boolean hidden;
    private final String description;
    private final Category category;
    public float offset;
    public boolean sliding;

    public Module(String string, String string2, Category category) {
        super(string);
        this.displayName = this.register(new Setting<>("DisplayName", string));
        this.description = string2;
        this.category = category;
        this.hasListener = true;
        this.hidden = false;
        this.alwaysListening = false;
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent render2DEvent) {
    }

    public void onRender3D(Render3DEvent render3DEvent) {
    }

    public void onUnload() {
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return !this.enabled.getValue();
    }

    public void setEnabled(boolean bl) {
        if (bl) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void enable() {
        this.enabled.setValue(Boolean.TRUE);
        this.onToggle();
        this.onEnable();
        if (HUD.INSTANCE().notifyToggles.getValue()) {
            TextComponentString textComponentString = new TextComponentString(MadCat.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + "[" + ChatFormatting.AQUA + this.getDisplayName() + ChatFormatting.WHITE + "]" + ChatFormatting.GRAY + " toggled " + ChatFormatting.GREEN + "on");
            Notifications.notifyList.add(new Notifications.Notifys(ChatFormatting.WHITE + "[" + ChatFormatting.AQUA + this.getDisplayName() + ChatFormatting.WHITE + "]" + ChatFormatting.GRAY + " toggled " + ChatFormatting.GREEN + "on"));
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(textComponentString, 1);
        }
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
        this.enabled.setValue(false);
        if (HUD.INSTANCE().notifyToggles.getValue()) {
            TextComponentString textComponentString = new TextComponentString(MadCat.commandManager.getClientMessage() + " " + ChatFormatting.WHITE + "[" + ChatFormatting.AQUA + this.getDisplayName() + ChatFormatting.WHITE + "]" + ChatFormatting.GRAY + " toggled " + ChatFormatting.RED + "off");
            Notifications.notifyList.add(new Notifications.Notifys(ChatFormatting.WHITE + "[" + ChatFormatting.AQUA + this.getDisplayName() + ChatFormatting.WHITE + "]" + ChatFormatting.GRAY + " toggled " + ChatFormatting.RED + "off"));
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(textComponentString, 1);
        }
        this.onToggle();
        this.onDisable();
    }

    public void toggle() {
        ClientEvent clientEvent = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post(clientEvent);
        if (!clientEvent.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String string) {
        Module module = MadCat.moduleManager.getModuleByDisplayName(string);
        Module module2 = ModuleManager.getModuleByName(string);
        if (module == null && module2 == null) {
            Command.sendMessage(this.getDisplayName() + ", name: " + this.getName() + ", has been renamed to: " + string);
            this.displayName.setValue(string);
            return;
        }
        Command.sendMessage(ChatFormatting.RED + "A module of this name already exists.");
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean bl) {
        this.drawn.setValue(bl);
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int n) {
        this.bind.setValue(new Bind(n));
    }

    public boolean listening() {
        return this.hasListener && this.isOn() || this.alwaysListening;
    }

    public String getFullArrayString() {
        return this.getDisplayName() + ChatFormatting.GRAY + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
    }

    public enum Category {
        COMBAT("Combat"),
        MISC("Misc"),
        RENDER("Render"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        EXPLOIT("Exploit"),
        CLIENT("Client"),
        LEGACY("Legacy");

        private final String name;

        Category(String string2) {
            this.name = string2;
        }

        public String getName() {
            return this.name;
        }
    }
}

