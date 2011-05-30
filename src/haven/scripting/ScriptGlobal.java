package haven.scripting;

import java.awt.event.KeyEvent;
import java.util.*;

import haven.*;

public class ScriptGlobal {
    
    private static ScriptGlobal instance;
    
    public static ScriptGlobal getInstance() {
        if (instance == null)
            instance = new ScriptGlobal();
        return instance;
    }
    
    private ScriptGlobal() {
    }
    
    public boolean checkHourglass() {
        return getEngine().isHourglass();
    }
    
    public void doClick(int objid, int btn, int modflags) {
        Coord sc, sz, oc;
        Gob o = null;
        synchronized (glob().oc) {
            o = glob().oc.getgob(objid);
        }
        if (o == null)
            return;
        if (UI.instance.mainview != null) {
            sz = UI.instance.mainview.sz;
            sc = new Coord(
                    (int)Math.round(Math.random() * 200 + sz.x / 2 - 100),
                    (int)Math.round(Math.random() * 200 + sz.y / 2 - 100));
            oc = o.getc();
            UI.instance.mainview.wdgmsg("click", sc, oc, btn, modflags, objid, oc);
        }
    }
    
    public void drop() {
        drop(0);
    }
    
    public void drop(int mod) {
        if (UI.instance.mainview != null)
            UI.instance.mainview.wdgmsg("drop", mod);
    }
    
    public ScriptChatWindow findChatWindow(String windowname) {
        if (ChatHWPanel.instance == null)
            return null;
        for (HWindow wnd : ChatHWPanel.instance.getwnds()) {
            if (wnd.title.contains(windowname) && wnd instanceof ChatHW) {
                return new ScriptChatWindow((ChatHW)wnd);
            }
        }
        return null;
    }
    
    public int findMapObject(String name, int radius, int x, int y) {
        Coord my = getMyCoords();
        my = MapView.tilify(my);
        Coord offset = new Coord(x, y).mul(MCache.tilesz);
        my = my.add(offset);
        double min = radius;
        Gob mingob = null;
        synchronized (glob().oc) {
            for (Gob gob : glob().oc) {
                double len = gob.getc().dist(my);
                boolean m = ((name.length() > 0) && (gob.resname().indexOf(name) >= 0)) || (name.length() < 1);
                if ((m) && (len < min)) {
                    min = len;
                    mingob = gob;
                }
            }
        }
        if (mingob != null)
            return mingob.id;
        else
            return 0;

    }
    
    public int findObjectByName(String name, int radius) {
        return findMapObject(name, radius * 11, 0, 0);
    }
    
    public ScriptWindow findWindow(String... windownames) {
        ScriptWindow[] windows = findWindows(windownames);
        if (windows.length > 0)
            return windows[0];
        else 
            return null;
    }
    
    public ScriptWindow[] findWindows(String... windownames) {
        ArrayList<ScriptWindow> list = new ArrayList<ScriptWindow>();
        for (Widget wdg = UI.instance.root.child; wdg != null; wdg = wdg.next) {
            if (wdg instanceof Window) {
                Window w = (Window)wdg;
                for (String windowname : windownames)
                    if (w.cap != null && w.cap.text != null && w.cap.text.contains(windowname)) {
                        list.add(new ScriptWindow(w));
                        break;
                    }
            }
        }
        return list.toArray(new ScriptWindow[list.size()]);
    }
    
    public ScriptBuff[] getBuffs() {
        ArrayList<ScriptBuff> list = new ArrayList<ScriptBuff>();
        synchronized(glob().buffs) {
            for (Buff b : glob().buffs.values())
                list.add(new ScriptBuff(b));
        }
        ScriptBuff[] arr = new ScriptBuff[list.size()];
        return arr;
    }
    
    public ScriptCharWindow getCharWindow() {
        for (Widget wdg = UI.instance.root.child; wdg != null; wdg = wdg.next)
            if (wdg instanceof CharWnd)
                return new ScriptCharWindow((CharWnd)wdg);
        return null;
    }
    
