package kt;

import haven.Coord;
import haven.GOut;
import haven.RichText;
import haven.Text;
import haven.Textlog;
import haven.Widget;

import java.awt.Color;
import java.awt.font.TextAttribute;

class LogWidget extends Textlog implements LogManager.LogView {
    static RichText.Foundry fnd = new RichText.Foundry(TextAttribute.FAMILY, "Sans Serif", TextAttribute.SIZE, 10, TextAttribute.FOREGROUND, Color.WHITE);
    
    private boolean update = false;
    private LogManager.Log log = null;

    public LogWidget(Coord c, Coord sz, Widget parent) {
        super(c, sz, parent);
    }
    
    @Override
    public void append(String line, Color col) {
        Text rl;
        if(col == null)
            col = defcolor;
        line = RichText.Parser.quote(line);
        rl = fnd.render(line, sz.x - (margin * 2) - sflarp.sz().x);
        synchronized(lines) {
            lines.add(rl);
        }
        if(cury == maxy)
            cury += rl.sz().y;
        maxy += rl.sz().y;
    }
    
    public void removefirst() {
        synchronized(lines) {
            if (lines.size() == 0)
                return;
            Text rl = lines.get(0);
            lines.remove(0);
            maxy -= rl.sz().y;
            if (cury > maxy)
                cury = maxy;
        }
    }
    
    public void clear() {
        lines.clear();
        update();
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
            g.frect(new Coord(0, 0), sz.sub(margin + sflarp.sz().x, 0));
        }
    }
    
    public void setlog(LogManager.Log log) {
        if (this.log != null)
            this.log.setview(null);
        if (this.log == log)
            return;
        this.log = log;
        beginupdate();
        lines.clear();
        for (String line : this.log.lines())
            append(line);
        endupdate();
        this.log.setview(this);             
    }
    
    private void update() {
        Text[] oldlines = new Text[lines.size()];
        oldlines = lines.toArray(oldlines);
        synchronized(lines) {
            lines.clear();
            cury = 0;
            maxy = 0;
            for (Text line : oldlines) {
                Text rl = fnd.render(line.text, sz.x - (margin * 2) - sflarp.sz().x);
                lines.add(rl);
                if(cury == maxy)
                    cury += rl.sz().y;
                maxy += rl.sz().y;
            }
        }
    }
}
