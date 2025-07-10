package com.chiefminingdad.autoplayer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AutoPlayerClient implements ClientModInitializer {
	MoveUntil moveUntil = new MoveUntil();

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ScreenManager screenManager = new ScreenManager();
		screenManager.init();

		AtomicBoolean MoveForwards = new AtomicBoolean(false);
		AtomicInteger TimeLeft = new AtomicInteger();
		KeyBindingBuilder.KeyBindtoRunningCode  Move2Secs= new KeyBindingBuilder.KeyBindtoRunningCode("StartMoving", true, GLFW.GLFW_KEY_J, "movecontroller",(Consumer<KeyBinding>)  (keyBinding -> {if (keyBinding.wasPressed()) {
			if (!MoveForwards.get()){ MoveForwards.set(true);
				TimeLeft.set(40);
			}

		};if (MoveForwards.get()){
			if(TimeLeft.get()==0){
				MoveForwards.set(false);
				MinecraftClient.getInstance().options.forwardKey.setPressed(false);
			}
			else{
				MinecraftClient.getInstance().options.forwardKey.setPressed(true);
				TimeLeft.set(TimeLeft.get()-1);
			}
		}}));

		moveUntil.init(moveUntil);
	}
}