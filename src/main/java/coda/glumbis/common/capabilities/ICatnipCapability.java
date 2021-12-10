package coda.glumbis.common.capabilities;

import coda.glumbis.Glumbis;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.Cat;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

//IItemHandler
public interface ICatnipCapability extends INBTSerializable<CompoundTag> {

    void setHasCatnip(boolean hasCatnip);

    boolean getHasCatnip();

    class Catnip implements ICatnipCapability {
        boolean hasCatnip = false;

        @Override
        public void setHasCatnip(boolean hasCatnip) {
            this.hasCatnip = hasCatnip;
        }

        @Override
        public boolean getHasCatnip() {
            return hasCatnip;
        }

        public static Catnip get(Cat cat) {
            return cat.getCapability(Glumbis.ENTITY_CATNIP_CAPABILITY).orElseThrow(RuntimeException::new);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();

            tag.putBoolean("hasCatnip", getHasCatnip());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            tag.getBoolean("hasCatnip");
        }
    }
}
