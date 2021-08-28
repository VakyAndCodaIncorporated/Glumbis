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
