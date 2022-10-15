package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import coda.glumbis.common.blocks.CatnipBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class GlumbisBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Glumbis.MOD_ID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> CATNIP = BLOCKS.register("catnip", () -> new CatnipBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)));
}
