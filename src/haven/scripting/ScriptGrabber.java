package haven.scripting;

import java.awt.event.KeyEvent;

import haven.*;

public abstract class ScriptGrabber extends Window implements MapView.Grabber {
    private final Label text;
    private final Widget btn;
    private boolean destroyed;
    
    public ScriptGrabber(String title, String label, Coord c, Widget parent) {
        super(c, new Coord(150, 50), parent, title);
        this.text = new Label(Coord.z, this, label);
        this.btn = new Button(this.asz.add(-50, -30), Integer.valueOf(40), this, "Done");
        this.ui.mainview.enol(new int[] { 0, 1, 16 });
        this.ui.mainview.grab(this);
        this.destroyed = false;
        this.justclose = true;
    }
    
    public final void close() {
        this.cbtn.click();
    }
    
    public abstract void done();
    
    @Override
    public void destroy() {
        this.ui.mainview.disol(new int[] { 0, 1, 16 });
        this.ui.mainview.release(this);
        super.destroy();
        this.destroyed = true;
    }
    
    public final boolean destroyed() {
        return destroyed;
    }
    
    public boolean type(char key, java.awt.event.KeyEvent ev) {
        if(key == KeyEvent.VK_ENTER) {
            done();
            close();
            return true;
        }
        return super.type(key, ev);
    }
    
    @Override
    public void wdgmsg(Widget wdg, String msg, Object... args) {
        if (wdg == this.btn) {
            done();
            close();
        } else {
            super.wdgmsg(wdg, msg, args);
        }
    }
}
