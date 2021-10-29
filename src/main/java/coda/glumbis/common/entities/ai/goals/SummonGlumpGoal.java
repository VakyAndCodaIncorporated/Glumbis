package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.init.GlumbisEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Vex;

public class SummonGlumpGoal extends Goal {
    private GlumbossEntity entity;
    
    public SummonGlumpGoal(GlumbossEntity entity){
        this.entity = entity;
    }
    
    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.entity.getHealth() == 150.0f || this.entity.getHealth() == 100.0f || this.entity.getHealth() == 50.0f) {
            this.entity.setSlamming(false);
            this.entity.setKicking(false);
            if (this.entity.getRandom().nextFloat() < 0.05f) {
                for (int i = 0; i < 3; ++i) {
                    BlockPos blockpos = this.entity.blockPosition().offset(-2 + this.entity.getRandom().nextInt(5), 1, -2 + this.entity.getRandom().nextInt(5));
                    GlumpEntity glump = GlumbisEntities.GLUMP.get().create(this.entity.level);
                    glump.moveTo(blockpos, 0.0f, 0.0f);
                    glump.finalizeSpawn((ServerLevel) this.entity.level,
                            this.entity.level.getCurrentDifficultyAt(blockpos),
                            MobSpawnType.MOB_SUMMONED,
                            null,
                            null);
                    this.entity.level.addFreshEntity(glump);
                    System.out.println("Summoning glump!");
                }
            }
        }
    }
}
