package coda.glumbis.client.model;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GlumbossModel extends AnimatedGeoModel<GlumbossEntity> {

    @Override
    public ResourceLocation getModelLocation(GlumbossEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "geo/entity/glumboss.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GlumbossEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glumboss.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GlumbossEntity animatable) {
        return new ResourceLocation(Glumbis.MOD_ID, "animations/entity/glumboss.animation.json");
    }

    @Override
    public void setLivingAnimations(GlumbossEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone catBody = this.getAnimationProcessor().getBone("catBody");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        catBody.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 350F));
    }
}
