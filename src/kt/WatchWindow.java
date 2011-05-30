package kt;

import haven.Coord;
import haven.Widget;

public class WatchWindow extends TransparentWindow {
    private final PropertyGrid grid;
    
    public WatchWindow(Coord c, Coord sz, Widget parent, String cap) {
        super(c, sz, parent, cap);
        grid = new PropertyGrid(new Coord(0, 0), this.asz.sub(mrgn), this);
    }

    @Override
    protected void recalcsz(Coord max) {
        super.recalcsz(max);
        if (grid != null) {
            grid.setsize(this.asz.sub(mrgn));
        }
    }
}
