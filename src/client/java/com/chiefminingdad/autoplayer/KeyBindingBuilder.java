package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.function.Consumer;

public class KeyBindingBuilder {
    public static KeyBinding BuildKeyBind(String name,  Boolean keyboard, int key,String category){
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autoplayer."+name,
                keyboard? InputUtil.Type.KEYSYM:InputUtil.Type.MOUSE,
                key,
                "category.autoplayer."+category
        ));
    }
    public static class KeyBindtoRunningCode{
        public KeyBinding keyBinding;
        KeyBindtoRunningCode(String name, Boolean keyboard, int key, String category, Consumer<KeyBinding> code){
            keyBinding = KeyBindingBuilder.BuildKeyBind(name,keyboard,key,category);

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                code.accept(keyBinding);
            });
        }
    }
}
