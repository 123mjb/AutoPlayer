package com.chiefminingdad.autoplayer.mixin.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientTickEvents.class)
public class DeclaringVariablesInTick {
//	@Inject(at = @At("CTOR_HEAD"), method = "<init>")
//	private void init(CallbackInfo info) {
//		// This code is injected into the start of CientTickEvents.<init>()
//	}


}