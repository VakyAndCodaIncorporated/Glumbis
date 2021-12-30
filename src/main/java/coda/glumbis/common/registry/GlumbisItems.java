package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import coda.glumbis.common.items.GlumbisItem;
import coda.glumbis.common.items.SocksArmorItem;
import coda.glumbis.common.items.SoggySocksArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GlumbisItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Glumbis.MOD_ID);

    public static final RegistryObject<Item> SOCK = ITEMS.register("sock", () -> new Item(new Item.Properties().tab(Glumbis.GROUP).stacksTo(16)));
    public static final RegistryObject<Item> GLUMBIS = ITEMS.register("glumbis", () -> new GlumbisItem(new Item.Properties().tab(Glumbis.GROUP).stacksTo(1)));
    public static final RegistryObject<Item> SOCKS = ITEMS.register("socks", () -> new SocksArmorItem(EquipmentSlot.FEET));
    public static final RegistryObject<Item> SOGGY_SOCKS = ITEMS.register("soggy_socks", () -> new SoggySocksArmorItem(EquipmentSlot.FEET));

    public static final RegistryObject<Item> CATNIP_SEEDS = ITEMS.register("catnip_seeds", () -> new ItemNameBlockItem(GlumbisBlocks.CATNIP.get(), (new Item.Properties()).tab(Glumbis.GROUP)));
    public static final RegistryObject<Item> CATNIP = ITEMS.register("catnip", () -> new Item(new Item.Properties().tab(Glumbis.GROUP)));
}
