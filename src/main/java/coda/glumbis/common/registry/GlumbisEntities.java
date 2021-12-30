package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GlumbisEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Glumbis.MOD_ID);

    public static final RegistryObject<EntityType<GlumbossEntity>> GLUMBOSS = create("glumboss", EntityType.Builder.of(GlumbossEntity::new, MobCategory.CREATURE).sized(1.5f, 2.0f).fireImmune());
    public static final RegistryObject<EntityType<GlumpEntity>> GLUMP = create("glump", EntityType.Builder.of(GlumpEntity::new, MobCategory.CREATURE).sized(0.6f, 0.9f));

    public static final RegistryObject<EntityType<RocketPropelledGlumpEntity>> ROCKET_PROPELLED_GLUMP = create("rocket_propelled_glump",EntityType.Builder.of(RocketPropelledGlumpEntity::new, MobCategory.MISC).sized(0.25f, 0.25f));

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Glumbis.MOD_ID + "." + name));
    }
}
