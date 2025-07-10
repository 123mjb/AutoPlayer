package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class MoveUntil {
    public ClientPlayerEntity player;
    public boolean Move = false;
    public float desiredrotation;
	public int desiredx;
	public int desiredy;
	public int desiredz;

    MoveUntil(){
        if (MinecraftClient.getInstance()!=null) player = MinecraftClient.getInstance().player;
    }

    public void init(MoveUntil moveUntil){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            moveUntil.MoveCorrectDirection();
        });
    }

    public void SetVars(int x,int y,int z, float rot){
        this.desiredrotation = rot;
        this.desiredx = x;
        this.desiredy = y;
        this.desiredz = z;

    }

    public void MoveCorrectDirection(){
        if (this.Move) {
            player.setYaw(this.desiredrotation);
        }
    }

}
