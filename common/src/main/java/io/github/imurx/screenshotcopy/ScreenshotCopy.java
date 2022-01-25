package io.github.imurx.screenshotcopy;

import io.github.imurx.arboard.ImageData;
import net.minecraft.client.texture.NativeImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.imurx.arboard.Clipboard;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class ScreenshotCopy {
    public static final String MOD_ID = "screencopy";
    private static final Logger LOGGER = LoggerFactory.getLogger("Screencopy");
    private static Clipboard clipboard;
    public static void init() {
        if(clipboard != null) LOGGER.warn("Someone tried to init me again", new IllegalStateException("Clipboard is already defined, can't init it again"));
        clipboard = new Clipboard();
    }

    public static void stop() {
        clipboard.close();
        clipboard = null;
    }

    public static void copyScreenshot(NativeImage image) {
        ByteBuffer imageBytes = ByteBuffer.allocate(image.getWidth() * image.getHeight() * 4);
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                imageBytes.putInt(image.getColor(x, y));
            }
        }
        try(ImageData data = new ImageData(image.getWidth(), image.getHeight(), imageBytes.array())) {
            clipboard.setImage(data);
        } catch(Exception ex) {
            LOGGER.error("Trying to create image data", ex);
        }
    }
}
