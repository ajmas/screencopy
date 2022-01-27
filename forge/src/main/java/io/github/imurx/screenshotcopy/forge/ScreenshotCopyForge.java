package io.github.imurx.screenshotcopy.forge;

import io.github.imurx.screenshotcopy.ScreencopyConfig;
import io.github.imurx.screenshotcopy.ScreenshotCopy;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.text.TranslatableText;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import io.github.imurx.arboard.Clipboard;

@Mod(ScreenshotCopy.MOD_ID)
public class ScreenshotCopyForge {
    public ScreenshotCopyForge() {
        var a = new Clipboard();
        a.close();
        AutoConfig.register(ScreencopyConfig.class, Toml4jConfigSerializer::new);
        ScreenshotCopy.init();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onScreenshot(ScreenshotEvent ev) {
        if(ev.getScreenshotFile().exists()) return;
        try {
            ScreenshotCopy.copyScreenshot(ev.getImage());
            if(!AutoConfig.getConfigHolder(ScreencopyConfig.class).getConfig().saveScreenshot) {
                ev.setResultMessage(new TranslatableText("screencopy.success"));
                ev.setCanceled(true);
            }
        } catch(Exception ex) {
            ev.setResultMessage(new TranslatableText("screencopy.failure", ex.toString()));
            if(!AutoConfig.getConfigHolder(ScreencopyConfig.class).getConfig().saveScreenshot) ev.setCanceled(true);
        }
    }
}
