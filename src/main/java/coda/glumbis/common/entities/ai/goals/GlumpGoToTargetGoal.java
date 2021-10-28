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
    private static final TargetingConditions SOCIALIZE_TARGETING = TargetingConditions.forCombat().range(32.0D).ignoreLineOfSight();
    protected final GlumpEntity entity;

    public GlumpGoToTargetGoal(GlumpEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() != null;
    }

    @Override
    public void tick() {
        for (LivingEntity livingEntity : entity.level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(10.0D, 10.0D, 10.0D))) {
            entity.moveTo(livingEntity.getX(), livingEntity.getY() + 1.0D, livingEntity.getZ());
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
