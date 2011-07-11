package kt;

import java.util.*;

import haven.*;

public class PropertyGrid extends ScrollableWidget {
    private final HashMap<String, Text> props;
    private final HashMap<Text, Text> values;

    public PropertyGrid(Coord c, Coord sz, Widget parent) {
        super(c, sz, parent);
        props = new HashMap<String, Text>();
        values = new HashMap<Text, Text>();
        
        setproperty("Cursor", haven.scripting.Engine.getInstance().getCursor());
        setproperty("Player", UI.instance.mainview.getplayergob());
        setproperty("1", 1);
        setproperty("2dadjaskjdklasjdlkajsdkljaslkdjaskljdl", 2);
    }
    
    public Coord getsize() {
        return sz;
    }
    
    public void setsize(Coord value) {
        sz = value;
    }

    @Override
    public void draw(GOut g) {
        int y = 0;
        for (Text prop : props.values()) {
            g.image(prop.tex(), new Coord(0, y));
            Text value = values.get(prop);
            if (value != null) {
                g.image(value.tex(), new Coord(prop.sz().x + 5, y));
            }
            y += prop.sz().y + 2;
        }
        super.draw(g);
    }
    
    private Text addproperty(String prop) {
        Text t = Text.render(prop);
        props.put(prop, t);
        return t;
    }
    
    public void setproperty(String prop, Object value) {
        Text ptext = props.get(prop);
        if (ptext == null)
            ptext = addproperty(prop);
        values.put(ptext, Text.render(value != null ? value.toString() : "<<NULL>>"));
    }
}
