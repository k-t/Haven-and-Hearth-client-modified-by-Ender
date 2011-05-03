package haven.scripting;

import haven.MapView;
import haven.UI;

public class ScriptInput {
    private final Engine engine;
    
    public ScriptInput(Engine engine) {
        this.engine = engine;
    }
    
    public int getObject(String msg) {
        MapView mv = UI.instance.mainview;        
        if (mv == null)
            return 0;
        
        ark.log.LogPrint("input get object....");
        
        mv.mode_select_object = true;
        while (mv.mode_select_object) 
            engine.wait(200);
        
        if (mv.gob_at_mouse != null) {
            ark.log.LogPrint("objid = " + mv.gob_at_mouse.id);
            return mv.gob_at_mouse.id;
        }
        
        return 0;
    }
}
