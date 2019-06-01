package com.blamejared.sewingkit.api.logger;


import org.openzen.zencode.java.ZenCodeType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@ZenCodeType.Name("sk.Logger")
public class SKLogger implements ZenCodeType {
    
    private static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)" + '\u00a7' + "[0-9A-FK-OR]");
    private final Writer writer;
    private final PrintWriter printWriter;
    private boolean isDefaultDisabled = false;
    
    public SKLogger(File output) {
        try {
            writer = new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8);
            printWriter = new PrintWriter(writer);
        }
        catch(FileNotFoundException ex) {
            throw new RuntimeException("Could not open log file " + output);
        }
    }
    
    @Method
    public void logCommand(String message) {
        try {
            writer.write(stripMessage(message) + "\n");
            writer.flush();
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Method
    public void logInfo(String message) {
        try {
            writer.write("INFO: " + stripMessage(message) + "\n");
            writer.flush();
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Method
    public void logWarning(String message) {
        try {
            writer.write("WARNING: " + stripMessage(message) + "\n");
            writer.flush();
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Method
    public void logError(String message) {
        try {
            writer.write("ERROR: " + stripMessage(message) + "\n");
            writer.flush();
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    /**
     * Returns a copy of the given string, with formatting codes stripped away.
     */
    public String stripMessage(String message) {
        return message == null ? null : FORMATTING_CODE_PATTERN.matcher(message).replaceAll("");
    }
    
    
    public void logDefault(String message) {
        if(!isLogDisabled())
            logInfo(message);
    }
    
    
    public boolean isLogDisabled() {
        return isDefaultDisabled;
    }
    
    
    public void setLogDisabled(boolean logDisabled) {
        this.isDefaultDisabled = logDisabled;
    }
}
