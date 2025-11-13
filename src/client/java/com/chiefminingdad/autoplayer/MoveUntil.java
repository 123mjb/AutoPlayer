package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import com.chiefminingdad.autoplayer.CustomClassHolder.*;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class MoveUntil {
    public MinecraftClient Instance;
    public ClientPlayerEntity player;
    public PathFindingAlgo Algorithm;
    public KeyBinding Jump;
    public KeyBinding Forwards;
    public boolean Move = false;
    public boolean findPath = false;
    public float desiredRotation;
	public int desiredX=Integer.MAX_VALUE;
	public int desiredY=Integer.MAX_VALUE;
	public int desiredZ=Integer.MAX_VALUE;

    public KeyBinding getJump(){
        if(Jump==null){
            Jump = getInstance().options.jumpKey;
        }return Jump;
    }
    public KeyBinding getForwards(){
        if(Forwards==null){
            Forwards = getInstance().options.forwardKey;
        }return Forwards;
    }
    public MinecraftClient getInstance(){
        if(Instance==null){
            Instance = MinecraftClient.getInstance();
        }return Instance;
    }

    MoveUntil(MinecraftClient client, ClientPlayerEntity Player, PathFindingAlgo algorithm){
        Algorithm = algorithm;
        Instance = client;
        player = Player;
        if (player == null){
            player = getInstance().player;
        }
        if (getInstance().options!=null) {

            Jump = getInstance().options.jumpKey;
            Forwards = getInstance().options.forwardKey;
        }
        InitKeybind();
        InitCommand();

    }


    private void InitKeybind(){
        new KeyBindingBuilder.KeyBindtoRunningCode("ToggleMoving", GLFW.GLFW_KEY_J,true, "movecontroller", keyBinding -> {if(keyBinding.wasPressed()){this.Move = !this.Move;}});
    }

    private void InitCommand(){
        PersonalCommandManager.Register("moveto", serverCommandSourceCommandContext -> {
            DesiredLocation locs;
            try {
                locs = serverCommandSourceCommandContext.getArgument("locs", DesiredLocation.class);
            }
            catch(Exception e) {
                return 1;
            }
            var source = serverCommandSourceCommandContext.getSource();
            for(int i=0;i<locs.length();i++) {
                int finalI = i;
                try {
                    source.sendFeedback(() -> Text.literal("%s:%s".formatted(finalI,locs.getItem(finalI))), true);
                }
                catch (Exception e)
                {
                    source.sendFeedback(() -> Text.literal("%s".formatted(finalI)),true);
                }
            }
            source.sendFeedback(()-> Text.literal("Going To (%s,%s,%s)".formatted(locs.X,locs.Y,locs.Z)),false);
            SetVars(locs.X,locs.Y,locs.Z);
            //source.sendFeedback(()->Text.literal("Success"),false);
            return 0;
        });
    }

    public void init(@NotNull MoveUntil moveUntil){
        ClientTickEvents.END_CLIENT_TICK.register(moveUntil::MoveCorrectDirection);
    }

    /**
     * Default Values:
     * 0, 0, 0, 0.0F
     */
    public void SetVars() {
        SetVars(0, 0, 0, 0.0F);
    }

    public void SetVars(int x, int y, int z, float rot){
        this.desiredRotation = rot;
        this.desiredX = x;
        this.desiredY = y;
        this.desiredZ = z;
    }
    public void SetVars(int x,int y,int z){
        this.desiredRotation = 0;
        this.desiredX = x;
        this.desiredY = y;
        this.desiredZ = z;
        this.Algorithm.reset();
        this.Algorithm.FindPath(desiredX,desiredY,desiredZ);
        this.findPath = true;
    }

    float fl = 0;
    public void MoveCorrectDirection(MinecraftClient cl){
        fl++;
        if (findPath){
            findPath = !Algorithm.doPathFinding();
        }
        else {
            if (player != null) {
                if (fl%20<2) Algorithm.spawnParticlesOnPath(player.getEntityWorld());
            } else {
                player = getInstance().player;
            }
        }
    }
}
