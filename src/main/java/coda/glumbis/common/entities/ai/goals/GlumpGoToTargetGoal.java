package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GlumpGoToTargetGoal extends Goal {
    protected final GlumpEntity entity;

    public GlumpGoToTargetGoal(GlumpEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return entity.getNavigation().isDone() && entity.getRandom().nextInt(10) == 0 && entity.getTarget() != null;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();

        if (target != null) {
            entity.lookAt(target, 1.0F, 1.0F);
            entity.moveTo(target.position());
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
