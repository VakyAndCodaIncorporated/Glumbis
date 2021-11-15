package coda.glumbis.client.renderer.layers;

import coda.glumbis.Glumbis;
import coda.glumbis.client.model.GlumbossModel;
import coda.glumbis.common.entities.GlumbossEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class GlumbossChargedLayer extends GeoLayerRenderer {
    private static final ResourceLocation POWER = new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glumboss/glumboss_armour.png");
    private static final ResourceLocation EYES = new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glumboss/glumboss_eye_glow.png");
    private static final ResourceLocation MODEL = new ResourceLocation(Glumbis.MOD_ID, "geo/entity/glumboss.geo.json");

    public GlumbossChargedLayer(IGeoRenderer<GlumbossEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType cameo =  RenderType.eyes(EYES);
        if(entityLivingBaseIn instanceof GlumbossEntity) {
            if(((GlumbossEntity) entityLivingBaseIn).getState() == 3) {
                matrixStackIn.pushPose();
                float f = ((float) entityLivingBaseIn.tickCount + partialTicks);
                matrixStackIn.scale(1.05f, 1.05f, 1.05f);
                matrixStackIn.translate(0.0d, -0.038d, 0.0d);
                RenderType glint = RenderType.energySwirl(this.POWER, this.xOffset(f), f * 0.01F);
                this.getRenderer().render(this.getEntityModel().getModel(this.getEntityModel().getModelLocation(entityLivingBaseIn)), entityLivingBaseIn, partialTicks, glint, matrixStackIn, bufferIn,
                        bufferIn.getBuffer(glint), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1f, 0.25f);
                matrixStackIn.popPose();
                this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                        bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            }
        }
    }
    protected float xOffset(float f) {
        return (float) (f * 0.01);
    }
}
