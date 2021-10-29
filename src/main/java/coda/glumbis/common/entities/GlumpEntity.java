package coda.glumbis.common.entities;

import coda.glumbis.common.entities.ai.control.SmoothFlyingMoveControl;
import coda.glumbis.common.entities.ai.goals.GlumpAttackGoal;
import coda.glumbis.common.entities.ai.goals.GlumpGoToTargetGoal;
import coda.glumbis.common.init.GlumbisSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
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

public class GlumpEntity extends Monster implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Boolean> EXPLODING = SynchedEntityData.defineId(GlumpEntity.class, EntityDataSerializers.BOOLEAN);

    public GlumpEntity(EntityType<? extends GlumpEntity> p_i48567_1_, Level p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
        this.setNoGravity(true);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EXPLODING, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new GlumpGoToTargetGoal(this));
        this.goalSelector.addGoal(1, new GlumpAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.265F).add(Attributes.FLYING_SPEED, 0.35F).add(Attributes.ATTACK_DAMAGE, 2.0F).add(Attributes.FOLLOW_RANGE, 32.0D);
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
        boolean soundTick = tickCount % 120 == 10;
        if(tickCount < 10){
            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 0, 0.08d, 0);

        }
        if (level.isRainingAt(blockPosition())) {
            if (damageTick) {
                hurt(DamageSource.DROWN, 1.0F);
            }
        }
        if(this.level.isClientSide()){
            if(this.getRandom().nextFloat() < 0.12f){
                this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 0, 0.08d, 0);
            }
        }
        if(getExploding()){
            if(this.level.isClientSide()) {
                this.level.addParticle(ParticleTypes.EXPLOSION, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 0, 0.08d, 0);
            }
        }
        if (soundTick) {
            playSound(GlumbisSounds.GLUMP_FLY.get(), 0.4F, 1.0F);
        }
        if (tickCount % 50 == 0) {
            Vec3 vec3 = getViewVector(1.0F);
            setDeltaMovement(getDeltaMovement().add(vec3.x(), 0, vec3.z()));
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

    public void setExploding(boolean exploding){
        this.entityData.set(EXPLODING, exploding);
    }

    public boolean getExploding(){
        return this.entityData.get(EXPLODING);
    }
}
