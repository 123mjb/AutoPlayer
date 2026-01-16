package com.chiefminingdad.autoplayer.Weight;

public class WalkingSpeed {
    final float Time;
    final boolean Placing;

    public WalkingSpeed(float time, boolean placing){
        Time = time;
        Placing = placing;
    }
    public WalkingSpeed(float time){
        Time = time;
        Placing = false;
    }
    public float getTime(){
        return Time+(Placing?10F:0F);
    }
}
