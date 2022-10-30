package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import coda.glumbis.PlatformLayer;
import coda.glumbis.common.entities.BigSockEntity;
import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class GlumbisEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Glumbis.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);

    public static final RegistrySupplier<EntityType<GlumbossEntity>> GLUMBOSS = create("glumboss", EntityType.Builder.of(GlumbossEntity::new, MobCategory.CREATURE).sized(1.5f, 2.95f).fireImmune());
    public static final RegistrySupplier<EntityType<GlumpEntity>> GLUMP = create("glump", EntityType.Builder.of(GlumpEntity::new, MobCategory.CREATURE).sized(0.6f, 0.9f));

    public static final RegistrySupplier<EntityType<RocketPropelledGlumpEntity>> ROCKET_PROPELLED_GLUMP = create("rocket_propelled_glump", EntityType.Builder.of(RocketPropelledGlumpEntity::new, MobCategory.MISC).sized(0.25f, 0.25f));
    public static final RegistrySupplier<EntityType<BigSockEntity>> BIG_SOCK = create("big_sock", EntityType.Builder.of(PlatformLayer.getBigSockFactory(), MobCategory.MISC).sized(1.35f, 2.4f));

    private static <T extends Entity> RegistrySupplier<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Glumbis.MOD_ID + "." + name));
    }
}
