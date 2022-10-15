package coda.glumbis.common.entities;

import coda.glumbis.common.registry.GlumbisSounds;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Predicate;

public class RocketPropelledGlumpEntity extends AbstractArrow implements IAnimatable, IAnimationTickable {
    public static final Predicate<LivingEntity> NO_CATS = (p_20436_) -> !(p_20436_ instanceof GlumpEntity) && !(p_20436_ instanceof GlumbossEntity) && !(p_20436_ instanceof Cat);
    private final AnimationFactory factory = new AnimationFactory(this);
    public Player owner;
    private BlockState lastState;


    public RocketPropelledGlumpEntity(EntityType<? extends AbstractArrow> arrow, Level level) {
        super(arrow, level);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        this.lastState = this.level.getBlockState(p_36755_.getBlockPos());
        super.onHitBlock(p_36755_);
        Vec3 vec3 = p_36755_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.inGround = true;
        this.shakeTime = 7;
        this.setSoundEvent(GlumbisSounds.GLUMP_EXPLODE.get());
        this.setShotFromCrossbow(false);
        this.explode();
    }


    public void explode(){
        System.out.println("boom");
        if(this.level.isClientSide()){
            for(int i = 0; i < 20; i++){
                this.level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0d, this.level.getRandom().nextFloat(), 0d);
            }
        }
        for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0D, 2.0D, 2.0D))) {
            livingentity.hurt(DamageSource.mobAttack(this.owner), 4.0f);
        }
        this.kill();
    }

    @Override
    public void tick() {
        super.tick();
        for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.4D, 0.4D, 0.4D))) {
            if(livingentity != getOwner()){
                explode();
            }
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 2, this::predicate));
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
}
