package com.chiefminingdad.autoplayer.Weight;

import com.chiefminingdad.autoplayer.managers.BlockManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public class BreakingSpeedManager {
    WorldView World;
    PlayerEntity Player;
    BlockPos Pos;
    BlockManager BM;

    public BreakingSpeedManager(WorldView world, PlayerEntity player, BlockPos pos, BlockManager blockManager){
        World = world;
        Player = player;
        Pos = pos;
        BM = blockManager;
    }

    public int getBestItem(BlockState state, @NotNull Inventory inventory){
        int bestItemLoc = 0;
        float bestItemWeight = -1F;
        for (int i=0;i<inventory.size();i++){
            float currentWeight = BreakingSpeedFinder.getSimpleSpeed(state,inventory.getStack(i),Pos);
            if (currentWeight<bestItemWeight){
                bestItemWeight = currentWeight;
                bestItemLoc = i;
            }
        }
        return bestItemLoc;
    }

    public float getFullSpeed(){
        BlockState state = BM.getBlock(Pos);
        if(state.getBlock()== Blocks.AIR) return 0F;
        Inventory inventory = Player.getInventory();
        return BreakingSpeedFinder.getFullSpeed(state,inventory.getStack(getBestItem(state,inventory)),Pos);
    }

    public float getSimpleSpeed(){
        BlockState state = BM.getBlock(Pos);
        if(state.getBlock()== Blocks.AIR) return 0F;
        Inventory inventory = Player.getInventory();
        return BreakingSpeedFinder.getSimpleSpeed(state,inventory.getStack(getBestItem(state,inventory)),Pos);
    }
}
