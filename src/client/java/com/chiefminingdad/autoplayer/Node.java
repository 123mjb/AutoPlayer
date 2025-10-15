package com.chiefminingdad.autoplayer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;

import java.util.ArrayList;

public class Node {
    public BlockPos Pos;
    private int X,Y,Z;
    public WeightFinder.WeightInfo Weight;
    public float SpecificWeight;
    public float DistanceWeight;

    private static final float DistanceWeightAdjustmentFactor = 4;

    public float getWeight(){
        return Weight.Total();
    }

    /**
     * Value to rank the Nodes on.
     * @return The sum of the weight + the distance weight
     */
    public float getTotalWeight() {
        return Weight.Total() + DistanceWeight;
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
        Weight = new WeightFinder.WeightInfo();
        SpecificWeight = 0;
    }

    public Node(BlockPos pos, WeightFinder.WeightInfo totalWeight, float specificWeight){
        Pos = pos;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
        Weight = totalWeight;
        SpecificWeight = specificWeight;
    }
    public boolean setTotalWeight(WeightFinder.WeightInfo newWeight){
        if(newWeight.lessThan(Weight)){
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

    public ArrayList<Node> GetAllSurroundingNodes(int x,int y, int z,WeightFinder WF){
        BlockPos[] PotentialBlocks = getSurrounding();
        ArrayList<Node> NewNodes= new ArrayList<>();


        for(BlockPos Positions:PotentialBlocks){
            WeightFinder.WeightInfo newWeight = findWeight(Pos,Positions,WF);
            float newDistanceWeight = findDistanceWeight(Positions,x,y,z);
            NewNodes.add(new Node(Positions,getWeight()+newWeight,newDistanceWeight));
        }
        return NewNodes;
    }

    public WeightFinder.WeightInfo findWeight(BlockPos One,BlockPos Two,WeightFinder WF){
        BlockManager
    }
//    public class BaseWeight{
//        //TODO: Rework Weights to allow for more information to stored on nodes such as best tool.
//        public BaseWeight(float belowWeight, WeightFinder ){
//
//        }
//    }

    public static float findDistanceWeight(BlockPos nextBlock, int X, int Y, int Z){
        return (float)Math.sqrt(
                Math.pow((X!=Integer.MAX_VALUE)?X-nextBlock.getX():0,2)+
                        Math.pow((Y!=Integer.MAX_VALUE)?Y-nextBlock.getY():0,2)+
                        Math.pow((Z!=Integer.MAX_VALUE)?Z-nextBlock.getZ():0,2))/DistanceWeightAdjustmentFactor;
    }
    
    @Contract(" -> new")
    public static @NotNull Node worstNode(){return new Node(new BlockPos(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE),Float.MAX_VALUE,Float.MAX_VALUE);}

    public float getDistanceWeightAdjustmentFactor() {
        return DistanceWeightAdjustmentFactor;
    }

    public static class AllNodeList<E> extends ArrayList<com.chiefminingdad.autoplayer.Node>{
        public int GetBestLocation(){
            Node BestNode = Node.worstNode();
            int BestLoc = -1;
            for(int i= 0;i<this.size();i++) {
                Node Current = this.get(i);
                if (Current.getTotalWeight() < BestNode.getTotalWeight()) {
                    BestNode = Current;
                    BestLoc = i;
                }
            }
            return BestLoc;
        }
        public void AddAllSurroundingNodes(int centre,int X,int Y,int Z){
            for(Node newNode:this.get(centre).GetAllSurroundingNodes(X,Y,Z,WeightFinder,WF)){
                    if(!this.contains(newNode)){
                        this.add(newNode);
                    }
            }
        }
        public void AddAllSurroundingNodes(int centre, @NotNull Vec3i Destined){
            AddAllSurroundingNodes(centre,Destined.getX(),Destined.getY(),Destined.getZ());
        }

        /**
         * @param o The Node to check if it is in the list.
         * @return Whether o is in the list.
         */
        public boolean contains(Node o) {
            for (Node node : this) {
                if (node.Pos == o.Pos) {
                    return true;
                }
            }
            return false;
        }
    }
}
