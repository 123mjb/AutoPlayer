package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import com.chiefminingdad.autoplayer.CustomClassHolder.*;
import org.lwjgl.glfw.GLFW;

public class MoveUntil {
    public final MinecraftClient Instance;
    public final ClientPlayerEntity player;
    public KeyBinding Jump;
    public KeyBinding Forwards;
    public boolean Move = false;
    public float desiredRotation;
	public int desiredX=Integer.MAX_VALUE;
	public int desiredY=Integer.MAX_VALUE;
	public int desiredZ=Integer.MAX_VALUE;

    MoveUntil(MinecraftClient client, ClientPlayerEntity Player){
        Instance = client;
        player = Player;
        if (Instance.options!=null) {
            Jump = Instance.options.jumpKey;
            Forwards = Instance.options.forwardKey;
        }
        InitKeybind();
        InitCommand();

    }

    private void InitKeybind(){
        new KeyBindingBuilder.KeyBindtoRunningCode("ToggleMoving", GLFW.GLFW_KEY_J, "movecontroller", keyBinding -> this.Move = !this.Move);
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
            return 1;
        });
    }

    public void init(MoveUntil moveUntil){
        ClientTickEvents.END_CLIENT_TICK.register(client -> moveUntil.MoveCorrectDirection());
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
    }

    public void MoveCorrectDirection(){
        if (this.Move) {
            player.setYaw(this.desiredRotation);

            if (desiredX != player.getX() | desiredY != player.getY() | desiredZ != player.getZ()) {
                Jump.setPressed(desiredY > player.getY());
                Forwards.setPressed(desiredX != player.getX() | desiredZ != player.getZ());
            }
            else this.Move = false;
        }

    }
}
