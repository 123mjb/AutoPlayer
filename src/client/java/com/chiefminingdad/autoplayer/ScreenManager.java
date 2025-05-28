package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ScreenManager {
    KeyBinding keyBindingTest;

    protected void init(){
        // make keybind
        keyBindingTest = KeyBindingBuilder.BuildKeyBind("test", true, GLFW.GLFW_KEY_V, "test");

        // make the keybind show the screen
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBindingTest.wasPressed()) {
                Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(
                        new CustomScreen(Text.empty(), currentScreen)
                );
            }
        });
    }
}
