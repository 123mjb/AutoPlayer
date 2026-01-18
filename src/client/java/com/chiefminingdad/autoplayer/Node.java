package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.managers.BlockManager;
import com.chiefminingdad.autoplayer.Weight.WeightFinder;
import com.chiefminingdad.autoplayer.Weight.WeightInfo;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Node {
    public BlockPos Pos;
    private int X, Y, Z;
    public WeightInfo Weight;
    public float HeuristicWeight;
    public boolean checked = false;

    /**
     * Value to rank the Nodes on.
     *
     * @return The sum of the weight + the distance weight
     */
    public float getTotalFWeight() {
        return Weight.getTotal() + HeuristicWeight;
    }

    /**
     * Total weight without heuristic, used for the basis of the weight for the next node in line.
     * @return Total weight
     */
    public float getFinalWeight() {
        return Weight.getTotal();
    }

    public Node setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    @Override
    public String toString() {
        return "Node{" +
                "checked=" + checked +
                ", Pos=" + Pos.toString() +
                ", X=" + X +
                ", Y=" + Y +
                ", Z=" + Z +
                ", Weight=" + Weight.toString() +
                ", DistanceWeight=" + HeuristicWeight +
                ", Total Weight=" + getFinalWeight() +
                '}';
    }

    /**
     * For First Node
     * @param player so the nodes starts where the player is.
     */
    public Node (ClientPlayerEntity player) {
        try {
            Pos = player.getBlockPos();
            X = Pos.getX();
            Y = Pos.getY();
            Z = Pos.getZ();
            Weight = new WeightFinder.StarterWeight();
            HeuristicWeight = 0.0F;
        }
        catch (Exception e) {
            player.sendMessage(Text.of(e.getMessage()), true);
        }
    }

    public Node(@NotNull BlockPos pos) {
        Pos = pos;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
        Weight = new WeightInfo();
    }

    public Node(@NotNull BlockPos pos, WeightInfo totalWeight, float heuristicWeight) {
        Pos = pos;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
        Weight = totalWeight;
        HeuristicWeight = heuristicWeight;
    }

    public boolean setTotalWeight(@NotNull WeightInfo newWeight) {
        if (newWeight.lessThan(Weight)) {
            Weight = newWeight;
            return true;
        }
        return false;
    }


    public BlockPos[] getSurrounding() {
        ArrayList<BlockPos> allSurrounding = new ArrayList<>() {};
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                for (int dz = -1; dz < 2; dz++) {
                    if (dx != 0 | dy != 0 | dz != 0) {
                        allSurrounding.add(new BlockPos(X + dx, Y + dy, Z + dz));
                    }
                }
            }
        }
        return allSurrounding.toArray(BlockPos[]::new);
    }

    /**
     * Finds the weight of walking through a block when given the BlockPos of the block the player would stand in.
     * @param pos The BlockPos of the block the player would stand in.
     * @param WF WeightFinder Instance to find Block Weights
     * @param BM BlockManager Instance to find BlockStates
     * @return The weight information for that block.
     */
    public WeightInfo findWeight(@NotNull BlockPos pos, WeightFinder WF, BlockManager BM) {
        ArrayList<BlockState> states = BM.getBlocks(new ArrayList<BlockPos>(Arrays.asList(pos.down(),pos,pos.up())));

        if (states.contains(null)){
            return new WeightFinder.UnattainableWeight();
        }

        return new WeightInfo(pos,WF.findMiningWeight(states.get(2),pos.up()),WF.findMiningWeight(states.get(1),pos),WF.findWalkingWeight(states.get(0),pos.down()));
    }

    public static float findHeuristicWeight(BlockPos nextBlock, int X, int Y, int Z) {
        float x = X != Integer.MAX_VALUE?Math.abs(nextBlock.getX() - X):0;
        float y = Y != Integer.MAX_VALUE?Math.abs(nextBlock.getY() - Y):0;
        float z = Z != Integer.MAX_VALUE?Math.abs(nextBlock.getZ() - Z):0;

        return (float) ((Math.sqrt(
                        Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) / 25) +
                        ( (x + y + z) / 20));
    }

    @Contract(" -> new")
    public static @NotNull Node worstNode() {
        return new Node(new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), new WeightFinder.WorstWeight(), Float.MAX_VALUE);
    }
}
