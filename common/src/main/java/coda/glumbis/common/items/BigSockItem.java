package coda.glumbis.common.items;

import coda.glumbis.common.entities.BigSockEntity;
import coda.glumbis.common.registry.GlumbisEntities;
import coda.glumbis.common.registry.GlumbisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BigSockItem extends Item {

    public BigSockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel)) {
            System.out.println(world.isClientSide());
            return InteractionResult.SUCCESS;
        } else if (context.getItemInHand().getItem().equals(GlumbisItems.BIG_SOCK.get())) {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            ItemStack stack = context.getItemInHand();
            BigSockEntity entity = GlumbisEntities.BIG_SOCK.get().create(context.getLevel());
            if (entity == null) return InteractionResult.FAIL;

            entity.moveTo(blockpos1.getX() + 0.5, blockpos1.getY(), blockpos1.getZ() + 0.5, context.getPlayer().yRotO, 0f);
            System.out.println(entity.position());

            if (stack.hasCustomHoverName()) entity.setCustomName(stack.getHoverName());

            if (context.getLevel().addFreshEntity(entity) && !context.getPlayer().isCreative()) {
                itemstack.shrink(1);
            }
            context.getLevel().playSound(null, entity.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 1, 1);

            return InteractionResult.CONSUME;
        }
        else {
            return super.useOn(context);
        }
    }
}
