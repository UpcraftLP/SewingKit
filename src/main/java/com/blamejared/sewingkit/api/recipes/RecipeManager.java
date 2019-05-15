package com.blamejared.sewingkit.api.recipes;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.item.MCItemStack;
import com.blamejared.sewingkit.api.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.crafting.*;
import net.minecraft.util.*;
import org.openzen.zencode.java.ZenCodeType;

import java.util.*;

@ZenCodeType.Name("sk.RecipeManager")
public class RecipeManager implements ZenCodeType {
    
    
    public List<Recipe> recipeListAddition = new ArrayList<>();
    public List<Identifier> recipeListRemoval = new ArrayList<>();
    
    public Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMap;
    
    
    @ZenCodeType.Method
    public void addShapelessRecipe(String name, MCItemStack output, MCItemStack... inputs) {
        DefaultedList<Ingredient> list = DefaultedList.create();
        
        for(ItemStack input : ItemStackUtils.getItemStack(inputs)) {
            list.add(Ingredient.ofStacks(input));
        }
        SKApi.logger.logInfo("Adding recipe: " + name + " output: " + output + " inputs: " + Arrays.toString(inputs));
        recipeListAddition.add(new ShapelessRecipe(new Identifier("sewingkit", name), "", output.getStack(), list));
    }
    
    
    @ZenCodeType.Method
    public void addShapedRecipe(String name, MCItemStack output, MCItemStack[][] inputs) {
        DefaultedList<Ingredient> list = DefaultedList.create();
        for(MCItemStack[] in : inputs) {
            for(ItemStack stack : ItemStackUtils.getItemStack(in)) {
                list.add(Ingredient.ofStacks(stack));
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
        for(Map.Entry<Identifier, Recipe<?>> entry : recipeMap.get(RecipeType.CRAFTING).entrySet()) {
            if(ItemStack.areEqual(entry.getValue().getOutput(), ItemStackUtils.getItemStack(stack))) {
                recipeListRemoval.add(entry.getKey());
            }
        }
        
    }
    
    
}
