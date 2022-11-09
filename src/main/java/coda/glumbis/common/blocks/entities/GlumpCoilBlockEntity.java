package coda.glumbis.common.blocks.entities;

import coda.glumbis.Glumbis;
import coda.glumbis.common.menu.GlumpCoilMenu;
import coda.glumbis.common.registry.GlumbisBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GlumpCoilBlockEntity extends BaseContainerBlockEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

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
        ItemStack itemstack = this.items.get(slot);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
        this.items.set(slot, stack);

        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (slot == 0 && !flag) {
            //this.cookingTotalTime = getTotalCookTime(this.level, this.recipeType, this);
            //this.cookingProgress = 0;
            //this.setChanged();
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

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
