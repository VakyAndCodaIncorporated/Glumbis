package coda.glumbis.client.forge;

import coda.glumbis.Glumbis;
import coda.glumbis.client.GlumbisClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Glumbis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GlumbisClientForge {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        GlumbisClient.clientSetup();
    }
}
