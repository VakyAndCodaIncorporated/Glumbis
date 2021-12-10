package coda.glumbis.common;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.GlumbossEntity;
import coda.glumbis.common.entities.GlumpEntity;
import coda.glumbis.common.init.GlumbisEntities;
import coda.glumbis.common.init.GlumbisItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = Glumbis.MOD_ID)
public class CommonEvents {
    private static final String DATA_CAT = "CatData";

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        Item heldItem = stack.getItem();
        Level level = event.getWorld();

        if (target instanceof Cat && heldItem == GlumbisItems.SOCK.get()) {
            if (!level.isClientSide) {

                ItemStack stack1 = new ItemStack(GlumbisItems.GLUMBIS.get());

                boolean more = stack.getCount() > 1;

                if (more) {
                    stack.shrink(1);

                    more = true;
                } else {
                    player.setItemInHand(hand, stack1);
                }

                CompoundTag targetTag = target.serializeNBT();
                targetTag.putString("OwnerName", player.getName().getString());
                CompoundTag tag = stack1.getOrCreateTag();
                tag.put(DATA_CAT, targetTag);
                stack1.setTag(tag);

                if (more) {
                    if (!player.getInventory().add(stack1)) player.drop(stack1, true);
                    else player.addItem(stack1);
                }

                target.discard();

                level.playSound(null, player.blockPosition(), SoundEvents.CAT_STRAY_AMBIENT, SoundSource.AMBIENT, 1, 1);
            }
/*            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag targetTag = stack.serializeNBT();

            ItemStack glumbis = new ItemStack(GlumbisItems.GLUMBIS.get());

            tag.put(DATA_CAT, targetTag);

            glumbis.setTag(tag);

            target.discard();

            player.setItemInHand(hand, glumbis);*/
        }
    }

    @SubscribeEvent
    public static void onPlayerSleep(PlayerWakeUpEvent event) {
        Player player = event.getPlayer();
        if (player.getItemBySlot(EquipmentSlot.FEET).is(GlumbisItems.SOGGY_SOCKS.get()) && player.getHealth() < player.getMaxHealth()) {
            player.heal(10);
        }
    }

    // Is there a worse way to do this?
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        final Predicate<LivingEntity> GLUMBIS_IN_PLAYERS_HOTBAR = (p_20440_) -> p_20440_ instanceof Player
                && p_20440_.getSlot(0).get().is(GlumbisItems.GLUMBIS.get())
                || p_20440_.getSlot(1).get().is(GlumbisItems.GLUMBIS.get())
                || p_20440_.getSlot(2).get().is(GlumbisItems.GLUMBIS.get())
                || p_20440_.getSlot(3).get().is(GlumbisItems.GLUMBIS.get())
                || p_20440_.getSlot(4).get().is(GlumbisItems.GLUMBIS.get())
                || p_20440_.getSlot(5).get().is(GlumbisItems.GLUMBIS.get())
                || p_20440_.getSlot(6).get().is(GlumbisItems.GLUMBIS.get())
                || p_20440_.getSlot(7).get().is(GlumbisItems.GLUMBIS.get())
                || p_20440_.getSlot(8).get().is(GlumbisItems.GLUMBIS.get());

        Entity entity = event.getEntity();

        if (entity instanceof Creeper creeper) {
            creeper.goalSelector.addGoal(0, new AvoidEntityGoal<>(creeper, Player.class, 12.0F, 1.0D, 1.2D, GLUMBIS_IN_PLAYERS_HOTBAR));
        }
    }
}
