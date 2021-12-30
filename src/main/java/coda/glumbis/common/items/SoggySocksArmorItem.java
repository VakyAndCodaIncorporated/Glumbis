package coda.glumbis.common.items;

import coda.glumbis.Glumbis;
import coda.glumbis.common.registry.GlumbisItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.Random;

public class SoggySocksArmorItem extends ArmorItem {
    public static final ArmorMaterial MATERIAL = new SockArmorMaterial(Glumbis.MOD_ID + ":soggy_socks", 2, new int[]{2, 3, 3, 2}, 9, SoundEvents.GENERIC_SPLASH, 0.0F, () -> Ingredient.of(Items.STRING));
    private final Random rand = new Random();

    public SoggySocksArmorItem(EquipmentSlot slot) {
        super(MATERIAL, slot, new Properties().tab(Glumbis.GROUP).stacksTo(1));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!player.isInWaterOrRain() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) == 0 && rand.nextInt(500) == 0) {
            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag targetTag = tag.copy();

            ItemStack drySocks = new ItemStack(GlumbisItems.SOCKS.get());

            drySocks.setTag(targetTag);
            player.setItemSlot(EquipmentSlot.FEET, drySocks);
        }

        double d0 = rand.nextGaussian() * 0.056D;
        double d1 = rand.nextGaussian() * 0.034D;
        double d2 = rand.nextGaussian() * 0.025D;
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);

        if (level > 0) {
            for (int i = 1; i < level + 1; i++) {
                world.addParticle(ParticleTypes.FALLING_WATER, player.getRandomX(0.25D), player.getY() + 0.15D, player.getRandomZ(0.25D), d0, d1, d2);
                world.addParticle(ParticleTypes.FALLING_WATER, player.getRandomX(0.25D), player.getY() + 0.15D, player.getRandomZ(0.25D), d0, d1, d2);
            }
        }
        else {
            world.addParticle(ParticleTypes.FALLING_WATER, player.getRandomX(0.25D), player.getY() + 0.15D, player.getRandomZ(0.25D), d0, d1, d2);
            world.addParticle(ParticleTypes.FALLING_WATER, player.getRandomX(0.25D), player.getY() + 0.15D, player.getRandomZ(0.25D), d0, d1, d2);
        }
    }
}
