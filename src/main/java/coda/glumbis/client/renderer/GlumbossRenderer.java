package coda.glumbis.client.renderer;

import coda.glumbis.client.model.GlumbossModel;
import coda.glumbis.client.renderer.layers.GlumbossChargedLayer;
import coda.glumbis.common.entities.GlumbossEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class GlumbossRenderer extends GeoEntityRenderer<GlumbossEntity> {
    public GlumbossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GlumbossModel());
        this.addLayer(new GlumbossChargedLayer(this));
        this.shadowRadius = 1.0F;
    }
}