    public ScriptFlowerMenu getContextMenu() {
        if (UI.flower_menu != null)
            return new ScriptFlowerMenu(UI.flower_menu);
        else
            return null;
    }
    
    public ScriptCraftWindow getCraftWindow() {
        for (HWindow wnd : ChatHWPanel.instance.getwnds()) {
            if (wnd instanceof Makewindow) {
                return new ScriptCraftWindow((Makewindow)wnd);
            }
        }
        return null;
    }
    
    public String getCursor() {
        return getEngine().getCursor();
    }
    
    public ScriptItem getDragItem() {
        Item i = Utils.getDragItem();
        return i != null ? new ScriptItem(i) : null;
    }
    
    private Engine getEngine() {
        return Engine.getInstance();
    }
    
    public ScriptItem getEquipItem(int index) {
        if (UI.equip != null) {
            Item i = UI.equip.equed.get(index);
            return i != null ? new ScriptItem(i) : null;
        }
        return null;
    }
    
    public ScriptGob[] getGobs() {
        ArrayList<ScriptGob> gobs = new ArrayList<ScriptGob>();
        synchronized(glob().oc) {
            for (Gob gob : glob().oc)
                gobs.add(new ScriptGob(gob));
        }
        ScriptGob[] arr = new ScriptGob[gobs.size()];
        return gobs.toArray(arr);
    }
    
    public int getHp() {
        return getEngine().getHp();
    }
    
    public int getHunger() {
        return getEngine().getHunger();
    }
    
    public ScriptInventory getInventory(String windowname) {
        Inventory inv = Utils.getWindowInventory(windowname);
        return (inv != null) ? new ScriptInventory(inv) : null;
    }
    
    private MapView getMapView() {
        return UI.instance.mainview;
    }
    
    private Coord getMyCoords() {
        Gob pl;
        synchronized (glob().oc) {
            pl = glob().oc.getgob(getPlayerId());
        }
        return (pl != null) ? pl.getc() : new Coord(0, 0);
    }
    
    public int getMyCoordX() { return getMyCoords().x; }
    
    public int getMyCoordY() { return getMyCoords().y; }
    
    public int getObjectBlob(int id, int index) {
        int r = 0;
        synchronized (glob().oc) {
            for (Gob gob : glob().oc) {
                if (gob.id == id) {
                    r = gob.getBlob(index);
                    break;
                }
            }
        }
        return r;
    }
    
    public int getPlayerId() {
        return (UI.instance.mainview != null) ? UI.instance.mainview.getPlayerGob() : -1;
    }
    
    public int getStamina() {
        return getEngine().getStamina();
    }
    
    public ScriptWindow[] getWindows() {
        ArrayList<ScriptWindow> list = new ArrayList<ScriptWindow>();
        for (Widget wdg = UI.instance.root.child; wdg != null; wdg = wdg.next)
            if (wdg instanceof Window)
                list.add(new ScriptWindow((Window)wdg));
        return list.toArray(new ScriptWindow[list.size()]);
    }
    
    private Glob glob() {
        return UI.instance.sess.glob;
    }
    
    public boolean hasContextMenu() {
        return getContextMenu() != null;
    }
    
    public boolean hasDragItem() {
        return getDragItem() != null;
    }
    
    public boolean hasInventory(String windowname) {
        return Utils.getWindowInventory(windowname) != null;
    }
    
    public boolean isMoving() {
        return UI.instance.mainview.player_moving;
    }
    
    // Click using absolute coords
    public void mapAbsClick(int x, int y, int btn, int mod) {
        Coord mc = new Coord(x,y);
        getMapView().wdgmsg("click", Utils.getScreenCenter(), mc, btn, mod);               
    }
    
    public void mapAbsInteractClick(int x, int y, int mod) {
        MapView mv = getMapView();
        Coord mc = new Coord(x,y);
        mv.wdgmsg("itemact", Utils.getScreenCenter(), mc, mod);      
    }
    
