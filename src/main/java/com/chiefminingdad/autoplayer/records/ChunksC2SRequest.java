package com.chiefminingdad.autoplayer.records;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ChunksC2SRequest(BlockPos p) implements CustomPayload {
    public static final Identifier CHUNK_REQUEST_ID = Identifier.of("autoplayer:chunkrequester");
    public static final CustomPayload.Id<ChunksC2SRequest> ID = new CustomPayload.Id<>(CHUNK_REQUEST_ID);
    public static final PacketCodec<RegistryByteBuf,ChunksC2SRequest> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC,ChunksC2SRequest::p,ChunksC2SRequest::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
