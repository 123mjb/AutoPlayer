package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.records.ChunksC2SRequest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockManager {

    final World CurrentWorld;
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

    public class WeightFinder{
        private final Block BlueIce = Blocks.BLUE_ICE;
        private final Block Ice = Blocks.ICE;
        private final Block FrostedIce = Blocks.FROSTED_ICE;
        private final Block PackedIce = Blocks.PACKED_ICE;

        public int FindBelowWeight(){
            Blocks.BLUE_ICE.getName()
        }
        public float WeightSwitches(BlockState block,int WhichPredicament){
//            switch(WhichPredicament){
//                case 0:
//                    return switch (block.getBlock()) {
//                        case BlueIce -> 4.376F;
//                        case Ice, FrostedIce, PackedIce -> 4.157F;
//                        default -> 4.317F;
//                    };
//                default:
//                    throw new IllegalStateException("Unexpected value: " + WhichPredicament);
//            }
            if(WhichPredicament==0){
                Block checkblock= block.getBlock()
                if()
            }
        }
    }
}
