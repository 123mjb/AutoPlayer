package com.chiefminingdad.autoplayer;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoPlayer implements ModInitializer {
	public static final String MOD_ID = "autoplayer";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ArgumentTypeRegistry.registerArgumentType(
				Identifier.of("fabric-docs", "location_pos"),
				CustomClassHolder.DesiredLocationArgumentType.class,
				ConstantArgumentSerializer.of(CustomClassHolder.DesiredLocationArgumentType::new)
		);

		ModItems.initialize();
		DND.initialize();

		LOGGER.info("Hello Fabric world!");
	}
}

