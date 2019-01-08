package com.blamejared.sewingkit.api.item;

import com.blamejared.sewingkit.api.SKApi;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.sortme.JsonLikeTagParser;
import net.minecraft.util.registry.Registry;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("sk.Itemstack")
public class MCItemStack implements ZenCodeType {
    
    private ItemStack stack;
    
    public MCItemStack(ItemStack stack) {
        this.stack = stack;
    }
    
    @Getter("name")
    public String getName() {
        return stack.getDisplayName().getFormattedText();
    }
    
    @Getter("amount")
    public int getAmount() {
        return stack.getAmount();
    }
    
    @Operator(OperatorType.MUL)
    public MCItemStack setAmount(int amount) {
        ItemStack copy = stack.copy();
        copy.setAmount(amount);
        return new MCItemStack(copy);
    }
    
    @Method
    public MCItemStack withTag(String tag) {
        ItemStack copy = stack.copy();
        try {
            copy.setTag(JsonLikeTagParser.parse(tag));
        } catch(CommandSyntaxException e) {
            SKApi.logger.logError(e.getMessage());
        }
        return new MCItemStack(copy);
    }
    
    @Getter("tag")
    public String getTag() {
        return quoteAndEscape(stack.getTag().toString());
    }
    
    @Getter("commandString")
    public String toCommandString() {
        return String.format("<item:%s>%s%s", Registry.ITEM.getId(stack.getItem()), stack.hasTag() ? ".withTag(\"" + getTag() + "\")" : "", getAmount() > 1 ? (" * " + getAmount()) : "");
    }
    
    public ItemStack getStack() {
        return stack;
    }
    
    @Override
    public String toString() {
        return toCommandString();
    }
    
    public static String quoteAndEscape(String p_193588_0_) {
        StringBuilder stringbuilder = new StringBuilder("");
        
        for(int i = 0; i < p_193588_0_.length(); ++i) {
            char c0 = p_193588_0_.charAt(i);
            
            if(c0 == '\\' || c0 == '"') {
                stringbuilder.append('\\');
            }
            
            stringbuilder.append(c0);
        }
        
        return stringbuilder.toString();
    }
}
