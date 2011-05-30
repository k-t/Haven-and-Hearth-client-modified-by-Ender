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
    
    public ScriptInventory getInventory() {
        ScriptInventory[] invs = getInventories();
        return (invs.length > 0) ? invs[0] : null;
    }
    
    public ScriptInventory[] getInventories() {
        Inventory[] invs = getWidgets(Inventory.class);
        ArrayList<ScriptInventory> result = new ArrayList<ScriptInventory>();
        for (Inventory inv : invs)
            result.add(new ScriptInventory(inv));
        return result.toArray(new ScriptInventory[result.size()]);        
    }
    
    public ScriptISBox findBuildBox(String name) {
        for (ISBox isbox : getWidgets(ISBox.class)) {
            ScriptISBox sbox = new ScriptISBox(isbox); 
            if (sbox.getTooltip().contains(name)) {
                return sbox;
            }
        }
        return null;
    }
}