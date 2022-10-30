package coda.glumbis.client;

import coda.glumbis.client.particle.StaticElectricityParticle;
import coda.glumbis.client.renderer.BigSockRenderer;
import coda.glumbis.client.renderer.GlumbossRenderer;
import coda.glumbis.client.renderer.GlumpRenderer;
import coda.glumbis.client.renderer.RocketPropelledGlumpRenderer;
import coda.glumbis.common.registry.GlumbisBlocks;
import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.common.registry.GlumbisParticles;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;

public class GlumbisClient {
    /**
     * Performs client setup like render types etc.
     */
    public static void clientSetup() {
        RenderTypeRegistry.register(RenderType.cutout(), GlumbisBlocks.CATNIP.get());
    }

    /**
     * Performs client-side registration steps.
     */
    public static void clientInit() {
        ParticleProviderRegistry.register(GlumbisParticles.STATIC_LIGHTNING, StaticElectricityParticle.Provider::new);

        EntityRendererRegistry.register(GlumbisEntities.GLUMBOSS, GlumbossRenderer::new);
        EntityRendererRegistry.register(GlumbisEntities.GLUMP, GlumpRenderer::new);
        EntityRendererRegistry.register(GlumbisEntities.ROCKET_PROPELLED_GLUMP, RocketPropelledGlumpRenderer::new);
        EntityRendererRegistry.register(GlumbisEntities.BIG_SOCK, BigSockRenderer::new);
    }
}
