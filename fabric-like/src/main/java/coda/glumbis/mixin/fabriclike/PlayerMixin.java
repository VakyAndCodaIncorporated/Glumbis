package coda.glumbis.mixin.fabriclike;

import coda.glumbis.common.CommonEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Triggers the on player wake event.
 */
@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "stopSleepInBed", at = @At("HEAD"))
    private void onStopSleepInBed(CallbackInfo callbackInfo) {
        CommonEvents.onPlayerWake((Player) (Object) this);
    }
}
