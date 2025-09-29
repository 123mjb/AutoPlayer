package com.chiefminingdad.autoplayer.mixin.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientTickEvents.class)
public class DeclaringVariablesInTick {
//	@Inject(at = @At("CTOR_HEAD"), method = "<init>")
//	private void init(CallbackInfo info) {
//		// This code is injected into the start of CientTickEvents.<init>()
//	}


}