package coda.glumbis.common.entities.ai.goals;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GlumbossKickAttackGoal extends GlumbossAttackGoal {
    private int timer;
    private final int cooldown = 30;
    private int cooldownTimer;

    public GlumbossKickAttackGoal(GlumbossEntity entity) {
        super(entity, GlumbossEntity.AttackType.KICK);
    }

    @Override
    public void start() {
        super.start();

//        LivingEntity target = entity.getTarget();
//        double x = Mth.clamp(target.getX() - entity.getX(), -0.25, 0.25);
//        double z = Mth.clamp(target.getZ() - entity.getZ(), -0.25, 0.25);
//        entity.setDeltaMovement(entity.getDeltaMovement().add(x, 0, z));

        this.timer = 0;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        if (target != null) {
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
        }
        else {
            LivingEntity target = entity.getTarget();

            if (target != null && this.timer <= 30) {
                this.timer++;
                this.entity.setKicking(true);
                if (this.timer == 15) {
                    //entity.playSound(SoundEvents.GHAST_SCREAM, 0.4F, 1.0F);

                    if (entity.distanceToSqr(target) <= 16) {
                        target.hurt(DamageSource.mobAttack(entity), (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
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
