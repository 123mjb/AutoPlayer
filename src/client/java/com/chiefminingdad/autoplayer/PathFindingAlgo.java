package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.Node.AllNodeList;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.Cast;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class PathFindingAlgo {
    final MinecraftClient mc;
    private ClientPlayerEntity player;
    private World CurrentWorld;

    private BlockManager blockManager;
    private WeightFinder WF;

    int X,Y,Z;

    public AllNodeList CheckedNodes = new AllNodeList();
    private int section = 0;
    private boolean RunningConcurrently = false;
    int bestLoc;
    Node bestNode;
    addSurrounding AddSurrounding=null;
    Thread CurrentRunning;
    Node[] PathBlocks = new Node[] {};
    Stack<Node> PathStack = new Stack<>();

    public PathFindingAlgo(MinecraftClient cl){
        mc = cl;
    }

    public void reset(){
        CheckedNodes = new AllNodeList();
        section = 0;
        RunningConcurrently = false;
        bestLoc = -1;
        bestNode = null;
        AddSurrounding=null;
        CurrentRunning = null;
        PathBlocks = new Node[] {};
        PathStack = new Stack<>();
    }

    public BlockManager getBlockManager() {
        if (blockManager == null){
            blockManager = new BlockManager(getCurrentWorld(),getPlayer());
        }
        return blockManager;
    }

    public WeightFinder getWF() {
        if (WF == null) {
            WF = new WeightFinder(getPlayer(),getBlockManager(),getCurrentWorld());
        }
        return WF;
    }

    public ClientPlayerEntity getPlayer() {
        if (player==null) {
            player = mc.player;
        }
        return player;
    }

    public World getCurrentWorld() {
        if (CurrentWorld==null) {
            CurrentWorld = mc.world;
        }
        return CurrentWorld;
    }



    /**
     * Will do a section of code to find the most optimum path as to not spend too long on a tick.
     * @return
     * Whether the algorithm has finished.
     */
    public boolean doPathFinding(){
            // TODO: Make it pause if a chunk is still being received. Should Do Now Check Code
            try {
                if (section == 0) {
                    //AutoPlayer.LOGGER.info("Section:0");
                    bestLoc = CheckedNodes.GetBestLocation();//TODO:does it actually check if a node has already been used or update the weight when a better one is found
                    bestNode = CheckedNodes.get(bestLoc);
                    AutoPlayer.LOGGER.info("using {}",bestNode.getTotalWeight());
                    //AutoPlayer.LOGGER.info("5");
                    if (BlockPosWorksForLoc(bestNode.Pos)){ section = 2;AutoPlayer.LOGGER.info("Finsiherd");}
                    else section = 1;
                    AutoPlayer.LOGGER.info("6");
                    AutoPlayer.LOGGER.info("{},{},{}",bestNode.Pos.getX(),bestNode.Pos.getY(),bestNode.Pos.getZ());
                }
                if (section == 1) {
                    //AutoPlayer.LOGGER.info("Section:1");
                    if (AddSurrounding == null) {
                        //AutoPlayer.LOGGER.info("Section:1.1");
                        AddSurrounding = new addSurrounding(bestLoc);
                    }
                    if (!RunningConcurrently) {
                        //AutoPlayer.LOGGER.info("Section:1.2");
                        CurrentRunning = new Thread(AddSurrounding);
                        CurrentRunning.start();
                        RunningConcurrently = true;
                    }
                }
                if (section == 2) {
                    //AutoPlayer.LOGGER.info("Section:2");
                    ConvertAllNodeListIntoPathStack();

                    PathBlocks = PathStack.toArray(PathBlocks);
                    return true;
                }
            }
            catch (Exception e) {
                AutoPlayer.LOGGER.error(e.getMessage());
            }
            return false;
    }
    public boolean BlockPosWorksForLoc(@NotNull BlockPos p){
        //AutoPlayer.LOGGER.info("BlockPosWorksForLoc");
        boolean works = (X == Integer.MAX_VALUE | X == p.getX()) & (Y == Integer.MAX_VALUE | Y == p.getY()+1) & (Z == Integer.MAX_VALUE | Z == p.getZ());
        //AutoPlayer.LOGGER.info(String.valueOf(works));
        return works;
    }
    public class addSurrounding implements Runnable{
        int BestLoc;
        public addSurrounding(int bestLoc){
            BestLoc = bestLoc;
        }

        @Override
        public void run() {
            if (CheckedNodes.AddAllSurroundingNodes(BestLoc, X, Y, Z,getWF(),getBlockManager())) {
                section=2;
            }
            else  section=0;
            RunningConcurrently = false;
            AddSurrounding = null;
            //AutoPlayer.LOGGER.info("addSurrounding Done");
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
        try {
            CheckedNodes.add(new Node(getPlayer()));
        }
        catch (Exception e) {
            getPlayer().sendMessage(Text.of(e.getMessage()), false);
        }
        //getPlayer().sendMessage(Text.of("FindPath"), false);
    }

    /**
     * @param Nodes
     * Array of all the blocks on the path.
     * @return
     * The minimum distance from the player to the supplied path.
     */
    public double DistanceFromPlayerToPath(@NotNull AllNodeList Nodes){
        double d = Double.MAX_VALUE;
        for (Node node: Nodes){
            double temp = mag(
                    node.Pos.getX() - getPlayer().getX(),
                    node.Pos.getY() - getPlayer().getY(),
                    node.Pos.getZ() - getPlayer().getZ()
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

    private void ConvertAllNodeListIntoPathStack(){
        int currentloc = bestLoc;
        while(currentloc!=-1){
            Node n = CheckedNodes.get(currentloc);
            PathStack.add(n);
            currentloc = CheckedNodes.findIndex(n.Weight.PreviousBlock);
            if(currentloc==0)currentloc=-1;
        }

    }

    public void spawnparticlesonpath(World world){
        for(Node n : PathBlocks){
            world.addParticleClient(AutoPlayer.SPARKLE_PARTICLE,n.Pos.getX()+0.5,n.Pos.getY()+1.5,n.Pos.getZ()+0.5,0,0,0);
        }
    }

}
