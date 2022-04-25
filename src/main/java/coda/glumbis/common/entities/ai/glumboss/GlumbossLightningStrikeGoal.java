package coda.glumbis.common.entities.ai.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;


public class GlumbossLightningStrikeGoal extends BaseGlumbossAttackGoal {
    public GlumbossEntity glumboss;
    public int timer;
    public final int timerEnd;
    public int coolDown;
    public final int coolDownEnd;
    public final int frameStart;
    public final int frameEnd;
    public boolean shouldStopMoving;
    public float range;
    public boolean isInRange;
    public Vec3 lightningPos;

    public GlumbossLightningStrikeGoal(GlumbossEntity glumboss, int timerEnd, int coolDownEnd, int animation, int frameStart, int frameEnd, boolean shouldStopMoving, float range) {
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
        if (this.glumboss.getTarget() != null) {
           return true;
            }
        return false;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {

        if(this.glumboss.getCharged()) {
            super.tick();
        }
    }


    public void attack() {
        LivingEntity target = this.glumboss.getTarget();
        Vec3 targetVec = target.getPosition(1f);
        Vec3 vec3 = this.glumboss.getPosition(1f);
        Vec3 vecTo = vec3.vectorTo(targetVec);
        if(this.glumboss.distanceTo(this.glumboss.getTarget()) > 5d) {
            for (int i = 0; i < 5; i++) {
                lightningPos = vec3.add((vecTo.multiply(0.15d, 0.15d, 0.15d)).multiply(i, i, i));
                BlockPos blockpos = this.glumboss.blockPosition();
                if (this.glumboss.level.canSeeSky(blockpos.above())) {
                    LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.glumboss.level);
                    lightningbolt.moveTo(lightningPos.x(), lightningPos.y(), lightningPos.z());
                    this.glumboss.level.addFreshEntity(lightningbolt);
                }
            }
        }
        else{
            for (int i = 0; i < 5; i++) {
                lightningPos = vec3.add(Math.sin(this.glumboss.tickCount + i), 0d, Math.cos(this.glumboss.tickCount + i));
                BlockPos blockpos = this.glumboss.blockPosition();
                if (this.glumboss.level.canSeeSky(blockpos.above())) {
                    LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.glumboss.level);
                    lightningbolt.moveTo(lightningPos.x(), lightningPos.y(), lightningPos.z());
                    this.glumboss.level.addFreshEntity(lightningbolt);
                }
            }

        }
    }
}





