package coda.glumbis.networking;

import coda.glumbis.common.entities.BigSockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InputMessage {
    public int key;

    public InputMessage(int key) {
        this.key = key;
    }

    public static void encode(InputMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.key);
    }

    public static InputMessage decode(FriendlyByteBuf buffer) {
        return new InputMessage(buffer.readInt());
    }

    public static void handle(InputMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player player = context.getSender();

            if (player.getVehicle() instanceof BigSockEntity bigSock && bigSock.getControllingPassenger() == player && !bigSock.isOnGround() && bigSock.attackTick == 0 && !bigSock.isSlamming()) {
                bigSock.setSlamming(true);
                bigSock.setDeltaMovement(0, -0.5, 0);

                bigSock.distance = (float) bigSock.getY();
            }
        });
        context.setPacketHandled(true);
    }
}
