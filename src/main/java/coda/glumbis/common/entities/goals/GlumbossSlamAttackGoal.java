package coda.glumbis.common.entities.goals;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class GlumbossSlamAttackGoal extends GlumbossAttackGoal {
    private int timer;
    private final int cooldown = 120;
    private int cooldownTimer;

    public GlumbossSlamAttackGoal(GlumbossEntity entity) {
        super(entity, GlumbossEntity.AttackType.SLAM);
    }

    @Override
    public void start() {
        super.start();

//        LivingEntity target = entity.getTarget();
//        double x = Mth.clamp(target.getX() - entity.getX(), -0.25, 0.25);
//        double z = Mth.clamp(target.getZ() - entity.getZ(), -0.25, 0.25);
//        entity.setDeltaMovement(entity.getDeltaMovement().add(x, 1.25, z));

        this.timer = 0;
    }

    @Override
    public boolean canUse() {
        return attackType == GlumbossEntity.AttackType.SLAM && entity.getTarget() != null && entity.distanceToSqr(entity.getTarget()) <= 100;
    }

    @Override
    public void tick() {
        super.tick();

        if (cooldownTimer < cooldown) {
            cooldownTimer++;
        }
        else {
            LivingEntity target = entity.getTarget();
            if (target != null && this.timer <= 45) {
                this.timer++;
                this.entity.setSlamming(true);

                //todo play a 'woosh' sound here
                if (this.timer == 43) {
                    entity.playSound(SoundEvents.GENERIC_EXPLODE, 0.4F, 1.0F);

                    for (LivingEntity livingEntity : entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(10))) {

                        if (livingEntity == entity) {
                            continue;
                        }

                        double distanceToGlumboss = livingEntity.distanceToSqr(entity);

                        if (distanceToGlumboss > 16) {
                            continue;
                        }

                        float damage = 1 - Mth.sqrt((float) distanceToGlumboss) / 10;

                        livingEntity.hurt(DamageSource.mobAttack(entity), (0.5F * damage + 0.5F) * 10);
                        livingEntity.setDeltaMovement(livingEntity.getDeltaMovement()
                                .add(livingEntity.position().subtract(entity.position()).normalize().multiply(1.5, 0.8, 1.5)));
                    }
                }
            }
            else {
                this.entity.setSlamming(false);
                this.timer = 0;
                this.cooldownTimer = 0;
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
