package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.records.ChunksC2SRequest;
import com.chiefminingdad.autoplayer.records.ChunksS2CConfirmation;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.BitSet;

public class Registerer {
    public Registerer(){
        PayloadTypeRegistry.playC2S().register(ChunksC2SRequest.ID,ChunksC2SRequest.CODEC);
        PayloadTypeRegistry.playS2C().register(ChunksS2CConfirmation.ID,ChunksS2CConfirmation.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ChunksC2SRequest.ID, Registerer::receive);

        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of("fabric-docs", "location_pos"),
                CustomClassHolder.DesiredLocationArgumentType.class,
                ConstantArgumentSerializer.of(CustomClassHolder.DesiredLocationArgumentType::new)
        );


    }

    private static void receive(ChunksC2SRequest payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity Player = context.player();
        BlockPos p = payload.p();

        PlayerManager PM = context.server().getPlayerManager();
        if (PM.isOperator(Player.getPlayerConfigEntry())) {
            WorldChunk chunk = Player.getEntityWorld().getChunk(p.getX()>>4, p.getZ()>>4);
            ChunkDataS2CPacket Payload = new ChunkDataS2CPacket(chunk,chunk.getWorld().getLightingProvider(),new BitSet(),new BitSet());
            Player.networkHandler.sendPacket(Payload);
            ServerPlayNetworking.send(Player,new ChunksS2CConfirmation(true,p));
        }
        else{
            ServerPlayNetworking.send(Player,new ChunksS2CConfirmation(false,p));
        }
        //TODO: Make it send info that the player is not moderator.
    }
}
