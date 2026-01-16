package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.Weight.WeightFinder;
import com.chiefminingdad.autoplayer.Weight.WeightInfo;
import com.chiefminingdad.autoplayer.managers.BlockManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.chiefminingdad.autoplayer.AutoPlayerClient.debugInfo;

public class AllNodeList extends ArrayList<Node> {
    int BottomY;
    int TopY;

    private void setMaxMinHeight() {
        World world = MinecraftClient.getInstance().world;
        if (world != null) {
            assert world != null;
            BottomY = world.getBottomY();
            TopY = world.getBottomY() + world.getHeight();
        }
    }
    /**
     * Iterates over the list finding the best Node.
     *
     * @return The index of the lowest weight node in the list that hasn't been checked yet.
     */
    public int GetBestLocation() {
        Node BestNode = Node.worstNode();
        int BestLoc = -1;
        for (int i = 0; i < this.size(); i++) {
            Node Current = this.get(i);
            if ((Current.getTotalFWeight() < BestNode.getTotalFWeight()) & !Current.checked) {
                BestNode = Current;
                BestLoc = i;
            }
        }
        BestNode.checked = true;
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
     * @return If the algorithm couldn't find the state of a block.
     */
    public boolean AddAllSurroundingNodes(int centre, int X, int Y, int Z, WeightFinder WF, BlockManager BM) {
        boolean CouldntFindABlock = false;
        for (Node newNode : GetAllSurroundingNodes(this.get(centre),X, Y, Z, WF, BM)) {
            //Checks if the chunk was completely unobtainable from the server.
            if (newNode.Weight.isUnattainable()) {
                CouldntFindABlock = true;
                continue;
            }
            //Sees if the new one is better than the old if so, replaces it
            if (this.contains(newNode)) {
                int oldNodeIndex = this.findIndex(newNode.Pos);
                Node oldNode = this.get(oldNodeIndex);
                if (oldNode.getFinalWeight() > newNode.getFinalWeight()) {
                    this.set(oldNodeIndex, newNode.setchecked(oldNode.checked));
                }
            } else {
                this.add(newNode);
            }
        }
        return CouldntFindABlock;
    }

    public void AddAllSurroundingNodes(int centre, @NotNull Vec3i Destined, WeightFinder WF, BlockManager BM) {
        AddAllSurroundingNodes(centre, Destined.getX(), Destined.getY(), Destined.getZ(), WF, BM);
    }

    public ArrayList<Node> GetAllSurroundingNodes(@NotNull Node CentreNode, int x, int y, int z, WeightFinder WF, BlockManager BM) {
        setMaxMinHeight();

        BlockPos[] PotentialBlocks = CentreNode.getSurrounding();
        ArrayList<Node> NewNodes = new ArrayList<>();//Todo add debugging logging using new function in DebugInfo

        for (BlockPos pos : PotentialBlocks) {
            if (!(BottomY<pos.getY() & pos.getY()<TopY)) continue;
            if ((this.contains(pos))){if (this.findNode(pos).checked){continue;}}
            WeightInfo newWeight = CentreNode.findWeight(pos, WF, BM);
            float newHeuristicWeight = Node.findHeuristicWeight(pos, x, y, z);
            Node n = new Node(pos, CentreNode.Weight.append(newWeight), newHeuristicWeight);
            NewNodes.add(n);
            debugInfo.AddExtra(n.getWeight() +" "+n.HeuristicWeight+" "+n.Weight.getContinuousWalkingTime());//n.toString());
        }
        return NewNodes;
    }


    /**
     * @param o The Node to check if it is in the list.
     * @return Whether o is in the list.
     */
    public boolean contains(Node o) {
        for (Node node : this) {
            if (PosEquals(node.Pos, o.Pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param p The BlockPos to check if it is in the list.
     * @return Whether o is in the list.
     */
    public boolean contains(BlockPos p) {
        for (Node node : this) {
            if (PosEquals(node.Pos, p)) {
                return true;
            }
        }
        return false;
    }

    public boolean PosEquals(@NotNull BlockPos pos1, @NotNull BlockPos pos2) {
        return pos1.getX() == pos2.getX() & pos1.getY() == pos2.getY() & pos1.getZ() == pos2.getZ();
    }

    public int findIndex(BlockPos p) {
        for (int i = 0; i < this.size(); i++) {
            if (PosEquals(this.get(i).Pos, p)) {
                return i;
            }
        }
        return -1;
    }

    public int findIndex(@NotNull Node o) {
        return findIndex(o.Pos);
    }

    public Node findNode(Node p) {
        return this.get(findIndex(p));
    }

    public Node findNode(BlockPos p) {
        return this.get(findIndex(p));
    }
}
