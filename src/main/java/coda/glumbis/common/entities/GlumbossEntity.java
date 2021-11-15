package coda.glumbis.common.entities;

import coda.glumbis.common.entities.ai.goals.glumboss.*;
import coda.glumbis.common.init.GlumbisParticles;
import coda.glumbis.common.init.GlumbisSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class GlumbossEntity extends PathfinderMob implements IAnimatable, IAnimationTickable {
    private final ServerBossEvent bossEvent = (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS));
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(GlumbossEntity.class, EntityDataSerializers.INT);

    public GlumbossEntity(EntityType<? extends GlumbossEntity> p_i48567_1_, Level p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GlumbossStaticChargeGoal(this));
        this.goalSelector.addGoal(2, new GlumbossSlamGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.9F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(3, new GlumbossKickGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new GlumbossGoToTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 12.0F).add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    protected void customServerAiStep() {
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        super.customServerAiStep();
    }

    public void startSeenByPlayer(ServerPlayer p_31483_) {
        super.startSeenByPlayer(p_31483_);
        this.bossEvent.addPlayer(p_31483_);
    }

    public void stopSeenByPlayer(ServerPlayer p_31488_) {
        super.stopSeenByPlayer(p_31488_);
        this.bossEvent.removePlayer(p_31488_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_STATE, 0);
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
    public boolean hurt(DamageSource source, float amount) {
        return !source.isProjectile() && super.hurt(source, amount);
    }

    @Override
    public void thunderHit(ServerLevel p_19927_, LightningBolt p_19928_) {
    }

    @Override
    public void tick() {
        super.tick();
        this.clearFire();
        if(this.getState() == 3){
            for(int i = 0; i < 3; i++) {
                this.level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), this.getRandomX(1.5D), this.getRandomY() + 0.85D, this.getRandomZ(1.5D), 0, 0.08d, 0);
            }
        }
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(getState() == 3){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.static_charge", false));
            return PlayState.CONTINUE;
        }
        if(getState() == 2){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.slam", false));
            return PlayState.CONTINUE;
        }
        if(getState() == 1){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.kick", false));
            return PlayState.CONTINUE;
        }
        if(event.isMoving()){
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
        data.addAnimationController(new AnimationController<>(this, "controller", 2, this::predicate));
    }

    public int getState(){
        return this.entityData.get(ATTACK_STATE);
    }

    public void setState(int state){
        this.entityData.set(ATTACK_STATE, state);
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

}
