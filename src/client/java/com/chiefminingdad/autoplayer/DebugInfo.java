package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class DebugInfo {
    boolean renderDebug = false;
    String debugHeading;
    String debugSubHeading;
    TextRenderer textRenderer;

    public TextRenderer getTextRenderer() {
        if(textRenderer==null)textRenderer = MinecraftClient.getInstance().textRenderer;
        return textRenderer;
    }

    int black = 0xffffffff;

    public DebugInfo(){
        new KeyBindingBuilder.KeyBindtoRunningCode("ToggleDebug", GLFW.GLFW_KEY_F3,true,"autoplayerdebug",keybinding-> renderDebug = !renderDebug);
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.of(AutoPlayer.MOD_ID,"debuginfo"),this::render);
    }
    public void Heading(String text){
        debugHeading = text;
    }
    public void SubHeading(String text){
        debugHeading = text;
    }

    public void render(DrawContext context, RenderTickCounter counter){
        if(renderDebug){
            if (debugHeading!=null)
                context.drawText(getTextRenderer(), Text.literal(debugHeading),0, context.getScaledWindowHeight() /2,black,false);
            if (debugSubHeading!=null)
                context.drawText(getTextRenderer(), Text.literal(debugSubHeading),0, context.getScaledWindowHeight() /2+textRenderer.fontHeight,black,false);
        }
    }

}
