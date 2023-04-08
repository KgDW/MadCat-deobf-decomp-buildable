package me.madcat;

import me.madcat.manager.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
    @Mod(modid = MadCat.MOD_ID, name = MadCat.MOD_NAME, version = MadCat.VERSION)
    public class MadCat {
        public static final String MOD_ID = "madcat";
        public static final String MOD_NAME = "m3dc3t";
        public static final String VERSION = "1.12.2";
        public static final String MOD_VERSION = "3.0";
        public static final Logger LOGGER = LogManager.getLogger("m3dc3t");
    public static PotionManager potionManager;
    public static HoleManager holeManager;
    public static PacketManager packetManager;
    public static PositionManager positionManager;
    public static FileManager fileManager;

    public static ColorManager colorManager;
    public static ReloadManager reloadManager;

    public static FriendManager friendManager;
    private static boolean unloaded;
    @Mod.Instance
    public static MadCat INSTANCE;
    public static EventManager eventManager;
    public static RotationManager rotationManager;
    public static InventoryManager inventoryManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static ConfigManager configManager;
    public static TextManager textManager;
    public static ModuleManager moduleManager;
    public static ServerManager serverManager;

    public static void load() {
        MadCat.LOGGER.info("loading madcat");
        MadCat.unloaded = false;
        if (MadCat.reloadManager != null) {
            MadCat.reloadManager.unload();
            MadCat.reloadManager = null;
        }
        MadCat.textManager = new TextManager();
        MadCat.commandManager = new CommandManager();
        MadCat.friendManager = new FriendManager();
        MadCat.moduleManager = new ModuleManager();
        MadCat.rotationManager = new RotationManager();
        MadCat.packetManager = new PacketManager();
        MadCat.eventManager = new EventManager();
        MadCat.speedManager = new SpeedManager();
        MadCat.potionManager = new PotionManager();
        MadCat.inventoryManager = new InventoryManager();
        MadCat.serverManager = new ServerManager();
        MadCat.fileManager = new FileManager();
        MadCat.colorManager = new ColorManager();
        MadCat.positionManager = new PositionManager();
        MadCat.configManager = new ConfigManager();
        MadCat.holeManager = new HoleManager();
        MadCat.LOGGER.info("Managers loaded.");
        MadCat.moduleManager.init();
        MadCat.LOGGER.info("Modules loaded.");
        MadCat.configManager.init();
        MadCat.eventManager.init();
        MadCat.LOGGER.info("EventManager loaded.");
        MadCat.textManager.init();
        MadCat.moduleManager.onLoad();
        MadCat.LOGGER.info("madcat successfully loaded!\n");
    }

    public static void onUnload() {
        if (!MadCat.unloaded) {
            MadCat.eventManager.onUnload();
            MadCat.moduleManager.onUnload();
            MadCat.configManager.saveConfig(MadCat.configManager.config.replaceFirst("MadCat/", ""));
            MadCat.moduleManager.onUnloadPost();
            MadCat.unloaded = true;
        }
    }

    @Mod.EventHandler
    public void preinit(final FMLPreInitializationEvent fmlPreInitializationEvent) {
        Display.setTitle("MadCat 3.0: Loading...");
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent fmlInitializationEvent) {
        load();
    }
}
 