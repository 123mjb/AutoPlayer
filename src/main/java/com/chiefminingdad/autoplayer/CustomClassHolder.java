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

//            int x = Integer.parseInt(split[0].trim());
//            int y = Integer.parseInt(split[1].trim());
//            int z = Integer.parseInt(split[2].trim());
            try{x = Integer.parseInt(split[0]);} catch(Exception e) {x = Integer.MAX_VALUE;}
            try{y = Integer.parseInt(split[1]);} catch(Exception e) {y = Integer.MAX_VALUE;}
            try{z = Integer.parseInt(split[2]);} catch(Exception e) {z = Integer.MAX_VALUE;}

//            x = (!split[0].trim().isEmpty())?Integer.parseInt(split[0]):null;
//            y = (!split[1].trim().isEmpty())?Integer.parseInt(split[1]):null;
//            z = (!split[2].trim().isEmpty())?Integer.parseInt(split[2]):null;

            return new Integer[] {x,y,z};

        }
    }
}
