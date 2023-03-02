package coda.glumbis.common.menu;

import coda.glumbis.common.blocks.entities.GlumpCoilBlockEntity;
import coda.glumbis.common.registry.GlumbisBlocks;
import coda.glumbis.common.registry.GlumbisItems;
import coda.glumbis.common.registry.GlumbisMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class GlumpCoilMenu extends AbstractContainerMenu {
    public final GlumpCoilBlockEntity glumpCoilBlockEntity;
    private final ContainerLevelAccess access;
    private final ResultContainer resultSlots = new ResultContainer();
    private final Container inputSlots = new SimpleContainer(2) {
        public void setChanged() {
            super.setChanged();
            GlumpCoilMenu.this.slotsChanged(this);
        }
    };

    // todo - fix the coil depleting energy when the chunk is unloaded (needs more testing)
    public GlumpCoilMenu(final int windowId, final Inventory playerInventory, GlumpCoilBlockEntity blockEntity) {
        super(GlumbisMenus.GLUMP_COIL.get(), windowId);
        this.glumpCoilBlockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(glumpCoilBlockEntity.getLevel()), glumpCoilBlockEntity.getBlockPos());

        this.addSlot(new Slot(inputSlots, 0, 27, 47) {

            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                GlumpCoilMenu.this.onTakeGear();
            }
        });
        this.addSlot(new Slot(inputSlots, 1, 76, 47) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(GlumbisItems.CAT_ESSENCE.get());
            }

            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                GlumpCoilMenu.this.onTakeEssence();
            }
        });
        this.addSlot(new Slot(blockEntity, 2, 134, 47) {

            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public boolean mayPickup(Player player) {
                return true;
            }

            public void onTake(Player player, ItemStack stack) {
                GlumpCoilMenu.this.onTake(stack);
            }

            @Override
            public void onQuickCraft(ItemStack p_40235_, ItemStack p_40236_) {
                energize(p_40236_);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public GlumpCoilMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    private void onTake(ItemStack stack) {
        this.shrinkStackInSlot(0);
        this.shrinkStackInSlot(1);

        ItemStack gearItem = resultSlots.getItem(0);

        energize(stack);
    }

    @Override
    public void removed(Player p_39790_) {
        super.removed(p_39790_);
        this.access.execute((p_39796_, p_39797_) -> {
            this.clearContainer(p_39790_, this.inputSlots);
        });
    }

    private void onTakeGear() {
        this.resultSlots.getItem(0).shrink(1);
        //this.shrinkStackInSlot(2);
    }

    private void onTakeEssence() {
        this.resultSlots.getItem(0).shrink(1);
        //this.shrinkStackInSlot(2);
    }

    private void shrinkStackInSlot(int p_40271_) {
        ItemStack itemstack = this.slots.get(p_40271_).getItem();
        itemstack.shrink(1);
        //this.slots.get(p_40271_).container.setItem(p_40271_, itemstack);
    }

    private boolean canBeEnergized(ItemStack stack) {
        return stack.getItem() instanceof SwordItem; // todo - make this work with all gear
    }

    public void slotsChanged(Container p_39778_) {
        super.slotsChanged(p_39778_);
        if (p_39778_ == inputSlots) {
            this.createResult();
        }
    }

    public void createResult() {
        ItemStack gearItem = getSlot(0).getItem();
        ItemStack essenceItem = getSlot(1).getItem();

        if (glumpCoilBlockEntity.energyLevel > 0 || !gearItem.isEmpty() || !essenceItem.isEmpty() || (gearItem.getOrCreateTag().get("Energized") == null && gearItem.getOrCreateTag().getInt("Energized") < 100)) {
            glumpCoilBlockEntity.setItem(2, gearItem);
        }

    }

    private void energize(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        int currentEnergy = 0;

        if (tag.get("Energized") != null) {
            currentEnergy = tag.getInt("Energized");
        }
        else {
            tag.putInt("Energized", 0);
        }

        int energyLevel = glumpCoilBlockEntity.energyLevel;
        int energyNeeded = 100 - currentEnergy;
        int energyUsed = Math.min(energyLevel, energyNeeded);

        glumpCoilBlockEntity.energyLevel = energyLevel - energyUsed;

        if (tag.get("Energized") == null) {
            tag.putInt("Energized", tag.getInt("Energized") + energyUsed);
        }
        else if (tag.getInt("Energized") < 100) {
            tag.putInt("Energized", tag.getInt("Energized") + energyUsed);
        }

    }

    // todo - fix quick crafting not copying/saving tags
    public ItemStack quickMoveStack(Player p_39792_, int p_39793_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_39793_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_39793_ == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (p_39793_ != 0 && p_39793_ != 1) {
                if (p_39793_ >= 3 && p_39793_ < 39) {
                    if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    }
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

            slot.onTake(p_39792_, itemstack1);
        }

        return itemstack;
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
