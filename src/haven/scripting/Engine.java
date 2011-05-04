package haven.scripting;

import java.io.IOException;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import haven.UI;

public class Engine {
    private static Engine instance = null;
    public static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
        }
        return instance;
    }
    
    private ScriptGlobal glob;
    private Logger log;
    
    private ScriptThread thread;
    private GroovyScriptEngine gse; 
    private Binding binding;
    
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
    
    public boolean isHourGlass() {
        return hourglass;
    }

    public void setHourGlass(boolean hourglass) {
        this.hourglass = hourglass;
    }

    public void init() {
        glob = new ScriptGlobal(this);
        log = new Logger();
        binding = new Binding();
        binding.setVariable("Log", log);
        binding.setVariable("Glob", glob);
        //binding.setVariable("Craft", new ScriptCraft(this));
        //binding.setVariable("Input", new ScriptInput(this));
        
        String[] roots = new String[] { ".", "./scripts/" };
        try {
            gse = new GroovyScriptEngine(roots);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean initialized() {
        return gse != null;
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
            if (thread != null)
             thread.stop();
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
                gse.run(filename, binding);
            } catch (ResourceException re) {
                re.printStackTrace();

            } catch (ScriptException se) {
                se.printStackTrace();
            }
            UI.instance.slen.error("Script finished");
        }
    }
}
