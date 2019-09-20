package com.blamejared.sewingkit.api.recipes;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.ingredients.IIngredient;
import com.blamejared.sewingkit.api.item.MCItemStack;
import com.blamejared.sewingkit.api.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@ZenCodeType.Name("sk.RecipeManager")
public class RecipeManager extends RecipeManagerBase<CraftingRecipe> implements ZenCodeType {


    @ZenCodeType.Method
    public void addShapelessRecipe(String name, MCItemStack output, IIngredient... inputs) {
        DefaultedList<Ingredient> list = Arrays.stream(inputs).map(IIngredient::asIngredient).collect(Collectors.toCollection(DefaultedList::of));
        SKApi.logger.logInfo("Adding recipe: " + name + " output: " + output + " inputs: " + Arrays.toString(inputs));
        recipeListAddition.add(new ShapelessRecipe(new Identifier("sewingkit", name), "", output.getStack(), list));
    }


    @ZenCodeType.Method
    public void addShapedRecipe(String name, MCItemStack output, IIngredient[][] inputs) {
        DefaultedList<Ingredient> list = Arrays.stream(inputs).flatMap(Arrays::stream).map(IIngredient::asIngredient).collect(Collectors.toCollection(DefaultedList::of));
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
            if(ItemStack.areEqualIgnoreDamage(entry.getValue().getOutput(), ItemStackUtils.getItemStack(stack))) {
                recipeListRemoval.add(entry.getKey());
            }
        }
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return RecipeType.CRAFTING;
    }
}
