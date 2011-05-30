package haven.scripting;

import haven.Makewindow;

public class ScriptCraftWindow extends ScriptWidget {
    private final Makewindow makewnd;

    public ScriptCraftWindow(Makewindow wnd) {
        super(wnd);
        this.makewnd = wnd;
    }
    
    public String getCraftName() {
        return makewnd.getCraftName();
    }
    
    public void craft() {
        makewnd.wdgmsg("make", 0);
    }
    
    public void craftAll() {
        makewnd.wdgmsg("make", 1);
    }
}
