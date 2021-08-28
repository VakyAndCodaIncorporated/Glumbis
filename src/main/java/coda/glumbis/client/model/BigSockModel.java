package coda.glumbis.client.model;

import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BigSockModel extends HierarchicalModel<GlumbossEntity> {
   public ModelPart root;
   public ModelPart sock;
   public ModelPart sock_base;
   public ModelPart sock_lid;
   private float jumpRotation;

   public BigSockModel(ModelPart root) {
      this.root = root;
      sock = root.getChild("sock");
      sock_base = sock.getChild("sock_base");
      sock_lid = sock.getChild("sock_lid");
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

      return LayerDefinition.create(data, 144, 112);
   }

   @Override
   public ModelPart root() {
      return root;
   }

   @Override
   public void setupAnim(GlumbossEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float f = ageInTicks - (float) entityIn.tickCount;
      this.jumpRotation = Mth.sin(entityIn.getJumpCompletion(f) * (float) Math.PI);
      this.sock.xRot = (this.jumpRotation * 25.0F) * ((float) Math.PI / 180F);
   }

   public void prepareMobModel(GlumbossEntity entity, float p_103544_, float p_103545_, float p_103546_) {
      super.prepareMobModel(entity, p_103544_, p_103545_, p_103546_);
      this.jumpRotation = Mth.sin(entity.getJumpCompletion(p_103546_) * (float)Math.PI);
   }
}