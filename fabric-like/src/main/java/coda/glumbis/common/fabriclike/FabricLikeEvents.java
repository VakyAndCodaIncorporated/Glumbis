package coda.glumbis.common.fabriclike;

import coda.glumbis.common.entities.BigSockEntity;
import coda.glumbis.common.registry.GlumbisItems;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockGatherCallback;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public class FabricLikeEvents {
    public static void init() {
        // Hook fabric pick block event.
        ClientPickBlockGatherCallback.EVENT.register((player, hitResult) -> {
            if (hitResult instanceof EntityHitResult entityHitResult) {
                if (entityHitResult.getEntity() instanceof BigSockEntity) {
                    return new ItemStack(GlumbisItems.BIG_SOCK.get());
                }
            }
            return ItemStack.EMPTY;
        });
    }
}
