package com.blamejared.sewingkit;

import com.blamejared.sewingkit.api.SKApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;

import java.util.*;

public class SewingKit implements ModInitializer {
    
    public static SKApi api;
    public static List<Recipe> recipeList = new ArrayList<>();
    
    public static Map<String, Item> items = new HashMap<>();
    
    @Override
    public void onInitialize() {
        CommandRegistry.INSTANCE.register(false, CommandHand::register);
    
    }
}
