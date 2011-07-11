package haven.scripting;

import haven.*;

public final class GobGrabber extends ScriptGrabber {
    private static final String title = "Object selection";
    private static final String defaulttext = "Select object:";
    private Gob gob;
    private boolean confirmed = false;
    
    public GobGrabber(String text, Coord c, Widget parent) {
        super(title, (text != null && text.length() > 0) ? text : defaulttext, c, parent);
    }
    
    public Gob selectedobject() {
        return confirmed ? gob : null;
    }
    
    @Override    
    public void destroy() {
        if (this.gob != null)
            this.ui.mainview.unhighlight(gob);
        super.destroy();
    }
    
    @Override
    public void done() {
        confirmed = true;
    }
    
    @Override
    public void mmousedown(Coord mc, int button) {
        if (gob != null)
            this.ui.mainview.unhighlight(gob);
        gob = this.ui.mainview.onmouse;
        if (gob != null)
            this.ui.mainview.highlight(gob);
    }

    @Override
    public void mmouseup(Coord mc, int button) {
    }

    @Override
    public void mmousemove(Coord mc) {
    }
}
