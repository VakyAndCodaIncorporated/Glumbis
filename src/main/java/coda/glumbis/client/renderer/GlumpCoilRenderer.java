package coda.glumbis.client.renderer;

import coda.glumbis.client.model.GlumpCoilModel;
import coda.glumbis.common.blocks.entities.GlumpCoilBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class GlumpCoilRenderer extends GeoBlockRenderer<GlumpCoilBlockEntity> {

    public GlumpCoilRenderer(BlockEntityRendererProvider.Context renderManager) {
        super(renderManager, new GlumpCoilModel());
    }

    @Override
    public void render(GeoModel model, GlumpCoilBlockEntity animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.translate(0, -0.01D, 0);

        super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}