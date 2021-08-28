package coda.glumbis.common.entities;

import coda.glumbis.common.init.GlumbisEntities;
import coda.glumbis.common.init.GlumbisItems;
import com.google.common.collect.Lists;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class BigSockEntity extends LivingEntity implements PlayerRideable {
   private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(BigSockEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(BigSockEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(BigSockEntity.class, EntityDataSerializers.FLOAT);
   private int lerpSteps;
   private double lerpX;
   private double lerpY;
   private double lerpZ;
   private double lerpYRot;
   private double lerpXRot;

   public BigSockEntity(EntityType<? extends BigSockEntity> p_38290_, Level p_38291_) {
      super(p_38290_, p_38291_);
      this.blocksBuilding = true;
   }

   public BigSockEntity(Level p_38293_, double p_38294_, double p_38295_, double p_38296_) {
      this(GlumbisEntities.BIG_SOCK.get(), p_38293_);
      this.setPos(p_38294_, p_38295_, p_38296_);
      this.xo = p_38294_;
      this.yo = p_38295_;
      this.zo = p_38296_;
   }

   protected float getEyeHeight(Pose p_38327_, EntityDimensions p_38328_) {
      return p_38328_.height;
   }

   protected Entity.MovementEmission getMovementEmission() {
      return Entity.MovementEmission.NONE;
   }

   protected void defineSynchedData() {
      this.entityData.define(DATA_ID_HURT, 0);
      this.entityData.define(DATA_ID_HURTDIR, 1);
      this.entityData.define(DATA_ID_DAMAGE, 0.0F);
   }

   public boolean canCollideWith(Entity p_38376_) {
      return canVehicleCollide(this, p_38376_);
   }

   public static boolean canVehicleCollide(Entity p_38324_, Entity p_38325_) {
      return (p_38325_.canBeCollidedWith() || p_38325_.isPushable()) && !p_38324_.isPassengerOfSameVehicle(p_38325_);
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public boolean isPushable() {
      return true;
   }

   @Override
   public HumanoidArm getMainArm() {
      return null;
   }

   @Override
   public Iterable<ItemStack> getArmorSlots() {
      return null;
   }

   @Override
   public ItemStack getItemBySlot(EquipmentSlot p_21127_) {
      return null;
   }

   @Override
   public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {
   }

   public boolean isPickable() {
      return !this.isRemoved();
   }

   public boolean canBeControlledByRider() {
      return this.getControllingPassenger() instanceof LivingEntity;
   }

   public void travel(Vec3 p_30633_) {
      if (this.isAlive()) {
         if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled()) {
            LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
            this.setYRot(livingentity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(livingentity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;
            float f = livingentity.xxa * 0.5F;
            float f1 = livingentity.zza;
            if (f1 <= 0.0F) {
               f1 *= 0.25F;
               this.gallopSoundCounter = 0;
            }

            if (this.onGround && this.playerJumpPendingScale == 0.0F && this.isStanding() && !this.allowStandSliding) {
               f = 0.0F;
               f1 = 0.0F;
            }

            if (this.playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround) {
               double d0 = this.getCustomJump() * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
               double d1 = d0 + this.getJumpBoostPower();
               Vec3 vec3 = this.getDeltaMovement();
               this.setDeltaMovement(vec3.x, d1, vec3.z);
               this.setIsJumping(true);
               this.hasImpulse = true;
               net.minecraftforge.common.ForgeHooks.onLivingJump(this);
               if (f1 > 0.0F) {
                  float f2 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F));
                  float f3 = Mth.cos(this.getYRot() * ((float)Math.PI / 180F));
                  this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.4F * f2 * this.playerJumpPendingScale), 0.0D, (double)(0.4F * f3 * this.playerJumpPendingScale)));
               }

               this.playerJumpPendingScale = 0.0F;
            }

            this.flyingSpeed = this.getSpeed() * 0.1F;
            if (this.isControlledByLocalInstance()) {
               this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
               super.travel(new Vec3((double)f, p_30633_.y, (double)f1));
            } else if (livingentity instanceof Player) {
               this.setDeltaMovement(Vec3.ZERO);
            }

            if (this.onGround) {
               this.playerJumpPendingScale = 0.0F;
               this.setIsJumping(false);
            }

            this.calculateEntityAnimation(this, false);
            this.tryCheckInsideBlocks();
         } else {
            this.flyingSpeed = 0.02F;
            super.travel(p_30633_);
         }
      }
   }


   public void lerpTo(double p_38299_, double p_38300_, double p_38301_, float p_38302_, float p_38303_, int p_38304_, boolean p_38305_) {
      this.lerpX = p_38299_;
      this.lerpY = p_38300_;
      this.lerpZ = p_38301_;
      this.lerpYRot = p_38302_;
      this.lerpXRot = p_38303_;
      this.lerpSteps = 10;
   }

   public InteractionResult interact(Player p_38330_, InteractionHand p_38331_) {
      if (!this.level.isClientSide) {
         p_38330_.setYRot(this.getYRot());
         p_38330_.setXRot(this.getXRot());
         p_38330_.startRiding(this);
         return InteractionResult.SUCCESS;
      }
      else {
         return InteractionResult.PASS;
      }
   }

   @Nullable
   public Entity getControllingPassenger() {
      return this.getFirstPassenger();
   }

   public Packet<?> getAddEntityPacket() {
      return new ClientboundAddEntityPacket(this);
   }

   @Override
   protected void addPassenger(Entity passenger) {
      super.addPassenger(passenger);
      if (this.isControlledByLocalInstance() && this.lerpSteps > 0) {
         this.lerpSteps = 0;
         this.absMoveTo(this.lerpX, this.lerpY, this.lerpZ, (float)this.lerpYRot, (float)this.lerpXRot);
      }
   }

   public ItemStack getPickResult() {
      return new ItemStack(GlumbisItems.SOCK.get());
   }
}
