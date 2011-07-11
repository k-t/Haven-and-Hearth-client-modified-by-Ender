package haven.minimap;

import java.awt.Color;
import java.util.*;

import haven.*;

public class Radar {
    private final RadarConfig config;
    private final Map<Integer, Marker> markers = Collections.synchronizedMap(new HashMap<Integer, Marker>());
    private final Map<Integer, Gob> undefined = Collections.synchronizedMap(new HashMap<Integer, Gob>());
    private final Map<Integer, Color> colors = Collections.synchronizedMap(new HashMap<Integer, Color>());
    
    public Radar(RadarConfig config) {
        this.config = config;
    }
    
    public void add(Gob g) {
        if (this.contains(g)) return;
        String[] names = g.resnames();
        if (names.length == 0) {
            // resource isn't loaded yet? 
            undefined.put(g.id, g);
        } else {
            add(names, g);
        }
    }
    
    private boolean add(String[] names, Gob gob) {
        if (names.length == 0) return false;
        MarkerClass m = config.getmarker(gob);
        if (m != null) {
            markers.put(gob.id, new Marker(gob, m));
            return true;
        }
        return false;
    }
    
    private void checkundefined() {
        synchronized (undefined) {
            if (undefined.size() == 0)
                return;
            Gob[] gs = undefined.values().toArray(new Gob[undefined.size()]);
            for (Gob gob : gs) {
                String[] names = gob.resnames();
                if (names.length > 0) {
                    add(names, gob);
                    undefined.remove(gob.id);
                }
            }
        }
    }
    
    private boolean contains(Gob g) {
        return undefined.containsKey(g.id) || markers.containsKey(g.id);
    }
    
    public Marker[] getmarkers() {
        checkundefined();
        synchronized (markers) {
            return markers.values().toArray(new Marker[markers.size()]);
        }
    }
    
    public synchronized void remove(int gobid) {
        markers.remove(gobid);
        undefined.remove(gobid);
    }
    
    public Color getcolor(Marker m) {
        Color c = colors.get(m.gobid());
        if (c != null)
            return c;
        else {
            // use red marker for unknown players
            return m.isplayer() ? Color.RED : Color.WHITE;
        }
    }
    
    public void setcolor(int gobid, Color c) {
        colors.put(gobid, c);
    }
}
