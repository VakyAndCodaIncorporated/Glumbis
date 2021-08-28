package coda.glumbis.client.renderer;

import coda.glumbis.Glumbis;
import coda.glumbis.client.model.BigSockModel;
import coda.glumbis.common.entities.BigSockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BigSockRenderer extends EntityRenderer<BigSockEntity> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation(Glumbis.MOD_ID, "big_sock"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Glumbis.MOD_ID, "textures/entity/big_sock.png");
    private final BigSockModel model;

    public BigSockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.0F;
        model = new BigSockModel(context.bakeLayer(MODEL_LAYER));
    }

    public ResourceLocation getTextureLocation(BigSockEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(BigSockEntity entity, float p_114486_, float p_114487_, PoseStack ms, MultiBufferSource buffer, int p_114490_) {
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(this.getTextureLocation(entity)), false, false);
        ms.pushPose();
        ms.mulPose(Vector3f.XP.rotationDegrees(180F));
        ms.translate(0.0D, -1.5D, 0.0D);
        this.model.renderToBuffer(ms, vertexconsumer, p_114490_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        ms.popPose();
        super.render(entity, p_114486_, p_114487_, ms, buffer, p_114490_);
    }
}
