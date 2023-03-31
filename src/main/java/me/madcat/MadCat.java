package me.madcat;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.madcat.manager.ServerManager;
import me.madcat.manager.ModuleManager;
import me.madcat.manager.TextManager;
import me.madcat.manager.ConfigManager;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import me.madcat.manager.CommandManager;
import me.madcat.manager.SpeedManager;
import me.madcat.manager.InventoryManager;
import me.madcat.manager.RotationManager;
import me.madcat.manager.EventManager;
import me.madcat.manager.FriendManager;
import org.apache.logging.log4j.Logger;
import me.madcat.manager.ReloadManager;
import me.madcat.manager.ColorManager;
import me.madcat.manager.FileManager;
import me.madcat.manager.PositionManager;
import me.madcat.manager.PacketManager;
import me.madcat.manager.HoleManager;
import me.madcat.manager.PotionManager;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "madcat", name = "madcat", version = "1.12.2")
public class MadCat
{
    public static PotionManager potionManager;
    public static HoleManager holeManager;
    public static PacketManager packetManager;
    public static PositionManager positionManager;
    public static FileManager fileManager;
    public static final String MOD_ID;
    public static ColorManager colorManager;
    public static ReloadManager reloadManager;
    public static final Logger LOGGER;
    public static FriendManager friendManager;
    public static final String MOD_NAME;
    public static final String VERSION;
    private static boolean unloaded;
    @Mod.Instance
    public static MadCat INSTANCE;
    public static EventManager eventManager;
    public static RotationManager rotationManager;
    public static InventoryManager inventoryManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static final EventBus EVENT_BUS;
    public static ConfigManager configManager;
    public static TextManager textManager;
    public static ModuleManager moduleManager;
    public static ServerManager serverManager;
    public static final String ID;

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

    static {
        ID = "3.0";
        MOD_ID = "madcat";
        VERSION = "1.12.2";
        MOD_NAME = "madcat";
        LOGGER = LogManager.getLogger("madcat");
        EVENT_BUS = new EventBus();
        MadCat.unloaded = false;
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent fmlInitializationEvent) {
        load();
    }
}
 