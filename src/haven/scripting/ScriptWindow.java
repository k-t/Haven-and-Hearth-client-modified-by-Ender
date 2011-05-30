package haven.scripting;

import java.util.ArrayList;

import haven.*;

public class ScriptWindow extends ScriptWidget {
    private final Window wnd;
    
    public ScriptWindow(Window w) {
        super(w);
        this.wnd = w;
    }
    
    public void close() {
        wnd.wdgmsg("close");
    }
    
    public String getCaption() {
        return (wnd.cap != null) ? wnd.cap.text : "";
    }
    
    public ScriptISBox findBuildBox(String tooltip) {
        for (ISBox isbox : getWidgets(ISBox.class)) {
            ScriptISBox sbox = new ScriptISBox(isbox); 
            if (sbox.getTooltip().contains(tooltip)) {
                return sbox;
            }
        }
        return null;
    }
}