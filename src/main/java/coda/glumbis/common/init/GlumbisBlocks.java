package coda.glumbis.common.init;

import coda.glumbis.Glumbis;
import coda.glumbis.common.blocks.CatnipBlock;
import coda.glumbis.common.items.GlumbisItem;
import coda.glumbis.common.items.SocksArmorItem;
import coda.glumbis.common.items.SoggySocksArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GlumbisBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Glumbis.MOD_ID);

    public static final RegistryObject<Block> CATNIP = BLOCKS.register("catnip", () -> new CatnipBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)));
}
