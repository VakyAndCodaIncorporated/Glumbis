package coda.glumbis.common.entities;

import coda.glumbis.common.entities.goals.GlumbossSlamAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GlumbossEntity extends PathfinderMob implements IAnimatable {
    private static final EntityDataAccessor<Boolean> SLAMMING = SynchedEntityData.defineId(GlumbossEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);
    public AttackType attackType;

    public GlumbossEntity(EntityType<? extends GlumbossEntity> p_i48567_1_, Level p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GlumbossSlamAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 50.0D).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 4.0F).add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    public void setSlamming(boolean isSlamming) {
        this.entityData.set(SLAMMING, isSlamming);
    }

    public boolean getSlamming() {
        return this.entityData.get(SLAMMING);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLAMMING, false);
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
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return !p_21016_.isProjectile();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getSlamming()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.slam", true));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.walk", true));
            return PlayState.CONTINUE;
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.idle", true));
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    public enum AttackType {
        SLAM
    }
}
