package coda.glumbis;

import coda.glumbis.common.entities.BigSockEntity;
import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import coda.glumbis.common.registry.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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

        GlumbisItems.ITEMS.register(bus);
        GlumbisEntities.ENTITIES.register(bus);
        GlumbisSounds.SOUNDS.register(bus);
        GlumbisParticles.PARTICLES.register(bus);
        GlumbisBlocks.BLOCKS.register(bus);

        bus.addListener(this::registerEntityAttributes);

        GeckoLib.initialize();
    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GlumbisEntities.GLUMBOSS.get(), GlumbossEntity.createAttributes().build());
        event.put(GlumbisEntities.GLUMP.get(), GlumpEntity.createAttributes().build());
        event.put(GlumbisEntities.ROCKET_PROPELLED_GLUMP.get(), RocketPropelledGlumpEntity.createAttributes().build());
        event.put(GlumbisEntities.BIG_SOCK.get(), BigSockEntity.createAttributes().build());
    }
}
