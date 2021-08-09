package coda.glumbis;

import coda.glumbis.init.GlumbisItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Glumbis.MOD_ID)
public class Glumbis {
    public static final String MOD_ID = "glumbis";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final String DATA_CAT = "CatData";

    public Glumbis() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        GlumbisItems.ITEMS.register(bus);

        forgeBus.addListener(this::onEntityInteract);
        forgeBus.addListener(this::onPlayerSleep);
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
}
