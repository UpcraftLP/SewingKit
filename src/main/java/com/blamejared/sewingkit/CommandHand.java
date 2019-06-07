package com.blamejared.sewingkit;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.item.MCItemStack;
import com.blamejared.sewingkit.api.tag.MCTag;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.network.chat.*;
import net.minecraft.server.command.*;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.Collection;


public class CommandHand {
    
    public static void register(CommandDispatcher<ServerCommandSource> cd) {
        cd.register(CommandManager.literal("skhand").requires((var0x) -> var0x.hasPermissionLevel(3)).executes((var0x) -> {
            String filepath = new File("sewingkit.log").getAbsolutePath();
            Style style = new Style();
            ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_FILE, filepath);
            style.setClickEvent(click);
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("Click to open [\u00A76" + filepath + "\u00A7r]"));
            style.setHoverEvent(hoverEvent);
            SKApi.logger.logCommand(new MCItemStack(var0x.getSource().getPlayer().inventory.getMainHandStack()).toCommandString());
            Collection<Identifier> tagsFor = ItemTags.getContainer().getTagsFor(var0x.getSource().getPlayer().inventory.getMainHandStack().getItem());
            SKApi.logger.logCommand("Tags");
            var0x.getSource().sendFeedback(new TranslatableComponent("Item: \u00A72" + new MCItemStack(var0x.getSource().getPlayer().inventory.getMainHandStack()).toCommandString() + "\u00A7a"), true);
            for(Identifier identifier : tagsFor) {
                MCTag tag = new MCTag(ItemTags.getContainer().get(identifier));
                SKApi.logger.logCommand("- " + tag.toString());
                var0x.getSource().sendFeedback(new TranslatableComponent("- " + tag.toString()), true);
            }
            var0x.getSource().sendFeedback(new TranslatableComponent("Open log").setStyle(style), true);
            return 0;
        }));
    }
    
}
