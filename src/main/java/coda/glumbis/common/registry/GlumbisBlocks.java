package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import coda.glumbis.common.blocks.CatnipBlock;
import coda.glumbis.common.blocks.GlumpCoilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GlumbisBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Glumbis.MOD_ID);

    public static final RegistryObject<Block> CATNIP = BLOCKS.register("catnip", () -> new CatnipBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)));
    public static final RegistryObject<Block> GLUMP_COIL = BLOCKS.register("glump_coil", () -> new GlumpCoilBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0F, 6.0F).requiresCorrectToolForDrops().randomTicks().sound(SoundType.METAL)));
}
