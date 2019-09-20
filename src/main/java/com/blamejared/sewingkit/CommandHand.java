package com.blamejared.sewingkit;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.item.MCItemStack;
import com.blamejared.sewingkit.api.tag.MCTag;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.ItemTags;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.Collection;


public class CommandHand {
    
    public static void register(CommandDispatcher<ServerCommandSource> cd) {
        cd.register(CommandManager.literal("skhand").requires((var0x) -> var0x.hasPermissionLevel(3)).executes(context -> {
            String filepath = new File("sewingkit.log").getAbsolutePath();
            Style style = new Style();
            ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_FILE, filepath);
            style.setClickEvent(click);
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click to open [\u00A76" + filepath + "\u00A7r]"));
            style.setHoverEvent(hoverEvent);
            SKApi.logger.logCommand(new MCItemStack(context.getSource().getPlayer().inventory.getMainHandStack()).toCommandString());
            Collection<Identifier> tagsFor = ItemTags.getContainer().getTagsFor(context.getSource().getPlayer().inventory.getMainHandStack().getItem());
            SKApi.logger.logCommand("Tags");
            context.getSource().sendFeedback(new LiteralText("Item: \u00A72" + new MCItemStack(context.getSource().getPlayer().inventory.getMainHandStack()).toCommandString() + "\u00A7a"), true);
            for(Identifier identifier : tagsFor) {
                MCTag tag = new MCTag(ItemTags.getContainer().get(identifier));
                SKApi.logger.logCommand("- " + tag.toString());
                context.getSource().sendFeedback(new LiteralText("- " + tag.toString()), true);
            }
            context.getSource().sendFeedback(new LiteralText("Open log").setStyle(style), true);
            return 0;
        }));
    }
    
}
