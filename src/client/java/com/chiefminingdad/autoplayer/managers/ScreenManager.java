package com.chiefminingdad.autoplayer.managers;

import com.chiefminingdad.autoplayer.CustomScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

import static com.chiefminingdad.autoplayer.managers.KeyBindingBuilder.*;

public class ScreenManager {
    KeyBindtoRunningCode keyBindingTest;

    public void init(){

        // make the keybind show the screen
        Consumer<KeyBinding> Code = keyBinding->{
            if (keyBinding.wasPressed()) {
                Screen currentScreen = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(
                        new CustomScreen(Text.empty(), currentScreen)
                );
            }
        };

        keyBindingTest = new KeyBindtoRunningCode("test", GLFW.GLFW_KEY_V,true, "test", Code);
    }
}
