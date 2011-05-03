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
    
    public int getX() {
        return item.coord_x();
    }
    
    public int getY() {
        return item.coord_y();
    }
    
    public String getName() {
        return item.getResName();
    }
    
    public int getNum() {
        return item.num;
    }
    
    public int getMeter() {
        return item.meter;
    }
    
    public String getTooltip() {
        String t = item.shorttip();
        return (t != null) ? t : "";
    }
    
    public int getQuality() {
        return item.q;
    }
    
    public boolean isName(String s) {
        return getName().indexOf(s) >= 0;
    }
    
    public boolean isTooltip(String s) {
        return getTooltip().indexOf(s) >= 0;
    }
}
