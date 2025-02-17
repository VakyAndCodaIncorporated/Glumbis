package coda.glumbis.common.blocks;

import coda.glumbis.common.blocks.entities.GlumpCoilBlockEntity;
import coda.glumbis.common.registry.GlumbisBlockEntities;
import coda.glumbis.common.registry.GlumbisParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class GlumpCoilBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Block.box(2.5D, 0.0D, 2.5D, 13.5D, 10.0D, 13.5D);

    public GlumpCoilBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return GlumbisBlockEntities.GLUMP_COIL.get().create(pos, state);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> type) {
        return createGlumpCoilTicker(level, type, GlumbisBlockEntities.GLUMP_COIL.get());
    }

    public static <T extends BlockEntity> BlockEntityTicker<T> createGlumpCoilTicker(Level p_151988_, BlockEntityType<T> p_151989_, BlockEntityType<? extends GlumpCoilBlockEntity> p_151990_) {
        return createTickerHelper(p_151989_, p_151990_, GlumpCoilBlockEntity::serverTick);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (level.getBlockEntity(pos) instanceof GlumpCoilBlockEntity coil) {
            int i = (coil.energyLevel / 100);

            for (int j = 1; j < i; j++) {
                level.addParticle(GlumbisParticles.STATIC_LIGHTNING.get(), pos.getX() + 0.35D + random.nextDouble(0.25D), pos.getY() + 1.8D, pos.getZ() + 0.35D + random.nextDouble(0.25D), 0D, 0D, 0D);
            }

        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof GlumpCoilBlockEntity coil) {
                NetworkHooks.openGui((ServerPlayer) player, coil, pos);
            }
            return InteractionResult.CONSUME;
        }
        else {
            return InteractionResult.SUCCESS;
        }
    }

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof Container container) {
                Containers.dropContents(worldIn, pos, container);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }
}
