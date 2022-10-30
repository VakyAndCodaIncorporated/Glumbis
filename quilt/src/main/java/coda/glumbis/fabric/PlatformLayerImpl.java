package coda.glumbis.fabric;

import coda.glumbis.PlatformLayer;
import coda.glumbis.common.entities.BigSockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Unfortunately this has to be in the fabric package due to how Architectury works.
 * This *could* be made common across fabric and quilt, and be put into the fabriclike project however keeping this separate allows you to expand to more differences in future.
 */
public class PlatformLayerImpl {
    /**
     * Implementation of {@link PlatformLayer#getBigSockFactory()}.
     */
    public static EntityType.EntityFactory<BigSockEntity> getBigSockFactory() {
        return BigSockEntity::new;
    }

    /**
     * Implementation of {@link PlatformLayer#onArrowLoose(ItemStack, Level, Player, int, boolean)}.
     */
    public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        return charge;
    }

    /**
     * Implementation of {@link PlatformLayer#onArrowNock(ItemStack, Level, Player, InteractionHand, boolean)}.
     */
    public static InteractionResultHolder<ItemStack> onArrowNock(ItemStack item, Level level, Player player, InteractionHand hand, boolean hasAmmo) {
        return null;
    }
}
