package coda.glumbis.common.entities;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class BigSockEntity extends Animal implements IAnimatable, IAnimationTickable {
	private final AnimationFactory factory = new AnimationFactory(this);

	public BigSockEntity(EntityType<? extends Animal> type, Level worldIn) {
		super(type, worldIn);
		this.noCulling = true;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (!this.isVehicle()) {
			player.startRiding(this);
			player.setYRot(getYRot());
			return super.mobInteract(player, hand);
		}
		return super.mobInteract(player, hand);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		//event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bike.idle", true));
		return PlayState.CONTINUE;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5F).add(Attributes.ATTACK_DAMAGE, 0.0F);
	}

	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		return false;
	}

	@Override
	public void tick() {
		super.tick();

		if (getSpeed() > 0.09 && isVehicle()) {
			LivingEntity passenger = (LivingEntity) this.getControllingPassenger();

		}
	}

	@Override
	public void travel(Vec3 pos) {
		if (this.isAlive()) {
			if (this.isVehicle()) {
				this.setSpeed(0.3F);
				LivingEntity passenger = (LivingEntity) this.getControllingPassenger();

				this.setYRot(passenger.getYHeadRot());

				super.travel(jump(pos));
			}
			else {
				super.travel(Vec3.ZERO);
			}
		}

	}

	private Vec3 jump(Vec3 pos) {
		LivingEntity passenger = (LivingEntity) this.getControllingPassenger();
		float f1 = passenger.zza;
		if (f1 <= 0.0F) {
			f1 *= 0.25F;
		}

		float distance = 3.5F;
		double x, z;

		if (Minecraft.getInstance().options.keyUp.isDown() && isOnGround()) {
			float yRot = passenger.getViewYRot(1.0F);

			x = -Mth.sin((float) (yRot * Math.PI/180F)) * distance;
			z = Mth.cos((float) (yRot * Math.PI/180F)) * distance;

			setDeltaMovement(x, 1, z);
		}
		else if (Minecraft.getInstance().options.keyDown.isDown() && isOnGround()) {
			float yRot = passenger.getViewYRot(1.0F);

			x = Mth.sin((float) (yRot * Math.PI/180F)) * (distance / 2);
			z = -Mth.cos((float) (yRot * Math.PI/180F)) * (distance / 2);

			setDeltaMovement(x, 0.5, z);
		}

		return new Vec3(0, pos.y, f1);
	}

	@Override
	public double getPassengersRidingOffset() {
		return 1.25;
	}

	@Nullable
	public Entity getControllingPassenger() {
		return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
	}

	@Override
	public boolean canBeControlledByRider() {
		return true;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
		return null;
	}

	@Override
	public int tickTimer() {
		return tickCount;
	}
}
