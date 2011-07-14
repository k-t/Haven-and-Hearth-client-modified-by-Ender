package haven.scripting;

import haven.*;

final class TileGrabber extends ScriptGrabber {
    static final String title = "Tile selection";
    static final String defaulttext = "Select tile:";
    Position tile;
    Coord c;
    MCache.Overlay ol;
    final MCache map;
    
    public TileGrabber(String text, Coord c, Widget parent) {
        super(title, (text != null && text.length() > 0) ? text : defaulttext, c, parent);
        this.map = this.ui.sess.glob.map;
    }
    
    public Position tile() {
        return tile;
    }
    
    @Override    
    public void destroy() {
        if (this.ol != null)
            this.ol.destroy();
        super.destroy();
    }
    
    @Override
    public void done() {
        if (c != null) {
            this.tile = Position.fromTileCoord(c);
        }
    }

    @Override
    public void mmousedown(Coord mc, int button) {
        c = mc.div(MCache.tilesz);
        if (this.ol != null)
            this.ol.destroy();
        this.ol = map.new Overlay(c, c, 65536);
    }

    @Override
    public void mmouseup(Coord mc, int button) {
    }

    @Override
    public void mmousemove(Coord mc) {
    }

    @Override
    public void uimsg(String msg, Object... args) {
        if (msg == "reset") {
            this.ol.destroy();
            this.ol = null;
            this.c = null;
        }
    }    
}
