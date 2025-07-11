package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;

public class MoveUntil {
    public MinecraftClient Instance;
    public ClientPlayerEntity player;
    public KeyBinding Jump;
    public KeyBinding Forwards;
    public boolean Move = false;
    public float desiredRotation;
	public int desiredX;
	public int desiredY;
	public int desiredZ;

    MoveUntil(){
        if (MinecraftClient.getInstance()!=null) {
            Instance = MinecraftClient.getInstance();
            player = Instance.player;
            Jump =Instance.options.jumpKey;
            Forwards = Instance.options.forwardKey;
        }
    }

    public void init(MoveUntil moveUntil){
        ClientTickEvents.END_CLIENT_TICK.register(client -> moveUntil.MoveCorrectDirection());
    }

    public void SetVars(int x,int y,int z, float rot){
        this.desiredRotation = rot;
        this.desiredX = x;
        this.desiredY = y;
        this.desiredZ = z;

    }

    public void MoveCorrectDirection(){
        if (this.Move) {
            player.setYaw(this.desiredRotation);

            if (desiredX != player.getX() | desiredY != player.getY() | desiredZ != player.getZ()) {
                Jump.setPressed(desiredY > player.getY());
                Forwards.setPressed(desiredX != player.getX() | desiredZ != player.getZ());
            }
            else this.Move = false;
        }

    }

}
