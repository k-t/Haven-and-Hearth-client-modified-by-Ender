package haven.scripting;

import groovy.lang.*;
import groovy.transform.ThreadInterrupt;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import haven.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import kt.LogManager;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;

public class Engine {
    private static Engine instance = null;
    
    public static Engine getInstance() {
        if (instance == null)
            instance = new Engine();
        return instance;
    }
    
    private ScriptThread thread;
    private Binding binding;
    private CompilerConfiguration configuration;
    private GroovyScriptEngine gse;
    private GroovyObject callback;
    
    private String cursor = null;
    private int hp = 0;
    private int hunger = 0;
    private int stamina = 0;
    private boolean hourglass = false;

    private Engine() { }
    
    public ILog log() {
        return LogManager.getlog("Messages");
    }
    
    public String getCursor() {
        return cursor;
    }
    
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
    
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
    
    public boolean isHourglass() {
        return hourglass;
    }

    public void setHourglass(boolean hourglass) {
        this.hourglass = hourglass;
    }

    public void init() {
        binding = new Binding();
        binding.setVariable("Log", log());
        // configuration for the script execution
        configuration = new CompilerConfiguration();
        configuration.setRecompileGroovySource(true);
        configuration.addCompilationCustomizers(
                new ASTTransformationCustomizer(ThreadInterrupt.class),
                new ScriptGlobalCustomizer());
        configuration.setClasspathList(Arrays.asList(".", "./scripts"));
        try {
            gse = new GroovyScriptEngine(
                    new String[] { ".", "./scripts" },
                    new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), configuration));
            gse.setConfig(configuration);
        } catch (Exception e) {
            e.printStackTrace();
            gse = null;
        }
        if (initialized()) {
            initcallback();
        }
    }
    
    private void initcallback() {
        try {
            Class<?> groovyClass = gse.loadScriptByName("haven.groovy");
            callback = (GroovyObject)groovyClass.newInstance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean initialized() {
        return gse != null;
    }
    
    private GroovyObject getScriptCallbackObject() {
        if (!initialized())
            init();
        return callback;
    }
    
    public boolean handleKeyEvent(KeyEvent ev) {
        GroovyObject callback = getScriptCallbackObject();
        if (callback != null) {
            try {
                return (Boolean)callback.invokeMethod("handleKeyEvent", new Object[] { ev });
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
    
    public boolean handleConsoleCmd(String[] cmdline) {
        GroovyObject callback = getScriptCallbackObject();
        if (callback != null)
            return (Boolean)callback.invokeMethod("handleConsoleCmd", new Object[] { cmdline });
        return false;
    }
    
    public void run(String scriptname) {
        if (!initialized())
            init();
        if (thread != null) {
            String msg = "Only one script in time is allowed";
            log().write(msg);
            UI.instance.slen.error(msg);
            return;
        }
        if (thread != null && thread.isAlive())
            return;
        String filename = scriptname + (scriptname.endsWith(".groovy") ? "" : ".groovy");
        thread = new ScriptThread(filename);
        log().write("Starting script " + filename + "...");
        thread.start();
    }
    
    public void stop() {
        try {
            if (thread != null)
                thread.interrupt();
        } catch (Exception e) { };
    }
    
    public void wait(int time) {
        try {
            if (thread != null)
                thread.wait(time);
        } catch (InterruptedException e) { }
    }
    
    private class ScriptThread extends Thread {
        private String filename;
        
        public ScriptThread(String filename) {
            this.filename = filename;
        }

        public void run() {
            if (!initialized())
                return;
            try {
                gse.run(filename, binding);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                thread = null;                
            }
            String msg = "Script finished";
            UI.instance.slen.error(msg);
            log().write(msg);
        }
    }
}
