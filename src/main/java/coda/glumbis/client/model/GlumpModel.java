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
}
