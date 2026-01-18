package com.chiefminingdad.autoplayer.Weight;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class WeightInfo {
    BreakingSpeedManager TopBlock = null;
    BreakingSpeedManager BottomBlock = null;
    WalkingSpeed WalkingTime = new WalkingSpeed(-1.0F);
    BlockPos CurrentBlock = null;
    WeightInfo PreviousWeightInfo = null;

    public WeightInfo(BlockPos pos,BreakingSpeedManager ItemSpeedTop, BreakingSpeedManager ItemSpeedBottom, WalkingSpeed walkingTime) {
        CurrentBlock = pos;
        TopBlock = ItemSpeedTop;
        BottomBlock = ItemSpeedBottom;
        WalkingTime = walkingTime;
    }

    public WeightInfo() {
    }

    @Override
    public String toString() {
        return "WeightInfo{" +
                "TopBlock=" + TopBlock.toString() +
                ", BottomBlock=" + BottomBlock.toString() +
                ", WalkingTime=" + WalkingTime.getTime() +
                (getPreviousBlock()!=null?", PreviousBlock=" + getPreviousBlock().toString():"")+
                ", PreviousWeight=" + PreviousWeightInfo.getTotal() +
                '}';
    }


    /**
     *
     * @return All the previous node's weight plus the current one.
     */
    public float getTotal() {
        return (PreviousWeightInfo!=null?PreviousWeightInfo.getTotal():0F) + TopBlock.getFullSpeed() + BottomBlock.getFullSpeed() + getContinuousWalkingTime();
    }

    public WalkingSpeed getWalkingTime() {
        return WalkingTime;
    }

    public float getPreviousWalkingTime(){
        return PreviousWeightInfo!=null?PreviousWeightInfo.getWalkingTime().getTime():0F;
    }

    public float getContinuousWalkingTime(){
        return distanceFromPrevious()*(getWalkingTime().getTime()+getPreviousWalkingTime())/2F;
    }

    public BlockPos getPreviousBlock() {
        return PreviousWeightInfo.CurrentBlock;
    }

    public boolean lessThan(@NotNull WeightInfo other) {
        return this.getTotal() < other.getTotal();
    }

    public WeightInfo append(@NotNull WeightInfo newLocation) {
        newLocation.PreviousWeightInfo = this;
        return newLocation;
    }

    public float distanceFromPrevious(){
        BlockPos prev = getPreviousBlock();
        return (prev!=null?MathHelper.sqrt(((float) CurrentBlock.withY(0).getSquaredDistance(prev.withY(0)))) + Math.abs(CurrentBlock.getY()-prev.getY()):0);
    }

    public boolean isUnattainable() {
        return false;
    }
}
