package coda.glumbis.common.items;

import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class CatEssenceItem extends Item {

    public CatEssenceItem(Properties builder) {
        super(builder);
    }

    // todo - figure out why cat essence despawns so fast and fix it
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
            for (int j = 0; j < 3; j++) {
                Random random = new Random();
                entity.level.addParticle(new DustParticleOptions(new Vector3f(COLORS2[j]), 1.0F), entity.getX() - 0.2d + (random.nextDouble()/2d), entity.getY() + (random.nextDouble() / 2.0d),  entity.getZ() - 0.2d + (random.nextDouble()/2d), 0, 0, 0);
            }

            Vec3 vec3 = entity.getDeltaMovement();
            if (!entity.level.isClientSide) {
                if(entity.tickCount > 60){
                entity.setDeltaMovement(vec3.subtract(0d, -0.3d, 0d));
                }
                else {
                    entity.setDeltaMovement(vec3);
                }
            }
        }
        return false;
    }
}
