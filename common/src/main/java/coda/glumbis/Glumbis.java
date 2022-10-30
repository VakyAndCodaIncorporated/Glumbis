package coda.glumbis;

import coda.glumbis.client.GlumbisClient;
import coda.glumbis.client.particle.StaticElectricityParticle;
import coda.glumbis.common.CommonEvents;
import coda.glumbis.common.entities.BigSockEntity;
import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.registry.*;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

public class Glumbis {
    public static final String MOD_ID = "glumbis";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final CreativeModeTab GROUP = CreativeTabRegistry.create(
            new ResourceLocation(MOD_ID, MOD_ID),
            () -> new ItemStack(GlumbisItems.GLUMBIS.get()));

    public static void init() {
        // Registry init
        GlumbisBlocks.BLOCKS.register();
        GlumbisItems.ITEMS.register();
        GlumbisEntities.ENTITIES.register();
        GlumbisSounds.SOUNDS.register();
        GlumbisParticles.PARTICLES.register();

        // Hook common mod events
        CommonEvents.init();

        // Register entity attributes
        EntityAttributeRegistry.register(GlumbisEntities.GLUMBOSS, GlumbossEntity::createAttributes);
        EntityAttributeRegistry.register(GlumbisEntities.GLUMP, GlumpEntity::createAttributes);
        EntityAttributeRegistry.register(GlumbisEntities.BIG_SOCK, BigSockEntity::createAttributes);

        // Initialize client
        EnvExecutor.runInEnv(Env.CLIENT, () -> GlumbisClient::clientInit);

        // Start geckolib
        GeckoLib.initialize();
    }
}
