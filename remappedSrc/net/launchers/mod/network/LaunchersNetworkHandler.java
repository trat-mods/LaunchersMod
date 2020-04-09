package net.launchers.mod.network;

import net.launchers.mod.network.packet.CustomPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class LaunchersNetworkHandler
{
    public static void sendToAll(CustomPacket packet, PlayerManager manager)
    {
        List<ServerPlayerEntity> targets = manager.getPlayerList();
        for(int i = 0; i < targets.size(); ++i)
        {
            packet.sendTo(targets.get(i));
        }
    }
}
