package coda.glumbis.common.items;

import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.common.registry.GlumbisItems;
import coda.glumbis.common.registry.GlumbisSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

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

        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(itemstack, p_40672_, p_40673_, p_40674_, flag);
        if (ret != null) return ret;

        if (!p_40673_.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            p_40673_.startUsingItem(p_40674_);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public void releaseUsing(ItemStack p_40667_, Level level, LivingEntity livingEntity, int p_40670_) {
        if (livingEntity instanceof Player player) {
            ItemStack itemstack = player.getProjectile(p_40667_);

            int i = this.getUseDuration(p_40667_) - p_40670_;
            i = ForgeEventFactory.onArrowLoose(p_40667_, level, player, i, !itemstack.isEmpty());
            if (i < 0) return;

            if (!itemstack.isEmpty()) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(GlumbisItems.ROCKET_PROPELLED_GLUMP.get());
                }

                if (!level.isClientSide) {
                    RocketPropelledGlumpEntity glump = createRPG(level);
                    glump.moveTo(player.position().add(0.0d, 0.6d, 0.0d));
                    glump.owner = player;
                    glump.setOwner(livingEntity);
                    p_40667_.hurtAndBreak(1, player, (p_40665_) -> p_40665_.broadcastBreakEvent(player.getUsedItemHand()));
                    glump.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.4F, 1.0F);

                    level.addFreshEntity(glump);

                }

                level.playSound(null, player.getX(), player.getY(), player.getZ(), GlumbisSounds.GLUMP_FLY.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.getInventory().removeItem(itemstack);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public RocketPropelledGlumpEntity createRPG(Level level) {
        return new RocketPropelledGlumpEntity(GlumbisEntities.ROCKET_PROPELLED_GLUMP.get(), level);
    }
}