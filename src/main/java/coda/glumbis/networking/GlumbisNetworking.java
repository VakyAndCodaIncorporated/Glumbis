package coda.glumbis.networking;

import coda.glumbis.Glumbis;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class GlumbisNetworking {
    public static final String NETWORK_VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Glumbis.MOD_ID, "network"), () -> NETWORK_VERSION, version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init() {
        CHANNEL.registerMessage(0,  InputMessage.class, InputMessage::encode, InputMessage::decode, InputMessage::handle);
    }
}
