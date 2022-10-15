package coda.glumbis.client.model;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.Random;

public class RocketPropelledGlumpModel extends AnimatedGeoModel<RocketPropelledGlumpEntity> {

    @Override
    public ResourceLocation getModelResource(RocketPropelledGlumpEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "geo/entity/glump.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RocketPropelledGlumpEntity object) {
       /* switch(object.getVariant()){
            case 9:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_0.png");
            case 1:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_1.png");
            case 2:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_2.png");
            case 3:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_3.png");
            case 4:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_4.png");
            case 5:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_5.png");
            case 6:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_6.png");
            case 7:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_7.png");
            case 8:
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_8.png");
            default:*/
        //haha Coda, you expected that the default one would be glump_0, but it WAS ME, WADOO!
        return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_7.png");
        // }
    }

    @Override
    public ResourceLocation getAnimationResource(RocketPropelledGlumpEntity animatable) {
        return new ResourceLocation(Glumbis.MOD_ID, "animations/entity/glump.animation.json");
    }

    @Override
    public void setLivingAnimations(RocketPropelledGlumpEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone root = this.getAnimationProcessor().getBone("root");
        IBone chest = this.getAnimationProcessor().getBone("chest");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        Random random = new Random();

        // root.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));
        root.setRotationX(random.nextInt(1500));
        //root.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
        root.setRotationZ(random.nextInt(1500));


        if (entity.tickCount % 10 == 0) {

            root.setScaleX(root.getScaleZ() + random.nextFloat() / 10);
            root.setScaleY(root.getScaleY() + random.nextFloat() / 10);
            root.setScaleX(root.getScaleX() + random.nextFloat() / 10);
        }
    }
}
