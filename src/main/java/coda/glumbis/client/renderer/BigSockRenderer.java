package coda.glumbis.client.renderer;

import coda.glumbis.client.model.BigSockModel;
import coda.glumbis.common.entities.BigSockEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class BigSockRenderer extends GeoEntityRenderer<BigSockEntity> {

    public BigSockRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BigSockModel());
        this.shadowRadius = 0.8F;
    }

}