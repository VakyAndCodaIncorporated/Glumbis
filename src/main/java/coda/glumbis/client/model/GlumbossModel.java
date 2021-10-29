package coda.glumbis.client.model;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GlumbossModel extends AnimatedGeoModel<GlumbossEntity> {
    public int blinkTimer;

    @Override
    public ResourceLocation getModelLocation(GlumbossEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "geo/entity/glumboss.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GlumbossEntity object) {
        if (object.getSlamming() || object.getKicking()) {
            return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glumboss_angry.png");
        }
        else{
            if(object.tickCount % 40 == 0){
                if(object.getRandom().nextFloat() < 0.33){
                    blinkTimer = 10;
                }
            }
            if(blinkTimer > 0){
                blinkTimer--;
                if(blinkTimer > 7){
                    return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glumboss_angry.png");
                }
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glumboss_blink.png");
            }
            else{
                return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glumboss.png");
            }
        }
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
