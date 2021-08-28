package coda.glumbis.client;

import coda.glumbis.Glumbis;
import coda.glumbis.client.model.BigSockModel;
import coda.glumbis.client.model.GlumbossModel;
import coda.glumbis.client.renderer.BigSockRenderer;
import coda.glumbis.client.renderer.GlumbossRenderer;
import coda.glumbis.common.init.GlumbisEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Glumbis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(GlumbisEntities.GLUMBOSS.get(), GlumbossRenderer::new);
        EntityRenderers.register(GlumbisEntities.BIG_SOCK.get(), BigSockRenderer::new);

        ForgeHooksClient.registerLayerDefinition(GlumbossRenderer.MODEL_LAYER, GlumbossModel::getLayer);
        ForgeHooksClient.registerLayerDefinition(BigSockRenderer.MODEL_LAYER, BigSockModel::getLayer);
    }
}
