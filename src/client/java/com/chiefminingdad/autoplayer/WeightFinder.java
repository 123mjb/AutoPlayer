package com.chiefminingdad.autoplayer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.World;

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

    public float FindBelowWeight(BlockState UnderneathBlock){
        return WeightSwitches(UnderneathBlock,0);
    }
    public float WeightSwitches(BlockState block,int WhichPredicament){
        if(WhichPredicament==0){
            Block checkblock= block.getBlock();
            if(checkblock==BlueIce)return 4.376F;
            else if(checkblock==Ice|checkblock==FrostedIce|checkblock==PackedIce)return 4.157F;
            else if(checkblock==SlimeBlock) return 3.04F;
            else if(checkblock==HoneyBlock| checkblock== SoulSand) return 2.508F;
        }
        else if(WhichPredicament==1){
            int BlockHardness = block.getHardness(world.getW)
        }
        return -1;
    }
}
