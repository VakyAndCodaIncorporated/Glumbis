package coda.glumbis.client.renderer;

import coda.glumbis.Glumbis;
import coda.glumbis.client.model.GlumbossModel;
import coda.glumbis.common.entities.GlumbossEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlumbossRenderer extends MobRenderer<GlumbossEntity, GlumbossModel> {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation(Glumbis.MOD_ID, "glumboss"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Glumbis.MOD_ID, "textures/entity/glumboss.png");

    public GlumbossRenderer(EntityRendererProvider.Context context) {
        super(context, new GlumbossModel(context.bakeLayer(MODEL_LAYER)), 1.0F);
    }

    public ResourceLocation getTextureLocation(GlumbossEntity entity) {
        return TEXTURE;
    }
}
