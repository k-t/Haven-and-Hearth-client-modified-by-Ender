package haven.minimap;

import haven.*;

public class Marker {
    private final Gob gob;
    private final MarkerClass mclass;
    private final Coord negc;
    
    public Marker(Gob gob, MarkerClass mclass) {
        this.gob = gob;
        this.mclass = mclass;
        this.negc = mclass.tex.sz().div(2);
    }
    
    public Coord getc() {
        return gob.getc();
    }
    
    public int gobid() {
        return gob.id;
    }
    
    public Coord negc() {
        return negc;
    }
    
    public Tex tex() {
        return mclass.tex;
    }
    
    public boolean visible() {
        return mclass.visible();
    }
    
    public boolean isplayer() {
        return mclass.isplayer();
    }
}

