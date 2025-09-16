package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class CustomClassHolder {
    public static class DesiredLocationArgumentType implements ArgumentType<Integer[]> {
        @Override
        public Integer[] parse(StringReader reader) throws CommandSyntaxException {
            Integer x;
            Integer y;
            Integer z;
            String location = reader.readUnquotedString();
            String[] split = location.split(",");
            x = !split[0].isEmpty()?Integer.parseInt(split[0]):null;
            y = !split[1].isEmpty()?Integer.parseInt(split[1]):null;
            z = !split[2].isEmpty()?Integer.parseInt(split[2]):null;

            return new Integer[] {x,y,z};

        }
    }
}
