package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class GlumbossGoToTargetGoal extends Goal {
    protected final GlumbossEntity entity;

    public GlumbossGoToTargetGoal(GlumbossEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return !this.entity.getKicking() && !this.entity.getSlamming() && this.entity.getTarget() != null;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = entity.getTarget();
        if (this.entity.tickCount % 20 == 0) {
            this.entity.getLookControl().setLookAt(livingEntity, 30, 30);
            this.entity.getNavigation().moveTo(this.entity.getNavigation().createPath(livingEntity, 1), 1.65d);
        }
    }

}
