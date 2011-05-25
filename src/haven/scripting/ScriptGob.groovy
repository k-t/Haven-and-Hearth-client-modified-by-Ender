package haven.scripting

import haven.Gob;
import haven.Speaking;

public class ScriptGob {
    private final Gob gob;
    
    public ScriptGob(Gob gob) {
        this.gob = gob;
    }
    
    public int id() {
        return gob.id;
    }
    
    public int x() {
        return gob.getc().x;
    }
    
    public int y() {
        return gob.getc().y;
    }
    
    public String resname() {
        return gob.resname();
    }
    
    public String speakingText() {
        Speaking speaking = gob.getattr(Speaking.class);
        return (speaking != null) ? speaking.text() : null;
    }
}
