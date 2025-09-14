package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import static net.minecraft.server.command.CommandManager.*;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Function;


public class PersonalCommandManager {

    public static void Register(String Name, Function<CommandContext<ServerCommandSource>, Integer> code) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment)-> dispatcher.register(literal(Name).
                then(argument("x", IntegerArgumentType.integer()).executes(code::apply)
                        .then(argument("y", IntegerArgumentType.integer()).executes(code::apply)
                                .then(argument("z", IntegerArgumentType.integer()).executes(code::apply))
                        )
                )
        ));
    }
}
