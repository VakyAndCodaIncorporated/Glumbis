package coda.glumbis.common.entities.ai.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Predicate;

public class GlumbossSlamGoal extends Goal {
    public static final Predicate<Entity> NOT_CATS = (p_20436_) -> !(p_20436_ instanceof GlumpEntity) && !(p_20436_ instanceof GlumbossEntity) && !(p_20436_ instanceof Cat);
    protected GlumbossEntity entity;
    private int timer;
    private final int COOLDOWN = 100;
    private int cooldownTimer;

    public GlumbossSlamGoal(GlumbossEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return this.entity.getState() == 0 || this.entity.getState() == 2;
    }

    @Override
    public void tick() {
        if (canUse()) {
            if (this.cooldownTimer < COOLDOWN) {
                this.cooldownTimer++;
            } else {
                if (this.timer <= 35) {
                    this.timer++;
                    this.entity.setState(2);
                    this.entity.getNavigation().stop();
                    if (this.timer == 35) {
                        this.tryHurtNearbyEntities(this.entity);
                        this.entity.playSound(GlumbisSounds.GLUMBOSS_SLAM.get(), 1.0F, 1.0F);
                    }
                } else {
                    this.timer = 0;
                    this.cooldownTimer = 0;
                    this.entity.setState(0);
                }
            }
        } else {
            this.stop();
        }
    }

    @Override
    public void stop() {
        this.timer = 0;
        this.cooldownTimer = 0;
        this.entity.setState(0);
    }

    protected void tryHurtNearbyEntities(GlumbossEntity entity) {
        List<LivingEntity> targets = this.entity.level.getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(5.0), NOT_CATS);
        for (LivingEntity target : targets) {
            double distanceToGlumboss = target.distanceToSqr(entity);
            float damage = 1 - Mth.sqrt((float) distanceToGlumboss) / 10;

            target.hurt(DamageSource.mobAttack(entity), (0.5F * damage + 0.5F) * 10);
            target.setDeltaMovement(target.getDeltaMovement().add(target.position().normalize().multiply(1.0, 1.4, 1.0)));
        }
    }
}