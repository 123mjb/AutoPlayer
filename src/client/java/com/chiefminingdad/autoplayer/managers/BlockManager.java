package com.chiefminingdad.autoplayer.managers;

import com.chiefminingdad.autoplayer.records.ChunksC2SRequest;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    public void AddUnavailable(BlockPos unavailableBlock){
        Unavailable.add(unavailableBlock);
    }
    public boolean UnavailableContains(BlockPos checkLoc){
        for(BlockPos loc:Unavailable){
            if(loc.equals(checkLoc)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<BlockState> getBlocks(ArrayList<BlockPos> positions) throws RuntimeException {
        try {
            return CompletableFuture.supplyAsync(()->{
                int size = positions.size();
                ArrayList<BlockState> states = new ArrayList<>();
                for (int i = 0; i < size; i++) states.add(null);
                for(int i=0;i<size;i++){
                    states.set(i,null);
                    Chunk RequestedChunk = CurrentWorld.getChunk(positions.get(i));
                    if(RequestedChunk==null){
                        ChunksC2SRequest Payload = new ChunksC2SRequest(positions.get(i));
                        ClientPlayNetworking.send(Payload);
                    }
                }
                int total;
                while (true) {
                    total=0;
                    for (int i = 0; i < size; i++) {
                        if (states.get(i) == null) {
                            if (CurrentWorld.getBlockState(positions.get(i)) != null) {
                                states.set(i, CurrentWorld.getBlockState(positions.get(i)));
                            } else if (UnavailableContains(positions.get(i))) {
                                total++;
                            }
                        } else {
                            total++;
                        }
                    }
                    if(total == size){
                        return states;
                    }
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
