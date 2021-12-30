package coda.glumbis;

import coda.glumbis.common.capabilities.CapabilityAttacher;
import coda.glumbis.common.capabilities.ICatnipCapability;
import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(Glumbis.MOD_ID)
public class Glumbis {
    public static final String MOD_ID = "glumbis";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab GROUP = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GlumbisItems.GLUMBIS.get());
        }
    };

    public Glumbis() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        GlumbisItems.ITEMS.register(bus);
        GlumbisEntities.ENTITIES.register(bus);
        GlumbisSounds.SOUNDS.register(bus);
        GlumbisParticles.PARTICLES.register(bus);
        GlumbisBlocks.BLOCKS.register(bus);

        bus.addListener(this::registerEntityAttributes);
        bus.addListener(this::commonSetup);

        GeckoLib.initialize();
    }

    @CapabilityInject(ICatnipCapability.Catnip.class)
    public static Capability<ICatnipCapability.Catnip> ENTITY_CATNIP_CAPABILITY = null;

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GlumbisEntities.GLUMBOSS.get(), GlumbossEntity.createAttributes().build());
        event.put(GlumbisEntities.GLUMP.get(), GlumpEntity.createAttributes().build());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(ICatnipCapability.class);
    }

    private void attachCapabilities(AttachCapabilitiesEvent<Cat> event) {
        if (event.getObject() == null) return;

        event.addCapability(new ResourceLocation(MOD_ID, "catCap"), new CapabilityAttacher());
    }
}
