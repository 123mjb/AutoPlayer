package com.chiefminingdad.autoplayer.records;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.Chunk;

public record ChunksS2C(Chunk RequestedChunk) implements CustomPayload {
    public static final Identifier CHUNK_PAYLOAD_ID = Identifier.of("autoplayer:ChunkRequester");
    public static final CustomPayload.Id<ChunksS2C> ID = new CustomPayload.ID<>(CHUNK_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf,ChunksS2C> CODEC = PacketCodec.tuple(Chunk.PACKET_CODEC);
}
