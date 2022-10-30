package coda.glumbis.common;

import coda.glumbis.common.registry.GlumbisItems;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
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
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.function.Predicate;

import static coda.glumbis.common.items.GlumbisItem.DATA_CAT;

public class CommonEvents {
    private static EventResult onEntityInteract(Player player, Entity target, InteractionHand hand) {
        Level level = player.getLevel();
        ItemStack stack = player.getItemInHand(hand);
        Item heldItem = stack.getItem();

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

                CompoundTag targetTag = target.saveWithoutId(new CompoundTag());
                targetTag.putString("id", target.getEncodeId());
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

                return EventResult.interruptTrue();
            }
        }

        if (target instanceof Cat cat && heldItem == GlumbisItems.CATNIP.get()) {
            cat.playSound(SoundEvents.CAT_AMBIENT, 0.5F, 1.0F);

            if (cat.level.getServer() != null && cat.getRandom().nextFloat() > 0F && cat.getRandom().nextFloat() <= 0.33F) {
                List<ItemStack> items = cat.level.getServer().getLootTables().get(BuiltInLootTables.FISHING).getRandomItems(new LootContext.Builder((ServerLevel) cat.level).withRandom(cat.getRandom()).create(LootContextParamSets.EMPTY));
                Containers.dropContents(cat.level, cat.blockPosition(), NonNullList.of(ItemStack.EMPTY, items.toArray(new ItemStack[0])));
                cat.setDeltaMovement(0, 0, 0);
            }
            if (cat.getRandom().nextFloat() > 0.33F && cat.getRandom().nextFloat() <= 0.66F) {
                cat.setLying(true);
                cat.setOrderedToSit(true);
            }

            return EventResult.interruptTrue();
        }

        return EventResult.pass();
    }

    private static final Predicate<LivingEntity> GLUMBIS_IN_PLAYERS_HOTBAR = (entity) -> entity instanceof Player
            && entity.getSlot(0).get().is(GlumbisItems.GLUMBIS.get())
            || entity.getSlot(1).get().is(GlumbisItems.GLUMBIS.get())
            || entity.getSlot(2).get().is(GlumbisItems.GLUMBIS.get())
            || entity.getSlot(3).get().is(GlumbisItems.GLUMBIS.get())
            || entity.getSlot(4).get().is(GlumbisItems.GLUMBIS.get())
            || entity.getSlot(5).get().is(GlumbisItems.GLUMBIS.get())
            || entity.getSlot(6).get().is(GlumbisItems.GLUMBIS.get())
            || entity.getSlot(7).get().is(GlumbisItems.GLUMBIS.get())
            || entity.getSlot(8).get().is(GlumbisItems.GLUMBIS.get());

    private static EventResult onEntityAdd(Entity entity, Level world) {
        if (entity instanceof Creeper creeper) {
            creeper.goalSelector.addGoal(0, new AvoidEntityGoal<>(creeper, Player.class, 12.0F, 1.0D, 1.2D, GLUMBIS_IN_PLAYERS_HOTBAR));
        }
        return EventResult.pass();
    }

    /**
     * Triggered by Forge Event or Fabric/Quilt mixin.
     */
    public static void onPlayerWake(Player player) {
        if (player.getItemBySlot(EquipmentSlot.FEET).is(GlumbisItems.SOGGY_SOCKS.get()) && player.getHealth() < player.getMaxHealth()) {
            player.heal(10);
        }
    }

    /**
     * Register common events with Architectury's event system.
     */
    public static void init() {
        InteractionEvent.INTERACT_ENTITY.register(CommonEvents::onEntityInteract);
        EntityEvent.ADD.register(CommonEvents::onEntityAdd);
    }
}
