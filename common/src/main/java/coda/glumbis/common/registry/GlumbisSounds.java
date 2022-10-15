package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class GlumbisSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Glumbis.MOD_ID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> GLUMBOSS_AMBIENT = SOUNDS.register("glumboss.ambient", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.ambient")));
    public static final RegistrySupplier<SoundEvent> GLUMBOSS_HURT = SOUNDS.register("glumboss.hurt", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.hurt")));
    public static final RegistrySupplier<SoundEvent> GLUMBOSS_DEATH = SOUNDS.register("glumboss.death", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.death")));
    public static final RegistrySupplier<SoundEvent> GLUMBOSS_CHARGE = SOUNDS.register("glumboss.charge", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.charge")));
    public static final RegistrySupplier<SoundEvent> GLUMBOSS_SLAM = SOUNDS.register("glumboss.slam", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glumboss.slam")));

    public static final RegistrySupplier<SoundEvent> GLUMP_FLY = SOUNDS.register("glump.fly", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glump.fly")));
    public static final RegistrySupplier<SoundEvent> GLUMP_HURT = SOUNDS.register("glump.hurt", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glump.hurt")));
    public static final RegistrySupplier<SoundEvent> GLUMP_EXPLODE = SOUNDS.register("glump.explode", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "glump.explode")));

    public static final RegistrySupplier<SoundEvent> BIG_SOCK_JUMP = SOUNDS.register("big_sock.jump", () -> new SoundEvent(new ResourceLocation(Glumbis.MOD_ID, "big_sock.jump")));
}
