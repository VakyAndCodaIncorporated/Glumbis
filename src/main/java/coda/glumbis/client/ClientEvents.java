package coda.glumbis.client;

import coda.glumbis.Glumbis;
import coda.glumbis.client.renderer.BigSockRenderer;
import coda.glumbis.client.renderer.GlumbossRenderer;
import coda.glumbis.client.renderer.GlumpRenderer;
import coda.glumbis.client.renderer.RocketPropelledGlumpRenderer;
import coda.glumbis.common.registry.GlumbisBlocks;
import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.networking.GlumbisNetworking;
import coda.glumbis.networking.InputMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@Mod.EventBusSubscriber(modid = Glumbis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        GlumbisKeybinds.register();

        EntityRenderers.register(GlumbisEntities.GLUMBOSS.get(), GlumbossRenderer::new);
        EntityRenderers.register(GlumbisEntities.GLUMP.get(), GlumpRenderer::new);
        EntityRenderers.register(GlumbisEntities.ROCKET_PROPELLED_GLUMP.get(), RocketPropelledGlumpRenderer::new);
        EntityRenderers.register(GlumbisEntities.BIG_SOCK.get(), BigSockRenderer::new);

        ItemBlockRenderTypes.setRenderLayer(GlumbisBlocks.CATNIP.get(), RenderType.cutout());
    }

    @Mod.EventBusSubscriber(modid = Glumbis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeBusEvents {

        @SubscribeEvent
        public static void onKeyPress(InputEvent.KeyInputEvent event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            if (GlumbisKeybinds.slamKey.isDown()) {
                GlumbisNetworking.CHANNEL.sendToServer(new InputMessage(KeyEvent.VK_G));
            }
        }

    }
}