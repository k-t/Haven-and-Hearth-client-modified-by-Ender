package haven.scripting;

import haven.UI;

public class ScriptCraft {
    private final Engine engine;
    
    public ScriptCraft(Engine engine) {
        this.engine = engine;
    }
    
    public void craft(boolean all) {
        if (isReady()) {
            UI.instance.wdgmsg(UI.make_window, "make", all ? 1 : 0);
        }
    }
    
    public String getCraftName() {
        return isReady() ? UI.make_window.getCraftName() : "";
    }
    
    public boolean isReady() {
        if (UI.make_window != null)
            return UI.make_window.isReady();
        return false;
    }
    
    public void wait(String windowname) {
        while (true) {
            if (isReady() && getCraftName().equals(windowname))
                return;
            engine.wait(300);
        }
    }
}
