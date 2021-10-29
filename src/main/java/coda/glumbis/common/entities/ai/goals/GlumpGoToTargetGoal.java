package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

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
    }

}
