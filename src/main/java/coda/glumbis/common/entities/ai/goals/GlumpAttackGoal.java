package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class GlumpAttackGoal extends Goal {
    private GlumpEntity entity;

    public GlumpAttackGoal(GlumpEntity entity){
        this.entity = entity;
    }
    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity livingEntity = this.entity.getTarget();
        if(this.entity.distanceToSqr(livingEntity) < 6.35f){
            this.entity.getNavigation().stop();
            this.entity.setExploding(true);
            doExplode(this.entity, livingEntity);
        }
    }

    public void doExplode(GlumpEntity entity, LivingEntity livingEntity){
        float damage = 1 - Mth.sqrt((float) livingEntity.distanceToSqr(entity)) / 10;
        entity.playSound(SoundEvents.GENERIC_EXPLODE, 0.4F, 1.0F);
        livingEntity.hurt(DamageSource.mobAttack(entity), (0.5F * damage + 0.5F) * 10);
        livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(livingEntity.position().subtract(entity.position()).normalize().multiply(1.5, 0.8, 1.5)));
        System.out.println("foo");
        this.entity.kill();
    }
}
