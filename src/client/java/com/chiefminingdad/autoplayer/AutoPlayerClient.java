package com.chiefminingdad.autoplayer;

import net.fabricmc.api.ClientModInitializer;

public class AutoPlayerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ScreenManager screenManager = new ScreenManager();
		screenManager.init();
	}
}