package com.chiefminingdad.autoplayer;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public BlockPos Pos;
    private int X,Y,Z;
    public Node(BlockPos pos){
        Pos = pos;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
    }
    public BlockPos[] getSurrounding(){
        ArrayList<BlockPos> allSurrounding = new ArrayList<>() {};

        for(int dx=-1;dx<2;dx++){
            for(int dy=-1;dy<2;dy++){
                for(int dz=-1;dz<2;dz++){
                    if(dx!=0&dy!=0&dz!=0){
                        allSurrounding.add(new BlockPos(X+dx,Y+dy,Z+dz));
                    }
                }
            }
        }
        return (BlockPos[]) allSurrounding.toArray();
    }
    public Node GetBestNode(int x,int y, int z){
        BlockPos[] PotentialBlocks = getSurrounding();

        for(BlockPos Positions:PotentialBlocks){

        }
    }

}
