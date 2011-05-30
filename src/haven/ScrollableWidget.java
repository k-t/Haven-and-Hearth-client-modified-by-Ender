package haven;

public class ScrollableWidget extends Widget {
    static Tex schain = Resource.loadtex("gfx/hud/schain");
    static Tex sflarp = Resource.loadtex("gfx/hud/sflarp");
    
    protected int scrollposition = 0;
    protected int scrollsize = 0;
    boolean drag = false;

    public ScrollableWidget(Coord c, Coord sz, Widget parent) {
        super(c, sz, parent);
    }
    
    protected final int scrollmargin() {
        return sflarp.sz().x;
    }
    
    @Override
    public void draw(GOut g) {
        super.draw(g);
        if(scrollsize > sz.y) {
            int fx = sz.x - sflarp.sz().x;
            int cx = fx + (sflarp.sz().x / 2) - (schain.sz().x / 2);
            for(int y = 0; y < sz.y; y += schain.sz().y - 1)
            g.image(schain, new Coord(cx, y));
            double a = (double)(scrollposition - sz.y) / (double)(scrollsize - sz.y);
            int fy = (int)((sz.y - sflarp.sz().y) * a);
            g.image(sflarp, new Coord(fx, fy));
        }
    }

    @Override
    public boolean mousedown(Coord c, int button) {
        int fx = sz.x - sflarp.sz().x;
        if((scrollsize > sz.y) && (c.x >= fx)) {
            drag = true;
            ui.grabmouse(this);
            mousemove(c);
            return(true);
        }
        return super.mousedown(c, button);
    }

    @Override
    public boolean mouseup(Coord c, int button) {
        if((button == 1) && drag) {
            drag = false;
            ui.grabmouse(null);
            return(true);
        }
        return super.mouseup(c, button);
    }

    @Override
    public boolean mousewheel(Coord c, int amount) {
        scrollposition += amount * 20;
        if(scrollposition < sz.y)
            scrollposition = sz.y;
        if(scrollposition > scrollsize)
            scrollposition = scrollsize;
        return true;
    }

    @Override
    public void mousemove(Coord c) {
        if(drag) {
            double a = (double)(c.y - (sflarp.sz().y / 2)) / (double)(sz.y - sflarp.sz().y);
            if(a < 0)
            a = 0;
            if(a > 1)
            a = 1;
            scrollposition = (int)(a * (scrollsize - sz.y)) + sz.y;
        } else {
            super.mousemove(c);
        }
    }
}
