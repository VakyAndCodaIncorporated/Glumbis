package coda.glumbis.common.items;

import coda.glumbis.Glumbis;
import coda.glumbis.common.registry.GlumbisItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class SocksArmorItem extends ArmorItem implements DyeableLeatherItem {
    public static final ArmorMaterial MATERIAL = new SockArmorMaterial(Glumbis.MOD_ID + ":socks", 2, new int[]{1, 2, 2, 1}, 9, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, () -> Ingredient.of(Items.STRING));

    public SocksArmorItem(EquipmentSlot slot) {
        super(MATERIAL, slot, new Item.Properties().tab(Glumbis.GROUP).stacksTo(1));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (player.isInWaterOrRain()) {
            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag targetTag = tag.copy();

            ItemStack wetSocks = new ItemStack(GlumbisItems.SOGGY_SOCKS.get());

            wetSocks.setTag(targetTag);
            player.setItemSlot(EquipmentSlot.FEET, wetSocks);
        }
    }

    @Override
    public int getColor(ItemStack p_41122_) {
        CompoundTag compoundtag = p_41122_.getTagElement("display");
        return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : 14145495;
    }
}
