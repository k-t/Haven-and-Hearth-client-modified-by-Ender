package haven.scripting

import haven.Coord;
import haven.Item;

public class ScriptItem extends ScriptWidget {
    private Item item;
    
    public ScriptItem(Item item) {
        super(item)
        this.item = item;
    }
    
    public void sendAction(String action) {
        sendAction(action, 0);
    }
    
    public void sendAction(String action, int mod) {
        if (action.equals("itemact") && Utils.getDragItem() != null) return;
        if ((!action.equals("take")) &&
            (!action.equals("transfer")) &&
            (!action.equals("drop")) &&
            (!action.equals("iact")) &&
            (!action.equals("itemact"))) {
            return; 
        }
        Coord c = Utils.getScreenCenter();
        if (action.equals("itemact"))
            item.wdgmsg("itemact", mod);
        else
            item.wdgmsg(action, c);
    }
    
    public int getRow() {
        return item.c.div(31).y;
    }
    
    public int getColumn() {
        return item.c.div(31).x;
    }
    
    public int getWidth() {
        return item.sz.div(30).x;
    }
    
    public int getHeight() {
        return item.sz.div(30).y;
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
        return this.name.indexOf(s) >= 0;
    }
    
    public boolean isTooltip(String s) {
        return this.tooltip.indexOf(s) >= 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof ScriptItem) {
            ScriptItem other = (ScriptItem)o;
            return other.item == this.item;
        }
        return false;
    }
}
