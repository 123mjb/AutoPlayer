package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class DebugInfo {

    public DebugInfo(){
        new KeyBindingBuilder.KeyBindtoRunningCode("ToggleDebug", GLFW.GLFW_KEY_F3,true,"autoplayerdebug",keybinding->{});
    }
}
