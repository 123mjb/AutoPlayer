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

    public final BlockManager blockManager;
    final WeightFinder WF;

    boolean FindingPath = false;
    boolean FoundPath = false;
    int X,Y,Z;

    BlockPos[] PathBlocks = new BlockPos[] {};
    Stack<BlockPos> PathStack = new Stack<>();

    public PathFindingAlgo(ClientPlayerEntity Player, World world){
        player = Player;
        CurrentWorld=world;
        blockManager = new BlockManager(CurrentWorld,player);
        WF = new WeightFinder(player,blockManager,CurrentWorld);
    }

    private boolean FindPath = false;
    public AllNodeList CheckedNodes = new AllNodeList();
    private int section = 0;
    private boolean RunningConcurrently = false;
    int bestLoc;
    addSurrounding AddSurrounding;
    Thread CurrentRunning;

    /**
     * Will do a section of code to find the most optimum path as to not spend too long on a tick.
     * @return
     * Whether the algorithm has finished.
     */
    public boolean doPathFinding(){
            if (FindPath){
                // TODO: Make it pause if a chunk is still being received. Should Do Now Check Code
                if(section == 0) {
                    bestLoc = CheckedNodes.GetBestLocation();//TODO:does it actually check if a node has already been used or update the weight when a better one is found
                    section++;
                }
                if(section == 1){
                    if(AddSurrounding == null){
                        AddSurrounding = new addSurrounding(bestLoc);
                    }
                    if (!RunningConcurrently){
                        CurrentRunning = new Thread(AddSurrounding);
                        CurrentRunning.start();
                        RunningConcurrently = true;
                    }
                }
                if(section == 2){
                    //TODO: Checking Some important stuff e.g. if the pathfinding has finished or got far enough.
                }
            }
            return false;
    }
    public class addSurrounding implements Runnable{
        int BestLoc;
        public addSurrounding(int bestLoc){
            BestLoc = bestLoc;
        }

        @Override
        public void run() {
            CheckedNodes.AddAllSurroundingNodes(BestLoc, X, Y, Z,WF,blockManager);
            RunningConcurrently = false;
            AddSurrounding = null;
            section = 2;
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
    public double DistanceFromPlayerToPath(AllNodeList Nodes){
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
