package com.blamejared.sewingkit.api.recipes;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.ingredients.IIngredient;
import com.blamejared.sewingkit.api.item.MCItemStack;
import com.blamejared.sewingkit.api.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Map;

@ZenCodeType.Name("sk.SmokingManager")
public class SmokingManager extends RecipeManagerBase implements ZenCodeType {
    
    @Method
    public void addRecipe(String name, MCItemStack output, IIngredient inputs, float xp, int cookTime) {
        SKApi.logger.logInfo("Adding smoking recipe: " + name + " output: " + output + " inputs: " + inputs);
        recipeListAddition.add(new SmokingRecipe(new Identifier("sewingkit", name), "", inputs.asIngredient(), ItemStackUtils.getItemStack(output), xp, cookTime));
    }
    
    @Method
    public void removeRecipe(String name) {
        recipeListRemoval.add(new Identifier(name));
    }
    
    @Method
    public void removeRecipe(MCItemStack stack) {
        for(Map.Entry<Identifier, Recipe<?>> entry : RecipeManagerBase.recipeMap.get(getType()).entrySet()) {
            if(ItemStack.areEqual(entry.getValue().getOutput(), ItemStackUtils.getItemStack(stack))) {
                recipeListRemoval.add(entry.getKey());
            }
        }
        
    }
    
    @Override
    public RecipeType getType() {
        return RecipeType.SMOKING;
    }
}
