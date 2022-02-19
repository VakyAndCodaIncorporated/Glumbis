package coda.glumbis.common.entities.ai.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.world.entity.ai.goal.Goal;

public class GlumbossHideGoal extends Goal {
    protected GlumbossEntity entity;
    private int timer;

    public GlumbossHideGoal(GlumbossEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return this.entity.getState() == 0 && this.entity.isHidden() && !entity.wasHidden;
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.isHidden();
    }

    @Override
    public void tick() {
        if (this.timer <= 200) {
            if (entity.tickCount % 2 == 0) {
                this.entity.heal(1);
            }
            this.entity.setState(0);
            this.timer++;
            this.entity.getNavigation().stop();
            this.entity.setInvulnerable(true);
            if (this.timer == 75 || this.timer == 150) {
                this.entity.playSound(GlumbisSounds.GLUMBOSS_AMBIENT.get(), 1.0F, 1.0F);
            }
            System.out.println(timer);
        }
        else {
            stop();
        }
    }

    @Override
    public void stop() {
        this.timer = 0;
        this.entity.setInvulnerable(false);
        this.entity.setHidden(false);
        this.entity.wasHidden = true;
    }
}
