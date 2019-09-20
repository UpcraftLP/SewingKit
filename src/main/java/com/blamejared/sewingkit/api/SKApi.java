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
    
    @Global("blasting")
    public static BlastingManager blastingManager = new BlastingManager();
    
    @Global("smoking")
    public static SmokingManager smokingManager = new SmokingManager();
    
    @Global("campfire")
    public static CampFireManager campFireManager = new CampFireManager();
    
    @Global("stoneCutter")
    public static StoneCuttingManager stoneCuttingManager = new StoneCuttingManager();
    
    
    @Global
    public static void print(String message) {
        logger.logInfo(message);
    }
    
    
    public static List<Class<?>> locate(String path, boolean arrays) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            for(ClassPath.ClassInfo info : ClassPath.from(SKApi.class.getClassLoader()).getAllClasses()) {
                if(!info.getPackageName().startsWith(path)) {
                    continue;
                }
                
                
                String name = info.getName();
                if(arrays) {
                    name = "[L" + name + ";";
                }
                Class<?> clazz = Class.forName(name, false, SKApi.class.getClassLoader());
                
                if(!clazz.isAnnotationPresent(ZenCodeType.Name.class)) {
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
