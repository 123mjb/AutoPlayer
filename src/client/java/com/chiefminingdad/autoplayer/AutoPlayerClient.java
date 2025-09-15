package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.chiefminingdad.autoplayer.KeyBindingBuilder.*;

public class AutoPlayerClient implements ClientModInitializer {
	MoveUntil moveUntil = new MoveUntil();
	ScreenManager screenManager = new ScreenManager();
	KeyBindtoRunningCode Move2Secs;


	@Override
	public void onInitializeClient() {
		ArgumentTypeRegistry.registerArgumentType(
				Identifier.of("fabric-docs", "location_pos"),
				CustomClassHolder.DesiredLocationArgumentType.class,
				ConstantArgumentSerializer.of(CustomClassHolder.DesiredLocationArgumentType::new)
		);
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

		PersonalCommandManager.Register("moveto", serverCommandSourceCommandContext -> {
			int x,y,z;
			try {
				x = IntegerArgumentType.getInteger(serverCommandSourceCommandContext, "x");
				y = IntegerArgumentType.getInteger(serverCommandSourceCommandContext, "y");
				z = IntegerArgumentType.getInteger(serverCommandSourceCommandContext, "z");
			}
			catch(Exception e) {
				return 1;
			}
			serverCommandSourceCommandContext.getSource().sendFeedback(()-> Text.literal("Going To (%s,%s,%s)".formatted(x,y,z)),false);
			return 1;
        });


	}
}