package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class GlumbisSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Glumbis.MOD_ID);

    public static final RegistryObject<SoundEvent> GLUMBOSS_AMBIENT = SOUNDS.register("glumboss.ambient", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.ambient")));
    public static final RegistryObject<SoundEvent> GLUMBOSS_HURT = SOUNDS.register("glumboss.hurt", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.hurt")));
    public static final RegistryObject<SoundEvent> GLUMBOSS_DEATH = SOUNDS.register("glumboss.death", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.death")));
    public static final RegistryObject<SoundEvent> GLUMBOSS_CHARGE = SOUNDS.register("glumboss.charge", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.charge")));
    public static final RegistryObject<SoundEvent> GLUMBOSS_SLAM = SOUNDS.register("glumboss.slam", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.slam")));

    public static final RegistryObject<SoundEvent> GLUMP_FLY = SOUNDS.register("glump.fly", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glump.fly")));
    public static final RegistryObject<SoundEvent> GLUMP_HURT = SOUNDS.register("glump.hurt", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glump.hurt")));
    public static final RegistryObject<SoundEvent> GLUMP_EXPLODE = SOUNDS.register("glump.explode", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glump.explode")));
}
