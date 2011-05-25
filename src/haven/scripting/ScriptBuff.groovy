package haven.scripting

import haven.Buff

public class ScriptBuff {
    private final Buff buff;
    
    public ScriptBuff(Buff buff) {
        this.buff = buff;
    }
    
    public String getName() {
        return buff.getName();
    }
    
    public int getMeter() {
        return buff.ameter;
    }
    
    public int getTimeLeft() {
        return buff.getTimeLeft();
    }
    
    public boolean isName(String name) {
        return name().indexOf(name) > 0;
    }
}
