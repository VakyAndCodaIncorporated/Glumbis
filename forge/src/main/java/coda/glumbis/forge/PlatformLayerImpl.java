package coda.glumbis.forge;

import coda.glumbis.PlatformLayer;
import coda.glumbis.common.entities.BigSockEntity;
import coda.glumbis.common.forge.entities.BigSockEntityForge;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PlatformLayerImpl {
    /**
     * Implementation of {@link PlatformLayer#getBigSockFactory()}.
     */
    public static EntityType.EntityFactory<BigSockEntity> getBigSockFactory() {
        return BigSockEntityForge::new;
    }

    /**
     * Implementation of {@link PlatformLayer#onArrowLoose(ItemStack, Level, Player, int, boolean)}.
     */
    public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        return ForgeEventFactory.onArrowLoose(stack, level, player, charge, hasAmmo);
    }

    /**
     * Implementation of {@link PlatformLayer#onArrowNock(ItemStack, Level, Player, InteractionHand, boolean)}.
     */
    public static InteractionResultHolder<ItemStack> onArrowNock(ItemStack item, Level level, Player player, InteractionHand hand, boolean hasAmmo) {
        return ForgeEventFactory.onArrowNock(item, level, player, hand, hasAmmo);
    }
}
