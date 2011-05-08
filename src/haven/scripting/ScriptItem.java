package haven.scripting;

import haven.Coord;
import haven.Item;

public class ScriptItem {
    private final ScriptGlobal ui;
    private Item item;
    
    public ScriptItem(Item item, ScriptGlobal ui) {
        this.item = item;
        this.ui = ui;
    }
    
    public void sendAction(String action) {
        sendAction(action, 0);
    }
    
    public void sendAction(String action, int mod) {
        if (action.equals("itemact") && !ui.hasDragItem()) return;
        if ((!action.equals("take")) &&
            (!action.equals("transfer")) &&
            (!action.equals("drop")) &&
            (!action.equals("iact")) &&
            (!action.equals("itemact"))) {
            return; 
        }
        Coord c = ui.getScreenCenter();
        if (action.equals("itemact"))
            item.wdgmsg("itemact", mod);
        else
            item.wdgmsg(action, c);
    }
    
    public int row() {
        return item.c.div(31).y;
    }
    
    public int column() {
        return item.c.div(31).x;
    }
    
    public int width() {
        return item.sz.div(30).x;
    }
    
    public int height() {
        return item.sz.div(30).y;
    }
    
    public String name() {
        return item.getResName();
    }
    
    public int num() {
        return item.num;
    }
    
    public int meter() {
        return item.meter;
    }
    
    public String tooltip() {
        String t = item.shorttip();
        return (t != null) ? t : "";
    }
    
    public int quality() {
        return item.q;
    }
    
    public boolean isName(String s) {
        return name().indexOf(s) >= 0;
    }
    
    public boolean isTooltip(String s) {
        return tooltip().indexOf(s) >= 0;
    }
}
