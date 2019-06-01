package com.blamejared.sewingkit.api.ingredients;


import net.minecraft.recipe.Ingredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("sk.Ingredient")
@ZenCodeType.Nullable
public interface IIngredient {
    
    Ingredient asIngredient();
    
}
