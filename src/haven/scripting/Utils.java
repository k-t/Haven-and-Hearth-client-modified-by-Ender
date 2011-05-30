package haven.scripting;

import haven.*;

public class Utils {
    public static Item getDragItem() {
        for (Widget wdg = UI.instance.root.child; wdg != null; wdg = wdg.next)
            if ((wdg instanceof Item) && (((Item)wdg).dm))
                return (Item)wdg;
        return null;
    }
        
    public static Window getWindow(String windowname) {
        for (Widget wdg = UI.instance.root.child; wdg != null; wdg = wdg.next)
            if (wdg instanceof Window) {
                Window w = (Window)wdg;
                if (w.cap != null && w.cap.text != null && w.cap.text.equals(windowname))
                    return w;
            }
        return null;
    }
        
    public static Inventory getWindowInventory(String windowname) {
        Window wnd = getWindow(windowname);
        return (wnd != null) ? getWidgetInventory(wnd) : null;
    }
    
    public static Inventory getWidgetInventory(Widget wdg) {
        for (Widget inv = wdg.child; inv != null; inv = inv.next)
            if (inv instanceof Inventory)
                return (Inventory)inv;
        return null;
    }
    
    /* Returns approximate coords for click in the center of the screen */
    public static Coord getScreenCenter() {
        Coord sc, sz;
        if (UI.instance.mainview != null) {
            sz = UI.instance.mainview.sz;
            sc = new Coord(
                (int)Math.round(Math.random() * 200 + sz.x / 2 - 100),
                (int)Math.round(Math.random() * 200 + sz.y / 2 - 100));
            return sc;
        }
        else
            return new Coord(400, 400);
    }
}
