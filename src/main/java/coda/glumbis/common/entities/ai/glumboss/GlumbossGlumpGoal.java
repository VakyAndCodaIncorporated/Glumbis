package coda.glumbis.common.entities.ai.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.registry.GlumbisEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class GlumbossGlumpGoal extends BaseGlumbossAttackGoal {
    public GlumbossEntity glumboss;
    public int timer;
    public final int timerEnd;
    public int coolDown;
    public int coolDownEnd;
    public final int frameStart;
    public final int frameEnd;
    public boolean shouldStopMoving;
    public float range;
    public boolean isInRange;
    public int glumpTimer;

    public GlumbossGlumpGoal(GlumbossEntity glumboss, int timerEnd, int coolDownEnd, int animation, int frameStart, int frameEnd, boolean shouldStopMoving, float range) {
        super(glumboss, timerEnd, coolDownEnd, animation, frameStart, frameEnd, shouldStopMoving, range);
        this.glumboss = glumboss;
        //how long the animation is in ticks
        this.timerEnd = timerEnd;
        //the minimum cooldown the attack must have to be able to happen again
        this.coolDownEnd = coolDownEnd;
        //the frame where you start doing special things, eg: dealing damage
        this.frameStart = frameStart;
        //the frame where you end doing special things
        this.frameEnd = frameEnd;
        //stops the entity from moving when plays its animation
        this.shouldStopMoving = shouldStopMoving;
    }

    @Override
    public boolean canUse() {
        if(this.glumboss.getTarget() != null){
            if(this.glumboss.distanceTo(this.glumboss.getTarget()) > 5f){
                return super.canUse();
            }
            return false;
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        if(this.glumboss.getCharged()){
            this.coolDownEnd = coolDown/2;
        }

        super.tick();
    }

    public void attack() {
        //6,19,31
        if(glumpTimer < 35){
            glumpTimer++;
        }
        else{
            glumpTimer = 0;
        }
        if(glumpTimer == 6 || glumpTimer == 19 || glumpTimer == 31){
            LivingEntity target = this.glumboss.getTarget();
            Vec3 distanceBetween = this.glumboss.getPosition(1f).vectorTo(target.getPosition(1f));
            BlockPos blockpos = this.glumboss.blockPosition().offset(0d, 2.8d, 0d);
            GlumpEntity glump = GlumbisEntities.GLUMP.get().create(this.glumboss.level);
            glump.moveTo(blockpos, 0.0F, 0.0F);
            glump.setNoGravity(false);
            glump.setDeltaMovement(distanceBetween.x()/12 + (this.glumboss.getRandom().nextFloat()/4), 1.32f,distanceBetween.z()/12 + (this.glumboss.getRandom().nextFloat()/4));
            glump.finalizeSpawn((ServerLevelAccessor) this.glumboss.level, this.glumboss.level.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, (SpawnGroupData)null, (CompoundTag)null);
            this.glumboss.level.addFreshEntity(glump);
        }

    }
}


