package com.chiefminingdad.autoplayer;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class PathFindingAlgo {
    ClientPlayerEntity player;
    World CurrentWorld;

    BlockManager blockManager;

    boolean FindingPath = false;
    boolean FoundPath = false;
    int X,Y,Z;

    BlockPos[] PathBlocks = new BlockPos[] {};
    Stack<BlockPos> PathStack = new Stack<>();

    public PathFindingAlgo(ClientPlayerEntity Player, World world){
        player = Player;
        CurrentWorld=world;
        blockManager = new BlockManager(CurrentWorld);
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
     * @param locs
     * Array of all the blocks on the path.
     * @return
     * The minimum distance from the player to the supplied path.
     */
    public double DistanceFromPlayerToPath(BlockPos @NotNull [] locs){
        double d = Double.MAX_VALUE;
        for (BlockPos loc : locs) {
            double temp = mag(
                    loc.getX() - player.getX(),
                    loc.getY() - player.getY(),
                    loc.getZ() - player.getZ()
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
