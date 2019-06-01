package com.blamejared.sewingkit.api.recipes;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.ingredients.IIngredient;
import com.blamejared.sewingkit.api.item.MCItemStack;
import com.blamejared.sewingkit.api.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.crafting.*;
import net.minecraft.util.*;
import org.openzen.zencode.java.ZenCodeType;

import java.util.*;

@ZenCodeType.Name("sk.RecipeManager")
public class RecipeManager extends RecipeManagerBase implements ZenCodeType {
    
    
    @ZenCodeType.Method
    public void addShapelessRecipe(String name, MCItemStack output, IIngredient... inputs) {
        DefaultedList<Ingredient> list = DefaultedList.create();
        
        
        for(IIngredient input : inputs) {
            list.add(input.asIngredient());
        }
        SKApi.logger.logInfo("Adding recipe: " + name + " output: " + output + " inputs: " + Arrays.toString(inputs));
        recipeListAddition.add(new ShapelessRecipe(new Identifier("sewingkit", name), "", output.getStack(), list));
    }
    
    
    @ZenCodeType.Method
    public void addShapedRecipe(String name, MCItemStack output, IIngredient[][] inputs) {
        DefaultedList<Ingredient> list = DefaultedList.create();
        for(IIngredient[] input : inputs) {
            for(IIngredient ingredient : input) {
                list.add(ingredient.asIngredient());
            }
        }
        SKApi.logger.logInfo("Adding Shaped recipe: " + name + " output: " + output);
        recipeListAddition.add(new ShapedRecipe(new Identifier("sewingkit", name), "", 3, 3, list, output.getStack()));
    }
    
    @ZenCodeType.Method
    public void removeRecipe(String name) {
        recipeListRemoval.add(new Identifier(name));
    }
    
    
    @ZenCodeType.Method
    public void removeRecipe(MCItemStack stack) {
        for(Map.Entry<Identifier, Recipe<?>> entry : RecipeManagerBase.recipeMap.get(getType()).entrySet()) {
            if(ItemStack.areEqual(entry.getValue().getOutput(), ItemStackUtils.getItemStack(stack))) {
                recipeListRemoval.add(entry.getKey());
            }
        }
        
    }
    
    @Override
    public RecipeType getType() {
        return RecipeType.CRAFTING;
    }
    
}
