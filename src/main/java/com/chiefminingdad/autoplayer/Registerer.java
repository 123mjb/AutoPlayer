package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.records.ChunksC2SRequest;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.WorldChunk;

import java.util.BitSet;
import java.util.Objects;

public class Registerer {
    public Registerer(){
        PayloadTypeRegistry.playC2S().register(ChunksC2SRequest.ID,ChunksC2SRequest.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ChunksC2SRequest.ID, Registerer::receive);

        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of("fabric-docs", "location_pos"),
                CustomClassHolder.DesiredLocationArgumentType.class,
                ConstantArgumentSerializer.of(CustomClassHolder.DesiredLocationArgumentType::new)
        );


    }

    private static void receive(ChunksC2SRequest payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity Player = context.player();
        PlayerManager PM = Objects.requireNonNull(Player.getServer()).getPlayerManager();
        if (PM.isOperator(Player.getGameProfile())) {
            WorldChunk chunk = Player.getServerWorld().getChunk(payload.x(), payload.z());
            ChunkDataS2CPacket Payload = new ChunkDataS2CPacket(chunk,chunk.getWorld().getLightingProvider(),new BitSet(),new BitSet());
            Player.networkHandler.sendPacket(Payload);
        }
    }
}
