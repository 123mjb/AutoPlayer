package com.chiefminingdad.autoplayer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

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

    public float FindBelowWeight(BlockState UnderneathBlock, BlockPos UnderneathBlockPos){
        return WeightSwitches(UnderneathBlock,0,UnderneathBlockPos);
    }
    public float WeightSwitches(BlockState block,int WhichPredicament, BlockPos blockPos){
        if(WhichPredicament==0){
            Block checkblock= block.getBlock();
            if(checkblock==BlueIce)return 4.376F;
            else if(checkblock==Ice|checkblock==FrostedIce|checkblock==PackedIce)return 4.157F;
            else if(checkblock==SlimeBlock) return 3.04F;
            else if(checkblock==HoneyBlock| checkblock== SoulSand) return 2.508F;
        }
        else if(WhichPredicament==1){
            float BlockHardness = block.getHardness(world,blockPos);
            int i = Player.canHarvest(block) ? 30 : 100;
            return Player.getBlockBreakingSpeed(block) / BlockHardness / i;
            for (ItemStack itemStack : Player.getInventory()) {
                if(IsItemEfficient(itemStack,block)){

                }
            }

        }
        return -1;
    }

    public ItemBlockBreakingSpeed getBestInventoryItemForBlock(PlayerInventory inventory,BlockState block){
        ItemBlockBreakingSpeed Best = null;
        for (int i = 0;i<inventory.size();i++) {
            ItemBlockBreakingSpeed Speed = new ItemBlockBreakingSpeed(world, block,inventory.getStack(i));
            if(Best==null) {Best = Speed;continue;}
            if(Speed.SimpleBetterThan())
        }
    }
    public class ItemBlockBreakingSpeed{
        ItemStack Item;
        BlockState Blck;
        WorldView World;
        public ItemBlockBreakingSpeed(WorldView world, BlockState block, ItemStack item){
            World = world;Blck = block; Item = item;
        }

        public float getSimpleSpeed(){

        }
    }

    public boolean IsItemEfficient(ItemStack stack,BlockState block){
        if (stack.isEmpty()) return false;
        return stack.isSuitableFor(block);
    }
}
