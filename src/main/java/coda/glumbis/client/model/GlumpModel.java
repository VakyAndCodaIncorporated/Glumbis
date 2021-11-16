package coda.glumbis.client.model;

import coda.glumbis.Glumbis;
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
        //System.out.println(object.getVariant());
        switch(object.getVariant()){
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
            default:
                //haha Coda, you expected that the default one would be glump_0, but it WAS ME, WADOO!
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glump/glump_7.png");
        }
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

        if(entity.tickCount % 10 == 0){
            root.setScaleZ(root.getScaleZ() + entity.getRandom().nextFloat()/10);
            root.setScaleY(root.getScaleY() + entity.getRandom().nextFloat()/10);
            root.setScaleX(root.getScaleX() + entity.getRandom().nextFloat()/10);
        }
    }
}
