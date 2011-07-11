package haven.minimap;

import haven.*;

public class MarkerClass {
    public final Tex tex;
    private final boolean visible;
    private final boolean isplayer;
    
    public MarkerClass(Tex tex, boolean visible, boolean isplayer) {
        this.tex = tex;
        this.visible = visible;
        this.isplayer = isplayer;
    }
    
    public boolean isplayer() {
        return isplayer;
    }
    
    public boolean visible() {
        return visible;
    }
}
