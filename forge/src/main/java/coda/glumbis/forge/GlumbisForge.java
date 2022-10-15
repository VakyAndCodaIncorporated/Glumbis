package coda.glumbis.forge;

import coda.glumbis.Glumbis;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Glumbis.MOD_ID)
public class GlumbisForge {
    public GlumbisForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Glumbis.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Glumbis.init();
    }
}
