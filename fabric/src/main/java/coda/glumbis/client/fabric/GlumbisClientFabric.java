package coda.glumbis.client.fabric;

import coda.glumbis.client.fabriclike.GlumbisClientFabricLike;
import net.fabricmc.api.ClientModInitializer;

public class GlumbisClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GlumbisClientFabricLike.clientInit();
    }
}
