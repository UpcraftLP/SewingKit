package com.blamejared.sewingkit.mixin;

import com.blamejared.sewingkit.api.SKApi;
import com.blamejared.sewingkit.api.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import org.openzen.zencode.java.*;
import org.openzen.zencode.shared.*;
import org.openzen.zenscript.codemodel.*;
import org.openzen.zenscript.codemodel.member.*;
import org.openzen.zenscript.codemodel.member.ref.FunctionalMemberRef;
import org.openzen.zenscript.lexer.ParseException;
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
    private Map<Identifier, Recipe> recipeMap;
    
    @Inject(at = @At("TAIL"), method = "onResourceReload")
    private void run(CallbackInfo info) {
        List<Class> list = SKApi.locate(false);
        
        ItemManager.rebuild();
        SKApi.recipes.recipeListAddition.clear();
        SKApi.recipes.recipeListRemoval.clear();
    
        SKApi.furnaceManager.recipeListAddition.clear();
        SKApi.furnaceManager.recipeListRemoval.clear();
    
        SKApi.recipes.recipeMap = recipeMap;
        
        try {
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
            
            final MethodMember methodMember = example.addClass(ItemManager.class).members.stream().filter(MethodMember.class::isInstance).map(MethodMember.class::cast).filter(DefinitionMember::isStatic).findAny().orElse(null);
            if(methodMember != null)
                bep.register("item", new SimpleBracketParser(engine.registry, new FunctionalMemberRef(methodMember, methodMember.header.getReturnType(), null)));
            
            
            engine.registerNativeProvided(example);
            File inputDirectory = new File("scripts");
            inputDirectory.mkdir();
            File[] inputFiles = Optional.ofNullable(inputDirectory.listFiles((dir, name) -> name.endsWith(".zs"))).orElseGet(() -> new File[0]);
            SourceFile[] sourceFiles = new SourceFile[inputFiles.length];
            for(int i = 0; i < inputFiles.length; i++)
                sourceFiles[i] = new FileSourceFile(inputFiles[i].getName(), inputFiles[i]);
            
            
            SemanticModule scripts = engine.createScriptedModule("scripts", sourceFiles, bep, new FunctionParameter[]{}, "example");
            if(!scripts.isValid()) {
                SKApi.logger.logError("Script failed to compile! Check the game log!");
                System.out.println(">>> this is bad, please report this!");
                return;
            }
            
            engine.registerCompiled(scripts);
            engine.run(Collections.emptyMap(), this.getClass().getClassLoader());
        } catch(CompileException | ParseException e) {
            e.printStackTrace();
        }
        
        for(Recipe recipe : SKApi.recipes.recipeListAddition) {
            recipeMap.put(recipe.getId(), recipe);
        }
        for(Identifier identifier : SKApi.recipes.recipeListRemoval) {
            recipeMap.remove(identifier);
        }
    
    
        for(Recipe recipe : SKApi.furnaceManager.recipeListAddition) {
            recipeMap.put(recipe.getId(), recipe);
        }
        for(Identifier identifier : SKApi.furnaceManager.recipeListRemoval) {
            recipeMap.remove(identifier);
        }
    }
    
}
