package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class CustomClassHolder {
    public static class DesiredLocation{
        public int X,Y,Z;
        public boolean AddX = false,AddY = false,AddZ = false;
        public DesiredLocation(int x,int y, int z,boolean addx,boolean addy,boolean addz){
            X=x;Y=y;Z=z;
            AddX=addx;AddY=addy;AddZ=addz;
        }
        public DesiredLocation(int x,int y, int z){
            X=x;Y=y;Z=z;
        }
        public int getindex(int index){
            return index>0?(index==1?Y:Z):X;
        }

        public int length(){return 3;}
    }

    public static class DesiredLocationArgumentType implements ArgumentType<DesiredLocation> {
        @Override
        public DesiredLocation parse(StringReader reader) throws CommandSyntaxException {
//            String location = reader.readUnquotedString();
            int x,y,z;

            String string = reader.readString();
            String[] split = string.split(",",3);

            try{x = Integer.parseInt(split[0]);} catch(Exception e) {x = Integer.MAX_VALUE;}
            try{y = Integer.parseInt(split[1]);} catch(Exception e) {y = Integer.MAX_VALUE;}
            try{z = Integer.parseInt(split[2]);} catch(Exception e) {z = Integer.MAX_VALUE;}

            return new DesiredLocation(x,y,z);

        }
    }
}
