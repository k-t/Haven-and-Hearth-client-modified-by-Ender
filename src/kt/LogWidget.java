package kt;

import haven.*;
import haven.RichText.Foundry;

import java.awt.Color;
import java.awt.font.TextAttribute;

class LogWidget extends Textlog implements LogManager.LogView {
    private boolean update = false;
    private LogManager.Log log = null;

    public LogWidget(Coord c, Coord sz, Widget parent) {
        super(c, sz, parent);
        this.textsize = 10;
    }
    
    public void beginupdate() {
        update = true;
    }
    
    public void endupdate() {
        update();
        update = false;
    }
    
    @Override
    public void draw(GOut g) {
        if (!update)
            super.draw(g);
        else {
            g.chcolor(new Color(0, 0, 0, 120));
            g.frect(new Coord(0, 0), sz.sub(margin + scrollmargin(), 0));
        }
    }
    
    public void setlog(LogManager.Log log) {
        if (this.log != null)
            this.log.setview(null);
        if (this.log == log)
            return;
        this.log = log;
        beginupdate();
        clear();
        for (String line : this.log.lines())
            append(line);
        endupdate();
        this.log.setview(this);             
    }
}
