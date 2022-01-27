package coda.glumbis.common.entities.ai.goals.glumboss;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;

public class GlumbossStaticChargeGoal extends Goal {
    protected GlumbossEntity entity;
    private int timer;
    private final int COOLDOWN = 200;
    private int cooldownTimer;

    public GlumbossStaticChargeGoal(GlumbossEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.entity.distanceToSqr(this.entity.getTarget()) < 35.0f;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entity.getState() == 0 || this.entity.getState() == 3) {
            if (this.cooldownTimer < COOLDOWN) {
                this.cooldownTimer++;
            } else {
                if (this.timer <= 50) {
                    this.timer++;
                    this.entity.setState(3);
                    this.entity.getNavigation().stop();
                    if (this.timer == 1) {
                        this.entity.playSound(GlumbisSounds.GLUMBOSS_CHARGE.get(), 3.0F, 1.0F);
                    }
                    if (this.timer == 42) {
                        this.tryHurtTarget(this.entity, this.entity.distanceTo(this.entity.getTarget()));
                        int lightningBoltAmount = entity.isInWaterOrRain() ? 10 : 3;
                        for(int i = 0; i <= lightningBoltAmount; i++) {
                            if (this.entity.level instanceof ServerLevel) {
                                double x = this.entity.level.getRandom().nextFloat();
                                if (this.entity.level.getRandom().nextFloat() <= 0.5) {
                                    x = (0.5 * x + 0.5) * 15;
                                }
                                double z = this.entity.level.getRandom().nextFloat();
                                if (this.entity.level.getRandom().nextFloat() <= 0.5) {
                                    z = (0.5 * z + 0.5) * 15;
                                }
                                BlockPos blockpos = this.entity.blockPosition().offset(x, 0, z);
                                if (this.entity.level.canSeeSky(blockpos.above())) {
                                    LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.entity.level);
                                    lightningbolt.moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                                    this.entity.level.addFreshEntity(lightningbolt);
                                }
                                GlumpEntity glump = GlumbisEntities.GLUMP.get().create(this.entity.level);
                                glump.moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                                glump.setNoGravity(false);
                                glump.finalizeSpawn((ServerLevel) this.entity.level, this.entity.level.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, null, null);
                                this.entity.level.addFreshEntity(glump);
                            }
                        }
                    }
                } else {
                    this.entity.setState(0);
                    this.timer = 0;
                    this.cooldownTimer = 0;
                }
            }
        }
        else{
            this.stop();
        }
    }

    protected void tryHurtTarget(GlumbossEntity entity, double distanceTo){
        if(distanceTo < this.getAttackReachSqr(entity)){
            LivingEntity target = this.entity.getTarget();
            double distanceToGlumboss = target.distanceToSqr(entity);
            float damage = 1 - Mth.sqrt((float) distanceToGlumboss) / 10;
            target.hurt(DamageSource.mobAttack(entity), (0.5F * damage + 0.5F) * 10);
            //target.setDeltaMovement(target.getDeltaMovement().add(target.position().multiply(1.04, 1.4, 1.04)));
        }
    }

    protected double getAttackReachSqr(LivingEntity entity) {
        return entity.getBbWidth() * 2.0F * entity.getBbWidth() * 2.0F + entity.getBbWidth();
    }
}