    // Click using relative coords
    public void mapClick(int x, int y, int btn, int mod) {
        MapView mv = getMapView();
        Gob pgob;
        synchronized(glob().oc) {
            pgob = glob().oc.getgob(mv.getPlayerGob());
        }
        if (pgob == null) return;
        Coord mc = MapView.tilify(pgob.getc());
        Coord offset = new Coord(x,y).mul(MCache.tilesz);
        mc = mc.add(offset);
        mv.wdgmsg("click", Utils.getScreenCenter(), mc, btn, mod);
    }
    
    public void mapInteractClick(int objid, int mod) {
        MapView mv = getMapView();
        Gob pgob, gob;
        synchronized(glob().oc) {
            pgob = glob().oc.getgob(mv.getPlayerGob());
            gob = glob().oc.getgob(objid);
        }
        if (pgob == null || gob == null) return;
        Coord mc = gob.getc();      
        mv.wdgmsg("itemact", Utils.getScreenCenter(), mc, mod, objid, mc);      
    }
    
    public void mapInteractClick(int x, int y, int mod) {
        MapView mv = getMapView();
        Gob pgob;
        synchronized(glob().oc) {
            pgob = glob().oc.getgob(mv.getPlayerGob());
        }
        if (pgob == null) return;
        Coord mc = MapView.tilify(pgob.getc());
        Coord offset = new Coord(x,y).mul(MCache.tilesz);
        mc = mc.add( offset );
        mv.wdgmsg("itemact", Utils.getScreenCenter(), mc, mod);      
    }
    
    // Move to specified object
    public void mapMove(int objid, int x, int y) {
        Coord oc, sc;
        int btn = 1; // left button click
        int modflags = 0; // no pressed keys
        Gob gob;
        synchronized(glob().oc) {
            gob = glob().oc.getgob(objid);
        }
        if (gob == null)
            return;
        MapView mv = getMapView();
        sc = new Coord(
                (int)Math.round(Math.random() * 200 + mv.sz.x / 2 - 100),
                (int)Math.round(Math.random() * 200 + mv.sz.y / 2 - 100));
        oc = gob.getc();
        oc = oc.add(new Coord(x, y));
        mv.wdgmsg("click", sc, oc, btn, modflags, objid, oc);              
    }
    
    public void mapMoveStep(int x, int y) {
        Gob pgob;
        int btn = 1;
        int modflags = 0;
        MapView mv = getMapView();
        synchronized(glob().oc) {
            pgob = glob().oc.getgob(mv.getPlayerGob());
        }
        if (pgob == null) return;
        Coord mc = MapView.tilify(pgob.getc());
        Coord offset = new Coord(x,y).mul(MCache.tilesz);
        mc = mc.add( offset );
        mv.wdgmsg("click", Utils.getScreenCenter(), mc, btn, modflags );                        
    }
    
    public void mapPlace(int x, int y, int btn, int mod) {
        MapView mv = getMapView();
        if (mv.isPlaceMode()) {
            Gob pgob;
            synchronized(glob().oc) {
                pgob = glob().oc.getgob(mv.getPlayerGob());
            }
            if (pgob == null) return;
            Coord mc = MapView.tilify(pgob.getc());
            Coord offset = new Coord(x,y).mul(MCache.tilesz);
            mc = mc.add( offset );          
            mv.wdgmsg("place", mc, btn, mod);
        }
    }

    public void openCharWindow() {
        UI.instance.root.wdgmsg("gk", KeyEvent.VK_CONTROL | KeyEvent.VK_T);
    }
    
    public void openEquipment() {
        UI.instance.root.wdgmsg("gk", KeyEvent.VK_CONTROL | KeyEvent.VK_E);
    }
    
    public void openInventory() {
        UI.instance.root.wdgmsg("gk", 9);
    }
    
    public void say(String message) {
        ScriptChatWindow wnd = findChatWindow("Area Chat");
        if (wnd != null)
            wnd.say(message);
    }
    
    public void sendAction(String... action) {
        if (UI.instance.mnu != null) {
            if (action.length == 1 && action[0].equals("laystone"))
                UI.instance.mnu.wdgmsg("act", "stoneroad", "stone");
            else
                UI.instance.mnu.wdgmsg("act", (Object[])action);
        }
    }
}
