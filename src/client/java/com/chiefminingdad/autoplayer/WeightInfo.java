package com.chiefminingdad.autoplayer;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class WeightInfo {
    ItemBlockBreakingSpeed TopBlock = null;
    ItemBlockBreakingSpeed BottomBlock = null;
    float WalkingTime = -1.0F;
    BlockPos PreviousBlock = null;
    float PreviousWeight = 0F;

    public WeightInfo(ItemBlockBreakingSpeed ItemSpeedTop, ItemBlockBreakingSpeed ItemSpeedBottom, float walkingTime) {
        TopBlock = ItemSpeedTop;
        BottomBlock = ItemSpeedBottom;
        WalkingTime = walkingTime;
    }

    public WeightInfo(ItemBlockBreakingSpeed ItemSpeedTop, ItemBlockBreakingSpeed ItemSpeedBottom, float walkingTime, BlockPos previousPos, float previousWeight) {
        TopBlock = ItemSpeedTop;
        BottomBlock = ItemSpeedBottom;
        WalkingTime = walkingTime;
        PreviousWeight = previousWeight;
        PreviousBlock = previousPos;
    }

    public WeightInfo() {
    }

    @Override
    public String toString() {
        return "WeightInfo{" +
                "TopBlock=" + TopBlock.toString() +
                ", BottomBlock=" + BottomBlock.toString() +
                ", WalkingTime=" + WalkingTime +
                ", PreviousBlock=" + PreviousBlock.toString() +
                ", PreviousWeight=" + PreviousWeight +
                '}';
    }

    public float Total() {
        return PreviousWeight + TopBlock.getFullSpeed() + BottomBlock.getFullSpeed() + WalkingTime;
    }

    public boolean lessThan(@NotNull WeightInfo other) {
        return this.Total() < other.Total();
    }

    public WeightInfo append(@NotNull WeightInfo newLocation, BlockPos oldPos) {
        newLocation.PreviousWeight = Total();
        newLocation.PreviousBlock = oldPos;
        return newLocation;
    }

    public boolean isUnattainable() {
        return false;
    }
}
