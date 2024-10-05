package io.github.ageuxo.FancyDoorMod.network;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.network.packet.C2SDetectorPacket;
import io.github.ageuxo.FancyDoorMod.network.packet.S2CDetectorPacket;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetRegistry {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            FancyDoorsMod.modRL("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;

        INSTANCE.registerMessage(id++, S2CDetectorPacket.class, S2CDetectorPacket::encode, S2CDetectorPacket::decode, S2CDetectorPacket::handle);
        INSTANCE.registerMessage(id++, C2SDetectorPacket.class, C2SDetectorPacket::encode, C2SDetectorPacket::decode, C2SDetectorPacket::handle);
    }

}
