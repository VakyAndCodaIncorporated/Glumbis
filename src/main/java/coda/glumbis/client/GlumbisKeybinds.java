package coda.glumbis.client;

import coda.glumbis.Glumbis;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class GlumbisKeybinds {
    public static KeyMapping slamKey;

    public static void register() {
        slamKey = create("slam_key", KeyEvent.VK_G);

        ClientRegistry.registerKeyBinding(slamKey);
    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + Glumbis.MOD_ID + "." + name, key, "key.category." + Glumbis.MOD_ID);
    }
}
