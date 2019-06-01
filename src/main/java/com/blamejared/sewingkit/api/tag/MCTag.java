package com.blamejared.sewingkit.api.tag;

import com.blamejared.sewingkit.api.ingredients.IIngredient;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import org.openzen.zencode.java.ZenCodeType;


@ZenCodeType.Name("sk.Tag")
public class MCTag implements ZenCodeType, IIngredient {
    
    private final Tag<Item> internal;
    
    public MCTag(Tag internal) {
        this.internal = internal;
    }
    
    @Getter("id")
    public String getId() {
        return internal.getId().toString();
    }
    
    public Tag<Item> getInternal() {
        return internal;
    }
    
    @Override
    public Ingredient asIngredient() {
        return Ingredient.fromTag(getInternal());
    }
    
    @ZenCodeType.Caster(implicit = true)
    public IIngredient asIIngredient() {
        return this;
    }
    
    @Override
    public String toString() {
        return "<tag:" + internal.getId().toString() + ">";
    }
}
