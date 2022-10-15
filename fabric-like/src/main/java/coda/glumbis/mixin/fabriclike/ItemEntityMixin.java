package coda.glumbis.mixin.fabriclike;

import coda.glumbis.common.items.CatEssenceItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements the custom item entity behaviour for {@link CatEssenceItem}.
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getItem();

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo callbackInfo) {
        ItemEntity itemEntity = ((ItemEntity) (Object) this);
        ItemStack stack = itemEntity.getItem();
        if (stack.getItem() instanceof CatEssenceItem catEssenceItem) {
            catEssenceItem.onEntityItemUpdate(stack, itemEntity);
        }
    }
}
