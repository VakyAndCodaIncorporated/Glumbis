package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GlumbossKickAttackGoal extends GlumbossAttackGoal {
    private int timer;
    private final int cooldown = 65;
    private int cooldownTimer;

    public GlumbossKickAttackGoal(GlumbossEntity entity) {
        super(entity, GlumbossEntity.AttackType.KICK);
    }

    @Override
    public void start() {
        super.start();
        this.timer = 0;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        if (target != null && !this.entity.getSlamming()) {
            return attackType == GlumbossEntity.AttackType.KICK && entity.distanceToSqr(target) <= 25;
        }
        else {
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();


        if (cooldownTimer < cooldown) {
            cooldownTimer++;
            this.timer = 0;
        }
        else {
            LivingEntity target = entity.getTarget();

            if (target != null && this.timer <= 20 && entity.distanceToSqr(target) <= 16) {
                this.timer++;
                this.entity.getNavigation().stop();
                this.entity.getLookControl().setLookAt(target, 30.0f, 30.0f);
                this.entity.setKicking(true);
                this.entity.setSlamming(false);
                if (this.timer == 17) {
                    if (entity.distanceToSqr(target) <= 16) {
                        target.hurt(DamageSource.mobAttack(entity), (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                        System.out.println(this.entity.getKicking() + ": kicking");
                        //this.entity.setKicking(false);
                    }
                }
            }
            else {
                this.entity.setKicking(false);
                this.timer = 0;
                this.cooldownTimer = 0;
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
