package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyBindingBuilder {
    public static KeyBinding BuildKeyBind(String name,  Boolean keyboard, int key,String category){
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autoplayer."+name,
                keyboard? InputUtil.Type.KEYSYM:InputUtil.Type.MOUSE,
                key,
                "category.autoplayer."+category
        ));
    }
}
