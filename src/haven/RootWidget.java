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

import haven.scripting.Engine;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GLException;

import kt.*;

import com.sun.opengl.util.Screenshot;

public class RootWidget extends ConsoleHost {
    public static Resource defcurs = Resource.load("gfx/hud/curs/arw");
    Logout logout = null;
    Profile gprof;
    boolean afk = false;
    boolean screenshot = false;
    
    private LogWindow logwindow = null;
	
    public RootWidget(UI ui, Coord sz) {
	super(ui, new Coord(0, 0), sz);
	setfocusctl(true);
	cursor = defcurs;
    }
	
    public boolean globtype(char key, KeyEvent ev) {
	if(!super.globtype(key, ev)) {
	    int code = ev.getKeyCode();
	    boolean ctrl = ev.isControlDown();
	    boolean alt = ev.isAltDown();
	    boolean shift = ev.isShiftDown();
	    if(Config.profile && (key == '`')) {
		new Profwnd(ui.slen, ui.mainview.prof, "MV prof");
	    } else if(Config.profile && (key == '~')) {
		new Profwnd(ui.slen, gprof, "Glob prof");
	    } else if(Config.profile && (key == '!')) {
		new Profwnd(ui.slen, ui.mainview.mask.prof, "ILM prof");
	    } else if((code == KeyEvent.VK_N)&&ctrl) {
		Config.nightvision = !Config.nightvision;
	    } else if((code == KeyEvent.VK_X)&&ctrl) {
		Config.xray = !Config.xray;
	    } else if((code == KeyEvent.VK_H)&&ctrl) {
		Config.hide = !Config.hide;
	    } else if((code == KeyEvent.VK_Q)&&alt&&!shift) {
		ui.spd.wdgmsg("set", 0);
	    } else if((code == KeyEvent.VK_W)&&alt&&!shift) {
		ui.spd.wdgmsg("set", 1);
	    } else if((code == KeyEvent.VK_E)&&alt&&!shift) {
		ui.spd.wdgmsg("set", 2);
	    } else if((code == KeyEvent.VK_R)&&alt&&!shift) {
		ui.spd.wdgmsg("set", 3);
	    } else if((code == KeyEvent.VK_G)&&ctrl) {
		Config.grid = !Config.grid;
	    } else if(((int)key == 2)&ctrl) {//CTRL-B have code of 02
		BuddyWnd.instance.visible = !BuddyWnd.instance.visible;
	    } else if(((int)key == 20)&ctrl) {//CTRL-T have code of 20
		CharWnd.instance.toggle();
	    } else if(code == KeyEvent.VK_HOME) {
		ui.mainview.resetcam();
	    } else if(code == KeyEvent.VK_END) {
		screenshot = true;
	    } else if(key == ':') {
		entercmd();
	    } else if (code == KeyEvent.VK_F12 && alt) {
	    	if (logwindow == null) {
        		logwindow = new LogWindow(new Coord(10, 10), new Coord(200, 200), this, null);
        		LogManager.addwindow(logwindow);
	    	} else if (logwindow.isVisible()) {
            	logwindow.hide();
            } else {
            	logwindow.show();
            }
	    } else if (code == KeyEvent.VK_Y && ctrl) {
	    	Config.render_enable = !Config.render_enable;
	    } else if (code == KeyEvent.VK_Q && ctrl) {
	    	Config.showq = !Config.showq;
	    	Config.saveOptions();
	    } else if (code == KeyEvent.VK_M && ctrl) {
	    	Window mw = UI.instance.slen.getMinimapWindow();
	    	if (mw != null) {
	    		mw.folded = !mw.folded;
	    		mw.checkfold();
	    	}
	    } else if (code == KeyEvent.VK_ENTER && alt) {
	    	ChatHWPanel cp = UI.instance.slen.getChatWindow();
	    	if (cp != null)
	    		cp.folded = !cp.folded;
	    } else if (code == KeyEvent.VK_F11 && alt) {
	    	Config.debug_flag =  !Config.debug_flag;
	    } else if (code == KeyEvent.VK_Q && alt && shift) {
	        Config.autospeedmode = AutoSpeedMode.Crawl;
	        Config.saveOptions();
	    } else if (code == KeyEvent.VK_W && alt && shift) {
	        Config.autospeedmode = AutoSpeedMode.Walk;
	        Config.saveOptions();
        } else if (code == KeyEvent.VK_E && alt && shift) {
            Config.autospeedmode = AutoSpeedMode.Run;
            Config.saveOptions();
        } else if (code == KeyEvent.VK_R && alt && shift) {
            Config.autospeedmode = AutoSpeedMode.Sprint;
            Config.saveOptions();
	    } else if(key != 0) {
	        wdgmsg("gk", (int)key);
	    } else {
	        // call script handler
	        Engine.getInstance().handleKeyEvent(ev);
	    }
	}
	return(true);
    }

    public void draw(GOut g) {
	if(screenshot&&Config.sshot_noui){visible = false;}
	super.draw(g);
	drawcmd(g, new Coord(20, 580));
	if(screenshot){
	    visible = true;
	    screenshot = false;
	    try {
		Coord s = MainFrame.getInnerSize();
		String stamp = Utils.sessdate(System.currentTimeMillis());
		String ext = Config.sshot_compress?".jpg":".png";
		File f = new File("screenshots/SS_"+stamp+ext);
		f.mkdirs();
		Screenshot.writeToFile(f, s.x, s.y);
	    } catch (GLException e){e.printStackTrace();}
	    catch (IOException e){e.printStackTrace();}
	}
	
//	if(!afk && (System.currentTimeMillis() - ui.lastevent > 300000)) {
//	    afk = true;
//	    Widget slen = findchild(SlenHud.class);
//	    if(slen != null)
//		slen.wdgmsg("afk");
//	} else if(afk && (System.currentTimeMillis() - ui.lastevent < 300000)) {
//	    afk = false;
//	}
    }
    
    public void error(String msg) {
    }
}
