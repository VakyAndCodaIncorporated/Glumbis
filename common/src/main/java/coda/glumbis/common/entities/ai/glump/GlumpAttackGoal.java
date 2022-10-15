package coda.glumbis.common.entities.ai.glump;

import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class GlumpAttackGoal extends Goal {
    protected final GlumpEntity entity;
    private int cooldownTimer;

    public GlumpAttackGoal(GlumpEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        this.cooldownTimer = 0;
    }

    @Override
    public void tick() {
        if (this.cooldownTimer < 20) {
            cooldownTimer++;
        } else {
            LivingEntity livingEntity = entity.getTarget();
            if (this.entity.distanceToSqr(livingEntity) < 1.0f) {
                this.entity.doHurtTarget(livingEntity);
                this.entity.setExploding(true);
                this.cooldownTimer = 0;
            }
        }
    }
}
