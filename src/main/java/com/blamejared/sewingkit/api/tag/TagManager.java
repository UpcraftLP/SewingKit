package com.blamejared.sewingkit.api.tag;

import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import org.openzen.zencode.java.ZenCodeType;

import java.util.*;

@ZenCodeType.Name("sk.tagManager")
public class TagManager implements ZenCodeType {
    
    private static final Map<String, MCTag> tags = new HashMap<>();
    
    public static void rebuild() {
        tags.clear();
        for(Identifier identifier : ItemTags.getContainer().getEntries().keySet()) {
            tags.put(identifier.toString(), new MCTag(ItemTags.getContainer().get(identifier)));
        }
    }
    
    @Method
    public static MCTag getTag(String name) {
        return tags.getOrDefault(name, null);
    }
}
