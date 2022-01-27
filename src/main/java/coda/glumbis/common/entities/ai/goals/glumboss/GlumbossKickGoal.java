package coda.glumbis.common.entities.ai.goals.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class GlumbossKickGoal extends Goal {
    protected GlumbossEntity entity;
    private int timer;
    private final int COOLDOWN = 40;
    private int cooldownTimer;

    public GlumbossKickGoal(GlumbossEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.entity.distanceToSqr(this.entity.getTarget()) < 10.0f;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entity.getState() == 0 || this.entity.getState() == 1) {
            if (this.cooldownTimer < COOLDOWN) {
                this.cooldownTimer++;
            } else {
                if (this.timer < 20) {
                    this.timer++;
                    this.entity.getNavigation().stop();
                    this.entity.setState(1);
                    if (this.timer == 13) {
                        this.tryHurtTarget(this.entity, this.entity.distanceTo(this.entity.getTarget()));
                    }
                } else {
                    this.timer = 0;
                    this.cooldownTimer = 0;
                    this.entity.setState(0);
                }
            }
        }
        else{
            this.stop();
        }
    }

    protected void tryHurtTarget(GlumbossEntity entity, double distanceTo){
        if(distanceTo < this.getAttackReachSqr(entity)){
            this.entity.doHurtTarget(this.entity.getTarget());
        }
    }

    protected double getAttackReachSqr(LivingEntity entity) {
        return (double)(entity.getBbWidth() * 1.2F * entity.getBbWidth() * 1.2F + entity.getBbWidth());
    }
}
