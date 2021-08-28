package coda.glumbis.client.model;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class GlumbossModel extends HierarchicalModel<GlumbossEntity> {
   public ModelPart root;
   public ModelPart sock;
   public ModelPart sock_base;
   public ModelPart sock_lid;
   public ModelPart head;
   public ModelPart right_ear;
   public ModelPart left_ear;
   public ModelPart right_paw;
   public ModelPart left_paw;
   public ModelPart tail;
   private float jumpRotation;

   public GlumbossModel(ModelPart root) {
      this.root = root;
      sock = root.getChild("sock");
      sock_base = sock.getChild("sock_base");
      head = sock.getChild("head");
      sock_lid = sock.getChild("sock_lid");
      left_ear = head.getChild("left_ear");
      right_ear = head.getChild("right_ear");
      right_paw = sock.getChild("right_paw");
      left_paw = sock.getChild("left_paw");
      tail = sock.getChild("tail");
   }

   public static LayerDefinition getLayer() {
      MeshDefinition data = new MeshDefinition();
      PartDefinition root = data.getRoot();

      PartDefinition sock = root.addOrReplaceChild(
              "sock",
              CubeListBuilder.create()
                      .texOffs(0, 69)
                      .mirror(false)
                      .addBox(-8.0F, -11.0F, -8.0F, 16.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, 0.0F, 0.0F)
      );

      PartDefinition sock_base = sock.addOrReplaceChild(
              "sock_base",
              CubeListBuilder.create()
                      .texOffs(0, 0)
                      .mirror(false)
                      .addBox(-10.0F, 0.0F, -19.0F, 20.0F, 13.0F, 29.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)
      );

      PartDefinition sock_lid = sock.addOrReplaceChild(
              "sock_lid",
              CubeListBuilder.create()
                      .texOffs(0, 42)
                      .mirror(false)
                      .addBox(-11.0F, -5.0F, -11.0F, 22.0F, 5.0F, 22.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.0F, 0.0F, 0.0F)
      );

      PartDefinition head = sock.addOrReplaceChild(
              "head",
              CubeListBuilder.create()
                      .texOffs(69, 0)
                      .mirror(false)
                      .addBox(-8.0F, -13.0F, -8.0F, 16.0F, 13.0F, 14.0F, new CubeDeformation(0.0F))
                      .texOffs(48, 76)
                      .mirror(false)
                      .addBox(-4.0F, -5.0F, -10.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(0.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.0F)
      );

      PartDefinition right_ear = head.addOrReplaceChild(
              "right_ear",
              CubeListBuilder.create()
                      .texOffs(0, 42)
                      .mirror(true)
                      .addBox(0.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                      .texOffs(0, 69)
                      .mirror(true)
                      .addBox(2.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                      .texOffs(0, 0)
                      .mirror(true)
                      .addBox(4.0F, -3.0F, -3.0F, 7.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(7.0F, -11.0F, 0.0F, 0.0F, 0.0F, -0.6109F)
      );

      PartDefinition left_ear = head.addOrReplaceChild(
              "left_ear",
              CubeListBuilder.create()
                      .texOffs(0, 42)
                      .mirror(false)
                      .addBox(-2.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                      .texOffs(0, 69)
                      .mirror(false)
                      .addBox(-4.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                      .texOffs(0, 0)
                      .mirror(false)
                      .addBox(-11.0F, -3.0F, -3.0F, 7.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(-6.0F, -12.0F, 0.0F, 0.0F, 0.0F, 1.1345F)
      );

      PartDefinition right_paw = sock.addOrReplaceChild(
              "right_paw",
              CubeListBuilder.create()
                      .texOffs(64, 77)
                      .mirror(true)
                      .addBox(-2.5F, -2.0F, -4.0F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                      .texOffs(16, 20)
                      .mirror(true)
                      .addBox(-2.5F, 1.0F, -3.5F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(7.5F, -16.0F, -8.0F, 0.0F, 0.0F, 0.0F)
      );

      PartDefinition left_paw = sock.addOrReplaceChild(
              "left_paw",
              CubeListBuilder.create()
                      .texOffs(64, 77)
                      .mirror(false)
                      .addBox(-2.5F, -2.0F, -4.0F, 5.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                      .texOffs(16, 20)
                      .mirror(false)
                      .addBox(-2.5F, 1.0F, -3.5F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(-7.5F, -16.0F, -8.0F, 0.0F, 0.0F, 0.0F)
      );

      PartDefinition tail = sock.addOrReplaceChild(
              "tail",
              CubeListBuilder.create()
                      .texOffs(58, 42)
                      .mirror(false)
                      .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 30.0F, new CubeDeformation(0.0F)),
              PartPose.offsetAndRotation(0.0F, -1.0F, 8.0F, 0.0F, 0.0F, 0.0F)
      );

      return LayerDefinition.create(data, 144, 112);
   }

   @Override
   public ModelPart root() {
      return root;
   }

   @Override
   public void setupAnim(GlumbossEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float f = ageInTicks - (float) entityIn.tickCount;
      float speed = 1.0f;
      float degree = 1.0f;
      limbSwing = ageInTicks;
      limbSwingAmount = 0.4F;
      this.jumpRotation = Mth.sin(entityIn.getJumpCompletion(f) * (float) Math.PI);
      this.sock.xRot = (this.jumpRotation * 25.0F) * ((float) Math.PI / 180F);
      //this.sock.y = Mth.cos(limbSwing * speed * 0.2F) * degree * limbSwingAmount + 10.0F;


      // TODO: use a more efficient way to do check if the glumboss isnt moving?
      if (entityIn.getDeltaMovement().x == 0.0D && entityIn.getDeltaMovement().z == 0.0D && entityIn.getDeltaMovement().y == 0.0D) {
         //this.left_ear.zRot = Mth.sin(ageInTicks * speed * 0.2F) * 0.2F + 0.25F;
      }
   }

   public void prepareMobModel(GlumbossEntity entity, float p_103544_, float p_103545_, float p_103546_) {
      super.prepareMobModel(entity, p_103544_, p_103545_, p_103546_);
      this.jumpRotation = Mth.sin(entity.getJumpCompletion(p_103546_) * (float)Math.PI);
   }
}