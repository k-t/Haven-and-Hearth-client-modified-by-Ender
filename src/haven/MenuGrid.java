/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import haven.Resource.AButton;
import java.util.*;

public class MenuGrid extends Widget {
    public final static Tex bg = Resource.loadtex("gfx/hud/invsq");
    public final static Coord bgsz = bg.sz().add(-1, -1);
    public final static MenuGridButton next = MenuGridButton.fromResource("gfx/hud/sc-next");
    public final static MenuGridButton bk = MenuGridButton.fromResource("gfx/hud/sc-back");
    public final static MenuGridButton script = MenuGridButton.fromResource("gfx/hud/script");
    public final static RichText.Foundry ttfnd = new RichText.Foundry(TextAttribute.FAMILY, "SansSerif", TextAttribute.SIZE, 10);
    private static Coord gsz = new Coord(4, 4);
    private MenuGridButton cur, pressed, dragging, layout[][] = new MenuGridButton[gsz.x][gsz.y];
    private int curoff = 0;
    private Map<Character, MenuGridButton> hotmap = new TreeMap<Character, MenuGridButton>();
	
    static {
	Widget.addtype("scm", new WidgetFactory() {
		public Widget create(Coord c, Widget parent, Object[] args) {
		    return(new MenuGrid(c, parent));
		}
	    });
    }
	
    public class PaginaException extends RuntimeException {
	public Resource res;
	
	public PaginaException(Resource r) {
	    super("Invalid pagina: " + r.name);
	    res = r;
	}
    }

    public Resource[] resbuttons() {
        Resource[] cp = new Resource[0];
        Resource[] all;
        {
            Collection<Resource> ta = new HashSet<Resource>();
            Collection<Resource> open;
            synchronized(ui.sess.glob.paginae) {
            open = new HashSet<Resource>(ui.sess.glob.paginae);
            }
            while(!open.isEmpty()) {
            for(Resource r : open.toArray(cp)) {
                if(!r.loading) {
                AButton ad = r.layer(Resource.action);
                if(ad == null)
                    throw(new PaginaException(r));
                if((ad.parent != null) && !ta.contains(ad.parent))
                    open.add(ad.parent);
                ta.add(r);
                open.remove(r);
                }
            }
            }
            all = ta.toArray(cp);
        }
        return all;
    }
    
    public MenuGridButton[] root() {
	Collection<MenuGridButton> tobe = new HashSet<MenuGridButton>();
	// add root button from the resource
	for(Resource r : resbuttons()) {
	    if(r.layer(Resource.action).parent == null)
		tobe.add(MenuGridButton.fromResource(r, this));
	}
	tobe.add(script);
	return(tobe.toArray(new MenuGridButton[0]));
    }
	
    public MenuGrid(Coord c, Widget parent) {
	super(c, bgsz.mul(gsz).add(1, 1), parent);
	updlayout();
	ui.mnu = this;
	ToolbarWnd.loadBelts();
	new ToolbarWnd(new Coord(0,300), ui.root, "toolbar1");
	new ToolbarWnd(new Coord(50,300), ui.root, "toolbar2", 2, 12, new Coord(4, 10), KeyEvent.VK_F1);
    }
	
    private void updlayout() {
	MenuGridButton[] cur = this.cur != null ? this.cur.children() : root();
	Arrays.sort(cur);
	int i;
	hotmap.clear();
	for (i = 0; i < cur.length; i++){
	    if(cur[i].hk() != 0)
		hotmap.put(Character.toUpperCase(cur[i].hk()), cur[i]);
	}
	i = curoff;
	for(int y = 0; y < gsz.y; y++) {
	    for(int x = 0; x < gsz.x; x++) {
		MenuGridButton btn = null;
		if((this.cur != null) && (x == gsz.x - 1) && (y == gsz.y - 1)) {
		    btn = bk;
		} else if((cur.length > ((gsz.x * gsz.y) - 1)) && (x == gsz.x - 2) && (y == gsz.y - 1)) {
		    btn = next;
		} else if(i < cur.length) {
		    btn = cur[i++];
		}
		layout[x][y] = btn;
	    }
	}
    }
	
    private static Text rendertt(MenuGridButton btn, boolean withpg) {
	Resource.Pagina pg = btn.pagina();
	String tt = btn.name();
	int pos = tt.toUpperCase().indexOf(Character.toUpperCase(btn.hk()));
	if(pos >= 0)
	    tt = tt.substring(0, pos) + "$col[255,255,0]{" + tt.charAt(pos) + "}" + tt.substring(pos + 1);
	else if(btn.hk() != 0)
	    tt += " [$col[255,255,0]{" + btn.hk() + "}]";
	if(withpg && (pg != null)) {
	    tt += "\n\n" + pg.text;
	}
	return(ttfnd.render(tt, 0));
    }

