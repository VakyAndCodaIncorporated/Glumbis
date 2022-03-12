package coda.glumbis.client.model;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.BigSockEntity;
import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BigSockModel extends AnimatedTickingGeoModel<BigSockEntity> {

    @Override
    public ResourceLocation getModelLocation(BigSockEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "geo/entity/big_sock.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BigSockEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/big_sock.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BigSockEntity animatable) {
        return new ResourceLocation(Glumbis.MOD_ID, "animations/entity/big_sock.animation.json");
    }

    @Override
    public void setLivingAnimations(BigSockEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }
}
