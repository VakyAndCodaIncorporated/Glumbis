package coda.glumbis.mixin.fabriclike;

import coda.glumbis.common.items.util.IArmorTick;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements armor ticking. See {@link IArmorTick}
 */
@Mixin(Inventory.class)
public class InventoryMixin {
    @Final
    @Shadow
    private NonNullList<ItemStack> armor;

    @Final
    @Shadow
    public Player player;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo callbackInfo) {
        armor.forEach(stack -> {
            if (stack.getItem() instanceof IArmorTick armorTick) {
                armorTick.onArmorTick(stack, player.level, player);
            }
        });
    }
}
