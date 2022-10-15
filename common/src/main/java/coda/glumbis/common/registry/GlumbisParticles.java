package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class GlumbisParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Glumbis.MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);

    public static final RegistrySupplier<SimpleParticleType> STATIC_LIGHTNING =
            PARTICLES.register("static_lightning", () -> new SimpleParticleType(false));
}
