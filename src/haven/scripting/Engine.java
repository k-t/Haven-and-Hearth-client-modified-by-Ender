package haven.scripting;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

import org.codehaus.groovy.control.*;
import org.codehaus.groovy.control.customizers.*;

import groovy.lang.*;
import groovy.util.*;
import groovy.transform.ThreadInterrupt;
import haven.UI;

public class Engine {
    private static Engine instance = null;
    
    public static Engine getInstance() {
        if (instance == null)
            instance = new Engine();
        return instance;
    }
    
    private ScriptGlobal glob;
    private Logger log;
    
    private ScriptThread thread;
    private Binding binding;
    private CompilerConfiguration configuration;
    private GroovyScriptEngine gse;
    
    private String cursor = null;
    private int hp = 0;
    private int hunger = 0;
    private int stamina = 0;
    private boolean hourglass = false;

    private Engine() { }
    
    public ScriptGlobal glob() {
        return glob;
    }
    
    public Logger log() {
        return log;
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
        glob = new ScriptGlobal(this);
        log = new Logger();
        binding = new Binding();
        binding.setVariable("Log", log);
        binding.setVariable("Glob", glob);
        // configuration for script execution
        configuration = new CompilerConfiguration();
        configuration.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));
        configuration.setClasspathList(Arrays.asList(".", "./scripts"));
        // init engine for working with callback scripts
        try {
            gse = new GroovyScriptEngine(new String[] { ".", "./scripts" });
            gse.setConfig(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public boolean initialized() {
        return binding != null;
    }
    
    private GroovyObject getScriptCallbackObject() {
        if (!initialized())
            init();        
        try {
            Class<?> groovyClass = gse.loadScriptByName("haven.groovy");
            return (GroovyObject)groovyClass.newInstance();
        } catch (CompilationFailedException e) {
            // TODO logging
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (ResourceException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean handleKeyEvent(KeyEvent ev) {
        GroovyObject callback = getScriptCallbackObject();
        if (callback != null) {
            try {
                return (Boolean)callback.invokeMethod("handleKeyEvent", new Object[] { ev });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public void run(String scriptname) {
        if (!initialized())
            init();
        if (thread != null && thread.isAlive())
            return;
        String filename = scriptname + (scriptname.endsWith(".groovy") ? "" : ".groovy");
        thread = new ScriptThread(filename);
        thread.start();
    }
    
    public void stop() {
        try {
            if (thread != null) {
             thread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void wait(int time) {
        try {
            thread.wait(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                GroovyShell shell = new GroovyShell(binding, configuration);
                shell.run(new File("./scripts/" + filename), new String[0]);
            } catch (IOException ie) {
                ie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            UI.instance.slen.error("Script finished");
        }
    }
}
