package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.chiefminingdad.autoplayer.KeyBindingBuilder.*;
import static com.chiefminingdad.autoplayer.PersonalCommandManager.*;

public class AutoPlayerClient implements ClientModInitializer {
	MoveUntil moveUntil = new MoveUntil();
	ScreenManager screenManager = new ScreenManager();
	KeyBindtoRunningCode Move2Secs;


	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		this.screenManager.init();

		AtomicBoolean MoveForwards = new AtomicBoolean(false);
		AtomicInteger TimeLeft = new AtomicInteger();
		this.Move2Secs= new KeyBindtoRunningCode("StartMoving", true, GLFW.GLFW_KEY_J, "movecontroller", keyBinding -> {if (keyBinding.wasPressed()) {if (!MoveForwards.get()){ MoveForwards.set(true);
				TimeLeft.set(40);
			}}if (MoveForwards.get()){
			if(TimeLeft.get()==0){
				MoveForwards.set(false);
				MinecraftClient.getInstance().options.forwardKey.setPressed(false);
			}
			else{
				MinecraftClient.getInstance().options.forwardKey.setPressed(true);
				TimeLeft.set(TimeLeft.get()-1);
			}
		}});

		this.moveUntil.init(this.moveUntil);

		PersonalCommandManager.Register("MoveTo", new Function<CommandContext<ServerCommandSource>, Integer>() {
			@Override
			public Integer apply(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
				try {
					final int x = IntegerArgumentType.getInteger(serverCommandSourceCommandContext, "x");
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				final int y = IntegerArgumentType.getInteger(serverCommandSourceCommandContext,"y");
				final int z = IntegerArgumentType.getInteger(serverCommandSourceCommandContext,"z");

				return 0;
			}
		});


	}
}