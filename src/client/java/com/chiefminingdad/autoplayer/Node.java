package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.BlockManager.BlockGetter;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public class Node {
    public BlockPos Pos;
    private int X, Y, Z;
    public WeightFinder.WeightInfo Weight;
    public float SpecificWeight;
    public float DistanceWeight;
    public boolean checked = false;

    private static final float DistanceWeightAdjustmentFactor = 20;

    public float getWeight() {
        return Weight.Total();
    }

    /**
     * Value to rank the Nodes on.
     *
     * @return The sum of the weight + the distance weight
     */
    public float getTotalWeight() {
        return Weight.Total() + DistanceWeight;
    }

    public float getSpecificWeight() {
        return SpecificWeight;
    }

    public float getTotalSpecificWeight() {
        return DistanceWeight + SpecificWeight;
    }

    /**
     * For First Node
     * @param player so the nodes starts where the player is.
     */
    public Node (ClientPlayerEntity player) {
        try {
            Pos = player.getBlockPos();
            X = Pos.getX();
            Y = Pos.getY()-1;
            Z = Pos.getZ();
            Weight = new WeightFinder.StarterWeight();
            DistanceWeight = 0.0F;
            SpecificWeight = 0.0F;
        }
        catch (Exception e) {
            player.sendMessage(Text.of(e.getMessage()), true);
        }
    }

    public Node(BlockPos pos) {
        Pos = pos;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
        Weight = new WeightFinder.WeightInfo();
        SpecificWeight = 0;
    }

    public Node(BlockPos pos, WeightFinder.WeightInfo totalWeight, float specificWeight) {
        Pos = pos;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
        Weight = totalWeight;
        SpecificWeight = specificWeight;
    }

    public boolean setTotalWeight(WeightFinder.WeightInfo newWeight) {
        if (newWeight.lessThan(Weight)) {
            Weight = newWeight;
            return true;
        }
        return false;
    }


    public BlockPos[] getSurrounding() {
        ArrayList<BlockPos> allSurrounding = new ArrayList<>() {
        };

        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                for (int dz = -1; dz < 2; dz++) {
                    if (dx != 0 & dy != 0 & dz != 0) {
                        allSurrounding.add(new BlockPos(X + dx, Y + dy, Z + dz));
                    }
                }
            }
        }
        return allSurrounding.toArray(BlockPos[]::new);
    }

    public ArrayList<Node> GetAllSurroundingNodes(int x, int y, int z, WeightFinder WF, BlockManager BM) {
        BlockPos[] PotentialBlocks = getSurrounding();
        ArrayList<Node> NewNodes = new ArrayList<>();


        for (BlockPos Positions : PotentialBlocks) {
            WeightFinder.WeightInfo newWeight = findWeight(Pos, Positions, WF, BM);
            //AutoPlayer.LOGGER.info("Found weight {}", newWeight.Total());
            float newDistanceWeight = findDistanceWeight(Positions, x, y+1, z);
            //AutoPlayer.LOGGER.info("Found distance weight {}", newDistanceWeight);
            NewNodes.add(new Node(Positions, Weight.append(newWeight, Pos), newDistanceWeight));
        }
        return NewNodes;
    }

    public WeightFinder.WeightInfo findWeight(BlockPos One, BlockPos Two, WeightFinder WF, BlockManager BM) {
        // TODO: Make only moderators be able to access chunks not yet loaded ingame
        BlockGetter[] getters = new BlockGetter[]{new BlockGetter(Two,BM), new BlockGetter(Two.up(1),BM), new BlockGetter(Two.up(2),BM)};
        BlockState[] blockStates = new BlockState[3];
        Optional<Boolean> tryget;
        while (blockStates[0] == null | blockStates[1] == null | blockStates[2] == null) {
            tryget = getters[0].tryget(BM.Unavailable);
            if(tryget.isEmpty()) {
                return new WeightFinder.UnattainableWeight();
            }
            if (tryget.get()) {
                blockStates[0] = getters[0].getState();
            }
            tryget = getters[1].tryget(BM.Unavailable);
            if(tryget.isEmpty()) {
                return new WeightFinder.UnattainableWeight();
            }
            if (tryget.get()) {
                blockStates[1] = getters[1].getState();
            }
            tryget = getters[2].tryget(BM.Unavailable);
            if(tryget.isEmpty()) {
                return new WeightFinder.UnattainableWeight();
            }
            if (tryget.get()) {
                blockStates[2] = getters[2].getState();
            }
        }
        WeightFinder.WeightInfo[] weightInfos = new WeightFinder.WeightInfo[]{
                WF.findBelowWeight(blockStates[0],Two),
                WF.findBottomWeight(blockStates[1],Two.up(1)),
                WF.findTopWeight(blockStates[2],Two.up(2))
        };
        WeightFinder.WeightInfo a = new WeightFinder.WeightInfo();
        a.merge(weightInfos[0]);
        a.merge(weightInfos[1]);
        a.merge(weightInfos[2]);
        return a;
    }

    public static float findDistanceWeight(BlockPos nextBlock, int X, int Y, int Z) {
        int x = X != Integer.MAX_VALUE?Math.abs(nextBlock.getX() - X):0;
        int y = Y != Integer.MAX_VALUE?Math.abs(nextBlock.getY() - Y):0;
        int z = Z != Integer.MAX_VALUE?Math.abs(nextBlock.getZ() - Z):0;

        return (float) (((float) Math.sqrt(
                        Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) / 7.5) +
                        ((float) (x + y + z) / 5));
    }

    @Contract(" -> new")
    public static @NotNull Node worstNode() {
        return new Node(new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), new WeightFinder.WorstWeight(), Float.MAX_VALUE);
    }

    public float getDistanceWeightAdjustmentFactor() {
        return DistanceWeightAdjustmentFactor;
    }



    public static class AllNodeList extends ArrayList<com.chiefminingdad.autoplayer.Node> {

        public int GetBestLocation() {
            //player.sendMessage(Text.of("1"), false);
            Node BestNode = Node.worstNode();
            int BestLoc = -1;
            for (int i = 0; i < this.size(); i++) {
                //player.sendMessage(Text.of("2"), false);
                Node Current = this.get(i);
                if ((Current.getTotalWeight() < BestNode.getTotalWeight())&!Current.checked) {
                    BestNode = Current;
                    BestLoc = i;
                    //player.sendMessage(Text.of("3"), false);
                }
            }
            BestNode.checked = true;
            //player.sendMessage(Text.of("4"), false);
            return BestLoc;
        }

        /**
         * Adds all the nodes around the node in the list with index centre.
         *
         * @param centre index of the block
         * @param X      x of the desired location
         * @param Y      y of the desired location
         * @param Z      z of the desired location
         * @param WF     WeightFinder class instance.
         *
         * @return
         * If the algorithm couldn't find the state of a block.
         */
        public boolean AddAllSurroundingNodes(int centre, int X, int Y, int Z, WeightFinder WF,BlockManager BM) {
            boolean CouldntFindABlock = false;
            //AutoPlayer.LOGGER.info("Add all surrounding nodes");
            for (Node newNode : this.get(centre).GetAllSurroundingNodes(X, Y, Z, WF,BM)) {//stuck
                //AutoPlayer.LOGGER.info("Adding");
                if (newNode.Weight.isUnattainable()){CouldntFindABlock=true;}
                if (this.contains(newNode)) {
                    int oldNodeIndex = this.findIndex(newNode.Pos);
                    Node oldNode = this.get(oldNodeIndex);
                    if (oldNode.getTotalWeight()>newNode.getTotalWeight()){
                        this.set(oldNodeIndex,newNode);
                    }
                } else {
                    this.add(newNode);
                }
            }
            //AutoPlayer.LOGGER.info("Added all surrounding nodes");
            return CouldntFindABlock;
        }

        public void AddAllSurroundingNodes(int centre, @NotNull Vec3i Destined, WeightFinder WF,BlockManager BM) {
            AddAllSurroundingNodes(centre, Destined.getX(), Destined.getY(), Destined.getZ(), WF,BM);
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
        public int findIndex(BlockPos p){
            for(int i = 0; i < this.size(); i++){
                if(this.get(i).Pos == p){
                    return i;
                }
            }
            return -1;
        }
        public int findIndex(Node o){
            return findIndex(o.Pos);
        }
        public Node findNode(Node p){
            return this.get(findIndex(p));
        }
        public Node findNode(BlockPos p){
            return this.get(findIndex(p));
        }
    }

}
