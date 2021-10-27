package coda.glumbis;

import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.init.GlumbisEntities;
import coda.glumbis.common.init.GlumbisItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.Random;
import java.util.function.Predicate;

@Mod(Glumbis.MOD_ID)
public class Glumbis {
    public static final String MOD_ID = "glumbis";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final String DATA_CAT = "CatData";

    public Glumbis() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        GlumbisItems.ITEMS.register(bus);
        GlumbisEntities.ENTITIES.register(bus);

        bus.addListener(this::registerEntityAttributes);

        forgeBus.addListener(this::onEntityInteract);
        forgeBus.addListener(this::onPlayerSleep);
        forgeBus.addListener(this::onEntityJoinWorld);

        GeckoLib.initialize();
    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GlumbisEntities.GLUMBOSS.get(), GlumbossEntity.createAttributes().build());
        event.put(GlumbisEntities.GLUMP.get(), GlumpEntity.createAttributes().build());
    }

    private void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        Item heldItem = stack.getItem();

        if (target instanceof Cat && heldItem == GlumbisItems.SOCK.get()) {

            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag targetTag = stack.serializeNBT();

            ItemStack glumbis = new ItemStack(GlumbisItems.GLUMBIS.get());

            tag.put(DATA_CAT, targetTag);

            glumbis.setTag(tag);

            target.discard();

            player.setItemInHand(hand, glumbis);
        }
    }

    private void onPlayerSleep(PlayerWakeUpEvent event) {
        Player player = event.getPlayer();
        if (player.getItemBySlot(EquipmentSlot.FEET).is(GlumbisItems.SOGGY_SOCKS.get()) && player.getHealth() < player.getMaxHealth()) {
            player.heal(10);
        }
    }

    // Is there a worse way to do this?
    private void onEntityJoinWorld(EntityJoinWorldEvent event) {
        final Predicate<LivingEntity> GLUMBIS_IN_PLAYERS_HOTBAR = (p_20440_) -> {
            return p_20440_ instanceof Player && p_20440_.getSlot(0).get().is(GlumbisItems.GLUMBIS.get())
                    || p_20440_.getSlot(1).get().is(GlumbisItems.GLUMBIS.get())
                    || p_20440_.getSlot(2).get().is(GlumbisItems.GLUMBIS.get())
                    || p_20440_.getSlot(3).get().is(GlumbisItems.GLUMBIS.get())
                    || p_20440_.getSlot(4).get().is(GlumbisItems.GLUMBIS.get())
                    || p_20440_.getSlot(5).get().is(GlumbisItems.GLUMBIS.get())
                    || p_20440_.getSlot(6).get().is(GlumbisItems.GLUMBIS.get())
                    || p_20440_.getSlot(7).get().is(GlumbisItems.GLUMBIS.get())
                    || p_20440_.getSlot(8).get().is(GlumbisItems.GLUMBIS.get());
        };
        Entity entity = event.getEntity();

// TODO additional cat type start (this wont work bc minecraft is stupid, need to use capabilities) **/
/*
        Cat.TEXTURE_BY_TYPE.put(11, new ResourceLocation(MOD_ID, "textures/entity/cat/cymes.png"));

        if (entity instanceof Cat cat) {
            cat.setCatType(new Random().nextInt(12));
        }
*/
        if (entity instanceof Creeper creeper) {
            creeper.goalSelector.addGoal(0, new AvoidEntityGoal<>(creeper, Player.class, 12.0F, 1.0D, 1.2D, GLUMBIS_IN_PLAYERS_HOTBAR));
        }
    }
}
