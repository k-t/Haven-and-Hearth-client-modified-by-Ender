package haven.scripting;

import haven.Gob;
import haven.Speaking;

public class ScriptGob {
    private final Gob gob;
    
    public ScriptGob(Gob gob) {
        this.gob = gob;
    }
    
    public int getId() {
        return gob.id;
    }
    
    public int getX() {
        return gob.getc().x;
    }
    
    public int getY() {
        return gob.getc().y;
    }
    
    public String getResname() {
        return gob.resname();
    }
    
    public String[] getResnames() {
        return gob.resnames();
    }
    
    public String getSpeakingText() {
        Speaking speaking = gob.getattr(Speaking.class);
        return (speaking != null) ? speaking.text() : null;
    }
}
