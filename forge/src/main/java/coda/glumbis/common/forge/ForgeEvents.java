package coda.glumbis.common.forge;

import coda.glumbis.Glumbis;
import coda.glumbis.common.CommonEvents;
import coda.glumbis.common.registry.GlumbisItems;
import lain.mods.cos.api.CosArmorAPI;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = Glumbis.MOD_ID)
public class ForgeEvents {

    @SubscribeEvent
    public static void onPlayerSleep(PlayerWakeUpEvent event) {
        CommonEvents.onPlayerWake(event.getEntity());
    }

    // TODO: When you move this to architectury, you want to use either TickEvent.PLAYER_PRE or TickEvent.PLAYER_POST.
    /*@SubscribeEvent
    public static void energizedGear(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        InteractionHand hand = event.player.getUsedItemHand();
        ItemStack stack = player.getItemInHand(hand);
        Level level = player.level;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack armor = player.getItemBySlot(slot);

            if (slot.getType().equals(EquipmentSlot.Type.ARMOR)) {
                CompoundTag tag = armor.getOrCreateTag();
                tag.putInt("Energized", 100);

                if (armor.getTag().get("Energized") != null && armor.getTag().getInt("Energized") > 0) {

                    armor.setHoverName(new TranslatableComponent("gear.glumbis.energized").append(armor.getItem().getName(armor)).withStyle(Style.EMPTY.withColor(0x9eb8ff).withItalic(false)));

                    // todo - make the particles only render to others players & if the player is in first person. we cant get the camera from the player, so idk what to do
                    //if (!Minecraft.getInstance().options.getCameraType().isFirstPerson())

                    float armorAmount = player.getArmorCoverPercentage();
                    float particleAmount = armorAmount * 2;

                    if (armorAmount >= 0.25F) {
                        if (player.tickCount % 40 == 0 && player.getRandom().nextFloat() < particleAmount) {
                            int i = slot.getIndex();

                            level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), player.getRandomX(1), player.getY() + (i * 0.4) + 0.25, player.getRandomZ(1), 0, 0.05, 0);

                            *//*System.out.println(i);
                            System.out.println("Armor Amount = " + armorAmount);
                            System.out.println("Particle Amount = " + particleAmount);
                            System.out.println("Did check pass? " + (player.getRandom().nextFloat() < particleAmount ? "Yes." : "No."));*//*
                        }
                    }
                }
            }
        }

        if (stack.is(Items.NETHERITE_SWORD)) {
            CompoundTag tag = stack.getOrCreateTag();

            // todo - needs a (boolean) tag for if its energized AND the energized amount
            // todo - also i think the energized amount should only go down when you attack / take damage (armor), because otherwise the item pops up and down when the tag changes
            tag.putInt("Energized", 100);

            if (stack.getTag().get("Energized") != null && stack.getTag().getInt("Energized") > 0) {

                stack.setHoverName(new TranslatableComponent("gear.glumbis.energized").append(stack.getItem().getName(stack)).withStyle(Style.EMPTY.withColor(0x9eb8ff).withItalic(false)));

                // account for skin customizability & camera mode
                boolean camera = Minecraft.getInstance().options.getCameraType().isFirstPerson();
                Vec3 pos = new Vec3(player.getMainArm() == HumanoidArm.LEFT ? 0.3 : -0.3, 0.95, camera ? 0.2 : 1.0).yRot(-player.yBodyRot * ((float) Math.PI / 180f)).add(player.getX(), player.getY(), player.getZ());

                // todo - make the particles only render to others players & if the player is in first person. we cant get the camera from the player, so idk what to do
                //if (!Minecraft.getInstance().options.getCameraType().isFirstPerson())
                if (player.tickCount % 40 == 0) {
                    level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), pos.x(), pos.y(), pos.z(), 0, 0.05, 0);
                }
            }
        }
    }*/

    // Forge only, cosmetic armor isn't on fabric.
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
