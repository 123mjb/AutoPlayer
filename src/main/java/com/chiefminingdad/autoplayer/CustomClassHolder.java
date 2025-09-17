package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class CustomClassHolder {
    public static class DesiredLocationArgumentType implements ArgumentType<Integer[]> {
        @Override
        public Integer[] parse(StringReader reader) throws CommandSyntaxException {
//            String location = reader.readUnquotedString();
            int x,y,z;

            String string = reader.readString();
            String[] split = string.split(",",3);

            try{x = Integer.parseInt(split[0]);} catch(Exception e) {x = Integer.MAX_VALUE;}
            try{y = Integer.parseInt(split[1]);} catch(Exception e) {y = Integer.MAX_VALUE;}
            try{z = Integer.parseInt(split[2]);} catch(Exception e) {z = Integer.MAX_VALUE;}

            return new Integer[] {x,y,z};

        }
    }
}
