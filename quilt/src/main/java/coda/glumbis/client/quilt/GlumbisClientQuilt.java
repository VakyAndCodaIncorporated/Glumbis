package coda.glumbis.client.quilt;

import coda.glumbis.client.fabriclike.GlumbisClientFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class GlumbisClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        GlumbisClientFabricLike.clientInit();
    }
}
