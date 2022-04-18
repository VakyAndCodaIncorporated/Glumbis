package coda.glumbis.common.entities.ai.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class BaseGlumbossAttackGoal extends Goal {
    public GlumbossEntity glumboss;
    public int timer;
    public final int timerEnd;
    public int coolDown;
    public final int coolDownEnd;
    public int animation;
    public final int frameStart;
    public final int frameEnd;
    public boolean shouldStopMoving;
    public float range;
    public boolean isInRange;

    public BaseGlumbossAttackGoal(GlumbossEntity glumboss, int timerEnd, int coolDownEnd, int animation, int frameStart, int frameEnd, boolean shouldStopMoving, float range){
        this.glumboss = glumboss;
        //how long the animation is in ticks
        this.timerEnd = timerEnd;
        //the minimum cooldown the attack must have to be able to happen again
        this.coolDownEnd = coolDownEnd;
        //the animation that needs to play, check the switch statement in predicate to see what number relates to what animation
        this.animation = animation;
        //the frame where you start doing special things, eg: dealing damage
        this.frameStart = frameStart;
        //the frame where you end doing special things
        this.frameEnd = frameEnd;
        //stops the entity from moving when plays its animation
        this.shouldStopMoving = shouldStopMoving;
    }

    @Override
    public boolean canUse() {
        if (glumboss.isAlive() && this.glumboss.getTarget() != null) {
                return true;
        }
            return false;
    }

    @Override
    public boolean canContinueToUse() {
        return glumboss.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        coolDown = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if(glumboss.getAnimState() == 0 || glumboss.getAnimState() == animation) {
            //moves to the entity if theyre too far away
            if (glumboss.getTarget() != null) {
                if (glumboss.distanceTo(glumboss.getTarget()) > range) {
                    glumboss.getNavigation().moveTo(glumboss.getTarget(), 1.8f);
                }
            }
            if (coolDown <= coolDownEnd) {
                coolDown++;
                timer = 0;
            } else {
                //starts a timer at 0 and loops all the way to however long the animation is
                if (timer <= timerEnd) {
                    timer += 1;
                    glumboss.setAnimState(animation);
                    if (shouldStopMoving) {
                        glumboss.getNavigation().stop();
                    }
                    //between frame A and frame B, you can run any code inside the attack() method
                    if (timer >= frameStart && timer <= frameEnd) {
                        attack();
                    }
                } else {
                    //resets everything
                    glumboss.setAnimState(0);
                    this.coolDown = 0;
                    this.timer = 0;
                    this.isInRange = false;
                }
            }
        }
    }


    public void attack(){
    }
}
