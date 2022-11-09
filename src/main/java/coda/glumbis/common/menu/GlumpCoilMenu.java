package coda.glumbis.common.menu;

import coda.glumbis.common.blocks.entities.GlumpCoilBlockEntity;
import coda.glumbis.common.registry.GlumbisBlocks;
import coda.glumbis.common.registry.GlumbisMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class GlumpCoilMenu extends AbstractContainerMenu {
    public final GlumpCoilBlockEntity glumpCoilBlockEntity;
    private final ContainerLevelAccess access;

    public GlumpCoilMenu(final int windowId, final Inventory playerInventory, GlumpCoilBlockEntity blockEntity) {
        super(GlumbisMenus.GLUMP_COIL.get(), windowId);
        this.glumpCoilBlockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(glumpCoilBlockEntity.getLevel()), glumpCoilBlockEntity.getBlockPos());

        this.addSlot(new Slot(blockEntity, 0, 27, 47));
        this.addSlot(new Slot(blockEntity, 0, 76, 47)); // todo - make this slot 'Cat Essence' only
        this.addSlot(new Slot(blockEntity, 0, 134, 47)); // todo - make this a result slot only

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public ItemStack quickMoveStack(Player p_38986_, int p_38987_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_38987_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_38987_ == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (p_38987_ != 1 && p_38987_ != 0) {
/*                if (this.canSmelt(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else */if (p_38987_ >= 3 && p_38987_ < 30) {
                    if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (p_38987_ >= 30 && p_38987_ < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(p_38986_, itemstack1);
        }

        return itemstack;
    }


    public GlumpCoilMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    private static GlumpCoilBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "Player Inventory cannot be null");
        Objects.requireNonNull(data, "Packet Buffer cannot be null");
        final BlockEntity blockEntity = playerInventory.player.level.getBlockEntity(data.readBlockPos());

        if (blockEntity instanceof GlumpCoilBlockEntity coil) {
            return coil;
        }

        throw new IllegalStateException("Block entity is not correct");
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, GlumbisBlocks.GLUMP_COIL.get());
    }
}
