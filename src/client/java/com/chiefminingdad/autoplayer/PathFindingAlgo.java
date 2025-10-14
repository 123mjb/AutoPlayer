package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.Node.AllNodeList;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class PathFindingAlgo {
    final ClientPlayerEntity player;
    final World CurrentWorld;

    final BlockManager blockManager;
    final WeightFinder WF;

    boolean FindingPath = false;
    boolean FoundPath = false;
    int X,Y,Z;

    BlockPos[] PathBlocks = new BlockPos[] {};
    Stack<BlockPos> PathStack = new Stack<>();

    public PathFindingAlgo(ClientPlayerEntity Player, World world){
        player = Player;
        CurrentWorld=world;
        blockManager = new BlockManager(CurrentWorld);
        WF = new WeightFinder(Player,blockManager,CurrentWorld);
    }

    private boolean FindPath = false;
    private AllNodeList<Node> CheckedNodes = new AllNodeList<Node>();
    private int section = 0;

    /**
     * Will do a section of code to find the most optimum path as to not spend too long on a tick.
     * @return
     * Whether the algorithm has finished.
     */
    public boolean doPathFinding(){
            if (FindPath){
                // TODO: Make it pause if a chunk is still being received.
                if(section == 0) {
                    CheckedNodes.AddAllSurroundingNodes(CheckedNodes.GetBestLocation(), X, Y, Z);
                    section++;
                }
                if(section == 1){

                }
            }
    }


    /**
     * @param x
     * The X value of the desired Location
     * @param y
     * The Y value of the desired Location
     * @param z
     * The Z value of the desired Location
     */
    public void FindPath(int x, int y, int z){
        X=x;Y=y;Z=z;
        FindingPath = true;
        FoundPath  = false;
    }

    /**
     * @param Nodes
     * Array of all the blocks on the path.
     * @return
     * The minimum distance from the player to the supplied path.
     */
    public double DistanceFromPlayerToPath(AllNodeList<Node> Nodes){
        double d = Double.MAX_VALUE;
        for (Node node: Nodes){
            double temp = mag(
                    node.Pos.getX() - player.getX(),
                    node.Pos.getY() - player.getY(),
                    node.Pos.getZ() - player.getZ()
            );
            if (temp < d) {
                d = temp;
            }
        }
        return d;
    }
    private double mag(double x,double y,double z) {
        return Math.sqrt(x*x+y*y+z*z);
    }

    private double distanceFromXYZtoBlockPos(double x, double y, double z, @NotNull BlockPos BP){
        return mag(x-BP.getX(),y-BP.getY(),z-BP.getZ());
    }





    

}
