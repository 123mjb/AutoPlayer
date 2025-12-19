package com.chiefminingdad.autoplayer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class WeightFinder{
    ClientPlayerEntity Player;
    BlockManager BM;
    World world;
    public WeightFinder(ClientPlayerEntity player, BlockManager bM, World wrld){
        Player = player;
        BM = bM;
        world = wrld;
    }

    private final Block BlueIce = Blocks.BLUE_ICE;
    private final Block Ice = Blocks.ICE;
    private final Block FrostedIce = Blocks.FROSTED_ICE;
    private final Block PackedIce = Blocks.PACKED_ICE;
    private final Block SlimeBlock = Blocks.SLIME_BLOCK;
    private final Block HoneyBlock = Blocks.HONEY_BLOCK;
    private final Block SoulSand = Blocks.SOUL_SAND;

    public float findWalkingWeight(BlockState block, BlockPos blockPos){
        Block checkblock = block.getBlock();
        if(checkblock==BlueIce)return 1/4.376F;
        else if(checkblock==Ice|checkblock==FrostedIce|checkblock==PackedIce)return 1/4.157F;
        else if(checkblock==SlimeBlock) return 1/3.04F;
        else if(checkblock==HoneyBlock| checkblock== SoulSand) return 1/2.508F;
        else if(checkblock == Blocks.AIR)return 10F;// Where to put Placing logic?
        else return 1/4.317F;
    }
    public ItemBlockBreakingSpeed findMiningWeight(@NotNull BlockState block, BlockPos blockPos){
        Block checkblock= block.getBlock();
        if(checkblock == Blocks.AIR){
            return new AirBreaking();
        }else {
            return getBestInventoryItemForBlock(Player.getInventory(), block, blockPos);
        }
    }

    public ItemBlockBreakingSpeed getBestInventoryItemForBlock(PlayerInventory inventory,BlockState block,BlockPos blockpos){
        ItemBlockBreakingSpeed Best = null;
        for (int i = 0;i<inventory.size();i++) {
            ItemBlockBreakingSpeed newSpeed = new ItemBlockBreakingSpeed(world, block,inventory.getStack(i),this.Player,blockpos);
            if(Best==null) {Best = newSpeed;continue;}
            if(newSpeed.SimpleBetterThan(Best)){Best = newSpeed;}
        }
        return Best;
    }

    public static class AirBreaking extends  ItemBlockBreakingSpeed{
        public AirBreaking() {

        }

        @Override
        public float getFullSpeed() {
            return 0F;
        }
        @Override
        public float getSimpleSpeed() {
            return 0F;
        }
    }

    public static class StarterWeight extends WeightInfo{
        @Override
        public float getTotal() {
            return 0.0F;
        }
    }

    public static class WorstWeight extends WeightInfo{
        @Override
        public float getTotal() {
            return Float.MAX_VALUE;
        }
    }
    public static class UnattainableWeight extends WorstWeight{
        @Override
        public boolean isUnattainable() {
            return true;
        }
    }
}
