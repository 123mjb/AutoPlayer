package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.records.ChunksC2SRequest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockManager {

    World CurrentWorld;
    @Nullable Supplier<BlockState> GetBlockState;
    public BlockManager(World currentWorld){
        CurrentWorld = currentWorld;
    }

    public @Nullable BlockState TryGetBlock(BlockPos pos){
        if (GetBlockState==null) GetBlockState = getBlock(pos);
        BlockState state = GetBlockState.get();
        if (state!=null){
            GetBlockState = null;
            return state;
        }
        return null;
    }

    public Supplier<@Nullable BlockState> getBlock(BlockPos pos){
        Chunk RequestedChunk = CurrentWorld.getChunk(pos);
        if (RequestedChunk==null){
            ChunksC2SRequest Payload = new ChunksC2SRequest(pos.getX()>>4,pos.getZ()>>4);
            ClientPlayNetworking.send(Payload);
            return ()-> CurrentWorld.getChunk(pos).getBlockState(pos);
        }
        return ()-> CurrentWorld.getBlockState(pos);
    }
}
