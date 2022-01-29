package coda.glumbis.common.entities.ai.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class GlumbossSlamGoal extends Goal {
    protected GlumbossEntity entity;
    private int timer;
    private final int COOLDOWN = 100;
    private int cooldownTimer;

    public GlumbossSlamGoal(GlumbossEntity entity) {
        this.entity = entity;
    }

    // TODO - make glumboss follow through with the slam goal even if the target is too far away, as nearby entities should still be affected
    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.entity.distanceToSqr(this.entity.getTarget()) < 49.0f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity.getState() == 0 || this.entity.getState() == 2 && canUse()) {
            if (this.cooldownTimer < COOLDOWN) {
                this.cooldownTimer++;
            } else {
                if (this.timer <= 60) {
                    this.timer++;
                    this.entity.setState(2);
                    this.entity.getNavigation().stop();
                    if (this.timer == 35) {
                        LivingEntity target = this.entity.getTarget();
                        this.tryHurtNearbyEntities(this.entity, this.entity.distanceTo(target));
                        this.entity.playSound(GlumbisSounds.GLUMBOSS_SLAM.get(), 1.0F, 1.0F);
                    }
                } else {
                    this.timer = 0;
                    this.cooldownTimer = 0;
                    this.entity.setState(0);
                }
            }
        }
        else {
            this.stop();
        }
    }

    @Override
    public void stop() {
        this.timer = 0;
        this.cooldownTimer = 0;
        this.entity.setState(0);
    }

    protected void tryHurtNearbyEntities(GlumbossEntity entity, double distanceTo){
        if (distanceTo < this.getAttackReachSqr(entity) /*/ 1.2*/){
            List<LivingEntity> targets = this.entity.level.getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(5.0));
            for (LivingEntity target : targets) {
                double distanceToGlumboss = target.distanceToSqr(entity);
                float damage = 1 - Mth.sqrt((float) distanceToGlumboss) / 10;

                target.hurt(DamageSource.mobAttack(entity), (0.5F * damage + 0.5F) * 10);
                target.setDeltaMovement(target.getDeltaMovement().add(target.position().normalize().multiply(1.0, 1.4, 1.0)));
            }
        }
    }

    protected double getAttackReachSqr(LivingEntity entity) {
        return entity.getBbWidth() * 2.0F * entity.getBbWidth() * 2.0F + entity.getBbWidth();
    }
}
