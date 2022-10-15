package coda.glumbis;

import coda.glumbis.common.entities.BigSockEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.nio.file.Path;

/**
 * This is a platform specific layer of the mod.
 * Anything with the ExpectPlatform annotation will be loaded from the fabric/quilt/forge implementations.
 * Note in Quilt as of current, the implementations need to be placed into the fabric package, so it's a little messy.
 */
public class PlatformLayer {
    /**
     * Get the big sock entity factory.
     * We do this because Forge has some nice ways of manipulating entity behaviour that we use for the sock, but we needed to use mixins on fabric targets.
     * These overrides could've just been added without the @Override annotation in BigSockEntity but I feel this is more suitable for readability.
     */
    @ExpectPlatform
    public static EntityType.EntityFactory<BigSockEntity> getBigSockFactory() {
        throw new AssertionError();
    }

    /**
     * Fire's the onArrowLoose event on Forge, does nothing on Fabric or Quilt.
     * This is here in case behaviours like these become available on these other targets in the future.
     */
    @ExpectPlatform
    public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        throw new AssertionError();
    }

    /**
     * The same deal here but for the onArrowNock event.
     */
    @ExpectPlatform
    public static InteractionResultHolder<ItemStack> onArrowNock(ItemStack item, Level level, Player player, InteractionHand hand, boolean hasAmmo) {
        throw new AssertionError();
    }
}
