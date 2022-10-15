package coda.glumbis.common.items;

import coda.glumbis.PlatformLayer;
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

import java.util.function.Predicate;

public class GlumpCannonItem extends ProjectileWeaponItem {
    public static final Predicate<ItemStack> RPG_ONLY = (p_43017_) -> p_43017_.is(GlumbisItems.ROCKET_PROPELLED_GLUMP.get());

    public GlumpCannonItem(Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return RPG_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 20;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean flag = !player.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = PlatformLayer.onArrowNock(itemstack, level, player, hand, flag);
        if (ret != null) return ret;

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);

            int i = getAdjustedUseDuration(stack, timeLeft);
            i = PlatformLayer.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
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
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
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

    // Forge overrides this to use the bow hooks
    protected int getAdjustedUseDuration(ItemStack stack, int timeLeft) {
        return this.getUseDuration(stack) - timeLeft;
    }

    public RocketPropelledGlumpEntity createRPG(Level level) {
        return new RocketPropelledGlumpEntity(GlumbisEntities.ROCKET_PROPELLED_GLUMP.get(), level);
    }
}