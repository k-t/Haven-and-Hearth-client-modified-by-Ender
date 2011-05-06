package haven.scripting;

import static haven.MCache.tilesz;

import java.util.ArrayList;

import haven.*;

public class ScriptGlobal {
    private Engine engine;
    
    public ScriptGlobal(Engine engine) {
        this.engine = engine;
    }
    
    private Glob glob() {
        return UI.instance.sess.glob;
    }
    
    public boolean checkHourglass() {
        return engine.isHourglass();
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
            ark.log.LogPrint("send object click: " + oc.toString() + " obj_id=" + objid + " btn=" + btn + " modflags=" + modflags);
            UI.instance.mainview.wdgmsg("click",sc, oc, btn, modflags, objid, oc);
        }
    }
    
    public void drop(int mod) {
        if (UI.instance.mainview != null)
            UI.instance.mainview.wdgmsg("drop", mod);
    }
    
    public void equip(int slot, String action) {
        if (UI.equip == null)
            return;
        if ((!action.equals("take")) &&
            (!action.equals("transfer")) &&
            (!action.equals("drop")) &&
            (!action.equals("iact")) &&
            (!action.equals("itemact"))) {
            return;       
        }
        if (action.equals("itemact"))
            UI.equip.wdgmsg("itemact", slot);
        else
            UI.equip.wdgmsg(action, slot, new Coord(10,10));
    }
    
    public int findMapObject(String name, int radius, int x, int y) {
        Coord my = getMyCoords();
        my = MapView.tilify(my);
        Coord offset = new Coord(x, y).mul(haven.MCache.tilesz);
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
        return findMapObject(name, radius * 11, 0,0);
    }
    
    public int findObjectByType(String name, int radius) {
        Coord my = getMyCoords();
        double min = radius * 11;
        Gob mingob = null;
        synchronized (glob().oc) {
            for (Gob gob : glob().oc) {
                boolean matched = false;
                if (name.equals("tree"))
                    // find trees and grow stage... 
                    matched = ((gob.resname().indexOf("trees") >= 0) && (gob.resname().indexOf("0") >= 0));
                if (matched) {
                    double len = gob.getc().dist(my);
                    if (len < min) {
                        min = len;
                        mingob = gob;
                    }
                }
            }
        }
        if (mingob != null)
            return mingob.id;
        else
            return 0;
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
    
    public ScriptFlowerMenu getContextMenu() {
        if (UI.flower_menu != null)
            return new ScriptFlowerMenu(UI.flower_menu);
        else
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
    
    public String getCursor() {
        return engine.getCursor();
    }
    
    public ScriptItem getDragItem() {
        for (Widget wdg = UI.instance.root.child; wdg != null; wdg = wdg.next)
            if ((wdg instanceof Item) && (((Item)wdg).dm))
                return new ScriptItem((Item)wdg, this);
        return null;
    }
   
    public ScriptItem getEquipItem(int index) {
        if (UI.equip != null) {
            return new ScriptItem(UI.equip.equed.get(index), this);
        }
        return null;
    }
    
    public ScriptInv getInventory(String windowname) {
        Inventory inv = getWindowInventory(windowname);
        return (inv != null) ? new ScriptInv(inv, this) : null;
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
    
    public int getStamina() {
        return engine.getStamina();
    }
    
    public int getHunger() {
        return engine.getHunger();
    }
    
    public int getHp() {
        return engine.getHp();
    }
    
    public int getMyCoordX() { return getMyCoords().x; }
    public int getMyCoordY() { return getMyCoords().y; }
    
    public int getPlayerId() {
        return (UI.instance.mainview != null)
            ? UI.instance.mainview.getPlayerGob()
            : -1;
    }
    
    /* Returns approximate coords for click in the center of the screen */
    public Coord getScreenCenter() {
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
    
    private Inventory getWindowInventory(String windowname) {
        Widget root = UI.instance.root;
        for (Widget wdg = root.child; wdg != null; wdg = wdg.next) {
            if (wdg instanceof Window) {
                Window w = (Window)wdg;
                if ( w.cap != null && w.cap.text != null && w.cap.text.equals(windowname) )
                    for (Widget inv = wdg.child; inv != null; inv = inv.next)
                        if (inv instanceof Inventory)
                            return (Inventory)inv;
            }
        }
        return null;
    }
    
    public boolean isMoving() {
        return UI.instance.mainview.player_moving;
    }
    
    // Move to specified object
    public void mapMove(int objid, Coord offset) {
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
        oc = oc.add(offset);
        ark.log.LogPrint("send object click: "+oc.toString()+" obj_id="+objid+" btn="+btn+" modflags="+modflags);
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
        Coord offset = new Coord(x,y).mul(tilesz);
        mc = mc.add( offset );
        ark.log.LogPrint("send map click: "+mc.toString()+" btn="+btn+" modflags="+modflags);
        mv.wdgmsg("click", this.getScreenCenter(), mc, btn, modflags );                        
    }
    
    /*public void mapPlace(int x, int y, int btn, int mod) {
        MapView mv = getMapView();
        if (mv.plob != null) {
            Gob pgob;
            synchronized(glob.oc) {
                pgob = glob.oc.getgob(mv.getPlayerGob());
            }
            if (pgob == null) return;
            Coord mc = MapView.tilify(pgob.getc());
            Coord offset = new Coord(x,y).mul(tilesz);
            mc = mc.add( offset );          
            mv.wdgmsg("place", mc, btn, mod);
        }
    }*/
    
    // Click using relative coords
    public void mapClick(int x, int y, int btn, int mod) {
        MapView mv = getMapView();
        Gob pgob;
        synchronized(glob().oc) {
            pgob = glob().oc.getgob(mv.getPlayerGob());
        }
        if (pgob == null) return;
        Coord mc = MapView.tilify(pgob.getc());
        Coord offset = new Coord(x,y).mul(tilesz);
        mc = mc.add(offset);
        ark.log.LogPrint("send map interact click: "+mc.toString()+" modflags="+mod);
        mv.wdgmsg("click", getScreenCenter(), mc, btn, mod);
    }
    
    // Click using absolute coords
    public void mapAbsClick(int x, int y, int btn, int mod) {
        Coord mc = new Coord(x,y);
        ark.log.LogPrint("send map interact click: "+mc.toString()+" modflags="+mod);
        getMapView().wdgmsg("click", getScreenCenter(), mc, btn, mod);               
    }
    
    public void mapInteractClick(int x, int y, int mod) {
        MapView mv = getMapView();
        Gob pgob;
        synchronized(glob().oc) {
            pgob = glob().oc.getgob(mv.getPlayerGob());
        }
        if (pgob == null) return;
        Coord mc = MapView.tilify(pgob.getc());
        Coord offset = new Coord(x,y).mul(tilesz);
        mc = mc.add( offset );
        ark.log.LogPrint("send map interact click: "+mc.toString()+" modflags="+mod);
        mv.wdgmsg("itemact", getScreenCenter(), mc, mod);      
    }
    
    public void mapAbsInteractClick(int x, int y, int mod) {
        MapView mv = getMapView();
        Gob pgob;
        synchronized(glob().oc) {
            pgob = glob().oc.getgob(mv.getPlayerGob());
        }
        if (pgob == null) return;
        Coord mc = new Coord(x,y);
        ark.log.LogPrint("send map interact click: "+mc.toString()+" modflags="+mod);
        mv.wdgmsg("itemact", getScreenCenter(), mc, mod);      
    }
    
    public void mapInteractClick(int id, int mod) {
        MapView mv = getMapView();
        Gob pgob, gob;
        synchronized(glob().oc) {
            pgob = glob().oc.getgob(mv.getPlayerGob());
            gob = glob().oc.getgob(id);
        }
        if (pgob == null || gob == null) return;
        Coord mc = gob.getc();      
        ark.log.LogPrint("send map interact click: "+mc.toString()+" modflags="+mod);
        mv.wdgmsg("itemact", getScreenCenter(), mc, mod, id, mc);      
    }
    
    public boolean hasContextMenu() {
        return getContextMenu() != null;
    }
    
    public boolean hasDragItem() {
        return getDragItem() != null;
    }
    
    public boolean hasInventory(String windowname) {
        return getWindowInventory(windowname) != null;
    }

    public void openInventory() {
        UI.instance.root.wdgmsg("gk", 9);
    }
    
    public void sendAction(String action) {
        if (UI.instance.mnu != null) {
            if (action.equals("laystone"))
                UI.instance.mnu.wdgmsg("act", "stoneroad", "stone");
            else
                UI.instance.mnu.wdgmsg("act", action);
        }
    }
    
    public void sendAction(String action1, String action2) {
        if (UI.instance.mnu != null) {
            UI.instance.mnu.wdgmsg("act", action1, action2);
        }
    }
}
