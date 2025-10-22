package com.chiefminingdad.autoplayer.records;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChunksS2CConfirmation(boolean canSend)implements CustomPayload {
    public static final Identifier CHUNK_CONFIRM_ID = Identifier.of("autoplayer:chunkconfirmer");
    public static final CustomPayload.Id<ChunksS2CConfirmation> ID = new CustomPayload.Id<>(CHUNK_CONFIRM_ID);
    public static final PacketCodec<RegistryByteBuf,ChunksS2CConfirmation> CODEC = PacketCodec.tuple(PacketCodecs.BOOLEAN,ChunksS2CConfirmation::canSend,ChunksS2CConfirmation::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
