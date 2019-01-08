package com.blamejared.sewingkit.api.recipes;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.item.MCItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.smelting.SmeltingRecipe;
import net.minecraft.util.Identifier;
import org.openzen.zencode.java.ZenCodeType;

import java.util.*;

@ZenCodeType.Name("sk.FurnaceManager")
public class FurnaceManager implements ZenCodeType {
    
    
    public List<Recipe> recipeListAddition = new ArrayList<>();
    public List<Identifier> recipeListRemoval = new ArrayList<>();
    
    @Method
    public void addRecipe(String name, MCItemStack output, MCItemStack inputs, float xp, int cookTime) {
        SKApi.logger.logInfo("Adding furnace recipe: " + name + " output: " + output + " inputs: " + inputs);
        recipeListAddition.add(new SmeltingRecipe(new Identifier("sewingkit", name), "", Ingredient.ofStacks(inputs.getStack()), output.getStack(), xp, cookTime));
    }
    
    @Method
    public void removeRecipe(String name) {
        recipeListRemoval.add(new Identifier(name));
    }
    
    @Method
    public void removeRecipe(MCItemStack stack) {
        for(Map.Entry<Identifier, Recipe> entry : SKApi.recipes.recipeMap.entrySet()) {
            if(ItemStack.areEqual(entry.getValue().getOutput(), stack.getStack())) {
                recipeListRemoval.add(entry.getKey());
            }
        }
        
    }
    
    
}
