package coda.glumbis.fabric;

import coda.glumbis.fabriclike.GlumbisFabricLike;
import net.fabricmc.api.ModInitializer;

public class GlumbisFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GlumbisFabricLike.init();
    }
}
