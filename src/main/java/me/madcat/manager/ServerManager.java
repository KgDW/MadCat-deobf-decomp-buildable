package me.madcat.manager;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import me.madcat.features.Feature;
import me.madcat.features.modules.client.HUD;
import me.madcat.util.Timer;

public class ServerManager
extends Feature {
    private final float[] tpsCounts = new float[10];
    private float TPS = 20.0f;
    private String serverBrand = "";
    private final DecimalFormat format = new DecimalFormat("##.00#");
    private long lastUpdate = -1L;
    private final Timer timer = new Timer();

    public void update() {
        float f;
        long l = System.currentTimeMillis();
        if (this.lastUpdate == -1L) {
            this.lastUpdate = l;
            return;
        }
        long l2 = l - this.lastUpdate;
        float f2 = (float)l2 / 20.0f;
        if (f2 == 0.0f) {
            f2 = 50.0f;
        }
        if ((f = 1000.0f / f2) > 20.0f) {
            f = 20.0f;
        }
        System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
        this.tpsCounts[0] = f;
        double d = 0.0;
        for (float f3 : this.tpsCounts) {
            d += f3;
        }
        if ((d /= this.tpsCounts.length) > 20.0) {
            d = 20.0;
        }
        this.TPS = Float.parseFloat(this.format.format(d));
        this.lastUpdate = l;
    }

    public float getTpsFactor() {
        return 20.0f / this.TPS;
    }

    public int getPing() {
        if (ServerManager.fullNullCheck()) {
            return 0;
        }
        try {
            return Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
        }
        catch (Exception exception) {
            return 0;
        }
    }

    public String getServerBrand() {
        return this.serverBrand;
    }

    public void setServerBrand(String string) {
        this.serverBrand = string;
    }

    public long serverRespondingTime() {
        return this.timer.getPassedTimeMs();
    }

    public float getTPS() {
        return this.TPS;
    }

    public boolean isServerNotResponding() {
        return this.timer.passedMs(HUD.INSTANCE().lagTime.getValue());
    }

    @Override
    public void reset() {
        Arrays.fill(this.tpsCounts, 20.0f);
        this.TPS = 20.0f;
    }

    public void onPacketReceived() {
        this.timer.reset();
    }
}

