package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class GlumbossAttackGoal extends Goal {
    protected final GlumbossEntity entity;
    protected final GlumbossEntity.AttackType attackType;

    protected GlumbossAttackGoal(GlumbossEntity entity, GlumbossEntity.AttackType attackType) {
        this.entity = entity;
        this.attackType = attackType;
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() != null && entity.attackType == null;
    }

    @Override
    public void start() {
        entity.attackType = attackType;
    }

    @Override
    public void stop() {
        entity.attackType = null;
    }
}
