package haven.scripting;

import haven.*;

public class AreaGrabber extends ScriptGrabber {
    static final String title = "Area selection";
    static final String defaulttext = "Select area:";
    boolean dm = false;
    Coord sc;
    Coord c1, c2;
    MCache.Overlay ol;
    final MCache map;
    private Area area;

    public AreaGrabber(String text, Coord c, Widget parent) {
        super(title, (text != null && text.length() > 0) ? text : defaulttext, c, parent);
        this.map = this.ui.sess.glob.map;
    }
    
    public Area area() {
        return area;
    }
    
    @Override    
    public void destroy() {
        if (this.ol != null)
            this.ol.destroy();
        super.destroy();
    }
    
    @Override
    public void done() {
        if (c1 != null && c2 != null) {
            Coord cc1 = MapView.tilify(c1.mul(MCache.tilesz));
            Coord cc2 = MapView.tilify(c2.mul(MCache.tilesz));
            int n = Math.min(cc1.y, cc2.y);
            int s = Math.max(cc1.y, cc2.y);
            int w = Math.min(cc1.x, cc2.x);
            int e = Math.max(cc1.x, cc2.x);
            this.area = new Area(n, w, s, e);
        }
    }
    
    @Override
    public void mmousedown(Coord mc, int button) {
        Coord c = mc.div(MCache.tilesz);
        if (this.ol != null)
            this.ol.destroy();
        this.ol = map.new Overlay(c, c, 65536);
        this.sc = c;
        this.dm = true;
        this.ui.grabmouse(this.ui.mainview);
    }

    @Override
    public void mmouseup(Coord mc, int button) {
        this.dm = false;
        this.ui.grabmouse(null);
    }

    @Override
    public void mmousemove(Coord mc) {
        if (!this.dm)
            return;
        Coord c = mc.div(MCache.tilesz);
        Coord localCoord2 = new Coord(0, 0);
        Coord localCoord3 = new Coord(0, 0);
        if (c.x < this.sc.x) {
            localCoord2.x = c.x;
            localCoord3.x = this.sc.x;
        } else {
            localCoord2.x = this.sc.x;
            localCoord3.x = c.x;
        }
        if (c.y < this.sc.y) {
            localCoord2.y = c.y;
            localCoord3.y = this.sc.y;
        } else {
            localCoord2.y = this.sc.y;
            localCoord3.y = c.y;
        }
        this.ol.update(localCoord2, localCoord3);
        this.c1 = localCoord2;
        this.c2 = localCoord3;
    }
    
    @Override
    public void uimsg(String msg, Object... args) {
        if (msg == "reset") {
            this.ol.destroy();
            this.ol = null;
            this.c1 = (this.c2 = null);
        }
    }
}
