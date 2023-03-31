package me.madcat.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.madcat.event.events.Render2DEvent;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.Feature;
import me.madcat.features.gui.Gui;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.BetterChat;
import me.madcat.features.modules.client.Capes;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.client.FontMod;
import me.madcat.features.modules.client.FovMod;
import me.madcat.features.modules.client.GUIBlur;
import me.madcat.features.modules.client.HUD;
import me.madcat.features.modules.client.Notifications;
import me.madcat.features.modules.combat.AntiBurrow;
import me.madcat.features.modules.combat.AntiCity;
import me.madcat.features.modules.combat.AntiPiston;
import me.madcat.features.modules.combat.AnvilAura;
import me.madcat.features.modules.combat.ArmorWarner;
import me.madcat.features.modules.combat.AutoCity;
import me.madcat.features.modules.combat.AutoPush;
import me.madcat.features.modules.combat.AutoTrap;
import me.madcat.features.modules.combat.AutoWeb;
import me.madcat.features.modules.combat.Burrow;
import me.madcat.features.modules.combat.Criticals;
import me.madcat.features.modules.combat.FeetPad;
import me.madcat.features.modules.combat.Flatten;
import me.madcat.features.modules.combat.KillAura;
import me.madcat.features.modules.combat.NewBurrow;
import me.madcat.features.modules.combat.NewSurround;
import me.madcat.features.modules.combat.Surround;
import me.madcat.features.modules.combat.TrapSelf;
import me.madcat.features.modules.exploit.BetterPortal;
import me.madcat.features.modules.exploit.BowGod;
import me.madcat.features.modules.exploit.Clip;
import me.madcat.features.modules.exploit.Crasher;
import me.madcat.features.modules.exploit.PacketFly;
import me.madcat.features.modules.exploit.Phase;
import me.madcat.features.modules.exploit.Phase2;
import me.madcat.features.modules.exploit.PopLag;
import me.madcat.features.modules.exploit.TeleportLog;
import me.madcat.features.modules.exploit.TestPacketFly;
import me.madcat.features.modules.legacy.AntiCev;
import me.madcat.features.modules.legacy.AutoCev;
import me.madcat.features.modules.legacy.AutoCiv;
import me.madcat.features.modules.legacy.AutoDupe;
import me.madcat.features.modules.legacy.BreakCheck;
import me.madcat.features.modules.legacy.CevSelect;
import me.madcat.features.modules.legacy.GameShaders;
import me.madcat.features.modules.legacy.HoleFiller;
import me.madcat.features.modules.legacy.InstantMine;
import me.madcat.features.modules.legacy.LegacyGlow;
import me.madcat.features.modules.legacy.LegacyShader;
import me.madcat.features.modules.misc.AntiAFK;
import me.madcat.features.modules.misc.AntiPackets;
import me.madcat.features.modules.misc.AutoBuilder;
import me.madcat.features.modules.misc.AutoEZ;
import me.madcat.features.modules.misc.AutoFish;
import me.madcat.features.modules.misc.AutoKit;
import me.madcat.features.modules.misc.AutoLog;
import me.madcat.features.modules.misc.AutoRespawn;
import me.madcat.features.modules.misc.ExtraTab;
import me.madcat.features.modules.misc.FakeKick;
import me.madcat.features.modules.misc.FakePlayer;
import me.madcat.features.modules.misc.FreeLook;
import me.madcat.features.modules.misc.Gamemode;
import me.madcat.features.modules.misc.GhostBlock;
import me.madcat.features.modules.misc.KillEffects;
import me.madcat.features.modules.misc.LightningDetect;
import me.madcat.features.modules.misc.MCF;
import me.madcat.features.modules.misc.MCP;
import me.madcat.features.modules.misc.Message;
import me.madcat.features.modules.misc.NickHider;
import me.madcat.features.modules.misc.NoEntityTrace;
import me.madcat.features.modules.misc.PopCounter;
import me.madcat.features.modules.misc.TNTTime;
import me.madcat.features.modules.movement.Anchor;
import me.madcat.features.modules.movement.AntiLevitate;
import me.madcat.features.modules.movement.AntiVoid;
import me.madcat.features.modules.movement.AntiWeb;
import me.madcat.features.modules.movement.AutoCenter;
import me.madcat.features.modules.movement.AutoWalk;
import me.madcat.features.modules.movement.BlockFly;
import me.madcat.features.modules.movement.BoatFly;
import me.madcat.features.modules.movement.FastSwim;
import me.madcat.features.modules.movement.Flight;
import me.madcat.features.modules.movement.InventoryMove;
import me.madcat.features.modules.movement.LongJump;
import me.madcat.features.modules.movement.NoSlow;
import me.madcat.features.modules.movement.NoWeb;
import me.madcat.features.modules.movement.ReverseStep;
import me.madcat.features.modules.movement.SafeWalk;
import me.madcat.features.modules.movement.Sprint;
import me.madcat.features.modules.movement.Step;
import me.madcat.features.modules.movement.Strafe;
import me.madcat.features.modules.movement.Velocity;
import me.madcat.features.modules.player.AntiChestGui;
import me.madcat.features.modules.player.AntiEatDesync;
import me.madcat.features.modules.player.AntiHunger;
import me.madcat.features.modules.player.AutoArmor;
import me.madcat.features.modules.player.AutoArmorPlus;
import me.madcat.features.modules.player.Blink;
import me.madcat.features.modules.player.BlockTweaks;
import me.madcat.features.modules.player.FastElytra;
import me.madcat.features.modules.player.FastPlace;
import me.madcat.features.modules.player.FreeCam;
import me.madcat.features.modules.player.Interact;
import me.madcat.features.modules.player.MultiTask;
import me.madcat.features.modules.player.NoFall;
import me.madcat.features.modules.player.NoRotate;
import me.madcat.features.modules.player.PacketEat;
import me.madcat.features.modules.player.PacketXP;
import me.madcat.features.modules.player.Reach;
import me.madcat.features.modules.player.Replenish;
import me.madcat.features.modules.player.TimerModule;
import me.madcat.features.modules.player.XCarry;
import me.madcat.features.modules.render.Ambience;
import me.madcat.features.modules.render.BlockHighLight;
import me.madcat.features.modules.render.BreakESP;
import me.madcat.features.modules.render.CameraClip;
import me.madcat.features.modules.render.Chams;
import me.madcat.features.modules.render.ChinaHat;
import me.madcat.features.modules.render.CityESP;
import me.madcat.features.modules.render.CrystalChams;
import me.madcat.features.modules.render.DMGParticles;
import me.madcat.features.modules.render.ESP2D;
import me.madcat.features.modules.render.EntityCircle;
import me.madcat.features.modules.render.EntityESP;
import me.madcat.features.modules.render.FullBright;
import me.madcat.features.modules.render.GlintModify;
import me.madcat.features.modules.render.HidePlayer;
import me.madcat.features.modules.render.HurtCam;
import me.madcat.features.modules.render.ItemPhysics;
import me.madcat.features.modules.render.LogESP;
import me.madcat.features.modules.render.Model;
import me.madcat.features.modules.render.NameTags;
import me.madcat.features.modules.render.NoLag;
import me.madcat.features.modules.render.NoRender;
import me.madcat.features.modules.render.Particles;
import me.madcat.features.modules.render.PopChams;
import me.madcat.features.modules.render.StorageESP;
import me.madcat.features.modules.render.TargetHUD;
import me.madcat.features.modules.render.Tracer;
import me.madcat.features.modules.render.Trajectories;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public class ModuleManager
extends Feature {
    public List<String> sortedModulesABC;
    public List<Module> sortedModules = new ArrayList<>();
    public static ArrayList<Module> test;
    public static final ArrayList<Module> modules;

    public <T extends Module> T getModuleT(Class<T> clazz) {
        return (T) modules.stream().filter(arg_0 -> ModuleManager.getModuleT3(clazz, arg_0)).map(ModuleManager::getModuleT4).findFirst().orElse(null);
    }

    public void onUpdate() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public ModuleManager() {
        this.sortedModulesABC = new ArrayList<>();
    }

    public void enableModule(String string) {
        Module module = ModuleManager.getModuleByName(string);
        if (module != null) {
            module.enable();
        }
    }

    public boolean isModuleEnabled(String string) {
        Module module = ModuleManager.getModuleByName(string);
        return module != null && module.isOn();
    }

    public void disableModule(String string) {
        Module module = ModuleManager.getModuleByName(string);
        if (module != null) {
            module.disable();
        }
    }

    private static void onRender3D2(Render3DEvent render3DEvent, Module module) {
        module.onRender3D(render3DEvent);
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = ModuleManager.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> arrayList = new ArrayList<>();
        for (Module module : modules) {
            if (!module.isEnabled()) {
                continue;
            }
            arrayList.add(module);
        }
        return arrayList;
    }

    public void onUnload() {
        EventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.getClass();
        modules.forEach(eventBus::unregister);
        modules.forEach(Module::onUnload);
    }

    public void init() {
        modules.add(new FreeCam());
        modules.add(new ClickGui());
        modules.add(new FontMod());
        modules.add(new PopCounter());
        modules.add(new AntiWeb());
        modules.add(new TestPacketFly());
        modules.add(new FastSwim());
        modules.add(new AutoArmorPlus());
        modules.add(new GUIBlur());
        modules.add(new HUD());
        modules.add(new Criticals());
        modules.add(new LogESP());
        modules.add(new LightningDetect());
        modules.add(new Message());
        modules.add(new KillAura());
        modules.add(new ExtraTab());
        modules.add(new StorageESP());
        modules.add(new AnvilAura());
        modules.add(new AutoEZ());
        modules.add(new AutoKit());
        modules.add(new LegacyShader());
        modules.add(new BreakCheck());
        modules.add(new NoEntityTrace());
        modules.add(new Notifications());
        modules.add(new NewBurrow());
        modules.add(new AntiCity());
        modules.add(new AutoTrap());
        modules.add(new CameraClip());
        modules.add(new MultiTask());
        modules.add(new BlockFly());
        modules.add(new LegacyGlow());
        modules.add(new SafeWalk());
        modules.add(new Chams());
        modules.add(new NoRotate());
        modules.add(new Crasher());
        modules.add(new Trajectories());
        modules.add(new TargetHUD());
        modules.add(new FullBright());
        modules.add(new NickHider());
        modules.add(new AutoRespawn());
        modules.add(new CityESP());
        modules.add(new GameShaders());
        modules.add(new EntityCircle());
        modules.add(new EntityESP());
        modules.add(new ESP2D());
        modules.add(new AntiChestGui());
        modules.add(new AntiEatDesync());
        modules.add(new AntiLevitate());
        modules.add(new NameTags());
        modules.add(new AntiVoid());
        modules.add(new AntiHunger());
        modules.add(new Gamemode());
        modules.add(new KillEffects());
        modules.add(new Capes());
        modules.add(new FovMod());
        modules.add(new ReverseStep());
        modules.add(new AutoDupe());
        modules.add(new Tracer());
        modules.add(new FastPlace());
        modules.add(new InventoryMove());
        modules.add(new LongJump());
        modules.add(new NoSlow());
        modules.add(new BlockHighLight());
        modules.add(new BowGod());
        modules.add(new Sprint());
        modules.add(new Velocity());
        modules.add(new AutoPush());
        modules.add(new AutoBuilder());
        modules.add(new Flight());
        modules.add(new ChinaHat());
        modules.add(new Surround());
        modules.add(new NewSurround());
        modules.add(new NoFall());
        modules.add(new FreeLook());
        modules.add(new AutoFish());
        modules.add(new AntiAFK());
        modules.add(new DMGParticles());
        modules.add(new BlockTweaks());
        modules.add(new BetterChat());
        modules.add(new NoLag());
        modules.add(new HurtCam());
        modules.add(new Phase2());
        modules.add(new Ambience());
        modules.add(new CrystalChams());
        modules.add(new AutoCity());
        modules.add(new FeetPad());
        modules.add(new Particles());
        modules.add(new PopChams());
        modules.add(new NoRender());
        modules.add(new InstantMine());
        modules.add(new AntiBurrow());
        modules.add(new Burrow());
        modules.add(new ArmorWarner());
        modules.add(new AntiPackets());
        modules.add(new AutoLog());
        modules.add(new AntiCev());
        modules.add(new AutoWalk());
        modules.add(new FakeKick());
        modules.add(new CevSelect());
        modules.add(new TrapSelf());
        modules.add(new AntiPiston());
        modules.add(new GlintModify());
        modules.add(new TNTTime());
        modules.add(new TeleportLog());
        modules.add(new Anchor());
        modules.add(new NoWeb());
        modules.add(new PopLag());
        modules.add(new AutoWeb());
        modules.add(new Clip());
        modules.add(new HoleFiller());
        modules.add(new Flatten());
        modules.add(new PacketFly());
        modules.add(new Model());
        modules.add(new BreakESP());
        modules.add(new ItemPhysics());
        modules.add(new Blink());
        modules.add(new FastElytra());
        modules.add(new AutoArmor());
        modules.add(new Interact());
        modules.add(new BetterPortal());
        modules.add(new TimerModule());
        modules.add(new Replenish());
        modules.add(new FakePlayer());
        modules.add(new Reach());
        modules.add(new MCP());
        modules.add(new PacketEat());
        modules.add(new MCF());
        modules.add(new PacketXP());
        modules.add(new XCarry());
        modules.add(new Phase());
        modules.add(new GhostBlock());
        modules.add(new Step());
        modules.add(new HidePlayer());
        modules.add(new AutoCiv());
        modules.add(new AutoCev());
        modules.add(new AutoCenter());
        modules.add(new Strafe());
        modules.add(new BoatFly());
    }

    public void disableModule(Class<Module> clazz) {
        Module module = ModuleManager.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void onRender3D(Render3DEvent render3DEvent) {
        modules.stream().filter(Feature::isEnabled).forEach(arg_0 -> ModuleManager.onRender3D2(render3DEvent, arg_0));
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onKeyPressed(int n) {
        if (n == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof Gui) {
            return;
        }
        modules.forEach(arg_0 -> ModuleManager.onKeyPressed6(n, arg_0));
    }

    public void onRender2D(Render2DEvent render2DEvent) {
        modules.stream().filter(Feature::isEnabled).forEach(arg_0 -> ModuleManager.onRender2D1(render2DEvent, arg_0));
    }

    public void onTick() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    private Integer sortModules5(boolean bl, Module module) {
        return this.renderer.getStringWidth(module.getFullArrayString()) * (bl ? -1 : 1);
    }

    public void enableModule(Class<Module> clazz) {
        Module module = ModuleManager.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    private static void onKeyPressed6(int n, Module module) {
        if (module.getBind().getKey() == n) {
            module.toggle();
        }
    }

    public void onLoad() {
        Stream<Module> stream = modules.stream().filter(Module::listening);
        EventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.getClass();
        stream.forEach(eventBus::register);
        modules.forEach(Module::onLoad);
    }

    private static Module getModuleT4(Module module) {
        return module;
    }

    private static void onRender2D1(final Render2DEvent render2DEvent, final Module module) {
        module.onRender2D(render2DEvent);
    }
    public Module getModuleByDisplayName(String string) {
        for (Module module : modules) {
            if (!module.getDisplayName().equalsIgnoreCase(string)) {
                continue;
            }
            return module;
        }
        return null;
    }

    public void sortModules(boolean bl) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(arg_0 -> this.sortModules5(bl, arg_0))).collect(Collectors.toList());
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Module module : modules) {
            if (!module.isEnabled()) continue;
            if (!module.isDrawn()) {
                continue;
            }
            arrayList.add(module.getFullArrayString());
        }
        return arrayList;
    }

    public void onLogout() {
        modules.forEach(Module::onLogout);
    }

    public void onUnloadPost() {
        for (Module module : modules) {
            module.enabled.setValue(false);
        }
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> arrayList = new ArrayList<>();
        modules.forEach(arg_0 -> ModuleManager.getModulesByCategory0(category, arrayList, arg_0));
        return arrayList;
    }

    public void onLogin() {
        modules.forEach(Module::onLogin);
    }

    private static boolean getModuleT3(Class clazz, Module module) {
        return module.getClass() == clazz;
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public static ArrayList<Module> getModules() {
        return test;
    }

    public static Module getModuleByName(String string) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(string)) {
                continue;
            }
            return module;
        }
        return null;
    }

    private static void getModulesByCategory0(Module.Category category, ArrayList arrayList, Module module) {
        if (module.getCategory() == category) {
            arrayList.add(module);
        }
    }

    static {
        modules = new ArrayList();
    }

    public static <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : modules) {
            if (!clazz.isInstance(module)) {
                continue;
            }
            return (T)module;
        }
        return null;
    }
}

