package haven.scripting;

import java.util.ArrayList;
import java.util.List;

import haven.*;

public class ScriptInv {
    private final ScriptGlobal ui;
    private Inventory inventory;
    
    public ScriptInv(Inventory inventory, ScriptGlobal ui) {
        this.inventory = inventory;
        this.ui = ui;
    }
    
    /* Drops currently dragging item into specified position inside inventory. */
    public void drop(int x, int y) {
        inventory.wdgmsg("drop", new Coord(x, y));
    }
    
    public ItemIterator getItems() {
        return this.new ItemIterator();
    }
    
    public int getWidth() {
        return inventory.getSize().x;
    }
    
    public int getHeight() {
        return inventory.getSize().y;
    }
    
    public void sendAction(int x, int y, String name) {
        sendAction(x, y, name, 0);
    }
    
    public void sendAction(int x, int y, String action, int mode) {
        if ((!action.equals("take")) &&
            (!action.equals("transfer")) &&
            (!action.equals("drop")) &&
            (!action.equals("iact")) &&
            (!action.equals("itemact"))) { 
            return;
        }
        
        // find item in specified position
        for (Widget i = inventory.child; i != null; i = i.next)
            if (i instanceof Item) {
                Item it = (Item)i;
                if ((it.coord_x() == x) && (it.coord_y() == y)) {
                    Coord c = ui.getScreenCenter();
                    if (action.equals("itemact"))
                        it.wdgmsg("itemact", mode);
                    else
                        it.wdgmsg(action, c);
                }
            }
    }
    
    public class ItemIterator {
        private List<ScriptItem> items;
        private int itemindex;
        
        public ItemIterator() {
            items = new ArrayList<ScriptItem>();
            reset();
        }
       
        public int count() {
            return items.size();
        }
        
        public ScriptItem get(int index) {
            if (index >= 0 && index < items.size())
                return items.get(index);
            else
                return null;
        }
        
        public ScriptItem next() {
            itemindex++;
            return get(itemindex);
        }
        
        public void reset() {
            items.clear();
            for (Widget i = inventory.child; i != null; i = i.next) {
                if (i instanceof Item)
                    items.add(new ScriptItem((Item)i, ui));
            }
            itemindex = -1;
        }
    }
}
