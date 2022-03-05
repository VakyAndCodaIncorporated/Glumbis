package coda.glumbis.common.items;

import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CatEssenceItem extends Item {

    public CatEssenceItem(Properties builder) {
        super(builder);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Vec3[] COLORS2 = Util.make(new Vec3[16], (p_154319_) -> {
            for(int i = 0; i <= 15; ++i) {
                float f = (float)i / 10.0F;
                float f1 = f * 0.2F + (f > 0.0F ? 0.7F : 0.5F);
                float f2 = Mth.clamp(0.7F - 0.2F, 0.0F, 0.5F);
                float f3 = Mth.clamp(0.6F + 0.2F, 0.9F, 1.4F);
                p_154319_[i] = new Vec3(f1, f2, f3);
            }
        });

        if (entity != null) {
            entity.setNoGravity(true);
            //entity.setExtendedLifetime();
            for (int j = 0; j < 10; j++) {
                entity.level.addParticle(new DustParticleOptions(new Vector3f(COLORS2[j]), 1.0F), entity.getX(), entity.getY() + 0.1F, entity.getZ(), 0, 0, 0);
            }

            Vec3 vec3 = entity.getDeltaMovement();
            if (!entity.level.isClientSide) {
                entity.setDeltaMovement(vec3);
            }
        }
        return false;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level world) {
        return 12000;
    }
}
