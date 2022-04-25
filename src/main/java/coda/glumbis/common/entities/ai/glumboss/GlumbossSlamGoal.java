package coda.glumbis.common.entities.ai.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class GlumbossSlamGoal extends BaseGlumbossAttackGoal {
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

    public GlumbossSlamGoal(GlumbossEntity glumboss, int timerEnd, int coolDownEnd, int animation, int frameStart, int frameEnd, boolean shouldStopMoving, float range) {
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
        if(this.glumboss.getCharged()) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public void start() {
        super.start();
        coolDown = 0;
    }

    @Override
    public void tick() {
        if(!this.glumboss.getCharged()) {
            super.tick();
        }
    }

    public void attack() {
        LivingEntity target = this.glumboss.getTarget();
            this.glumboss.playSound(SoundEvents.GENERIC_EXPLODE, 0.4f, 1f);
            for (LivingEntity livingentity : this.glumboss.level.getEntitiesOfClass(LivingEntity.class, this.glumboss.getBoundingBox().inflate(5.2D, 0.4D, 5.2D).move(0d, -0.5, 0d))) {
                if (livingentity != this.glumboss && !(livingentity instanceof GlumpEntity)) {
                    this.glumboss.doHurtTarget(livingentity);
                    livingentity.setDeltaMovement(0d, 1.0d, 0d);
                }
            }
    }
}


