package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class GlumpGoToTargetGoal extends Goal {
    protected final GlumpEntity entity;

    public GlumpGoToTargetGoal(GlumpEntity entity) {
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
        this.entity.getNavigation().moveTo(this.entity.getNavigation().createPath(livingEntity, 1), 1.65d);
        if (this.entity.tickCount % 50 == 0 && this.entity.distanceToSqr(livingEntity) > 8) {
            Vec3 vec3 = this.entity.getViewVector(1.0F);
            this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(vec3.x(), 0, vec3.z()));
        }
    }

}
