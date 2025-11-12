package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class DebugInfo {
    boolean renderDebug = false;
    String debugText;
    Tessellator t;

    public DebugInfo(){
        new KeyBindingBuilder.KeyBindtoRunningCode("ToggleDebug", GLFW.GLFW_KEY_F3,true,"autoplayerdebug",keybinding->{renderDebug = !renderDebug;});
        t = Tessellator.getInstance();
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.of(AutoPlayer.MOD_ID,"debuginfo"),this::render);
    }
    public void Out(String text){
        debugText = text;
    }
    public void render(DrawContext context, RenderTickCounter counter){
        if(renderDebug){
            context.drawText(new TextRenderer());
        }
    }

}
