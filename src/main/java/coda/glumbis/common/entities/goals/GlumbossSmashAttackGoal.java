package coda.glumbis.common.entities.goals;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class GlumbossSmashAttackGoal extends GlumbossAttackGoal {

    public GlumbossSmashAttackGoal(GlumbossEntity entity) {
        super(entity, GlumbossEntity.AttackType.SMASH);
    }

    @Override
    public void start() {
        super.start();

        LivingEntity target = entity.getTarget();

        entity.setJumping(true);

        double x = Mth.clamp(target.getX() - entity.getX(), -0.25, 0.25);
        double z = Mth.clamp(target.getZ() - entity.getZ(), -0.25, 0.25);

        entity.setDeltaMovement(entity.getDeltaMovement().add(x, 1.25, z));
    }

    // TODO that one thing ash said
    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void stop() {
        super.stop();

        for (LivingEntity livingEntity : entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3.5))) {

            if (livingEntity == entity) {
                continue;
            }

            double distanceToGlumboss = livingEntity.distanceToSqr(entity);

            if (distanceToGlumboss > 25) {
                continue;
            }

            float damage = 1 - Mth.sqrt((float) distanceToGlumboss) / 5;

            livingEntity.hurt(DamageSource.mobAttack(entity), (0.5F * damage + 0.5F) * 12);

            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(livingEntity.position().subtract(entity.position()).normalize().multiply(1.5, 1.1, 1.5)));
        }


        entity.setJumping(false);
    }

    @Override
    public boolean canContinueToUse() {
        return !entity.isOnGround();
    }
}
