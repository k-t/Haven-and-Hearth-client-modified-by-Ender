package haven.scripting

import haven.*;

public class ScriptInventory extends ScriptWidget {
    private Inventory inventory;
    
    public ScriptInventory(Inventory inventory) {
        super(inventory)
        this.inventory = inventory;
    }
    
    /* Drops currently dragging item into specified position inside inventory. */
    public void drop(int row, int col) {
        inventory.wdgmsg("drop", new Coord(col, row));
    }
    
    public ScriptItem[] getItems() {
        ArrayList<ScriptItem> list = new ArrayList<ScriptItem>();
        for (Widget i = inventory.child; i != null; i = i.next) {
            if (i instanceof Item)
                list.add(new ScriptItem((Item)i));
        }
        ScriptItem[] arr = new ScriptItem[list.size()];
        return list.toArray(arr);
    }
    
    public int getRowCount() {
        return inventory.getSize().y;
    }
    
    public int getColumnCount() {
        return inventory.getSize().x;
    }
    
    public ScriptItem getItemAt(int row, int col) {
        for (Widget i = inventory.child; i != null; i = i.next)
            if (i instanceof Item) {
                ScriptItem it = new ScriptItem((Item)i);
                if ((it.column == col) && (it.row == row))
                    return it;
            }
        return null;
    }
}
