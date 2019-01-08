package com.blamejared.sewingkit.api;

import com.blamejared.sewingkit.api.logger.SKLogger;
import com.blamejared.sewingkit.api.recipes.*;
import com.google.common.reflect.ClassPath;
import org.openzen.zencode.java.*;

import java.io.*;
import java.util.*;

public class SKApi implements ZenCodeGlobals {
    
    @Global("logger")
    public static SKLogger logger = new SKLogger(new File("sewingkit.log"));
    
    @Global("recipes")
    public static RecipeManager recipes = new RecipeManager();
    
    @Global("furnace")
    public static FurnaceManager furnaceManager = new FurnaceManager();
    
    @Global
    public static void print(String message) {
        logger.logInfo(message);
    }
    
    
    public static List<Class> locate(boolean arrays) {
        List<Class> classes = new ArrayList<>();
        try {
            for(ClassPath.ClassInfo info : ClassPath.from(SKApi.class.getClassLoader()).getAllClasses()) {
                if(!info.getPackageName().startsWith("com.blamejared.sewingkit.api")) {
                    continue;
                }
                
                
                String name = info.getName();
                if(arrays) {
                    name = "[L" + name + ";";
                }
                Class<?> clazz = Class.forName(name, false, SKApi.class.getClassLoader());
                
                if(clazz.isAnnotationPresent(ZenCodeType.Name.class) && !clazz.getAnnotation(ZenCodeType.Name.class).value().startsWith("sk")) {
                    continue;
                }
                
                if(ZenCodeType.class.isAssignableFrom(clazz)) {
                    classes.add(clazz);
                }
            }
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classes;
    }
    
    
}
