package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import coda.glumbis.common.blocks.entities.GlumpCoilBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GlumbisBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Glumbis.MOD_ID);

    public static final RegistryObject<BlockEntityType<GlumpCoilBlockEntity>> GLUMP_COIL = BLOCK_ENTITIES.register("glump_coil", () -> BlockEntityType.Builder.of(GlumpCoilBlockEntity::new, GlumbisBlocks.GLUMP_COIL.get()).build(null));

}
