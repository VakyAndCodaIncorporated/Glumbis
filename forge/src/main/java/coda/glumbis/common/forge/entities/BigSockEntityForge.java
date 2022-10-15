package coda.glumbis.common.forge.entities;

import coda.glumbis.common.entities.BigSockEntity;
import coda.glumbis.common.registry.GlumbisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

/**
 * Applies some forge-specific functionality to the big sock.
 * This avoids using unnecessary mixins on the forge side.
 */
public class BigSockEntityForge extends BigSockEntity {
    public BigSockEntityForge(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
    }

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(GlumbisItems.BIG_SOCK.get());
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	@Override
	protected void addLandingParticles(BlockState blockState, BlockPos pos, int i) {
		if (!blockState.addLandingEffects((ServerLevel)this.level, pos, blockState, this, i)) {
			((ServerLevel)this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState).setPos(pos), this.getX(), this.getY(), this.getZ(), i, 0.0D, 0.0D, 0.0D, 0.15D);
		}
	}
}
