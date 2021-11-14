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
                this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                        bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            }
        }
    }

}
