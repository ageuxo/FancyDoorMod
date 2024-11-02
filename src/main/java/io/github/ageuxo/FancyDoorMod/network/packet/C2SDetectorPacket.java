package io.github.ageuxo.FancyDoorMod.network.packet;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.FancyDoorMod.block.entity.DetectorBlockEntity;
import io.github.ageuxo.FancyDoorMod.network.CollectionDiff;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public record C2SDetectorPacket(BlockPos pos, int x, int y, int z, CollectionDiff<String> diff) {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Codec<C2SDetectorPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(C2SDetectorPacket::pos),
            Codec.INT.fieldOf("x").forGetter(C2SDetectorPacket::x),
            Codec.INT.fieldOf("y").forGetter(C2SDetectorPacket::y),
            Codec.INT.fieldOf("z").forGetter(C2SDetectorPacket::z),
            CollectionDiff.STRING.fieldOf("diff").forGetter(C2SDetectorPacket::diff)
    ).apply(instance, C2SDetectorPacket::new));

    public static void encode(C2SDetectorPacket packet, FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(CODEC, packet);
    }

    public static C2SDetectorPacket decode(FriendlyByteBuf buf) {
        return buf.readJsonWithCodec(CODEC);
    }

    public static void handle(C2SDetectorPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER){
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();

                if (player != null) {
                    BlockPos blockPos = packet.pos;
                    double distance = player.position().distanceTo(blockPos.getCenter());
                    if (distance < 8) {
                        if (player.level().isLoaded(blockPos) && player.level().getBlockEntity(blockPos) instanceof DetectorBlockEntity detector) {
                            detector.setFromPacket(packet);
                        }
                    }
                }
            });
        } else {
            LOGGER.error("Received C2SDetectorPacket on wrong side");
        }
    }
}
