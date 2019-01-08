package com.blamejared.sewingkit;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.item.MCItemStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.*;
import net.minecraft.text.*;
import net.minecraft.text.event.*;

import java.io.File;


public class CommandHand {
    
    public static void register(CommandDispatcher<ServerCommandSource> cd) {
        String filepath = new File("sewingkit.log").getAbsolutePath();
        Style style = new Style();
        ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_FILE, filepath);
        style.setClickEvent(click);
        
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableTextComponent("Click to open [\u00A76" + filepath + "\u00A7r]"));
        style.setHoverEvent(hoverEvent);
        
        cd.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ServerCommandManager.literal("skhand").requires((var0x) -> var0x.hasPermissionLevel(3))).executes((var0x) -> {
            SKApi.logger.logCommand(new MCItemStack(((ServerCommandSource) var0x.getSource()).getPlayer().inventory.getMainHandStack()).toCommandString());
            ((ServerCommandSource) var0x.getSource()).sendFeedback(new TranslatableTextComponent("Item: \u00A72" + new MCItemStack(((ServerCommandSource) var0x.getSource()).getPlayer().inventory.getMainHandStack()).toCommandString() + "\u00A7a", new Object[0]), true);
            ((ServerCommandSource) var0x.getSource()).sendFeedback(new TranslatableTextComponent("Open log", new Object[0]).setStyle(style), true);
            return 0;
        }));
    }
    
}
