package com.blamejared.sewingkit.api.item;

import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.openzen.zencode.java.ZenCodeType;

import java.util.*;

@ZenCodeType.Name("sk.Itemstack")
public class ItemManager implements ZenCodeType {
    
    
    private static final Map<String, MCItemStack> items = new HashMap<>();
    
    public static void rebuild() {
        items.clear();
        for(Identifier key : Registry.ITEM.getIds()) {
            Item item = Registry.ITEM.get(key);
            items.put(key.toString(), new MCItemStack(new ItemStack(item)));
        }
    }
    
    @ZenCodeType.Method
    public static MCItemStack getItem(String name) {
        if(items.containsKey(name)) {
            return items.get(name);
        }
        return null;
    }
}
