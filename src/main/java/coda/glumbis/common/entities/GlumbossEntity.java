package coda.glumbis.common.entities;

import coda.glumbis.common.entities.ai.glumboss.*;
import coda.glumbis.common.registry.GlumbisParticles;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
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
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(GlumbossEntity.class, EntityDataSerializers.INT);
    private int timer;
    public int slamTimer;
    private static final EntityDataAccessor<Boolean> HALFHEALTH = SynchedEntityData.defineId(GlumbossEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHARGED = SynchedEntityData.defineId(GlumbossEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimationFactory factory = new AnimationFactory(this);

    public GlumbossEntity(EntityType<? extends GlumbossEntity> entity, Level level) {
        super(entity, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.9F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));

        this.goalSelector.addGoal(3, new GlumbossKickGoal(this, 20, 35, 1,4, 4, true, 5f));
        this.goalSelector.addGoal(2, new GlumbossSlamGoal(this, 30, 40, 2,28, 28, true, 8f));
        this.goalSelector.addGoal(2, new GlumbossGlumpGoal(this, 70, 100, 3,18, 53, true, 8f));
        this.goalSelector.addGoal(2, new GlumbossLightningStrikeGoal(this, 70, 100, 5,56, 56, true, 14f));


        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 200.0D).add(Attributes.FOLLOW_RANGE, 25F).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 3.0F).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D).add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE, 0);
        this.entityData.define(HALFHEALTH, false);
        this.entityData.define(CHARGED, false);
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
    public void thunderHit(ServerLevel p_19927_, LightningBolt p_19928_) {
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getCharged()){
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27D);
            this.level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), this.getRandomX(1f), this.getY(), this.getRandomZ(1f), 0d, this.getRandom().nextFloat(), 0d);
        }
        if(getAnimState() == 2){
            if(slamTimer <= 30){
                slamTimer++;
                if(slamTimer == 28){
                    if(this.level.isClientSide()){
                        for(int i = 0; i < 100; i++){
                            this.level.addParticle(ParticleTypes.POOF, this.getRandomX(3f), this.getY(), this.getRandomZ(3f), 0d, this.getRandom().nextFloat()/2, 0d);
                        }
                    }
                }
            }
            else{
                slamTimer = 0;
            }
        }
        else{
            slamTimer = 0;
        }

        if(this.getHealth() < this.getMaxHealth()/2 && !this.getHalfHealth() && this.getAnimState() == 0){
            this.setHalfHealth(true);
            this.setAnimState(4);
        }
        if(this.getAnimState() == 4){
            if(timer <= 150){
                timer++;
                this.getNavigation().stop();
                this.setDeltaMovement(this.getDeltaMovement().multiply(0d, 1d, 0d));
                if(timer == 20){
                    this.playSound(GlumbisSounds.GLUMBOSS_CHARGE.get(), 1f, 1f);
                }
                if(timer == 130){
                    BlockPos blockpos = this.blockPosition();
                    setCharged(true);
                    if (this.level.canSeeSky(blockpos.above())) {
                        for(int i = 0; i < 10; i++) {
                            LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level);
                            lightningbolt.moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                            this.level.addFreshEntity(lightningbolt);
                        }
                    }
                }
                if(this.level.isClientSide()) {
                    for(int i = 0; i < Math.round(timer/4); i++){
                        this.level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), this.getRandomX(1f), this.getY(), this.getRandomZ(1f), 0d, this.getRandom().nextFloat(), 0d);
                    }
                }
            }
            else{
                this.setAnimState(0);
            }
        }
        else{
            this.timer = 0;
        }
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(getCharged() && getAnimState() == 0){
            event.getController().setAnimationSpeed(2);
        }
        else{
            event.getController().setAnimationSpeed(1);
        }
        switch (getAnimState()) {
            case 0:
                if(event.isMoving()){
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.walk", true));
                    return PlayState.CONTINUE;
                }
                if(!event.isMoving()){
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.idle", true));
                    return PlayState.CONTINUE;
                }
                return PlayState.CONTINUE;
            case 1:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.kick", false));
                return PlayState.CONTINUE;
            case 2:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.slam", false));
                return PlayState.CONTINUE;
            case 3:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.summon_glumps", false));
                return PlayState.CONTINUE;
            case 4:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.transform", false));
                return PlayState.CONTINUE;
            case 5:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glumboss.static_charge", false));
                return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 2, this::predicate));
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
        return GlumbisSounds.GLUMBOSS_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    public void setAnimState(int animState){
        this.entityData.set(ANIM_STATE, animState);
    }

    public int getAnimState(){
        return this.entityData.get(ANIM_STATE);
    }

    public void setHalfHealth(boolean halfHealth){
        this.entityData.set(HALFHEALTH, halfHealth);
    }

    public boolean getHalfHealth(){
        return this.entityData.get(HALFHEALTH);
    }

    public void setCharged(boolean charged){
        this.entityData.set(CHARGED, charged);
    }

    public boolean getCharged(){
        return this.entityData.get(CHARGED);
    }
}