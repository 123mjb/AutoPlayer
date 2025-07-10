package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class MoveUntil {
    public double desiredrotation;
	int desiredx;
	int desiredy;
	int desiredz;

    public void init(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MinecraftClient.getInstance()
        });
    }

    public void SetVars(int x,int y,int z, double rot){
        this.desiredrotation = rot;
        this.desiredx = x;
        this.desiredy = y;
        this.desiredz = z;
    }

}
