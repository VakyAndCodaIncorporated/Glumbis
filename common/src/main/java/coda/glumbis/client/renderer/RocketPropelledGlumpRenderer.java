package coda.glumbis.client.renderer;

import coda.glumbis.client.model.RocketPropelledGlumpModel;
import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RocketPropelledGlumpRenderer extends GeoProjectilesRenderer<RocketPropelledGlumpEntity> {

    public RocketPropelledGlumpRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RocketPropelledGlumpModel());
        this.shadowRadius = 0.4F;
    }

}