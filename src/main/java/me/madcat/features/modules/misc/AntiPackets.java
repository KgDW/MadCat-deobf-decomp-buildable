package me.madcat.features.modules.misc;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiPackets
extends Module {
    private final Setting<Mode> mode = this.register(new Setting<>("Packets", Mode.CLIENT));
    private final Setting<Integer> page = this.register(new Setting<Object>("SPackets", 1, 1, 10, this::new0));
    private final Setting<Boolean> AdvancementInfo = this.register(new Setting<Object>("AdvancementInfo", Boolean.FALSE, this::new1));
    private final Setting<Boolean> Animation = this.register(new Setting<Object>("Animation", Boolean.FALSE, this::new2));
    private final Setting<Boolean> BlockAction = this.register(new Setting<Object>("BlockAction", Boolean.FALSE, this::new3));
    private final Setting<Boolean> BlockBreakAnim = this.register(new Setting<Object>("BlockBreakAnim", Boolean.FALSE, this::new4));
    private final Setting<Boolean> BlockChange = this.register(new Setting<Object>("BlockChange", Boolean.FALSE, this::new5));
    private final Setting<Boolean> Camera = this.register(new Setting<Object>("Camera", Boolean.FALSE, this::new6));
    private final Setting<Boolean> ChangeGameState = this.register(new Setting<Object>("ChangeGameState", Boolean.FALSE, this::new7));
    private final Setting<Boolean> Chat = this.register(new Setting<Object>("Chat", Boolean.FALSE, this::new8));
    private final Setting<Boolean> ChunkData = this.register(new Setting<Object>("ChunkData", Boolean.FALSE, this::new9));
    private final Setting<Boolean> CloseWindow = this.register(new Setting<Object>("CloseWindow", Boolean.FALSE, this::new10));
    private final Setting<Boolean> CollectItem = this.register(new Setting<Object>("CollectItem", Boolean.FALSE, this::new11));
    private final Setting<Boolean> CombatEvent = this.register(new Setting<Object>("Combatevent", Boolean.FALSE, this::new12));
    private final Setting<Boolean> ConfirmTransaction = this.register(new Setting<Object>("ConfirmTransaction", Boolean.FALSE, this::new13));
    private final Setting<Boolean> Cooldown = this.register(new Setting<Object>("Cooldown", Boolean.FALSE, this::new14));
    private final Setting<Boolean> CustomPayload = this.register(new Setting<Object>("CustomPayload", Boolean.FALSE, this::new15));
    private final Setting<Boolean> CustomSound = this.register(new Setting<Object>("CustomSound", Boolean.FALSE, this::new16));
    private final Setting<Boolean> DestroyEntities = this.register(new Setting<Object>("DestroyEntities", Boolean.FALSE, this::new17));
    private final Setting<Boolean> Disconnect = this.register(new Setting<Object>("Disconnect", Boolean.FALSE, this::new18));
    private final Setting<Boolean> DisplayObjective = this.register(new Setting<Object>("DisplayObjective", Boolean.FALSE, this::new19));
    private final Setting<Boolean> Effect = this.register(new Setting<Object>("Effect", Boolean.FALSE, this::new20));
    private final Setting<Boolean> Entity = this.register(new Setting<Object>("Entity", Boolean.FALSE, this::new21));
    private final Setting<Boolean> EntityAttach = this.register(new Setting<Object>("EntityAttach", Boolean.FALSE, this::new22));
    private final Setting<Boolean> EntityEffect = this.register(new Setting<Object>("EntityEffect", Boolean.FALSE, this::new23));
    private final Setting<Boolean> EntityEquipment = this.register(new Setting<Object>("EntityEquipment", Boolean.FALSE, this::new24));
    private final Setting<Boolean> EntityHeadLook = this.register(new Setting<Object>("EntityHeadLook", Boolean.FALSE, this::new25));
    private final Setting<Boolean> EntityMetadata = this.register(new Setting<Object>("EntityMetadata", Boolean.FALSE, this::new26));
    private final Setting<Boolean> EntityProperties = this.register(new Setting<Object>("EntityProperties", Boolean.FALSE, this::new27));
    private final Setting<Boolean> EntityStatus = this.register(new Setting<Object>("EntityStatus", Boolean.FALSE, this::new28));
    private final Setting<Boolean> EntityTeleport = this.register(new Setting<Object>("EntityTeleport", Boolean.FALSE, this::new29));
    private final Setting<Boolean> EntityVelocity = this.register(new Setting<Object>("EntityVelocity", Boolean.FALSE, this::new30));
    private final Setting<Boolean> Explosion = this.register(new Setting<Object>("Explosion", Boolean.FALSE, this::new31));
    private final Setting<Boolean> HeldItemChange = this.register(new Setting<Object>("HeldItemChange", Boolean.FALSE, this::new32));
    private final Setting<Boolean> JoinGame = this.register(new Setting<Object>("JoinGame", Boolean.FALSE, this::new33));
    private final Setting<Boolean> KeepAlive = this.register(new Setting<Object>("KeepAlive", Boolean.FALSE, this::new34));
    private final Setting<Boolean> Maps = this.register(new Setting<Object>("Maps", Boolean.FALSE, this::new35));
    private final Setting<Boolean> MoveVehicle = this.register(new Setting<Object>("MoveVehicle", Boolean.FALSE, this::new36));
    private final Setting<Boolean> MultiBlockChange = this.register(new Setting<Object>("MultiBlockChange", Boolean.FALSE, this::new37));
    private final Setting<Boolean> OpenWindow = this.register(new Setting<Object>("OpenWindow", Boolean.FALSE, this::new38));
    private final Setting<Boolean> Particles = this.register(new Setting<Object>("Particles", Boolean.FALSE, this::new39));
    private final Setting<Boolean> PlaceGhostRecipe = this.register(new Setting<Object>("PlaceGhostRecipe", Boolean.FALSE, this::new40));
    private final Setting<Boolean> PlayerAbilities = this.register(new Setting<Object>("PlayerAbilities", Boolean.FALSE, this::new41));
    private final Setting<Boolean> PlayerListHeaderFooter = this.register(new Setting<Object>("PlayerListHeaderFooter", Boolean.FALSE, this::new42));
    private final Setting<Boolean> PlayerListItem = this.register(new Setting<Object>("PlayerListItem", Boolean.FALSE, this::new43));
    private final Setting<Boolean> PlayerPosLook = this.register(new Setting<Object>("PlayerPosLook", Boolean.FALSE, this::new44));
    private final Setting<Boolean> RecipeBook = this.register(new Setting<Object>("RecipeBook", Boolean.FALSE, this::new45));
    private final Setting<Boolean> RemoveEntityEffect = this.register(new Setting<Object>("RemoveEntityEffect", Boolean.FALSE, this::new46));
    private final Setting<Boolean> ResourcePackSend = this.register(new Setting<Object>("ResourcePackSend", Boolean.FALSE, this::new47));
    private final Setting<Boolean> Respawn = this.register(new Setting<Object>("Respawn", Boolean.FALSE, this::new48));
    private final Setting<Boolean> ScoreboardObjective = this.register(new Setting<Object>("ScoreboardObjective", Boolean.FALSE, this::new49));
    private final Setting<Boolean> SelectAdvancementsTab = this.register(new Setting<Object>("SelectAdvancementsTab", Boolean.FALSE, this::new50));
    private final Setting<Boolean> ServerDifficulty = this.register(new Setting<Object>("ServerDifficulty", Boolean.FALSE, this::new51));
    private final Setting<Boolean> SetExperience = this.register(new Setting<Object>("SetExperience", Boolean.FALSE, this::new52));
    private final Setting<Boolean> SetPassengers = this.register(new Setting<Object>("SetPassengers", Boolean.FALSE, this::new53));
    private final Setting<Boolean> SetSlot = this.register(new Setting<Object>("SetSlot", Boolean.FALSE, this::new54));
    private final Setting<Boolean> SignEditorOpen = this.register(new Setting<Object>("SignEditorOpen", Boolean.FALSE, this::new55));
    private final Setting<Boolean> SoundEffect = this.register(new Setting<Object>("SoundEffect", Boolean.FALSE, this::new56));
    private final Setting<Boolean> SpawnExperienceOrb = this.register(new Setting<Object>("SpawnExperienceOrb", Boolean.FALSE, this::new57));
    private final Setting<Boolean> SpawnGlobalEntity = this.register(new Setting<Object>("SpawnGlobalEntity", Boolean.FALSE, this::new58));
    private final Setting<Boolean> SpawnMob = this.register(new Setting<Object>("SpawnMob", Boolean.FALSE, this::new59));
    private final Setting<Boolean> SpawnObject = this.register(new Setting<Object>("SpawnObject", Boolean.FALSE, this::new60));
    private final Setting<Boolean> SpawnPainting = this.register(new Setting<Object>("SpawnPainting", Boolean.FALSE, this::new61));
    private final Setting<Boolean> SpawnPlayer = this.register(new Setting<Object>("SpawnPlayer", Boolean.FALSE, this::new62));
    private final Setting<Boolean> SpawnPosition = this.register(new Setting<Object>("SpawnPosition", Boolean.FALSE, this::new63));
    private final Setting<Boolean> Statistics = this.register(new Setting<Object>("Statistics", Boolean.FALSE, this::new64));
    private final Setting<Boolean> TabComplete = this.register(new Setting<Object>("TabComplete", Boolean.FALSE, this::new65));
    private final Setting<Boolean> Teams = this.register(new Setting<Object>("Teams", Boolean.FALSE, this::new66));
    private final Setting<Boolean> TimeUpdate = this.register(new Setting<Object>("TimeUpdate", Boolean.FALSE, this::new67));
    private final Setting<Boolean> Title = this.register(new Setting<Object>("Title", Boolean.FALSE, this::new68));
    private final Setting<Boolean> UnloadChunk = this.register(new Setting<Object>("UnloadChunk", Boolean.FALSE, this::new69));
    private final Setting<Boolean> UpdateBossInfo = this.register(new Setting<Object>("UpdateBossInfo", Boolean.FALSE, this::new70));
    private final Setting<Boolean> UpdateHealth = this.register(new Setting<Object>("UpdateHealth", Boolean.FALSE, this::new71));
    private final Setting<Boolean> UpdateScore = this.register(new Setting<Object>("UpdateScore", Boolean.FALSE, this::new72));
    private final Setting<Boolean> UpdateTileEntity = this.register(new Setting<Object>("UpdateTileEntity", Boolean.FALSE, this::new73));
    private final Setting<Boolean> UseBed = this.register(new Setting<Object>("UseBed", Boolean.FALSE, this::new74));
    private final Setting<Boolean> WindowItems = this.register(new Setting<Object>("WindowItems", Boolean.FALSE, this::new75));
    private final Setting<Boolean> WindowProperty = this.register(new Setting<Object>("WindowProperty", Boolean.FALSE, this::new76));
    private final Setting<Boolean> WorldBorder = this.register(new Setting<Object>("WorldBorder", Boolean.FALSE, this::new77));
    private final Setting<Boolean> PlayerDigging = this.register(new Setting<Object>("PlayerDigging", Boolean.FALSE, this::new78));
    private final Setting<Integer> pages = this.register(new Setting<Object>("CPackets", 1, 1, 4, this::new79));
    private final Setting<Boolean> Animations = this.register(new Setting<Object>("Animations", Boolean.FALSE, this::new80));
    private final Setting<Boolean> ChatMessage = this.register(new Setting<Object>("ChatMessage", Boolean.FALSE, this::new81));
    private final Setting<Boolean> ClickWindow = this.register(new Setting<Object>("ClickWindow", Boolean.FALSE, this::new82));
    private final Setting<Boolean> ClientSettings = this.register(new Setting<Object>("ClientSettings", Boolean.FALSE, this::new83));
    private final Setting<Boolean> ClientStatus = this.register(new Setting<Object>("ClientStatus", Boolean.FALSE, this::new84));
    private final Setting<Boolean> CloseWindows = this.register(new Setting<Object>("CloseWindows", Boolean.FALSE, this::new85));
    private final Setting<Boolean> ConfirmTeleport = this.register(new Setting<Object>("ConfirmTeleport", Boolean.FALSE, this::new86));
    private final Setting<Boolean> ConfirmTransactions = this.register(new Setting<Object>("ConfirmTransactions", Boolean.FALSE, this::new87));
    private final Setting<Boolean> CreativeInventoryAction = this.register(new Setting<Object>("CreativeInventoryAction", Boolean.FALSE, this::new88));
    private final Setting<Boolean> CustomPayloads = this.register(new Setting<Object>("CustomPayloads", Boolean.FALSE, this::new89));
    private final Setting<Boolean> EnchantItem = this.register(new Setting<Object>("EnchantItem", Boolean.FALSE, this::new90));
    private final Setting<Boolean> EntityAction = this.register(new Setting<Object>("EntityAction", Boolean.FALSE, this::new91));
    private final Setting<Boolean> HeldItemChanges = this.register(new Setting<Object>("HeldItemChanges", Boolean.FALSE, this::new92));
    private final Setting<Boolean> Input = this.register(new Setting<Object>("Input", Boolean.FALSE, this::new93));
    private final Setting<Boolean> KeepAlives = this.register(new Setting<Object>("KeepAlives", Boolean.FALSE, this::new94));
    private final Setting<Boolean> PlaceRecipe = this.register(new Setting<Object>("PlaceRecipe", Boolean.FALSE, this::new95));
    private final Setting<Boolean> Player = this.register(new Setting<Object>("Player", Boolean.FALSE, this::new96));
    private final Setting<Boolean> PlayerAbility = this.register(new Setting<Object>("PlayerAbility", Boolean.FALSE, this::new97));
    private final Setting<Boolean> PlayerTryUseItem = this.register(new Setting<Object>("PlayerTryUseItem", Boolean.FALSE, this::new98));
    private final Setting<Boolean> PlayerTryUseItemOnBlock = this.register(new Setting<Object>("TryUseItemOnBlock", Boolean.FALSE, this::new99));
    private final Setting<Boolean> RecipeInfo = this.register(new Setting<Object>("RecipeInfo", Boolean.FALSE, this::new100));
    private final Setting<Boolean> ResourcePackStatus = this.register(new Setting<Object>("ResourcePackStatus", Boolean.FALSE, this::new101));
    private final Setting<Boolean> SeenAdvancements = this.register(new Setting<Object>("SeenAdvancements", Boolean.FALSE, this::new102));
    private final Setting<Boolean> PlayerPackets = this.register(new Setting<Object>("PlayerPackets", Boolean.FALSE, this::new103));
    private final Setting<Boolean> Spectate = this.register(new Setting<Object>("Spectate", Boolean.FALSE, this::new104));
    private final Setting<Boolean> SteerBoat = this.register(new Setting<Object>("SteerBoat", Boolean.FALSE, this::new105));
    private final Setting<Boolean> TabCompletion = this.register(new Setting<Object>("TabCompletion", Boolean.FALSE, this::new106));
    private final Setting<Boolean> UpdateSign = this.register(new Setting<Object>("UpdateSign", Boolean.FALSE, this::new107));
    private final Setting<Boolean> UseEntity = this.register(new Setting<Object>("UseEntity", Boolean.FALSE, this::new108));
    private final Setting<Boolean> VehicleMove = this.register(new Setting<Object>("VehicleMove", Boolean.FALSE, this::new109));
    private int hudAmount = 0;

    public AntiPackets() {
        super("AntiPackets", "Blocks Packets and BookBan", Module.Category.MISC);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        if (AntiPackets.fullNullCheck()) {
            return;
        }
        if (!this.isEnabled()) {
            return;
        }
        if (send.getPacket() instanceof CPacketAnimation && this.Animations.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketChatMessage && this.ChatMessage.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketClickWindow && this.ClickWindow.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketClientSettings && this.ClientSettings.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketClientStatus && this.ClientStatus.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketCloseWindow && this.CloseWindows.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketConfirmTeleport && this.ConfirmTeleport.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketConfirmTransaction && this.ConfirmTransactions.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketCreativeInventoryAction && this.CreativeInventoryAction.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketCustomPayload && this.CustomPayloads.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketEnchantItem && this.EnchantItem.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketEntityAction && this.EntityAction.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketHeldItemChange && this.HeldItemChanges.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketInput && this.Input.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketKeepAlive && this.KeepAlives.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketPlaceRecipe && this.PlaceRecipe.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketPlayer && this.Player.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketPlayerAbilities && this.PlayerAbility.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketPlayerDigging && this.PlayerDigging.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketPlayerTryUseItem && this.PlayerTryUseItem.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && this.PlayerTryUseItemOnBlock.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketRecipeInfo && this.RecipeInfo.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketResourcePackStatus && this.ResourcePackStatus.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketSeenAdvancements && this.SeenAdvancements.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketSpectate && this.Spectate.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketSteerBoat && this.SteerBoat.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketTabComplete && this.TabCompletion.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketUpdateSign && this.UpdateSign.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketUseEntity && this.UseEntity.getValue()) {
            send.setCanceled(true);
        }
        if (send.getPacket() instanceof CPacketVehicleMove && this.VehicleMove.getValue()) {
            send.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive receive) {
        if (AntiPackets.fullNullCheck()) {
            return;
        }
        if (!this.isEnabled()) {
            return;
        }
        if (receive.getPacket() instanceof SPacketAdvancementInfo && this.AdvancementInfo.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketAnimation && this.Animation.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketBlockAction && this.BlockAction.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketBlockBreakAnim && this.BlockBreakAnim.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketBlockChange && this.BlockChange.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCamera && this.Camera.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketChangeGameState && this.ChangeGameState.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketChat && this.Chat.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketChunkData && this.ChunkData.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCloseWindow && this.CloseWindow.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCollectItem && this.CollectItem.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCombatEvent && this.CombatEvent.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketConfirmTransaction && this.ConfirmTransaction.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCooldown && this.Cooldown.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCustomPayload && this.CustomPayload.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCustomSound && this.CustomSound.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketDestroyEntities && this.DestroyEntities.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketDisconnect && this.Disconnect.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketChunkData && this.ChunkData.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCloseWindow && this.CloseWindow.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketCollectItem && this.CollectItem.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketDisplayObjective && this.DisplayObjective.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEffect && this.Effect.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntity && this.Entity.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityAttach && this.EntityAttach.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityEffect && this.EntityEffect.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityEquipment && this.EntityEquipment.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityHeadLook && this.EntityHeadLook.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityMetadata && this.EntityMetadata.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityProperties && this.EntityProperties.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityStatus && this.EntityStatus.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityTeleport && this.EntityTeleport.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketEntityVelocity && this.EntityVelocity.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketExplosion && this.Explosion.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketHeldItemChange && this.HeldItemChange.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketJoinGame && this.JoinGame.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketKeepAlive && this.KeepAlive.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketMaps && this.Maps.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketMoveVehicle && this.MoveVehicle.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketMultiBlockChange && this.MultiBlockChange.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketOpenWindow && this.OpenWindow.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketParticles && this.Particles.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketPlaceGhostRecipe && this.PlaceGhostRecipe.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketPlayerAbilities && this.PlayerAbilities.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketPlayerListHeaderFooter && this.PlayerListHeaderFooter.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketPlayerListItem && this.PlayerListItem.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketPlayerPosLook && this.PlayerPosLook.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketRecipeBook && this.RecipeBook.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketRemoveEntityEffect && this.RemoveEntityEffect.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketResourcePackSend && this.ResourcePackSend.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketRespawn && this.Respawn.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketScoreboardObjective && this.ScoreboardObjective.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSelectAdvancementsTab && this.SelectAdvancementsTab.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketServerDifficulty && this.ServerDifficulty.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSetExperience && this.SetExperience.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSetPassengers && this.SetPassengers.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSetSlot && this.SetSlot.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSignEditorOpen && this.SignEditorOpen.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSoundEffect && this.SoundEffect.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSpawnExperienceOrb && this.SpawnExperienceOrb.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSpawnGlobalEntity && this.SpawnGlobalEntity.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSpawnMob && this.SpawnMob.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSpawnObject && this.SpawnObject.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSpawnPainting && this.SpawnPainting.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSpawnPlayer && this.SpawnPlayer.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketSpawnPosition && this.SpawnPosition.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketStatistics && this.Statistics.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketTabComplete && this.TabComplete.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketTeams && this.Teams.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketTimeUpdate && this.TimeUpdate.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketTitle && this.Title.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketUnloadChunk && this.UnloadChunk.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketUpdateBossInfo && this.UpdateBossInfo.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketUpdateHealth && this.UpdateHealth.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketUpdateScore && this.UpdateScore.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketUpdateTileEntity && this.UpdateTileEntity.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketUseBed && this.UseBed.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketWindowItems && this.WindowItems.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketWindowProperty && this.WindowProperty.getValue()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketWorldBorder && this.WorldBorder.getValue()) {
            receive.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {
        String string = "§aAntiPackets On!§f Cancelled Packets: ";
        StringBuilder stringBuilder = new StringBuilder(string);
        if (!this.settings.isEmpty()) {
            for (Setting setting : this.settings) {
                if (!(setting.getValue() instanceof Boolean) || !(Boolean) setting.getValue() || setting.getName().equalsIgnoreCase("Enabled") || setting.getName().equalsIgnoreCase("drawn")) continue;
                String string2 = setting.getName();
                stringBuilder.append(string2).append(", ");
            }
        }
        if (stringBuilder.toString().equals(string)) {
            Command.sendMessage("§aAntiPackets On!§f Currently not cancelling any Packets.");
        } else {
            String string3 = this.removeLastChar(this.removeLastChar(stringBuilder.toString()));
            Command.sendMessage(string3);
        }
    }

    @Override
    public void onUpdate() {
        if (AntiPackets.fullNullCheck()) {
            return;
        }
        int n = 0;
        if (!this.settings.isEmpty()) {
            for (Setting setting : this.settings) {
                if (!(setting.getValue() instanceof Boolean) || !(Boolean) setting.getValue() || setting.getName().equalsIgnoreCase("Enabled") || setting.getName().equalsIgnoreCase("drawn")) continue;
                ++n;
            }
        }
        this.hudAmount = n;
    }

    @Override
    public String getDisplayInfo() {
        if (this.hudAmount == 0) {
            return "";
        }
        return String.valueOf(this.hudAmount);
    }

    @SubscribeEvent
    public void onLeavingDeathEvent(PacketEvent.Receive receive) {
        if (AntiPackets.fullNullCheck()) {
            return;
        }
        if (!(receive.getPacket() instanceof CPacketClickWindow)) {
            return;
        }
        CPacketClickWindow cPacketClickWindow = receive.getPacket();
        if (!(cPacketClickWindow.getClickedItem().getItem() instanceof ItemWrittenBook)) {
            return;
        }
        receive.isCancelled();
        AntiPackets.mc.player.openContainer.slotClick(cPacketClickWindow.getSlotId(), cPacketClickWindow.getUsedButton(), cPacketClickWindow.getClickType(), AntiPackets.mc.player);
    }

    public String removeLastChar(String string) {
        if (string != null && string.length() > 0) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    private boolean new109(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4;
    }

    private boolean new108(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4;
    }

    private boolean new107(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4;
    }

    private boolean new106(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4;
    }

    private boolean new105(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4;
    }

    private boolean new104(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4;
    }

    private boolean new103(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 4;
    }

    private boolean new102(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3;
    }

    private boolean new101(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3;
    }

    private boolean new100(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3;
    }

    private boolean new99(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3;
    }

    private boolean new98(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3;
    }

    private boolean new97(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3;
    }

    private boolean new96(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 3;
    }

    private boolean new95(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2;
    }

    private boolean new94(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2;
    }

    private boolean new93(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2;
    }

    private boolean new92(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2;
    }

    private boolean new91(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2;
    }

    private boolean new90(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2;
    }

    private boolean new89(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2;
    }

    private boolean new88(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 2;
    }

    private boolean new87(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1;
    }

    private boolean new86(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1;
    }

    private boolean new85(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1;
    }

    private boolean new84(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1;
    }

    private boolean new83(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1;
    }

    private boolean new82(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1;
    }

    private boolean new81(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1;
    }

    private boolean new80(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.pages.getValue() == 1;
    }

    private boolean new79(Object object) {
        return this.mode.getValue() == Mode.CLIENT;
    }

    private boolean new78(Object object) {
        return this.mode.getValue() == Mode.CLIENT && this.page.getValue() == 3;
    }

    private boolean new77(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10;
    }

    private boolean new76(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10;
    }

    private boolean new75(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10;
    }

    private boolean new74(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10;
    }

    private boolean new73(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 10;
    }

    private boolean new72(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9;
    }

    private boolean new71(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9;
    }

    private boolean new70(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9;
    }

    private boolean new69(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9;
    }

    private boolean new68(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9;
    }

    private boolean new67(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9;
    }

    private boolean new66(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9;
    }

    private boolean new65(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 9;
    }

    private boolean new64(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8;
    }

    private boolean new63(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8;
    }

    private boolean new62(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8;
    }

    private boolean new61(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8;
    }

    private boolean new60(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8;
    }

    private boolean new59(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8;
    }

    private boolean new58(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8;
    }

    private boolean new57(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 8;
    }

    private boolean new56(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7;
    }

    private boolean new55(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7;
    }

    private boolean new54(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7;
    }

    private boolean new53(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7;
    }

    private boolean new52(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7;
    }

    private boolean new51(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7;
    }

    private boolean new50(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7;
    }

    private boolean new49(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 7;
    }

    private boolean new48(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6;
    }

    private boolean new47(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6;
    }

    private boolean new46(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6;
    }

    private boolean new45(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6;
    }

    private boolean new44(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6;
    }

    private boolean new43(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6;
    }

    private boolean new42(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6;
    }

    private boolean new41(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 6;
    }

    private boolean new40(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5;
    }

    private boolean new39(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5;
    }

    private boolean new38(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5;
    }

    private boolean new37(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5;
    }

    private boolean new36(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5;
    }

    private boolean new35(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5;
    }

    private boolean new34(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5;
    }

    private boolean new33(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 5;
    }

    private boolean new32(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4;
    }

    private boolean new31(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4;
    }

    private boolean new30(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4;
    }

    private boolean new29(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4;
    }

    private boolean new28(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4;
    }

    private boolean new27(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4;
    }

    private boolean new26(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4;
    }

    private boolean new25(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 4;
    }

    private boolean new24(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3;
    }

    private boolean new23(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3;
    }

    private boolean new22(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3;
    }

    private boolean new21(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3;
    }

    private boolean new20(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3;
    }

    private boolean new19(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3;
    }

    private boolean new18(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3;
    }

    private boolean new17(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 3;
    }

    private boolean new16(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2;
    }

    private boolean new15(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2;
    }

    private boolean new14(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2;
    }

    private boolean new13(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2;
    }

    private boolean new12(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2;
    }

    private boolean new11(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2;
    }

    private boolean new10(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2;
    }

    private boolean new9(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 2;
    }

    private boolean new8(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1;
    }

    private boolean new7(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1;
    }

    private boolean new6(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1;
    }

    private boolean new5(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1;
    }

    private boolean new4(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1;
    }

    private boolean new3(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1;
    }

    private boolean new2(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1;
    }

    private boolean new1(Object object) {
        return this.mode.getValue() == Mode.SERVER && this.page.getValue() == 1;
    }

    private boolean new0(Object object) {
        return this.mode.getValue() == Mode.SERVER;
    }

    public enum Mode {
        CLIENT,
        SERVER

    }
}

