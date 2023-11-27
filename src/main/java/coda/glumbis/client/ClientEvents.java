package coda.glumbis.client;

import coda.glumbis.Glumbis;
import coda.glumbis.client.renderer.*;
import coda.glumbis.client.screen.GlumpCoilScreen;
import coda.glumbis.common.registry.GlumbisBlockEntities;
import coda.glumbis.common.registry.GlumbisBlocks;
import coda.glumbis.common.registry.GlumbisMenus;
import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.networking.GlumbisNetworking;
import coda.glumbis.networking.InputMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.*;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;
import java.util.List;

@Mod.EventBusSubscriber(modid = Glumbis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        GlumbisKeybinds.register();

        MenuScreens.register(GlumbisMenus.GLUMP_COIL.get(), GlumpCoilScreen::new);

        ItemBlockRenderTypes.setRenderLayer(GlumbisBlocks.GLUMP_COIL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(GlumbisBlocks.CATNIP.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(GlumbisBlockEntities.GLUMP_COIL.get(), GlumpCoilRenderer::new);
        event.registerEntityRenderer(GlumbisEntities.GLUMBOSS.get(), GlumbossRenderer::new);
        event.registerEntityRenderer(GlumbisEntities.GLUMP.get(), GlumpRenderer::new);
        event.registerEntityRenderer(GlumbisEntities.ROCKET_PROPELLED_GLUMP.get(), RocketPropelledGlumpRenderer::new);
        event.registerEntityRenderer(GlumbisEntities.BIG_SOCK.get(), BigSockRenderer::new);
   }

    @Mod.EventBusSubscriber(modid = Glumbis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeBusEvents {

        @SubscribeEvent
        public static void onKeyPress(InputEvent.KeyInputEvent event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            if (GlumbisKeybinds.slamKey.isDown()) {
                GlumbisNetworking.CHANNEL.sendToServer(new InputMessage(KeyEvent.VK_G));
            }
        }

        @SubscribeEvent
        public static void addToolTips(ItemTooltipEvent e) {
            ItemStack stack = e.getItemStack();
            List<Component> list = e.getToolTip();

            if (stack.getTag() != null && stack.getTag().get("Energized") != null) {
                MutableComponent empty = new TextComponent("⚡ - ").withStyle(Style.EMPTY.withColor(0x9eb8ff));

                int mth = (stack.getTag().getInt("Energized") / 10) + 1;
                int level = Mth.clamp(mth, (stack.getTag().getInt("Energized") / 10) + 1, 10);

                if (level > 0) {
                    MutableComponent emptyBolt = new TextComponent("▌").withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
                    MutableComponent litBolt = new TextComponent("▌").withStyle(Style.EMPTY.withColor(0x9eb8ff));

                    for (int i = 0; i < level; i++) {
                        empty.append(litBolt);
                    }
                    for (int i = level; i < 10; i++) {
                        empty.append(emptyBolt);
                    }
                }

                empty.append(new TextComponent(String.format(" - %s", stack.getTag().getInt("Energized"))).withStyle(Style.EMPTY.withColor(0x9eb8ff)))
                        .append("%").withStyle(Style.EMPTY.withColor(0x9eb8ff).withBold(false));

                list.add(1, empty);
            }
        }
    }
}