package haven.minimap;

import haven.*;

public class MarkerClass {
    public final Tex tex;
    private final boolean visible;
    
    public MarkerClass(Tex tex, boolean visible) {
        this.tex = tex;
        this.visible = visible;
    }
    
    public boolean visible() {
        return visible;
    }
}
