package coda.glumbis.common.items.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Helper for armor ticking. Added as I know more armor is planned and this may come in handy in more than just one place.
 * On forge, this will just be the same as in IForgeItem, on fabric and quilt this will be used by our mixin.
 */
public interface IArmorTick {
    void onArmorTick(ItemStack stack, Level level, Player player);
}
