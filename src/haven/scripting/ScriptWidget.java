package haven.scripting;

import java.lang.reflect.Array;
import java.util.*;

import haven.*;

class ScriptWidget {
    private final Widget widget;
    
    public ScriptWidget(Widget widget) {
        this.widget = widget;
    }
    
    public final int getId() {
        return UI.instance.rwidgets.get(widget);
    }
    
    public String getTypeName() {
        return widget.getClass().getSimpleName();
    }
    
    public int getX() { return widget.c.x; }
    public void setX(int value) { widget.c.x = value; }
    
    public int getY() { return widget.c.y; }
    public void setY(int value) { widget.c.y = value; }
    
    public final int getWidth() { return widget.sz.x; }
    public final void setWidth(int value) { widget.sz.x = value; }
    
    public final int getHeight() { return widget.sz.y; }
    public final void setHeight(int value) { widget.sz.y = value; }
    
    public final void message(String msg, Object... args) {
        widget.wdgmsg(msg, args);
    }
    
    public ScriptWidget[] getWidgets() {
        ArrayList<ScriptWidget> list = new ArrayList<ScriptWidget>();
        for (Widget wdg = widget.child; wdg != null; wdg = wdg.next)
            list.add(new ScriptWidget(wdg));
        return list.toArray(new ScriptWidget[list.size()]);
    }
    
    public ScriptButton findButton(String caption) {
        for (Button b : getWidgets(Button.class)) {
            if (b.text != null && b.text.text.equals(caption))
                return new ScriptButton(b);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    protected final <T extends Widget> T[] getWidgets(Class<T> c) {
        ArrayList<T> list = new ArrayList<T>();
        for (Widget wdg = widget.child; wdg != null; wdg = wdg.next)
            if (wdg.getClass().isAssignableFrom(c))
                list.add((T)wdg);
        T[] arr = (T[])Array.newInstance(c, list.size());
        return list.toArray(arr);
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
	
    @Override
    public boolean equals(Object o) {
        if (o instanceof ScriptWidget) {
            ScriptWidget other = (ScriptWidget)o;
            return other.widget == this.widget;
        }
        return false;
    }
}
