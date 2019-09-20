package com.blamejared.sewingkit.api.recipes;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class RecipeManagerBase<T extends Recipe<?>> {

    public static final List<RecipeManagerBase<?>> managers = new ArrayList<>();
    public static Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMap;
    public List<Recipe<?>> recipeListAddition = new ArrayList<>();
    public List<Identifier> recipeListRemoval = new ArrayList<>();

    public RecipeManagerBase() {
        RecipeManagerBase.managers.add(this);
    }

    public void cleanUp() {
        this.recipeListAddition.clear();
        this.recipeListRemoval.clear();
    }

    public abstract RecipeType<T> getType();
}
