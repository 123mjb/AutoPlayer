package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.records.ChunksC2SRequest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

public class BlockManager {

    final World CurrentWorld;
    final ClientPlayerEntity playerEntity;
    public ArrayList<BlockPos> Unavailable;
    public BlockManager(World currentWorld, ClientPlayerEntity Player){
        CurrentWorld = currentWorld;
        playerEntity = Player;
        Unavailable = new  ArrayList<>();
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

    public void AddUnavailable(BlockPos unavailableBlock){
        Unavailable.add(unavailableBlock);
    }



    /**
     *
     */
    public static class BlockGetter{
        private final Supplier<BlockState> GetBlockState;
        private final BlockPos Pos;
        private @Nullable BlockState state;

        /**
         */
        public BlockGetter(BlockPos pos,BlockManager BM){
            Pos = pos;
            GetBlockState = BM.getBlock(pos);
        }


        public Optional<Boolean> tryget(ArrayList<BlockPos> Unavailable){
            if (Unavailable.contains(Pos)){return Optional.empty();}
            state = GetBlockState.get();
            return Optional.of(!(state==null));
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
