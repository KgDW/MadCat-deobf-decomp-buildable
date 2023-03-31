package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.Timer;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.apache.commons.lang3.RandomStringUtils;

public class Message
extends Module {
    private final Timer timer = new Timer();
    private final Setting<String> custom = this.register(new Setting<>("Custom", "MadCat 3.0 release? 589191561"));
    private final Setting<Integer> random = this.register(new Setting<>("Random", 0, 0, 20));
    private final Setting<Integer> delay = this.register(new Setting<>("Delay", 100, 0, 10000));

    public Message() {
        super("Spammer", "Message", Module.Category.MISC);
    }

    @Override
    public void onTick() {
        if (!this.timer.passedMs(this.delay.getValue())) {
            return;
        }
        Message.mc.player.connection.sendPacket(new CPacketChatMessage(this.custom.getValue() + " " + RandomStringUtils.randomAlphanumeric(this.random.getValue())));
        this.timer.reset();
    }
}

