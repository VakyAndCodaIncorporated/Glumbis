package coda.glumbis.common.entities.ai.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

// todo - figure out why glumboss isnt kicking
public class GlumbossKickGoal extends BaseGlumbossAttackGoal {
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

    public GlumbossKickGoal(GlumbossEntity glumboss, int timerEnd, int coolDownEnd, int animation, int frameStart, int frameEnd, boolean shouldStopMoving, float range) {
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
        if (this.glumboss.getTarget() != null && !this.glumboss.getCharged()) {
            return this.glumboss.getTarget().isOnGround();
        }
        return false;
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
        if(this.glumboss.distanceTo(target) < 3.5f) {
            this.glumboss.doHurtTarget(this.glumboss.getTarget());
            this.glumboss.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.4f, 1f);
            target.setDeltaMovement(target.getDeltaMovement().add(0d, 0.2d, 0d));
        }
    }
}





