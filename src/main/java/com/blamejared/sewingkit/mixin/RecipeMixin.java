package com.blamejared.sewingkit.mixin;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.item.ItemManager;
import com.blamejared.sewingkit.api.item.MCItemStack;
import com.blamejared.sewingkit.api.recipes.RecipeManagerBase;
import com.blamejared.sewingkit.api.tag.TagManager;
import com.google.gson.JsonObject;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.openzen.zencode.java.JavaNativeModule;
import org.openzen.zencode.java.ScriptingEngine;
import org.openzen.zencode.java.ZenCodeType;
import org.openzen.zencode.shared.FileSourceFile;
import org.openzen.zencode.shared.SourceFile;
import org.openzen.zenscript.codemodel.FunctionParameter;
import org.openzen.zenscript.codemodel.SemanticModule;
import org.openzen.zenscript.codemodel.member.DefinitionMember;
import org.openzen.zenscript.codemodel.member.MethodMember;
import org.openzen.zenscript.codemodel.member.ref.FunctionalMemberRef;
import org.openzen.zenscript.parser.PrefixedBracketParser;
import org.openzen.zenscript.parser.SimpleBracketParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class RecipeMixin {
    
    @Shadow
    private Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMap;
    
    @Inject(at = @At("TAIL"), method = "method_20705")
    private void run(Map<Identifier, JsonObject> map_1, ResourceManager resourceManager_1, Profiler profiler_1, CallbackInfo ci) {
        System.out.println(">>> start");
        List<Class<?>> list = SKApi.locate("com.blamejared.sewingkit.api", false);
        
        ItemManager.rebuild();
        TagManager.rebuild();
        
        
        RecipeManagerBase.managers.forEach(RecipeManagerBase::cleanUp);
        RecipeManagerBase.recipeMap = recipeMap;
        
        try {
            System.out.println("Start Scripting");
            SKApi.logger.logInfo("Started!");
            ScriptingEngine engine = new ScriptingEngine();
            engine.debug = true;
            
            JavaNativeModule zs = engine.createNativeModule("zs", "org.openzen");
            zs.addClass(ZenCodeType.class);
            
            
            JavaNativeModule arrays = engine.createNativeModule("arrays", "[Lcom", zs);
            arrays.addClass(MCItemStack[].class);
            
            
            JavaNativeModule arraysArray = engine.createNativeModule("arraysArray", "[[Lcom", arrays);
            arraysArray.addClass(MCItemStack[][].class);
            
            
            JavaNativeModule example = engine.createNativeModule("example", "sk", arraysArray);
            list.remove(ItemManager.class);
            list.forEach(example::addClass);
            example.addGlobals(SKApi.class);
            
            
            PrefixedBracketParser bep = new PrefixedBracketParser(null);
            final MethodMember itemGetter = example.addClass(ItemManager.class).members.stream().filter(MethodMember.class::isInstance).map(MethodMember.class::cast).filter(DefinitionMember::isStatic).findAny().orElse(null);
            if(itemGetter != null)
                bep.register("item", new SimpleBracketParser(engine.registry, new FunctionalMemberRef(itemGetter, itemGetter.header.getReturnType(), null)));
            
            final MethodMember tagGetter = example.addClass(TagManager.class).members.stream().filter(MethodMember.class::isInstance).map(MethodMember.class::cast).filter(DefinitionMember::isStatic).findAny().orElse(null);
            if(tagGetter != null)
                bep.register("tag", new SimpleBracketParser(engine.registry, new FunctionalMemberRef(tagGetter, tagGetter.header.getReturnType(), null)));
            
            engine.registerNativeProvided(example);
            System.out.println("Done engine");
            File inputDirectory = new File("scripts");
            System.out.println(inputDirectory.getPath());
            inputDirectory.mkdir();
            File[] inputFiles = Optional.ofNullable(inputDirectory.listFiles((dir, name) -> name.endsWith(".zs"))).orElseGet(() -> new File[0]);
            SourceFile[] sourceFiles = new SourceFile[inputFiles.length];
            for(int i = 0; i < inputFiles.length; i++)
                sourceFiles[i] = new FileSourceFile(inputFiles[i].getName(), inputFiles[i]);
            
            
            SemanticModule scripts = engine.createScriptedModule("scripts", sourceFiles, bep, new FunctionParameter[] {}, "example");
            if(!scripts.isValid()) {
                SKApi.logger.logError("Script failed to compile! Check the game log!");
                System.out.println("This is bad, please report this!");
                return;
            }
            
            engine.registerCompiled(scripts);
            engine.run(Collections.emptyMap(), this.getClass().getClassLoader());
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
        for(RecipeManagerBase<?> base : RecipeManagerBase.managers) {
            for(Identifier identifier : base.recipeListRemoval) {
                recipeMap.get(base.getType()).remove(identifier);
            }
            for(Recipe<?> recipe : base.recipeListAddition) {
                recipeMap.get(base.getType()).put(recipe.getId(), recipe);
            }
        }
    }
    
}
