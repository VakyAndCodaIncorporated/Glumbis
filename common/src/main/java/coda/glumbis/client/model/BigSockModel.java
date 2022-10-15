package coda.glumbis.client.model;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.BigSockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class BigSockModel extends AnimatedTickingGeoModel<BigSockEntity> {

    @Override
    public ResourceLocation getModelResource(BigSockEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "geo/entity/big_sock.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BigSockEntity object) {
        return new ResourceLocation(Glumbis.MOD_ID, "textures/entity/big_sock.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BigSockEntity animatable) {
        return new ResourceLocation(Glumbis.MOD_ID, "animations/entity/big_sock.animation.json");
    }

    @Override
    public void setLivingAnimations(BigSockEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }
}

