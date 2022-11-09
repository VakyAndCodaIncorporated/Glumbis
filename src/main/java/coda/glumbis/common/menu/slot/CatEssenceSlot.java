package coda.glumbis.common.menu.slot;

import coda.glumbis.common.registry.GlumbisItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CatEssenceSlot extends Slot {

    public CatEssenceSlot(Container p_40223_, int p_40224_, int p_40225_, int p_40226_) {
        super(p_40223_, p_40224_, p_40225_, p_40226_);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(GlumbisItems.CAT_ESSENCE.get());
    }
}
