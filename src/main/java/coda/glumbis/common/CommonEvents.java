package coda.glumbis.common;

import coda.glumbis.Glumbis;
import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.common.registry.GlumbisItems;
import coda.glumbis.common.registry.GlumbisParticles;
import lain.mods.cos.api.CosArmorAPI;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;
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

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (ModList.get().isLoaded("cosmeticarmorreworked") && CosArmorAPI.getCAStacks(player.getUUID()).getStackInSlot(0).is(GlumbisItems.SOGGY_SOCKS.get())) {

            Random rand = new Random();
            Level world = player.getCommandSenderWorld();
            double d0 = rand.nextGaussian() * 0.056D;
            double d1 = rand.nextGaussian() * 0.034D;
            double d2 = rand.nextGaussian() * 0.025D;
            int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, CosArmorAPI.getCAStacks(player.getUUID()).getStackInSlot(0));

            if (level > 0) {
                for (int i = 1; i < level + 1; i++) {
                    world.addParticle(ParticleTypes.FALLING_WATER, player.getRandomX(0.25D), player.getY() + 0.15D, player.getRandomZ(0.25D), d0, d1, d2);
                    world.addParticle(ParticleTypes.FALLING_WATER, player.getRandomX(0.25D), player.getY() + 0.15D, player.getRandomZ(0.25D), d0, d1, d2);
                }
            }
            else {
                world.addParticle(ParticleTypes.FALLING_WATER, player.getRandomX(0.25D), player.getY() + 0.15D, player.getRandomZ(0.25D), d0, d1, d2);
                world.addParticle(ParticleTypes.FALLING_WATER, player.getRandomX(0.25D), player.getY() + 0.15D, player.getRandomZ(0.25D), d0, d1, d2);
            }
        }
    }
}
