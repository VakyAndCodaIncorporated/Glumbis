package coda.glumbis.common.entities;

import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.common.registry.GlumbisParticles;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

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
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        explode();
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

    // a bunch of math stolen from Minecraft
    private void explode() {
        // TODO - fix the explosion sound and add static explosion particles
        this.level.playLocalSound(getX(), getY(), getZ(), GlumbisSounds.GLUMP_EXPLODE.get(), SoundSource.PLAYERS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);

        float f2 = 4.0F;
        int k1 = Mth.floor(getX() - (double)f2 - 1.0D);
        int l1 = Mth.floor(getX() + (double)f2 + 1.0D);
        int i2 = Mth.floor(getY() - (double)f2 - 1.0D);
        int i1 = Mth.floor(getY() + (double)f2 + 1.0D);
        int j2 = Mth.floor(getZ() - (double)f2 - 1.0D);
        int j1 = Mth.floor(getZ() + (double)f2 + 1.0D);
        List<Entity> list = this.level.getEntities(getOwner(), new AABB(k1, i2, j2, l1, i1, j1));
        Vec3 vec3 = new Vec3(getX(), getY(), getZ());

        for(int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);
            if (!entity.ignoreExplosion()) {
                double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f2;
                if (d12 <= 1.0D) {
                    double d5 = entity.getX() - getX();
                    double d7 = entity.getEyeY() - getY();
                    double d9 = entity.getZ() - getZ();
                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = getSeenPercent(vec3, entity);
                        double d10 = (1.0D - d12) * d14;
                        entity.hurt(DamageSource.explosion((LivingEntity) getOwner()), (float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f2 + 1.0D)));
                        double d11 = d10;
                        if (entity instanceof LivingEntity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, d10);
                        }

                        entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                    }
                }
            }
        }
    }

    public static float getSeenPercent(Vec3 p_46065_, Entity p_46066_) {
        AABB aabb = p_46066_.getBoundingBox();
        double d0 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;

            for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
                for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
                    for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                        double d5 = Mth.lerp(f, aabb.minX, aabb.maxX);
                        double d6 = Mth.lerp(f1, aabb.minY, aabb.maxY);
                        double d7 = Mth.lerp(f2, aabb.minZ, aabb.maxZ);
                        Vec3 vec3 = new Vec3(d5 + d3, d6, d7 + d4);
                        if (p_46066_.level.clip(new ClipContext(vec3, p_46065_, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p_46066_)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (onGround && tickCount % 30 == 0) {
            explode();
            this.discard();
        }

        if (!level.isClientSide && !isOnGround() && getBlockStateOn().isAir()) {
            if (getTarget() != null) {
                // TODO - make it continue straight if it loses its target so it doesnt find a new target
                Vec3 vec3 = getTarget().position().subtract(position()).normalize().multiply(5, 5, 5);

                setDeltaMovement(vec3);

                Vec3 rotationVec = getTarget().position().subtract(position());
                double atan = Math.atan2(vec3.z, vec3.x) - (Math.PI / 2);
                double rotXZ = Math.toDegrees(atan);
                setXRot((float) rotXZ);
                setYRot((float) Mth.atan2(vec3.y, vec3.horizontalDistance()));
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
        return level.getNearestEntity(Chicken.class, TargetingConditions.forCombat(), null, getX(), getY(), getZ(), getBoundingBox().inflate(50));
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
