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

package kt;


import haven.Coord;
import haven.GOut;
import haven.Resource;
import haven.Widget;
import haven.Window;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class TransparentWindow extends Window {
	static final BufferedImage grip = Resource.loadimg("gfx/hud/gripbr");
    static final Coord gzsz = new Coord(16,17);
    private static final Coord minsz = new Coord(150, 125);

    boolean rsm = false;
	
    public TransparentWindow(Coord c, Coord sz, Widget parent, String cap, Coord tlo, Coord rbo) {
    super(c, sz, parent, cap, tlo, rbo);
    pack();
    }
	
    public TransparentWindow(Coord c, Coord sz, Widget parent, String cap) {
	this(c, sz, parent, cap, new Coord(0, 0), new Coord(0, 0));
	pack();
    }
	
    @Override
    public void draw(GOut og) {
	GOut g = og.reclip(tlo, wsz);
	g.chcolor(new Color(0, 0, 0, 120));
	g.frect(Coord.z, sz);
	g.chcolor();
	cdraw(og.reclip(xlate(Coord.z, true), sz));
	wbox.draw(g, Coord.z, wsz);
	if(cap != null) {
	    GOut cg = og.reclip(new Coord(0, -7), sz.add(0, 7));
	    int w = cap.tex().sz().x;
		int x0 = (sz.x / 2) - (w / 2);
	    cg.image(cl, new Coord(x0 - cl.sz().x, 0));
	    cg.image(cm, new Coord(x0, 0), new Coord(w, cm.sz().y));
	    cg.image(cr, new Coord(x0 + w, 0));
	    cg.image(cap.tex(), new Coord(x0, 0));
	}
	g.image(grip, sz.sub(gzsz));
	Widget next;
	for(Widget wdg = child; wdg != null; wdg = next) {
	    next = wdg.next;
	    if(!wdg.isVisible())
		continue;
	    Coord cc = xlate(wdg.c, true);
	    wdg.draw(g.reclip(cc, wdg.sz));
	}
    }
    
    protected Coord getminsz() {
    	return minsz;
    }
	
    @Override
    protected void recalcsz(Coord max)
    {
    sz.x = Math.max(getminsz().x, max.x);
    sz.y = Math.max(getminsz().y, max.y);
	wsz = sz.sub(tlo).sub(rbo);
	asz = new Coord(wsz.x - wbox.bl.sz().x - wbox.br.sz().x - mrgn.x, wsz.y - wbox.bt.sz().y - wbox.bb.sz().y - mrgn.y);
    }
    
    @Override
    public void pack() {
	Coord max = sz;
	for(Widget wdg = child; wdg != null; wdg = wdg.next) {
	    if((wdg == cbtn))
		continue;
	    Coord br = wdg.c.add(wdg.sz);
	    if(br.x > max.x)
		max.x = br.x;
	    if(br.y > max.y)
		max.y = br.y;
	}
	recalcsz(max);
	placecbtn();
    }
	
    @Override	
    public boolean mousedown(Coord c, int button) {
	parent.setfocus(this);
	raise();
	for(Widget wdg = lchild; wdg != null; wdg = wdg.prev) {
	    if(!wdg.isVisible())
		continue;
	    Coord cc = xlate(wdg.c, true);
	    if(c.isect(cc, (wdg.hsz == null)?wdg.sz:wdg.hsz)) {
		if(wdg.mousedown(c.add(cc.inv()), button)) {
		    return(true);
		}
	    }
	}
	if(!c.isect(tlo, sz.add(tlo.inv()).add(rbo.inv())))
	    return(false);
	if(button == 1 && c.isect(sz.sub(gzsz), gzsz)) {
		doff = sz.sub(c);
    	ui.grabmouse(this);
		rsm = true;
	    return true;
	}
	return super.mousedown(c, button);
    }
	
    @Override
    public boolean mouseup(Coord c, int button) {
	if (rsm) {
	    ui.grabmouse(null);
	    rsm = false;
	} else {
	    super.mouseup(c, button);
	}
	return(true);
    }
	
    @Override
    public void mousemove(Coord c) {
	if (rsm) {
	    sz = c.add(doff);
	    pack();
	} else {
	    super.mousemove(c);
	}
    }

    @Override
    public void wdgmsg(Widget sender, String msg, Object... args) {
	if(sender == cbtn) {
		ui.destroy(this);
	} else {
	    super.wdgmsg(sender, msg, args);
	}
    }
	
    @Override
    public boolean type(char key, java.awt.event.KeyEvent ev) {
	if(key == 27) {
		ui.destroy(this);
	    return(true);
	}
	return(super.type(key, ev));
    }
}
