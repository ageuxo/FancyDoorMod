package io.github.ageuxo.FancyDoorMod.network.packet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.FancyDoorMod.network.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record S2CDetectorPacket(BlockPos pos,
                                int xValue, int xMin, int xMax,
                                int yValue, int yMin, int yMax,
                                int zValue, int zMin, int zMax,
                                List<String> filters) {
    public static final Codec<S2CDetectorPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(S2CDetectorPacket::pos),
            Codec.INT.fieldOf("xValue").forGetter(S2CDetectorPacket::xValue),
            Codec.INT.fieldOf("xMin").forGetter(S2CDetectorPacket::xMin),
            Codec.INT.fieldOf("xMax").forGetter(S2CDetectorPacket::xMax),
            Codec.INT.fieldOf("yValue").forGetter(S2CDetectorPacket::yValue),
            Codec.INT.fieldOf("yMin").forGetter(S2CDetectorPacket::yMin),
            Codec.INT.fieldOf("yMax").forGetter(S2CDetectorPacket::yMax),
            Codec.INT.fieldOf("zValue").forGetter(S2CDetectorPacket::zValue),
            Codec.INT.fieldOf("zMin").forGetter(S2CDetectorPacket::zMin),
            Codec.INT.fieldOf("zMax").forGetter(S2CDetectorPacket::zMax),
            Codec.STRING.listOf().optionalFieldOf("filters", new ArrayList<>()).forGetter(S2CDetectorPacket::filters)
    ).apply(instance, S2CDetectorPacket::new));

    public static void encode(S2CDetectorPacket packet, FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(CODEC, packet);
    }

    public static S2CDetectorPacket decode(FriendlyByteBuf buf) {
        return buf.readJsonWithCodec(CODEC);
    }

    public static void handle(S2CDetectorPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(
                ()-> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                        ()-> ()-> ClientPacketHandler.handlePacket(packet, ctx))
        );
        ctx.get().setPacketHandled(true);
    }
}
