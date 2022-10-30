package coda.glumbis.quilt;

import coda.glumbis.fabriclike.GlumbisFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class GlumbisQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        GlumbisFabricLike.init();
    }
}
