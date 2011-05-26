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
import java.awt.font.TextAttribute;
import java.util.LinkedList;
import java.util.List;

public class Textlog extends ScrollableWidget {
    static Tex texpap = Resource.loadtex("gfx/hud/texpap");
    static RichText.Foundry fnd = new RichText.Foundry(TextAttribute.FAMILY, "Sans Serif", TextAttribute.SIZE, 12, TextAttribute.FOREGROUND, Color.BLACK);
    List<Text> lines;
    protected int margin = 3;
    protected int textsize = 12;
    public boolean drawbg = true;
    public Color defcolor = Color.BLACK;
	
    static {
	Widget.addtype("log", new WidgetFactory() {
		public Widget create(Coord c, Widget parent, Object[] args) {
		    return(new Textlog(c, (Coord)args[0], parent));
		}
	    });
    }
	
    public void draw(GOut g) {
	Coord dc = new Coord();
	if (drawbg)
	    for (dc.y = 0; dc.y < sz.y; dc.y += texpap.sz().y) {
		for (dc.x = 0; dc.x < sz.x; dc.x += texpap.sz().x) {
		    g.image(texpap, dc);
		}
	    }
	g.chcolor();
	int y = -this.scrollposition;
	synchronized(lines) {
	    for(Text line : lines) {
		int dy1 = sz.y + y;
		int dy2 = dy1 + line.sz().y;
		if((dy2 > 0) && (dy1 < sz.y))
		    g.image(line.tex(), new Coord(margin, dy1));
		y += line.sz().y;
	    }
	}
	super.draw(g);
    }
	
    public Textlog(Coord c, Coord sz, Widget parent) {
	super(c, sz, parent);
	lines = new LinkedList<Text>();
    }
    
    public final void append(String line, Color col) {
	line = RichText.Parser.quote(line);
	if(Config.use_smileys){
	    line = Config.mksmiley(line);
	}
	add(line, col);
    }
        
    public final void append(String line) {
	append(line, null);
    }
    
    /** Appends line without processing. */
    private void add(String line, Color col) {
        Text rl;
        if(col == null)
            col = defcolor;
        rl = fnd.render(line, sz.x - (margin * 2) - sflarp.sz().x, TextAttribute.FOREGROUND, col, TextAttribute.SIZE, textsize);
        synchronized(lines) {
            lines.add(rl);
        }
        if(scrollposition == scrollsize)
            scrollposition += rl.sz().y;
        scrollsize += rl.sz().y;
    }
    
    /** Removes all lines. */
    public final void clear() {
        synchronized (lines) {
            lines.clear();
            scrollposition = 0;
            scrollsize = 0;
        }
    }
    
    /** Returns count of lines in the text log. */
    public final int linecount() {
        return lines.size();
    }
    
    
    /** Removes the line specified by the given index. */
    public final void removeat(int index) {
        synchronized(lines) {
            if (lines.size() <= index)
                return;
            Text rl = lines.get(index);
            lines.remove(index);
            scrollsize -= rl.sz().y;
            if (scrollposition > scrollsize)
                scrollposition = scrollsize;
        }
    }
	
    public void uimsg(String msg, Object... args) {
	if(msg == "apnd") {
	    append((String)args[0]);
	}
    }
    
    public final void update() {
        Text[] oldlines = new Text[lines.size()];
        oldlines = lines.toArray(oldlines);
        clear();
        for (Text line : oldlines) {
            add(line.text, null);
        }
    }
        
    public boolean mousedown(Coord c, int button) {
	if(button != 1)
	    return(false);
	return super.mousedown(c, button);
    }
}
