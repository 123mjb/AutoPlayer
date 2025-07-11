package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Consumer;

public class PersonalCommandManager {
    PersonalCommandManager(){
        Register("MoveTo", ctx->{});
    }

    public void Register(String Name, Consumer<CommandContext<ServerCommandSource>> code) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment)->{
            dispatcher.register(CommandManager.literal(Name).executes(MinecraftClient.getInstance().A));
        });
    }
}
