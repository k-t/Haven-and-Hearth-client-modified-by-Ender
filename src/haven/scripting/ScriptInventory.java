package haven.scripting;

import java.util.ArrayList;
import java.util.List;

import haven.*;

public class ScriptInventory {
    private final ScriptGlobal ui;
    private Inventory inventory;
    
    public ScriptInventory(Inventory inventory, ScriptGlobal ui) {
        this.inventory = inventory;
        this.ui = ui;
    }
    
    /* Drops currently dragging item into specified position inside inventory. */
    public void drop(int row, int col) {
        inventory.wdgmsg("drop", new Coord(col, row));
    }
    
    public ScriptItem[] getItems() {
        ArrayList<ScriptItem> list = new ArrayList<ScriptItem>();
        for (Widget i = inventory.child; i != null; i = i.next) {
            if (i instanceof Item)
                list.add(new ScriptItem((Item)i, ui));
        }
        ScriptItem[] arr = new ScriptItem[list.size()];
        return list.toArray(arr);
    }
    
    public int rowCount() {
        return inventory.getSize().y;
    }
    
    public int columnCount() {
        return inventory.getSize().x;
    }
    
    public void sendAction(int row, int col, String name) {
        sendAction(row, col, name, 0);
    }
    
    public void sendAction(int row, int col, String action, int mode) {
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
                ScriptItem it = new ScriptItem((Item)i, this.ui);
                if ((it.column() == col) && (it.row() == row)) {
                    Coord c = ui.getScreenCenter();
                    if (action.equals("itemact"))
                        it.sendAction("itemact", mode);
                    else
                        it.sendAction(action);
                }
            }
    }
}
