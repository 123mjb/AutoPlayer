package com.chiefminingdad.autoplayer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoPlayerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ScreenManager screenManager = new ScreenManager();
		screenManager.init();

		KeyBinding StartMovingKeyBind = KeyBindingBuilder.BuildKeyBind("StartMoving", true, GLFW.GLFW_KEY_J, "movecontroller");

		AtomicBoolean MoveForwards = new AtomicBoolean(false);
		AtomicInteger TimeLeft = new AtomicInteger();
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (StartMovingKeyBind.wasPressed()) {
				if (!MoveForwards.get()){ MoveForwards.set(true);
					TimeLeft.set(40);
				}

			}
			if (MoveForwards.get()){
				if(TimeLeft.get()==0){
					MoveForwards.set(false);
					MinecraftClient.getInstance().options.forwardKey.setPressed(false);
				}
				else{
					MinecraftClient.getInstance().options.forwardKey.setPressed(true);
					TimeLeft.set(TimeLeft.get()-1);
				}
			}
		});
	}
}