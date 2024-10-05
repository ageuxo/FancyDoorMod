package io.github.ageuxo.FancyDoorMod.network.packet;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.FancyDoorMod.block.entity.DetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public record C2SDetectorPacket(BlockPos pos, int x, int y, int z) {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Codec<C2SDetectorPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(C2SDetectorPacket::pos),
            Codec.INT.fieldOf("x").forGetter(C2SDetectorPacket::x),
            Codec.INT.fieldOf("y").forGetter(C2SDetectorPacket::y),
            Codec.INT.fieldOf("z").forGetter(C2SDetectorPacket::z)
    ).apply(instance, C2SDetectorPacket::new));

    public static void encode(C2SDetectorPacket packet, FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(CODEC, packet);
    }

    public static C2SDetectorPacket decode(FriendlyByteBuf buf) {
        return buf.readJsonWithCodec(CODEC);
    }

    public static void handle(C2SDetectorPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(()-> {
            ServerPlayer player = ctx.get().getSender();

            if (player != null){
                BlockPos blockPos = packet.pos;
                double distance = player.position().distanceTo(blockPos.getCenter());
                LOGGER.debug("{} less than 8? {}", distance, distance < 8);
                if (distance < 8) {
                    if (player.level().isLoaded(blockPos) && player.level().getBlockEntity(blockPos) instanceof DetectorBlockEntity detector) {
                        LOGGER.debug("Set from packet");
                        detector.setFromPacket(packet);
                    }
                }
            }
        });
    }
}
