package coda.glumbis.client.model;

import coda.glumbis.Glumbis;
import coda.glumbis.common.blocks.entities.GlumpCoilBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GlumpCoilModel extends AnimatedGeoModel<GlumpCoilBlockEntity> {

    @Override
    public ResourceLocation getAnimationFileLocation(GlumpCoilBlockEntity animatable) {
        return new ResourceLocation(Glumbis.MOD_ID, "animations/block/glump_coil.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(GlumpCoilBlockEntity animatable) {
        return new ResourceLocation(Glumbis.MOD_ID, "geo/block/glump_coil.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GlumpCoilBlockEntity entity) {
        return new ResourceLocation(Glumbis.MOD_ID, "textures/block/glump_coil.png");
    }
}