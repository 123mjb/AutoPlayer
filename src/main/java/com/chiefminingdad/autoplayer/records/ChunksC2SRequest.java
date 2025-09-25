package com.chiefminingdad.autoplayer.records;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChunksC2SRequest(Integer x, Integer z) implements CustomPayload {
    public static final Identifier CHUNK_REQUEST_ID = Identifier.of("autoplayer:ChunkRequester");
    public static final CustomPayload.Id<ChunksC2SRequest> ID = new CustomPayload.Id<>(CHUNK_REQUEST_ID);
    public static final PacketCodec<RegistryByteBuf,ChunksC2SRequest> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER,ChunksC2SRequest::x,PacketCodecs.INTEGER,ChunksC2SRequest::z,ChunksC2SRequest::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