    public void draw(GOut g) {
	//updlayout();
	for(int y = 0; y < gsz.y; y++) {
	    for(int x = 0; x < gsz.x; x++) {
		Coord p = bgsz.mul(new Coord(x, y));
		g.image(bg, p);
		MenuGridButton btn = layout[x][y];
		if(btn != null) {
		    Tex btex = btn.tex();
		    g.image(btex, p.add(1, 1));
		    if(btn == pressed) {
			g.chcolor(new Color(0, 0, 0, 128));
			g.frect(p.add(1, 1), btex.sz());
			g.chcolor();
		    }
		}
	    }
	}
	if(dragging != null) {
	    final Tex dt = dragging.tex();
	    ui.drawafter(new UI.AfterDraw() {
		    public void draw(GOut g) {
			g.image(dt, ui.mc.add(dt.sz().div(2).inv()));
		    }
		});
	}
    }
	
    private MenuGridButton curttr = null;
    private boolean curttl = false;
    private Text curtt = null;
    private long hoverstart;
    public Object tooltip(Coord c, boolean again) {
	MenuGridButton bt = bhit(c);
	long now = System.currentTimeMillis();
	if((bt != null) && bt.hasTooltip()) {
	    if(!again)
		hoverstart = now;
	    boolean ttl = (now - hoverstart) > 500;
	    if((bt != curttr) || (ttl != curttl)) {
		curtt = rendertt(bt, ttl);
		curttr = bt;
		curttl = ttl;
	    }
	    return(curtt);
	} else {
	    hoverstart = now;
	    return("");
	}
    }

    private MenuGridButton bhit(Coord c) {
	Coord bc = c.div(bgsz);
	if((bc.x >= 0) && (bc.y >= 0) && (bc.x < gsz.x) && (bc.y < gsz.y))
	    return(layout[bc.x][bc.y]);
	else
	    return(null);
    }
	
    public boolean mousedown(Coord c, int button) {
    MenuGridButton h = bhit(c);
	if((button == 1) && (h != null)) {
	    pressed = h;
	    ui.grabmouse(this);
	}
	return(true);
    }
	
    public void mousemove(Coord c) {
	if((dragging == null) && (pressed != null)) {
	    MenuGridButton h = bhit(c);
	    if(h != pressed)
		dragging = pressed;
	}
    }
	
    public void use(MenuGridButton b) {
	if (b == null)
	    return;
    if(b.children().length > 0) {
	    cur = b;
	    curoff = 0;
	    updlayout();
	} else if(b == bk) {
	    cur = cur.parent();
	    curoff = 0;
	    updlayout();
	} else if(b == next) {
	    if((curoff + 14) >= cur.children().length)
		curoff = 0;
	    else
		curoff += 14;
	    updlayout();
	} else {
	    b.use();
	    updlayout();
	}
	}
    
    
    public boolean mouseup(Coord c, int button) {
	MenuGridButton h = bhit(c);
	if(button == 1) {
	    if(dragging != null) {
		ui.dropthing(ui.root, ui.mc, dragging);
		dragging = pressed = null;
	    } else if(pressed != null) {
		if(pressed == h)
		    use(h);
		pressed = null;
	    }
	    ui.grabmouse(null);
	}
	updlayout();
	return(true);
    }
	
    public void uimsg(String msg, Object... args) {
	if(msg == "goto") {
	    String res = (String)args[0];
	    if(res.equals(""))
		cur = null;
	    else
		cur = MenuGridButton.fromResource(res, this);
	    curoff = 0;
	    updlayout();
	}
    }

    public boolean globtype(char k, KeyEvent ev) {
	if((k == 27) && (this.cur != null)) {
	    this.cur = null;
	    curoff = 0;
	    updlayout();
	    return(true);
	} else if((k == 'N') && (layout[gsz.x - 2][gsz.y - 1] == next)) {
	    use(next);
	    return(true);
	}
	MenuGridButton r = hotmap.get(Character.toUpperCase(k));
	if(r != null) {
	    use(r);
	    return(true);
	}
	return(false);
    }

    public void gotoroot() {
        this.cur = null;
        this.curoff = 0;
        updlayout();
    }
}
