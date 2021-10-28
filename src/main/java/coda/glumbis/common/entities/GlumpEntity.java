package coda.glumbis.common.entities;

import coda.glumbis.common.entities.ai.control.SmoothFlyingMoveControl;
import coda.glumbis.common.init.GlumbisSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class GlumpEntity extends PathfinderMob implements IAnimatable, FlyingAnimal {
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(GlumpEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);

    public GlumpEntity(EntityType<? extends GlumpEntity> p_i48567_1_, Level p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
        this.moveControl = new SmoothFlyingMoveControl(this, 90, true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new GlumpChargeAttackGoal());
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.265F).add(Attributes.FLYING_SPEED, 1.0F).add(Attributes.ATTACK_DAMAGE, 2.0F).add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public boolean canBeLeashed(Player p_21418_) {
        return false;
    }

    public void setCharging(boolean isCharging) {
        this.entityData.set(CHARGING, isCharging);
    }

    public boolean getCharging() {
        return this.entityData.get(CHARGING);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHARGING, false);
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void tick() {
        super.tick();
        boolean damageTick = tickCount % 20 == 10;
        boolean soundTick = tickCount % 50 == 10;

        if (level.isRainingAt(blockPosition())) {
            if (damageTick) {
                hurt(DamageSource.DROWN, 1.0F);
            }
        }

        if (soundTick) {
            playSound(GlumbisSounds.GLUMP_FLY.get(), 0.4F, 1.0F);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return GlumbisSounds.GLUMP_HURT.get();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glump.walk", true));
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glump.idle", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    public boolean isFlying() {
        return false;
    }

    class GlumpChargeAttackGoal extends Goal {
        public GlumpChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (GlumpEntity.this.getTarget() != null && !GlumpEntity.this.getMoveControl().hasWanted() && GlumpEntity.this.random.nextInt(7) == 0) {
                return GlumpEntity.this.distanceToSqr(GlumpEntity.this.getTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return GlumpEntity.this.getMoveControl().hasWanted() && GlumpEntity.this.getCharging() && GlumpEntity.this.getTarget() != null && GlumpEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = GlumpEntity.this.getTarget();
            Vec3 vec3 = livingentity.getEyePosition();
            GlumpEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
            GlumpEntity.this.setCharging(true);
            GlumpEntity.this.playSound(GlumbisSounds.GLUMP_FLY.get(), 1.0F, 1.0F);
        }

        public void stop() {
            GlumpEntity.this.setCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = GlumpEntity.this.getTarget();
            if (GlumpEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                GlumpEntity.this.doHurtTarget(livingentity);
                GlumpEntity.this.setCharging(false);
            } else {
                double d0 = GlumpEntity.this.distanceToSqr(livingentity);
                if (d0 < 9.0D) {
                    Vec3 vec3 = livingentity.getEyePosition();
                    GlumpEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                }
            }

        }
    }
}
