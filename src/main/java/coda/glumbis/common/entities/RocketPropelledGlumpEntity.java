package coda.glumbis.common.entities;

import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.common.registry.GlumbisParticles;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.CallbackI;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

public class RocketPropelledGlumpEntity extends AbstractHurtingProjectile implements IAnimatable, IAnimationTickable {
    private final AnimationFactory factory = new AnimationFactory(this);

    public RocketPropelledGlumpEntity(EntityType<? extends AbstractHurtingProjectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    public RocketPropelledGlumpEntity(Level p_36861_, double p_36862_, double p_36863_, double p_36864_) {
        this(GlumbisEntities.ROCKET_PROPELLED_GLUMP.get(), p_36861_);
        this.setPos(p_36862_, p_36863_, p_36864_);
    }

    public RocketPropelledGlumpEntity(LivingEntity p_36718_, Level p_36719_) {
        this(p_36719_, p_36718_.getX(), p_36718_.getEyeY() - (double)0.1F, p_36718_.getZ());
        this.setOwner(p_36718_);
        this.xPower = 0.5F;
        this.yPower = 0.5F;
        this.zPower = 0.5F;
    }

    @Override
    protected float getInertia() {
        return super.getInertia();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        explode((Player) getOwner());
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        Vec3 vec3 = p_36755_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.onGround = true;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        if (onGround && tickCount % 30 == 0 && getOwner() instanceof Player player) {
            explode(player);
        }

        if (!isOnGround() && getBlockStateOn().isAir()) {
            if (getTarget() != null) {
                // TODO - make it continue straight if it loses its target so it doesnt find a new target
                Vec3 entityToTarget = getTarget().position().subtract(position());
                Vec3 direction = entityToTarget.normalize();
                setDeltaMovement(direction);
            }
            else {
                if (getOwner() instanceof Player player) {
                    Vec3 vec3 = player.getViewVector(1.0F);

                    setDeltaMovement(vec3);
                }
            }
        }

        setDeltaMovement(getDeltaMovement().multiply(0.35, 0.35, 0.35));

        Vec3 direction = getDeltaMovement();
        double angle = Mth.atan2(direction.z, direction.x);
        setYRot((float) Math.toDegrees(angle));
        // setXRot((float) Mth.atan2(entityToTarget.y, entityToTarget.horizontalDistance()));

        super.tick();
    }

    private void explode(Player player) {
        level.playLocalSound(getX(), getY(), getZ(), GlumbisSounds.GLUMP_EXPLODE.get(), SoundSource.PLAYERS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);

        for(int i = 0; i < 20; i++) {
            this.level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), this.getRandomX(3.5D), (this.getPosition(1.0f).y() - 0.5) , this.getRandomZ(3.5D), 0, this.getRandomY() * 2, 0);
        }
        tryHurtEntity(player, 16);
        discard();
    }

    protected void tryHurtEntity(Player player, double distanceTo) {
        if (distanceTo < this.getAttackReachSqr(player) / 1.2) {
            List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(50));
            for (LivingEntity target : list) {
                double distanceToGlumboss = target.distanceToSqr(player);
                float damage = 1 - Mth.sqrt((float) distanceToGlumboss) / 10;
                target.hurt(DamageSource.playerAttack(player), (0.5F * damage + 0.5F) * 10);
                target.setDeltaMovement(target.getDeltaMovement().add(target.position().normalize().multiply(1.0, 1.4, 1.0)));
            }
        }
    }

    protected double getAttackReachSqr(LivingEntity entity) {
        return entity.getBbWidth() * 2.0F * entity.getBbWidth() * 2.0F + entity.getBbWidth();
    }

    protected ParticleOptions getTrailParticle() {
        return GlumbisParticles.STATIC_LIGHTNING.get();
    }

    public LivingEntity getTarget() {
        return level.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), null, getX(), getY(), getZ(), getBoundingBox().inflate(50));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 2, this::predicate));
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }
}
