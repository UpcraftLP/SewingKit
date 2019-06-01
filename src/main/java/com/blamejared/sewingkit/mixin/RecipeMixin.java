package com.blamejared.sewingkit.mixin;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.item.*;
import com.blamejared.sewingkit.api.recipes.RecipeManagerBase;
import com.blamejared.sewingkit.api.tag.TagManager;
import net.minecraft.recipe.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.openzen.zencode.java.*;
import org.openzen.zencode.shared.*;
import org.openzen.zenscript.codemodel.*;
import org.openzen.zenscript.codemodel.member.*;
import org.openzen.zenscript.codemodel.member.ref.FunctionalMemberRef;
import org.openzen.zenscript.parser.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.*;

@Mixin(RecipeManager.class)
public abstract class RecipeMixin {
    
    @Shadow
    @Final
    private Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMap;
    
    @Inject(at = @At("TAIL"), method = "apply")
    private void run(ResourceManager resourceManager_1, CallbackInfo info) {
        System.out.println(">>> start");
        List<Class> list = SKApi.locate("com.blamejared.sewingkit.api", false);
        
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
        
        
        for(RecipeManagerBase base : RecipeManagerBase.managers) {
            for(Identifier identifier : base.recipeListRemoval) {
                recipeMap.get(base.getType()).remove(identifier);
            }
            for(Recipe<?> recipe : base.recipeListAddition) {
                recipeMap.get(base.getType()).put(recipe.getId(), recipe);
            }
        }
    }
    
}
