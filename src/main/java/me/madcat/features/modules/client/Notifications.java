package me.madcat.features.modules.client;

import java.util.ArrayList;
import me.madcat.event.events.Render2DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.FadeUtils;
import me.madcat.util.RenderUtil;

public class Notifications
extends Module {
    public static final ArrayList<Notifys> notifyList = new ArrayList();
    private final Setting<Integer> notifyY = this.register(new Setting<>("Y", 18, 25, 500));
    private final Setting<Integer> alpha = this.register(new Setting<>("Alpha", 155, 0, 255));

    public Notifications() {
        super("Notifications", "Notify toggle module", Module.Category.CLIENT);
    }

    @Override
    public void onRender2D(Render2DEvent render2DEvent) {
        boolean bl = true;
        int n = this.renderer.scaledHeight - this.notifyY.getValue();
        int n2 = this.renderer.scaledWidth;
        int n3 = ColorUtil.toRGBA(ClickGui.INSTANCE().red.getValue(), ClickGui.INSTANCE().green.getValue(), ClickGui.INSTANCE().blue.getValue());
        for (Notifys notifys : notifyList) {
            if (notifys == null || notifys.first == null || notifys.firstFade == null || notifys.delayed < 1) continue;
            bl = false;
            if (notifys.delayed < 5 && !notifys.end) {
                notifys.end = true;
                notifys.endFade.reset();
            }
            n = (int)((double)n - 18.0 * notifys.yFade.easeOutQuad());
            String string = notifys.first;
            double d = notifys.delayed < 5 ? (double)n2 - (double)(this.renderer.getStringWidth(string) + 10) * (1.0 - notifys.endFade.easeOutQuad()) : (double)n2 - (double)(this.renderer.getStringWidth(string) + 10) * notifys.firstFade.easeOutQuad();
            RenderUtil.drawRectangleCorrectly((int)d, n, 10 + this.renderer.getStringWidth(string), 15, ColorUtil.toRGBA(20, 20, 20, this.alpha.getValue()));
            this.renderer.drawString(string, 5 + (int)d, 4 + n, ColorUtil.toRGBA(255, 255, 255), true);
            if (notifys.delayed < 5) {
                n = (int)((double)n + 18.0 * notifys.yFade.easeOutQuad() - 18.0 * (1.0 - notifys.endFade.easeOutQuad()));
                continue;
            }
            RenderUtil.drawRectangleCorrectly((int)d, n + 14, (10 + this.renderer.getStringWidth(string)) * (notifys.delayed - 4) / 62, 1, n3);
        }
        if (bl) {
            notifyList.clear();
        }
    }

    @Override
    public void onUpdate() {
        for (Notifys notifys : notifyList) {
            if (notifys == null || notifys.first == null || notifys.firstFade == null) continue;
            --notifys.delayed;
        }
    }

    @Override
    public void onDisable() {
        notifyList.clear();
    }

    public static class Notifys {
        public final FadeUtils firstFade = new FadeUtils(500L);
        public final FadeUtils endFade;
        public final FadeUtils yFade = new FadeUtils(500L);
        public final String first;
        public int delayed = 55;
        public boolean end;

        public Notifys(String string) {
            this.endFade = new FadeUtils(350L);
            this.first = string;
            this.firstFade.reset();
            this.yFade.reset();
            this.endFade.reset();
            this.end = false;
        }
    }
}

