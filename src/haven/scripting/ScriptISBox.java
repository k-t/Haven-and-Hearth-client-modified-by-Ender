package haven.scripting;

import haven.*;
import haven.Resource.Tooltip;

public class ScriptISBox extends ScriptWidget {
    private final ISBox isbox;
    
    public ScriptISBox(ISBox isbox) {
        super(isbox);
        this.isbox = isbox;
    }
    
    public String getName() {
        return isbox.res != null ? isbox.res.name : "";
    }
    
    public String getLabel() {
        return isbox.label != null ? isbox.label.text : "";
    }
    
    public int getRemaining() {
        return isbox.getremaining();
    }
    
    public int getAvailable() {
        return isbox.getavailable();
    }
    
    public int getBuilt() {
        return isbox.getbuilt();
    }
    
    public String getTooltip() {
        if (isbox.res == null)
            return "";
        Tooltip t = isbox.res.layer(Resource.tooltip);
        return t != null ? t.t : "";
    }
    
    public void click() {
        isbox.wdgmsg("click");
    }
    
    public void drop() {
        isbox.drop(Coord.z, Coord.z);
    }
    
    public void transferFrom() {
        isbox.wdgmsg("xfer");
    }
    
    public void transferFrom(int mod) {
        isbox.wdgmsg("xfer2", -1, mod);
    }
    
    public void transferTo(int mod) {
        isbox.wdgmsg("xfer2", 1, mod);
    }
}
