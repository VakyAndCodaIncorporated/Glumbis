package coda.glumbis.common.menu;

import coda.glumbis.common.blocks.entities.GlumpCoilBlockEntity;
import coda.glumbis.common.registry.GlumbisBlocks;
import coda.glumbis.common.registry.GlumbisMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class GlumpCoilMenu extends AbstractContainerMenu {
    public final GlumpCoilBlockEntity glumpCoilBlockEntity;
    private final ContainerLevelAccess access;

    public GlumpCoilMenu(final int windowId, final Inventory playerInventory, GlumpCoilBlockEntity blockEntity) {
        super(GlumbisMenus.GLUMP_COIL.get(), windowId);
        this.glumpCoilBlockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(Objects.requireNonNull(glumpCoilBlockEntity.getLevel()), glumpCoilBlockEntity.getBlockPos());
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
