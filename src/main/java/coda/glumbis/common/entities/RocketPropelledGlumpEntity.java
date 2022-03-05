package coda.glumbis.common.entities;

import coda.glumbis.common.registry.GlumbisParticles;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.function.Predicate;

public class RocketPropelledGlumpEntity extends PathfinderMob implements IAnimatable, IAnimationTickable {
    public static final Predicate<LivingEntity> NO_CATS = (p_20436_) -> !(p_20436_ instanceof GlumpEntity) && !(p_20436_ instanceof GlumbossEntity) && !(p_20436_ instanceof Cat);
    private final AnimationFactory factory = new AnimationFactory(this);
    public Player owner;

    public RocketPropelledGlumpEntity(EntityType<? extends PathfinderMob> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
        this.lookControl = new SmoothSwimmingLookControl(this, 90);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5F).add(Attributes.ATTACK_DAMAGE, 0.0F);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource != DamageSource.OUT_OF_WORLD && this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            this.markHurt();
            Entity entity = pSource.getEntity();
            if (entity != null) {
                Vec3 vec3 = entity.getLookAngle();
                this.setDeltaMovement(vec3);
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean lookAt(RocketPropelledGlumpEntity rpgEntity, LivingEntity entity, double range) {
        Vec3 vec3 = rpgEntity.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(entity.getX() - rpgEntity.getX(), entity.getY() - rpgEntity.getEyeY(), entity.getZ() - rpgEntity.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - range / d0;
    }

    @Override
    public void tick() {
        super.tick();

        // get a new target
        if (getTarget() == null) {
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(50), NO_CATS)) {
                if (entity != null && !(entity instanceof RocketPropelledGlumpEntity) && entity != owner && lookAt(this, entity, 30F)) {
                    setTarget(entity);
                }
            }
        }

        // movement

        double d0 = this.getX() + getDeltaMovement().x;
        double d1 = this.getY() + getDeltaMovement().y;
        double d2 = this.getZ() + getDeltaMovement().z;
        /*
        Vec3 vec3 = this.getDeltaMovement();
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);
        float f = 0.95F;
        if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                this.level.addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        }

        this.setDeltaMovement(vec3.add(0.5F, 0.5F, 0.5F).scale(f));
        this.setPos(d0, d1, d2);
        */

        // particles
        this.level.addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);

        // explode when on ground
        if (getFeetBlockState().isRedstoneConductor(level, blockPosition()) && tickCount % 30 == 0) {

            for (LivingEntity entity : getNearbyEntities()) {
                //explode(entity);
            }
        }

        // movement
        if (!isOnGround() && getBlockStateOn().isAir()) {
            float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            setYRot(this.rotlerp(getYRot(), f, 90F));
            yBodyRot = getYRot();
            yHeadRot = getYRot();

            float f1 = (float)(getAttributeValue(Attributes.MOVEMENT_SPEED));
            double d4 = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(d1) > (double)1.0E-5F || Math.abs(d4) > (double)1.0E-5F) {
                float f2 = -((float)(Mth.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
                f2 = Mth.clamp(Mth.wrapDegrees(f2), -90F, 90F);
                setXRot(this.rotlerp(getXRot(), f2, 5.0F));
            }

            float f4 = Mth.cos(getXRot() * ((float)Math.PI / 180F));
            float f3 = Mth.sin(getXRot() * ((float)Math.PI / 180F));
            zza = f4 * f1;
            yya = -f3 * f1;

            if (getTarget() != null) {
                // TODO - make it continue straight if it loses its target so it doesnt find a new target
                Vec3 entityToTarget = getTarget().position().subtract(position());
                Vec3 direction = entityToTarget.normalize();
                setDeltaMovement(direction);
            }
            else {
                setDeltaMovement(getViewVector(1.0F));
            }
        }

        setDeltaMovement(getDeltaMovement().multiply(0.35, 0.35, 0.35));

        move(MoverType.SELF, getDeltaMovement());

        // explode after 20 seconds
        if (tickCount > 400) {
            for (LivingEntity entity : getNearbyEntities()) {
                explode(entity);
            }
        }

        // explode on contact with an entity
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.3D))) {
            if (entity != owner && entity.isAlive()) {
                //explode(entity);
            }
        }
    }

    private List<LivingEntity> getNearbyEntities() {
        return level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(10));
    }

    private void explode(LivingEntity entity) {
        // todo - figure out why the sound cant be heard when the player is more than a few blocks away & fix it
        playSound(GlumbisSounds.GLUMP_EXPLODE.get(),2.0F, 1.0F);

        // todo - fix particles not playing. maybe its because the projectile they are playing from is removed?
        for(int i = 0; i < 50; i++) {
            entity.level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), this.getRandomX(3.5D), this.getPosition(1.0f).y() + (random.nextDouble() * 2) , this.getRandomZ(3.5D), 0, this.getRandomY() * 2, 0);
            entity.level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), this.getRandomX(3.5D), this.getPosition(1.0f).y() + (random.nextDouble() * 2) , this.getRandomZ(3.5D), 0, this.getRandomY() * 2, 0);
        }
        tryHurtEntity(entity);
        discard();
    }

    protected void tryHurtEntity(LivingEntity entity) {
        double distanceTo = distanceToSqr(entity);
        float damage = 1 - Mth.sqrt((float) distanceTo) / 10;
        entity.hurt(DamageSource.explosion(entity), (0.5F * damage + 0.5F) * 5);

        entity.setDeltaMovement(entity.getDeltaMovement().add(entity.position().normalize().multiply(1.0, 1.0, 1.0)));
    }

    protected ParticleOptions getTrailParticle() {
        return GlumbisParticles.STATIC_LIGHTNING.get();
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
