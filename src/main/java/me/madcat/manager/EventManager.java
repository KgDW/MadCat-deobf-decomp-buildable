package me.madcat.manager;

import com.google.common.base.Strings;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import me.madcat.MadCat;
import me.madcat.event.events.ConnectionEvent;
import me.madcat.event.events.DeathEvent;
import me.madcat.event.events.PacketEvent;
import me.madcat.event.events.Render2DEvent;
import me.madcat.event.events.Render3DEvent;
import me.madcat.event.events.TotemPopEvent;
import me.madcat.event.events.UpdateWalkingPlayerEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.modules.client.HUD;
import me.madcat.features.modules.misc.KillEffects;
import me.madcat.features.modules.misc.PopCounter;
import me.madcat.util.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class EventManager
extends Feature {
    private final AtomicBoolean tickOngoing;
    int breakTimer = 0;
    boolean needShort = false;
    private final Timer logoutTimer = new Timer();
    int ticks = 0;
    int length = 0;

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post post) {
        if (post.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            MadCat.textManager.updateResolution();
        }
    }

    public EventManager() {
        this.tickOngoing = new AtomicBoolean(false);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        if (updateWalkingPlayerEvent.getStage() == 0) {
            MadCat.speedManager.updateValues();
            MadCat.rotationManager.updateRotations();
            MadCat.positionManager.updatePosition();
        }
        if (updateWalkingPlayerEvent.getStage() == 1) {
            MadCat.rotationManager.restoreRotations();
            MadCat.positionManager.restorePosition();
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent clientChatEvent) {
        block4: {
            if (clientChatEvent.getMessage().startsWith(Command.getCommandPrefix())) {
                clientChatEvent.setCanceled(true);
                try {
                    EventManager.mc.ingameGUI.getChatGUI().addToSentMessages(clientChatEvent.getMessage());
                    if (clientChatEvent.getMessage().length() > 1) {
                        MadCat.commandManager.executeCommand(clientChatEvent.getMessage().substring(Command.getCommandPrefix().length() - 1));
                        break block4;
                    }
                    Command.sendMessage("Please enter a command.");
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    Command.sendMessage(ChatFormatting.RED + "An error occurred while running this command. Check the log!");
                }
            }
        }
    }

    public void onUnload() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent clientConnectedToServerEvent) {
        this.logoutTimer.reset();
        MadCat.moduleManager.onLogin();
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent renderWorldLastEvent) {
        if (renderWorldLastEvent.isCanceled()) {
            return;
        }
        EventManager.mc.profiler.startSection("madcat");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        Render3DEvent render3DEvent = new Render3DEvent(renderWorldLastEvent.getPartialTicks());
        MadCat.moduleManager.onRender3D(render3DEvent);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        EventManager.mc.profiler.endSection();
    }

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent clientTickEvent) {
        this.updateTitle();
        if (EventManager.fullNullCheck()) {
            return;
        }
        MadCat.moduleManager.onTick();
        for (EntityPlayer entityPlayer : EventManager.mc.world.playerEntities) {
            if (entityPlayer == null) continue;
            if (entityPlayer.getHealth() > 0.0f) {
                continue;
            }
            MinecraftForge.EVENT_BUS.post(new DeathEvent(entityPlayer));
            PopCounter.INSTANCE().onDeath(entityPlayer);
            KillEffects.INSTANCE().onDeath(entityPlayer);
        }
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent clientDisconnectionFromServerEvent) {
        MadCat.moduleManager.onLogout();
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(InputEvent.KeyInputEvent keyInputEvent) {
        if (Keyboard.getEventKeyState()) {
            MadCat.moduleManager.onKeyPressed(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() != 0)
            return;
        MadCat.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity(mc.world) instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) packet.getEntity(mc.world);
                MinecraftForge.EVENT_BUS.post(new TotemPopEvent(player));
            }
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && !fullNullCheck() && this.logoutTimer.passedS(1.0D)) {
            SPacketPlayerListItem packet = event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.getAction()))
                return;
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> (!Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null))
                    .forEach(data -> {
                        String name;
                        EntityPlayer entity;
                        UUID id = data.getProfile().getId();
                        switch (packet.getAction()) {
                            case ADD_PLAYER:
                                name = data.getProfile().getName();
                                MinecraftForge.EVENT_BUS.post(new ConnectionEvent(0, id, name));
                                break;
                            case REMOVE_PLAYER:
                                entity = mc.world.getPlayerEntityByUUID(id);
                                if (entity != null) {
                                    String logoutName = entity.getName();
                                    MinecraftForge.EVENT_BUS.post(new ConnectionEvent(1, entity, id, logoutName));
                                    break;
                                }
                                MinecraftForge.EVENT_BUS.post(new ConnectionEvent(2, id, null));
                                break;
                        }
                    });
        }
        if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate)
            MadCat.serverManager.update();
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        if (!EventManager.fullNullCheck() && livingUpdateEvent.getEntity().getEntityWorld().isRemote && livingUpdateEvent.getEntityLiving().equals(EventManager.mc.player)) {
            MadCat.inventoryManager.update();
            MadCat.moduleManager.onUpdate();
            if (HUD.INSTANCE().renderingMode.getValue() == HUD.RenderingMode.Length) {
                MadCat.moduleManager.sortModules(true);
            } else {
                MadCat.moduleManager.sortModulesABC();
            }
        }
    }

    public void updateTitle() {
        String string = "MadCat 3.0";
        Display.setTitle(string.substring(0, string.length() - this.length) + "\u0020\u007c\u0020\u0051\u7fa4\u0020\u0035\u0038\u0039\u0031\u0039\u0031\u0035\u0036\u0031");
        ++this.ticks;
        if (this.ticks % 17 == 0) {
            if (this.length == string.length() && this.breakTimer != 2 || this.length == 0 && this.breakTimer != 4) {
                ++this.breakTimer;
                return;
            }
            this.breakTimer = 0;
            if (this.length == string.length()) {
                this.needShort = true;
            }
            this.length = this.needShort ? --this.length : ++this.length;
            if (this.length == 0) {
                this.needShort = false;
            }
        }
    }

    public boolean ticksOngoing() {
        return this.tickOngoing.get();
    }

    @SubscribeEvent(priority=EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text text) {
        if (text.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(text.getPartialTicks(), scaledResolution);
            MadCat.moduleManager.onRender2D(render2DEvent);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}

