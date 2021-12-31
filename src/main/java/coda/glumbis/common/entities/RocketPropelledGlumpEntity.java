package coda.glumbis.common.entities;

import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.common.registry.GlumbisParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

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
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1, Explosion.BlockInteraction.NONE);
        this.discard();
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
    public void tick() {
        super.tick();
        if (onGround && tickCount % 30 == 0) {
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1, Explosion.BlockInteraction.NONE);
            this.discard();
        }

        if (!level.isClientSide && !isOnGround() && getBlockStateOn().isAir()) {
            if (getTarget() != null) {
                // TODO - make it continue straight if it loses its target so it doesnt find a new target
                Vec3 vec3 = getTarget().position().subtract(position()).normalize().multiply(5, 5, 5);

                setDeltaMovement(vec3);

                Vec3 rotationVec = getTarget().position().subtract(position());
                double atan = Math.atan2(rotationVec.z, rotationVec.x) - (Math.PI / 2);
                double rotXZ = Math.toDegrees(atan);
                setXRot((float) rotXZ);
                setYRot((float) -rotationVec.y);
            }
            else {
                // TODO - make it fire straight if theres no nearby targets
            }

        }

        setDeltaMovement(getDeltaMovement().multiply(0.1, 0.1, 0.1));
    }

    protected ParticleOptions getTrailParticle() {
        return GlumbisParticles.STATIC_LIGHTNING.get();
    }

    public LivingEntity getTarget() {
        return level.getNearestEntity(Chicken.class, TargetingConditions.forCombat(), null, 1.0, 1.0, 1.0, getBoundingBox().inflate(50));
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
