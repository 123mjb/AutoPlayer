package com.chiefminingdad.autoplayer;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import static com.chiefminingdad.autoplayer.KeyBindingBuilder.*;

public class AutoPlayerClient implements ClientModInitializer {
    MinecraftClient client;
    ClientPlayerEntity player;
    World world;


	MoveUntil moveUntil;
    PathFindingAlgo PathFinding;
	ScreenManager screenManager = new ScreenManager();
	KeyBindtoRunningCode Move2Secs;


	@Override
	public void onInitializeClient() {
        SetVars();
        InitializeClasses();



		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		this.screenManager.init();
		this.moveUntil.init(this.moveUntil);




	}
    private void SetVars(){
        client = MinecraftClient.getInstance();
        if (client.player != null) {
            player = client.player;
        }
        world = client.world;
    }

    private void InitializeClasses(){
        moveUntil = new MoveUntil(client,player);
        PathFinding = new PathFindingAlgo(player, world);
    }
}