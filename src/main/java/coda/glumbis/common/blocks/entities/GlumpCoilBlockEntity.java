package coda.glumbis.common.blocks.entities;

import coda.glumbis.Glumbis;
import coda.glumbis.common.menu.GlumpCoilMenu;
import coda.glumbis.common.registry.GlumbisBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GlumpCoilBlockEntity extends BlockEntity implements IAnimatable, MenuProvider, Nameable {
    private final AnimationFactory factory = new AnimationFactory(this);
    public int energyLevel = 0;

    public GlumpCoilBlockEntity(BlockPos pos, BlockState state) {
        super(GlumbisBlockEntities.GLUMP_COIL.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public Component getName() {
        return getDefaultName();
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container." + Glumbis.MOD_ID + ".glump_coil");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new GlumpCoilMenu(id, inv, this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GlumpCoilBlockEntity coil) {
        if (coil.energyLevel < 400) {
            if (level.isThundering() && level.getGameTime() % 4 == 0) {
                coil.energyLevel += 2;
            }

        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energyChanged(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("EnergyLevel", energyLevel);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void energyChanged(CompoundTag tag) {
        if (getLevel() != null && !getLevel().isClientSide()) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }

        energyLevel = tag.getInt("EnergyLevel");
    }

    @Override
    public void handleUpdateTag(final CompoundTag tag) {
        super.handleUpdateTag(tag);
        energyChanged(tag);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 2, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (energyLevel > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glump_coil.activated", true));
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.glump_coil.deactivated", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}
