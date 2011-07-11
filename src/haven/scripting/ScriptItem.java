package haven.scripting;

import haven.Item;

public class ScriptItem extends ScriptWidget {
    private Item item;
    
    public ScriptItem(Item item) {
        super(item);
        this.item = item;
    }
    
    public int getRow() {
        return item.c.div(31).y;
    }
    
    public int getColumn() {
        return item.c.div(31).x;
    }
    
    public int getColumnSize() {
        return item.sz.div(30).x;
    }
    
    public int getRowSize() {
        return item.sz.div(30).y;
    }
    
    public String getName() {
        return item.resname();
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
        return this.getName().indexOf(s) >= 0;
    }
    
    public boolean isTooltip(String s) {
        return this.getTooltip().indexOf(s) >= 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof ScriptItem) {
            ScriptItem other = (ScriptItem)o;
            return other.item == this.item;
        }
        return false;
    }
    
    public void interact() {
       interact(0);
    }
    
    public void interact(int mod) {
        if (Utils.getDragItem() == null) return;
        item.wdgmsg("itemact", mod);
    }
    
    public void take() {
        item.wdgmsg("take", Utils.getScreenCenter());
    }
    
    public void transfer() {
        item.wdgmsg("transfer", Utils.getScreenCenter());
    }
    
    public void drop() {
        item.wdgmsg("drop", Utils.getScreenCenter());
    }
    
    public void act() {
        item.wdgmsg("iact", Utils.getScreenCenter());
    }
}
