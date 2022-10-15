package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import coda.glumbis.common.items.*;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;

public class GlumbisItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Glumbis.MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> SOCK = ITEMS.register("sock", () -> new Item(new Item.Properties().tab(Glumbis.GROUP).stacksTo(16)));
    public static final RegistrySupplier<Item> GLUMBIS = ITEMS.register("glumbis", () -> new GlumbisItem(new Item.Properties().tab(Glumbis.GROUP).stacksTo(1)));
    public static final RegistrySupplier<Item> SOCKS = ITEMS.register("socks", () -> new SocksArmorItem(EquipmentSlot.FEET));
    public static final RegistrySupplier<Item> SOGGY_SOCKS = ITEMS.register("soggy_socks", () -> new SoggySocksArmorItem(EquipmentSlot.FEET));

    public static final RegistrySupplier<Item> ROTTEN_EGG_SPAWN_EGG = ITEMS.register("rotten_egg_spawn_egg", () -> new ArchitecturySpawnEggItem(GlumbisEntities.GLUMBOSS, 0xfbfcfc, 0xfff167, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistrySupplier<Item> CAT_ESSENCE = ITEMS.register("cat_essence", () -> new CatEssenceItem(new Item.Properties().tab(Glumbis.GROUP).rarity(Rarity.EPIC)));
    public static final RegistrySupplier<Item> GLUMBO = ITEMS.register("glumbo", () -> new BowlFoodItem(new Item.Properties().tab(Glumbis.GROUP).rarity(Rarity.RARE).food(new FoodProperties.Builder().saturationMod(1.0F).nutrition(12).effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1.0F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0), 1.0F).effect(new MobEffectInstance(MobEffects.REGENERATION, 400, 0), 1.0F).alwaysEat().build()).stacksTo(16)));
    public static final RegistrySupplier<Item> GLUMP_CANNON = ITEMS.register("glump_cannon", () -> new GlumpCannonItem(new Item.Properties().rarity(Rarity.RARE).tab(Glumbis.GROUP).stacksTo(1).durability(124)));
    public static final RegistrySupplier<Item> ROCKET_PROPELLED_GLUMP = ITEMS.register("rocket_propelled_glump", () -> new Item(new Item.Properties().tab(Glumbis.GROUP).stacksTo(16)));
    public static final RegistrySupplier<Item> BIG_SOCK = ITEMS.register("big_sock", () -> new BigSockItem(new Item.Properties().tab(Glumbis.GROUP).stacksTo(1).rarity(Rarity.EPIC)));

    public static final RegistrySupplier<Item> CATNIP_SEEDS = ITEMS.register("catnip_seeds", () -> new ItemNameBlockItem(GlumbisBlocks.CATNIP.get(), (new Item.Properties()).tab(Glumbis.GROUP)));
    public static final RegistrySupplier<Item> CATNIP = ITEMS.register("catnip", () -> new Item(new Item.Properties().tab(Glumbis.GROUP)));


}


