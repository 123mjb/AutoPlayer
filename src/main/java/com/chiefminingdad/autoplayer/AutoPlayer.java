package com.chiefminingdad.autoplayer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoPlayer implements ModInitializer {
	public static final String MOD_ID = "autoplayer";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private final Registerer registerer = new Registerer();
    public static final SimpleParticleType SPARKLE_PARTICLE = FabricParticleTypes.simple();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "sparkle_particle"), SPARKLE_PARTICLE);

		ModItems.initialize();
		DND.initialize();

		LOGGER.info("Hello Fabric world!");
	}
}

