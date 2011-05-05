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
    
    public void click(String action) {
        click(action, 0);
    }
    
    public void click(String action, int mod) {
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
    
    public int x() {
        return item.coord_x();
    }
    
    public int y() {
        return item.coord_y();
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
