package haven.scripting;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.codehaus.groovy.control.*;
import org.codehaus.groovy.control.customizers.*;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import groovy.transform.ThreadInterrupt;
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
    private Binding binding;
    private CompilerConfiguration configuration;
    
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
        
        configuration = new CompilerConfiguration();
        configuration.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt.class));
        ArrayList<String> cp = new ArrayList<String>();
        cp.add(".");
        cp.add("./scripts");
        configuration.setClasspathList(cp);
    }
    
    public boolean initialized() {
        return binding != null;
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
                shell.run(new File("./scripts/" + filename), new String[] { });
            } catch (IOException ie) {
                ie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            UI.instance.slen.error("Script finished");
        }
    }
}
