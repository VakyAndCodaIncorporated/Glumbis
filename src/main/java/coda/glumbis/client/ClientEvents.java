package coda.glumbis.client;

import coda.glumbis.Glumbis;
import coda.glumbis.client.renderer.GlumbossRenderer;
import coda.glumbis.client.renderer.GlumpRenderer;
import coda.glumbis.common.init.GlumbisBlocks;
import coda.glumbis.common.init.GlumbisEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Glumbis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(GlumbisEntities.GLUMBOSS.get(), GlumbossRenderer::new);
        EntityRenderers.register(GlumbisEntities.GLUMP.get(), GlumpRenderer::new);

        ItemBlockRenderTypes.setRenderLayer(GlumbisBlocks.CATNIP.get(), RenderType.cutout());

    }
}
