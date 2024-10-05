package io.github.ageuxo.FancyDoorMod.network;

import io.github.ageuxo.FancyDoorMod.gui.DetectorScreen;
import io.github.ageuxo.FancyDoorMod.network.packet.S2CDetectorPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handlePacket(S2CDetectorPacket packet, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().setScreen(
                new DetectorScreen(packet.pos(),
                        packet.xValue(), packet.xMin(), packet.xMax(),
                        packet.yValue(), packet.yMin(), packet.yMax(),
                        packet.zValue(), packet.zMin(), packet.zMax()
                )
        );
    }
}
