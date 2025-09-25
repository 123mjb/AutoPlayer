package com.chiefminingdad.autoplayer;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

public class CustomClassHolder {
    public static class DesiredLocation{
        public int X,Y,Z;
        public boolean AddX = false,AddY = false,AddZ = false;
        public DesiredLocation(int x, int y, int z, boolean addX, boolean addY, boolean addZ){
            X=x;Y=y;Z=z;
            AddX= addX;AddY= addY;AddZ= addZ;
        }
        public DesiredLocation(int x, int y, int z, boolean @NotNull [] adds){
            X=x;Y=y;Z=z;
            AddX= adds[0];AddY= adds[1];AddZ= adds[2];
        }
        public DesiredLocation(int x,int y, int z){
            X=x;Y=y;Z=z;
        }
        public int getitem(int index){
            return index>0?(index==1?Y:Z):X;
        }

        public int length(){return 3;}
    }

    public static class DesiredLocationArgumentType implements ArgumentType<DesiredLocation> {
        @Override
        public DesiredLocation parse(@NotNull StringReader reader) throws CommandSyntaxException {
//            String location = reader.readUnquotedString();
            int x,y,z;
            boolean[] adds = new  boolean[]{false,false,false};

            String string = reader.readString();
            String[] split = string.split(",",3);
            for (int i=0;i<split.length;i++) {
                if (split[i].contains("~")) {
                    split[i] = split[i].replace("~", "");
                    adds[i] = true;
                }
            }
            try{x = Integer.parseInt(split[0]);} catch(Exception e) {x = Integer.MAX_VALUE;}
            try{y = Integer.parseInt(split[1]);} catch(Exception e) {y = Integer.MAX_VALUE;}
            try{z = Integer.parseInt(split[2]);} catch(Exception e) {z = Integer.MAX_VALUE;}

            return new DesiredLocation(x,y,z,adds);

        }
    }
}
