package coda.glumbis;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.init.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.function.Predicate;

@Mod(Glumbis.MOD_ID)
public class Glumbis {
    public static final String MOD_ID = "glumbis";
    public static final Logger LOGGER = LogManager.getLogger();

    public Glumbis() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

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
    }
}
