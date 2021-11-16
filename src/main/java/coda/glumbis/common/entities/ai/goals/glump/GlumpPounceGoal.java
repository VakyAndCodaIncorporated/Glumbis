package coda.glumbis.common.entities.ai.goals.glump;

import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class GlumpPounceGoal extends Goal {
    protected final GlumpEntity entity;

    public GlumpPounceGoal(GlumpEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() != null;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = entity.getTarget();
        this.entity.getLookControl().setLookAt(livingEntity, 30, 30);
        this.entity.getNavigation().moveTo(this.entity.getNavigation().createPath(livingEntity, 1), 0.85d);
        if (this.entity.tickCount % 30 == 0 && this.entity.distanceToSqr(livingEntity) > 2) {
            Vec3 vec3 = this.entity.getViewVector(1.0F);
            this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(vec3.x() * 1.5, 0.45, vec3.z() * 1.5));
        }
    }
}
