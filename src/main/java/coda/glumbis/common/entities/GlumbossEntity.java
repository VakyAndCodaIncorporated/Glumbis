package coda.glumbis.common.entities;

import coda.glumbis.common.entities.ai.goals.GlumbossKickAttackGoal;
import coda.glumbis.common.entities.ai.goals.GlumbossSlamAttackGoal;
import coda.glumbis.common.entities.ai.goals.SummonGlumpGoal;
import coda.glumbis.common.init.GlumbisSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
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

import javax.annotation.Nullable;

public class GlumbossEntity extends PathfinderMob implements IAnimatable {
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS));
    private static final EntityDataAccessor<Boolean> SLAMMING = SynchedEntityData.defineId(GlumbossEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> KICKING = SynchedEntityData.defineId(GlumbossEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);
    public AttackType attackType;

    public GlumbossEntity(EntityType<? extends GlumbossEntity> p_i48567_1_, Level p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new GlumbossSlamAttackGoal(this));
        this.goalSelector.addGoal(6, new GlumbossKickAttackGoal(this));
        this.goalSelector.addGoal(3, new SummonGlumpGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.9F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new MoveTowardsTargetGoal(this, 1.0D, 32));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 6.0F).add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    public void setSlamming(boolean isSlamming) {
        this.entityData.set(SLAMMING, isSlamming);
    }

    public boolean getSlamming() {
        return this.entityData.get(SLAMMING);
    }

    public void setKicking(boolean isKicking) {
        this.entityData.set(KICKING, isKicking);
    }

    public boolean getKicking() {
        return this.entityData.get(KICKING);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLAMMING, false);
        this.entityData.define(KICKING, false);
    }

    public void readAdditionalSaveData(CompoundTag p_31474_) {
        super.readAdditionalSaveData(p_31474_);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable Component p_31476_) {
        super.setCustomName(p_31476_);
        this.bossEvent.setName(this.getDisplayName());
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
    public boolean canBeLeashed(Player p_21418_) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return !p_21016_.isProjectile() && super.hurt(p_21016_, p_21017_);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getKicking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.kick", true));
            return PlayState.CONTINUE;
        }
        else if (getSlamming()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.slam", true));
            return PlayState.CONTINUE;
        }
        else if (event.isMoving()) {
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
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return GlumbisSounds.GLUMBOSS_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return GlumbisSounds.GLUMBOSS_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return GlumbisSounds.GLUMBOSS_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    public enum AttackType {
        SLAM,
        KICK
    }
}
