package haven.scripting;

import haven.*;

public class ScriptCharWindow extends ScriptWidget {
    private final CharWnd wnd;
    
    public ScriptCharWindow(CharWnd wnd) {
        super(wnd);
        this.wnd = wnd;
    }
    
    public ScriptInventory getStudyInventory() {
        Inventory inv = Utils.getWidgetInventory(wnd.study);
        return inv != null ? new ScriptInventory(inv) : null;
    }
    
    public int getAttention() {
        return UI.instance.sess.glob.cattr.get("intel").getcomp();
    }
    
    public int getUsedAttention() {
        String att = wnd.snlbl.gettext(); 
        try {
            return att != null ? Integer.parseInt(att) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
