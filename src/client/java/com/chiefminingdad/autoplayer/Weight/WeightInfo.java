package com.chiefminingdad.autoplayer.Weight;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class WeightInfo {
    ItemBlockBreakingSpeed TopBlock = null;
    ItemBlockBreakingSpeed BottomBlock = null;
    WalkingSpeed WalkingTime = new WalkingSpeed(-1.0F);
    BlockPos CurrentBlock = null;
    SimpleWeightInfo PreviousWeightInfo = null;

    public WeightInfo(BlockPos pos,ItemBlockBreakingSpeed ItemSpeedTop, ItemBlockBreakingSpeed ItemSpeedBottom, WalkingSpeed walkingTime) {
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
                ", PreviousWeight=" + PreviousWeightInfo.TotalWeight +
                '}';
    }

    public float getTotal() {
        return (PreviousWeightInfo!=null?PreviousWeightInfo.TotalWeight:0F) + TopBlock.getFullSpeed() + BottomBlock.getFullSpeed() + getContinuousWalkingTime();
    }

    public WalkingSpeed getWalkingTime() {
        return WalkingTime;
    }
    public float getPreviousWalkingTime(){
        return PreviousWeightInfo!=null?PreviousWeightInfo.WalkingTime:0F;
    }

    public float getContinuousWalkingTime(){
        return distanceFromPrevious()*(getWalkingTime().getTime()+getPreviousWalkingTime())/2F;
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
        return new SimpleWeightInfo(getContinuousWalkingTime(), getTotal(), CurrentBlock);
    }

    public float distanceFromPrevious(){
        BlockPos prev = getPreviousBlock();
        return (prev!=null?MathHelper.sqrt(((float) CurrentBlock.withY(0).getSquaredDistance(prev.withY(0)))) + Math.abs(CurrentBlock.getY()-prev.getY()):0);
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
