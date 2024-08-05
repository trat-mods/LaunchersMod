package net.launchers.mod.network;

import net.launchers.mod.network.packet.UnboundedEntityVelocityPayload;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class NetworkHandler {
    public static void sendToAll(UnboundedEntityVelocityPayload packet, PlayerManager manager) {
        List<ServerPlayerEntity> targets = manager.getPlayerList();
        for (ServerPlayerEntity target : targets) {
            packet.sendTo(target);
        }
    }
}
