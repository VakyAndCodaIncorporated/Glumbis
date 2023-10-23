package coda.glumbis.common.menu;

import coda.glumbis.common.blocks.entities.GlumpCoilBlockEntity;
import coda.glumbis.common.registry.GlumbisBlocks;
import coda.glumbis.common.registry.GlumbisItems;
import coda.glumbis.common.registry.GlumbisMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class GlumpCoilMenu extends AbstractContainerMenu {
    public final GlumpCoilBlockEntity glumpCoilBlockEntity;
    private final ContainerLevelAccess access;
    public static final String ENERGIZED = "Energized";
    private final ResultContainer resultSlots = new ResultContainer();
    private final Container inputSlots = new SimpleContainer(2) {
        public void setChanged() {
            super.setChanged();
            GlumpCoilMenu.this.slotsChanged(this);
        }
    };

    // todo - fix the coil depleting energy when the chunk is unloaded (needs more testing)
    // todo - fix the coil disregarding essence (or lack of) when energizing
    // todo - fix cat essence item consumption (should be 1 consumed for every 50% energy)
    public GlumpCoilMenu(final int windowId, final Inventory playerInventory, GlumpCoilBlockEntity blockEntity) {
        super(GlumbisMenus.GLUMP_COIL.get(), windowId);
        this.glumpCoilBlockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(glumpCoilBlockEntity.getLevel()), glumpCoilBlockEntity.getBlockPos());

        this.addSlot(new Slot(inputSlots, 0, 27, 47) {

            @Override
            public boolean mayPlace(ItemStack p_40231_) {
                return canBeEnergized(p_40231_);
            }

            @Override
            public void onTake(Player p_150645_, ItemStack p_150646_) {
                GlumpCoilMenu.this.onTakeGear();
                super.onTake(p_150645_, p_150646_);
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
                super.onTake(p_150645_, p_150646_);
            }
        });
        this.addSlot(new Slot(resultSlots, 2, 134, 47) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return GlumpCoilMenu.this.mayPickup();
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                GlumpCoilMenu.this.onTake(stack);
                super.onTake(player, stack);
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

        if (canEnergize(stack)) {
            energize(stack, true);
        }
    }

    private boolean canEnergize(ItemStack stack) {
        return glumpCoilBlockEntity.energyLevel > 0 && stack.getOrCreateTag().getInt(ENERGIZED) < 100;
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
    }

    private void onTakeEssence() {
        this.resultSlots.getItem(0).shrink(1);
    }

    private void shrinkStackInSlot(int p_40271_) {
        ItemStack itemstack = this.slots.get(p_40271_).getItem();
        itemstack.shrink(1);
    }

    // todo - add a tag for tools that can be energized so people can add mod compat if needed
    private boolean canBeEnergized(ItemStack stack) {
        return stack.getItem() instanceof TieredItem || stack.getItem() instanceof ArmorItem;
    }

    public void slotsChanged(Container p_39778_) {
        super.slotsChanged(p_39778_);
        if (p_39778_ == inputSlots && !inputSlots.getItem(1).isEmpty() && inputSlots.getItem(1).is(GlumbisItems.CAT_ESSENCE.get())) {
            this.createResult();
        }
        else if (inputSlots.getItem(1).isEmpty()) {
            resultSlots.clearContent();
        }
    }

    private boolean mayPickup() {
        return !inputSlots.getItem(0).isEmpty() && inputSlots.getItem(1).is(GlumbisItems.CAT_ESSENCE.get()) && canBeEnergized(inputSlots.getItem(0));
    }

    public void createResult() {
        ItemStack gearItem = getSlot(0).getItem();
        ItemStack essenceItem = getSlot(1).getItem();

        if (glumpCoilBlockEntity.energyLevel > 0 || !gearItem.isEmpty() || !essenceItem.isEmpty() || (gearItem.getOrCreateTag().contains(ENERGIZED) && gearItem.getOrCreateTag().getInt(ENERGIZED) < 100)) {

            if (!gearItem.isEmpty()) {
                ItemStack preview = gearItem.copy();

                energize(preview, false);
                resultSlots.setItem(0, preview);
                this.broadcastChanges();
            }
        }
    }

    private void energize(ItemStack stack, boolean depleteEnergy) {
        CompoundTag tag = stack.getOrCreateTag();

        int currentEnergy = 0;

        if (tag.contains(ENERGIZED)) {
            currentEnergy = tag.getInt(ENERGIZED);
        }
        else {
            tag.putString("CachedName", stack.getHoverName().getString());
            tag.putInt(ENERGIZED, 0);
        }

        int energyLevel = glumpCoilBlockEntity.getEnergyLevel();
        int energyNeeded = 100 - currentEnergy;
        int energyUsed = Math.min(energyLevel, energyNeeded);

        if (depleteEnergy) {
            glumpCoilBlockEntity.setEnergylevel(glumpCoilBlockEntity.getEnergyLevel() - energyUsed);
        }

        if (!tag.contains(ENERGIZED)) {
            tag.putInt(ENERGIZED, tag.getInt(ENERGIZED) + energyUsed);
        } else if (tag.getInt(ENERGIZED) < 100) {
            tag.putInt(ENERGIZED, tag.getInt(ENERGIZED) + energyUsed);
        }

        Component name = tag.contains("CachedName") ? new TextComponent(tag.getString("CachedName")) : stack.getItem().getName(stack);
        stack.setHoverName(new TranslatableComponent("gear.glumbis.energized").append(name).withStyle(Style.EMPTY.withColor(0x9eb8ff).withItalic(false)));
    }

    private void showEnergizePreview(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        int currentEnergy = 0;

        if (tag.contains(ENERGIZED)) {
            currentEnergy = tag.getInt(ENERGIZED);
        }
        else {
            tag.putString("CachedName", stack.getHoverName().getString());
            tag.putInt(ENERGIZED, 0);
        }

        int energyLevel = glumpCoilBlockEntity.energyLevel;
        int energyNeeded = 100 - currentEnergy;
        int energyUsed = Math.min(energyLevel, energyNeeded);

        if (!tag.contains(ENERGIZED)) {
            tag.putInt(ENERGIZED, tag.getInt(ENERGIZED) + energyUsed);
        } else if (tag.getInt(ENERGIZED) < 100) {
            tag.putInt(ENERGIZED, tag.getInt(ENERGIZED) + energyUsed);
        }

        Component name = tag.contains("CachedName") ? new TextComponent(tag.getString("CachedName")) : stack.getItem().getName(stack);
        stack.setHoverName(new TranslatableComponent("gear.glumbis.energized").append(name).withStyle(Style.EMPTY.withColor(0x9eb8ff).withItalic(false)));
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 2) {

                energize(itemstack1, true);

                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index != 0 && index != 1) {
                if (index >= 3 && index < 39) {
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

            slot.onTake(player, itemstack1);
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
