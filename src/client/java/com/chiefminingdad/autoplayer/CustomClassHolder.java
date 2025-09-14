package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.Optional;

public class CustomClassHolder {
    public class DesiredLocationArgumentType implements ArgumentType<Integer[]> {
        @Override
        public Integer[] parse(StringReader reader) throws CommandSyntaxException {
            Optional<Integer> x;
            Optional<Integer> y;
            Optional<Integer> z;
            String location = reader.readUnquotedString();
            String[] split = location.split(",");
            x = !split[0].isEmpty()?Integer.parseInt(split[0]):null;
            y = Integer.parseInt(split[1]);
            z = Integer.parseInt(split[2]);

            return new Integer[]{x!=?x:null, y, z};

        }
    }
}
