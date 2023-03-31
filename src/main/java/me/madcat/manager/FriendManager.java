package me.madcat.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import me.madcat.features.Feature;
import me.madcat.features.setting.Setting;
import me.madcat.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

public class FriendManager
extends Feature {
    private List<Friend> friends = new ArrayList<>();

    public FriendManager() {
        super("Friends");
    }

    public void saveFriends() {
        this.clearSettings();
        this.cleanFriends();
        for (Friend friend : this.friends) {
            this.register(new Setting<>(friend.getUuid().toString(), friend.getUsername()));
        }
    }

    public List<Friend> getFriends() {
        this.cleanFriends();
        return this.friends;
    }

    public void addFriend(Friend friend) {
        this.friends.add(friend);
    }

    public void removeFriend(String string) {
        this.cleanFriends();
        for (Friend friend : this.friends) {
            if (!friend.getUsername().equalsIgnoreCase(string)) {
                continue;
            }
            this.friends.remove(friend);
            break;
        }
    }

    private static boolean isfriend2(String string, Friend friend) {
        return Friend.access(friend).equalsIgnoreCase(string);
    }

    public void cleanFriends() {
        this.friends.stream().filter(Objects::nonNull).filter(FriendManager::cleanFriends);
    }

    public Friend getFriendByName(String string) {
        UUID uUID = PlayerUtil.getUUIDFromName(string);
        if (uUID != null) {
            return new Friend(string, uUID);
        }
        return null;
    }

    public void onLoad() {
        this.friends = new ArrayList<>();
        this.clearSettings();
    }

    public boolean isFriend(String string) {
        this.cleanFriends();
        return this.friends.stream().anyMatch(arg_0 -> FriendManager.isfriend2(string, arg_0));
    }

    private static boolean cleanFriends(Friend friend) {
        return friend.getUsername() != null;
    }

    public void addFriend(String string) {
        Friend friend = this.getFriendByName(string);
        if (friend != null) {
            this.friends.add(friend);
        }
        this.cleanFriends();
    }

    public boolean isFriend(EntityPlayer entityPlayer) {
        return this.isFriend(entityPlayer.getName());
    }

    public static class Friend {
        private final String username;
        private final UUID uuid;

        static String access(Friend friend) {
            return friend.username;
        }

        public String getUsername() {
            return this.username;
        }

        public Friend(String string, UUID uUID) {
            this.username = string;
            this.uuid = uUID;
        }

        public UUID getUuid() {
            return this.uuid;
        }
    }
}

