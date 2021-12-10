package coda.glumbis.common.capabilities;

import coda.glumbis.Glumbis;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class CapabilityAttacher implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final ICatnipCapability catnipCapability = new ICatnipCapability.Catnip();

    private final LazyOptional<ICatnipCapability> catOptional = LazyOptional.of(() -> catnipCapability);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        return capability == Glumbis.ENTITY_CATNIP_CAPABILITY ? catOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return catnipCapability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        catnipCapability.deserializeNBT(nbt);
    }

    void invalidate() {
        catOptional.invalidate();
    }
}
