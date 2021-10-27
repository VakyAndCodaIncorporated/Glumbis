package coda.glumbis.common.init;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GlumbisEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Glumbis.MOD_ID);

    public static final RegistryObject<EntityType<GlumbossEntity>> GLUMBOSS = create("glumboss", EntityType.Builder.of(GlumbossEntity::new, MobCategory.CREATURE).sized(1.5f, 2.0f));
    public static final RegistryObject<EntityType<GlumpEntity>> GLUMP = create("glump", EntityType.Builder.of(GlumpEntity::new, MobCategory.CREATURE).sized(0.5f, 0.5f));

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Glumbis.MOD_ID + "." + name));
    }
}
