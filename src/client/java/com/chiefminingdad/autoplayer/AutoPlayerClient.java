package com.chiefminingdad.autoplayer;

import com.chiefminingdad.autoplayer.records.ChunksS2CConfirmation;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.world.World;

import static com.chiefminingdad.autoplayer.KeyBindingBuilder.*;

public class AutoPlayerClient implements ClientModInitializer {
    MinecraftClient client;
    ClientPlayerEntity player;
    World world;


	MoveUntil moveUntil;
    PathFindingAlgo PathFinding;
	final ScreenManager screenManager = new ScreenManager();
	KeyBindtoRunningCode Move2Secs;


	@Override
	public void onInitializeClient() {
        SetVars();
        InitializeClasses();

        ClientPlayNetworking.registerGlobalReceiver(ChunksS2CConfirmation.ID,(payload,context)->{
           if (!payload.canSend())PathFinding.blockManager.AddUnavailable(payload.BlockLoc());
        });

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		this.screenManager.init();
		this.moveUntil.init(this.moveUntil);

        ParticleFactoryRegistry.getInstance().register(AutoPlayer.SPARKLE_PARTICLE, EndRodParticle.Factory::new);


    }
    private void SetVars(){
        client = MinecraftClient.getInstance();
        if (client.player != null) {
            player = client.player;
        }
        world = client.world;
    }

    private void InitializeClasses(){
        PathFinding = new PathFindingAlgo(player, world);
        moveUntil = new MoveUntil(client,player,PathFinding);
    }
}