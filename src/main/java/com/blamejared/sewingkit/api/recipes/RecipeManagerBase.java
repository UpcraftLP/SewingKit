package com.blamejared.sewingkit.api.recipes;

import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class RecipeManagerBase {
    
    
    public static final List<RecipeManagerBase> managers = new ArrayList<>();
    
    public RecipeManagerBase() {
        RecipeManagerBase.managers.add(this);
    }
    
    public List<Recipe> recipeListAddition = new ArrayList<>();
    public List<Identifier> recipeListRemoval = new ArrayList<>();
    
    public static Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMap;
    
    public void cleanUp() {
        this.recipeListAddition.clear();
        this.recipeListRemoval.clear();
    }
    
    public abstract RecipeType getType();
    
}
