package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.records.ChunksC2SRequest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockManager {

    final World CurrentWorld;
    final ClientPlayerEntity playerEntity;
    public BlockManager(World currentWorld, ClientPlayerEntity Player){
        CurrentWorld = currentWorld;
        playerEntity = Player;
    }

//    public @Nullable BlockState TryGetBlock(BlockPos pos){
//        if (GetBlockState==null) GetBlockState = getBlock(pos);
//        BlockState state = GetBlockState.get();
//        if (state!=null){
//            GetBlockState = null;
//            return state;
//        }
//        return null;
//    }

    /**
     *
     */
    public static class BlockGetter{
        private final Supplier<BlockState> GetBlockState;
        private @Nullable BlockState state;

        /**
         */
        public BlockGetter(BlockPos pos,BlockManager BM){
            GetBlockState = BM.getBlock(pos);
        }
        public boolean tryget(){
            state = GetBlockState.get();
            return !(state==null);
        }
        public @Nullable BlockState getState(){
            return state;
        }
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
