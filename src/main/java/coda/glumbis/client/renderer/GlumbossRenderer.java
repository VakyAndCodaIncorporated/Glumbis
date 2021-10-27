package coda.glumbis.client.renderer;

import coda.glumbis.client.model.GlumbossModel;
import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GlumbossRenderer extends GeoEntityRenderer<GlumbossEntity> {

    public GlumbossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GlumbossModel());
        this.shadowRadius = 1.25F;
    }

}