package com.chiefminingdad.autoplayer;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Node {
    public BlockPos Pos;
    private int X,Y,Z;
    public float Weight;
    public float SpecificWeight;
    public float DistanceWeight;

    public float getWeight(){
        return Weight;
    }

    public float getTotalWeight() {
        return Weight + DistanceWeight;
    }

    public float getSpecificWeight() {
        return SpecificWeight;
    }

    public float getTotalSpecificWeight(){
        return DistanceWeight+SpecificWeight;
    }


    public Node(BlockPos pos){
        Pos = pos;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
        Weight = 0;
        SpecificWeight = 0;
    }

    public Node(BlockPos pos,float totalWeight,float specificWeight){
        Pos = pos;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
        Weight = totalWeight;
        SpecificWeight = specificWeight;
    }
    public boolean setTotalWeight(float newWeight){
        if(newWeight <Weight){
            Weight = newWeight;
            return true;
        }
        return false;
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

    public ArrayList<Node> GetAllSurroundingNodes(int x,int y, int z){
        BlockPos[] PotentialBlocks = getSurrounding();
        ArrayList<Node> NewNodes= new ArrayList<>();


        for(BlockPos Positions:PotentialBlocks){
            float newWeight = findWeight(Pos,Positions);
            float newDistanceWeight = findDistanceWeight(Positions,x,y,z);
            NewNodes.add(new Node(Positions,getWeight()+newWeight,newDistanceWeight));
        }
        return NewNodes;
    }


    public Node GetBestNode(int x,int y, int z){
        BlockPos[] PotentialBlocks = getSurrounding();

        Node Lowest = new Node(new BlockPos(0,0,0),Float.MAX_VALUE,Float.MAX_VALUE);

        for(BlockPos Positions:PotentialBlocks){
            float newWeight = findWeight(Pos,Positions);
            float newDistanceWeight = findDistanceWeight(Positions,x,y,z);
            if (newWeight + newDistanceWeight < Lowest.getSpecificWeight()) {

            }
        }
    }

    public static float findWeight(BlockPos One,BlockPos Two){

    }

    public static float findDistanceWeight(BlockPos nextBlock, int X, int Y, int Z){
        return (float)Math.sqrt(Math.pow(X-nextBlock.getX(),2)+Math.pow(Y-nextBlock.getY(),2)+Math.pow(Z-nextBlock.getZ(),2))/4;
    }
}
