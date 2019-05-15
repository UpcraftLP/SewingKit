package com.blamejared.sewingkit.api.utils;

import com.blamejared.sewingkit.api.item.MCItemStack;
import net.minecraft.item.*;

public class ItemStackUtils {
    
    
    public static ItemStack getItemStack(MCItemStack item) {
        if(item == null) {
            return ItemStack.EMPTY;
        }
        return item.getStack();
    }
    
    
    public static ItemStack[] getItemStack(MCItemStack... items) {
        if(items == null || items.length == 0) {
            return new ItemStack[0];
        }
        ItemStack[] newItems = new ItemStack[items.length];
        for(int i = 0; i < items.length; i++) {
            newItems[i] = getItemStack(items[i]);
        }
        return newItems;
    }
    
}
