package coda.glumbis.client.screen;

import coda.glumbis.Glumbis;
import coda.glumbis.common.menu.GlumpCoilMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlumpCoilScreen extends AbstractContainerScreen<GlumpCoilMenu> {
    private static final ResourceLocation COIL_GUI = new ResourceLocation(Glumbis.MOD_ID, "textures/gui/glump_coil.png");

    public GlumpCoilScreen(GlumpCoilMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);

        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.playerInventoryTitle, (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
        this.font.draw(matrixStack, new TranslatableComponent("container." + Glumbis.MOD_ID + ".glump_coil"), ((float) imageWidth / 2 - font.width("Glump Coil") / 2F), 6f, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, COIL_GUI);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int energyLevel = menu.glumpCoilBlockEntity.energyLevel;

        if (energyLevel > 0) {
            float blitAmount = 106F * (energyLevel / 400F);

            this.blit(matrixStack, leftPos + 21, topPos + 9, 0, 173, (int) blitAmount, 20);
        }

    }
}