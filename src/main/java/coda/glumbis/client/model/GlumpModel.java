package coda.glumbis.client.model;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GlumpModel extends AnimatedGeoModel<GlumpEntity> {

    @Override
    public ResourceLocation getModelLocation(GlumpEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "geo/entity/glump.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GlumpEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_1.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GlumpEntity animatable) {
        return new ResourceLocation(Glumbis.MOD_ID, "animations/entity/glump.animation.json");
    }

    @Override
    public void setLivingAnimations(GlumpEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone root = this.getAnimationProcessor().getBone("root");
        IBone chest = this.getAnimationProcessor().getBone("chest");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        chest.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        root.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

        if (entity.getExploding()) {
            root.setScaleX((float) (root.getScaleX() + 0.1));
            root.setScaleY((float) (root.getScaleY() + 0.1));
            root.setScaleZ((float) (root.getScaleZ() + 0.1));
        }
    }
}
