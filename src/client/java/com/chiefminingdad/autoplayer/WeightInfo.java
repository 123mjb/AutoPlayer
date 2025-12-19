package com.chiefminingdad.autoplayer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class WeightInfo {
    ItemBlockBreakingSpeed TopBlock = null;
    ItemBlockBreakingSpeed BottomBlock = null;
    float WalkingTime = -1.0F;
    BlockPos CurrentBlock = null;
    SimpleWeightInfo PreviousWeightInfo = null;

    public WeightInfo(BlockPos pos,ItemBlockBreakingSpeed ItemSpeedTop, ItemBlockBreakingSpeed ItemSpeedBottom, float walkingTime) {
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
                ", WalkingTime=" + WalkingTime +
                (getPreviousBlock()!=null?", PreviousBlock=" + getPreviousBlock().toString():"")+
                ", PreviousWeight=" + PreviousWeightInfo.TotalWeight +
                '}';
    }

    public float getTotal() {
        return (PreviousWeightInfo!=null?PreviousWeightInfo.TotalWeight:0F) + TopBlock.getFullSpeed() + BottomBlock.getFullSpeed() + getContinuousWalkingTime();
    }

    public float getWalkingTime() {
        return WalkingTime;
    }
    public float getPreviousWalkingTime(){
        return PreviousWeightInfo!=null?PreviousWeightInfo.WalkingTime:0F;
    }

    public float getContinuousWalkingTime(){
        return distanceFromPrevious()*(getWalkingTime()+getPreviousWalkingTime())/2;
    }

    public BlockPos getPreviousBlock() {
        return PreviousWeightInfo.Pos;
    }

    public boolean lessThan(@NotNull WeightInfo other) {
        return this.getTotal() < other.getTotal();
    }

    public WeightInfo append(@NotNull WeightInfo newLocation) {
        newLocation.PreviousWeightInfo = makeSimple();
        return newLocation;
    }

    public SimpleWeightInfo makeSimple(){
        return new SimpleWeightInfo(getWalkingTime(), getTotal(), CurrentBlock);
    }

    public float distanceFromPrevious(){
        return MathHelper.sqrt((getPreviousBlock()!=null?(float) CurrentBlock.withY(0).getSquaredDistance(getPreviousBlock().withY(0)):0));
    }

    public boolean isUnattainable() {
        return false;
    }

    public static class SimpleWeightInfo {
        final float WalkingTime;
        final float TotalWeight;
        final BlockPos Pos;
        public SimpleWeightInfo(float walkingTime, float totalWeight, BlockPos pos){
            WalkingTime = walkingTime;
            TotalWeight = totalWeight;
            Pos = pos;
        }
    }
}
