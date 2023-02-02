package coda.glumbis.common.blocks.entities;

import coda.glumbis.Glumbis;
import coda.glumbis.common.menu.GlumpCoilMenu;
import coda.glumbis.common.registry.GlumbisBlockEntities;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GlumpCoilBlockEntity extends BaseContainerBlockEntity implements IAnimatable, Tickable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    public int energyLevel = 0;

    public GlumpCoilBlockEntity(BlockPos pos, BlockState state) {
        super(GlumbisBlockEntities.GLUMP_COIL.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container." + Glumbis.MOD_ID + ".glump_coil");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new GlumpCoilMenu(id, player, this);
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return ContainerHelper.removeItem(this.items, p_18942_, p_18943_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return ContainerHelper.takeItem(this.items, p_18951_);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GlumpCoilBlockEntity coil) {
        if (coil.energyLevel < 400) {
            if (level.isThundering() && level.getGameTime() % 4 == 0) {
                coil.energyLevel += 2;
            }
        }

    }

    @Override
    public void tick() {
        if (energyLevel < 400) {
            if (level.isThundering() && level.getGameTime() % 4 == 0) {
                energyLevel += 2;
            }
        }
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return p_18946_.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        items.clear();
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
