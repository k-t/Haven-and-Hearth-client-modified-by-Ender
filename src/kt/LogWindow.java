package kt;


import haven.Button;
import haven.Coord;
import haven.Widget;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

public class LogWindow extends TransparentWindow {
	private LogWidget tl;
	private HashMap<String, ToggleButton> tabs;
	private HashMap<String, LogManager.Log> logs;
	private Button clearbtn;
	private String currenttab;
	
	public LogWindow(Coord c, Coord sz, Widget parent, String cap) {
	    super(c, sz, parent, cap);
	    
	    tabs = new HashMap<String, ToggleButton>();
	    logs = new HashMap<String, LogManager.Log>();
	    // text log
	    tl = new LogWidget(new Coord(0, 25), new Coord(0, 0), this);
	    tl.drawbg = false;
	    tl.defcolor = Color.WHITE;
	    // clear button
	    clearbtn = new Button(new Coord(0, 0), 40, this, "Clear") {
	    	public void click() {
	    		clear();
	    	}
	    };
	    pack();
    }
	
	public void addlog(String name, LogManager.Log log) {
		// add tab button
		ToggleButton tb = new ToggleButton(new Coord(tabs.size() * 85, 0), 80, this, name) {
			public void click() {
			    settab(this);
			}
	    };
	    tabs.put(name, tb);
	    logs.put(name, log);
	    // activate first added tab
	    if (tabs.size() == 1)
	    	settab(tb);
	    pack();
	}
	
	private void clear() {
		if (currenttab == null)
			return;
		logs.get(currenttab).clear();
		tl.clear();
	}
	
	@Override
	protected synchronized Coord getminsz() {
		int tabwidth = 0;
		if (tabs != null) {
			tabwidth = tabs.size() * 85;				
		}
		int minwidth = Math.max(tabwidth, 40);
		return new Coord(minwidth + this.mrgn.x * 2, 125);
	}
	
	private void settab(ToggleButton tabbtn) {
		synchronized(tabs) {
			for (Entry<String, ToggleButton> entry : tabs.entrySet()) {
				String tab = entry.getKey();
				ToggleButton btn = entry.getValue();
				btn.settoggled(btn == tabbtn);
				if (btn == tabbtn) {
					currenttab = tab;
					tl.setlog(logs.get(tab));
				}
			}
		}
	}
	
	@Override
	protected void recalcsz(Coord max) {
		super.recalcsz(max);
		if (tl != null) {
			tl.sz = this.asz.sub(0, 55);
		}
		if (clearbtn != null) {
			clearbtn.c = new Coord(0, this.asz.y - 25);
		}
	}
	
    @Override	
    public boolean mousedown(Coord c, int button) {
    	boolean b = super.mousedown(c, button);
    	if (rsm)
    		tl.beginupdate(); // stop text rendering
    	return b;
    }
    
    @Override
    public boolean mouseup(Coord c, int button) {
    	if (rsm)
    		tl.endupdate();
    	return super.mouseup(c, button);
    }
	
    @Override
    public void wdgmsg(Widget sender, String msg, Object... args) {
	if(sender == cbtn) {
		this.hide();
	} else {
	    super.wdgmsg(sender, msg, args);
	}
    }
    
    @Override
    public boolean type(char key, java.awt.event.KeyEvent ev) {
	if(key == 27) {
		this.hide();
	    return(true);
	}
	return(super.type(key, ev));
    }
}
