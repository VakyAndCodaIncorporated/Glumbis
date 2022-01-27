package coda.glumbis.common.items;

import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import coda.glumbis.common.registry.GlumbisItems;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class GlumpCannonItem extends ProjectileWeaponItem {
    public static final Predicate<ItemStack> RPG_ONLY = (p_43017_) -> p_43017_.is(GlumbisItems.ROCKET_PROPELLED_GLUMP.get());

    public GlumpCannonItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return RPG_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 20;
    }

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        boolean flag = !p_40673_.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, p_40672_, p_40673_, p_40674_, flag);
        if (ret != null) return ret;

        if (!p_40673_.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            p_40673_.startUsingItem(p_40674_);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public void releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
        if (p_40669_ instanceof Player) {
            Player player = (Player) p_40669_;
            ItemStack itemstack = player.getProjectile(p_40667_);

            int i = this.getUseDuration(p_40667_) - p_40670_;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(p_40667_, p_40668_, player, i, !itemstack.isEmpty());
            if (i < 0) return;

            if (!itemstack.isEmpty()) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(GlumbisItems.ROCKET_PROPELLED_GLUMP.get());
                }

                boolean flag1 = player.getAbilities().instabuild || itemstack.getItem() instanceof RocketPropelledGlumpItem;
                if (!p_40668_.isClientSide) {
                    RocketPropelledGlumpItem glumpItem = (RocketPropelledGlumpItem) (itemstack.getItem() instanceof RocketPropelledGlumpItem ? itemstack.getItem() : GlumbisItems.ROCKET_PROPELLED_GLUMP.get());
                    RocketPropelledGlumpEntity glump = glumpItem.createRPG(p_40668_, player);
                    glump.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);

                    p_40667_.hurtAndBreak(1, player, (p_40665_) -> p_40665_.broadcastBreakEvent(player.getUsedItemHand()));

                    p_40668_.addFreshEntity(glump);
                }

                p_40668_.playSound(null, player.getX(), player.getY(), player.getZ(), GlumbisSounds.GLUMP_FLY.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (p_40668_.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                if (!flag1 && !player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.getInventory().removeItem(itemstack);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }
}