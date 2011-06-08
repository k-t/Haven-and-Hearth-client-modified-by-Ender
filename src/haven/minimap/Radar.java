package haven.minimap;

import java.awt.Color;
import java.util.*;

import haven.*;

public class Radar {
    private final RadarConfig config;
    private final Map<Integer, Marker> markers = Collections.synchronizedMap(new HashMap<Integer, Marker>());
    private final Map<Integer, Gob> undefined = Collections.synchronizedMap(new HashMap<Integer, Gob>());
    
    public Radar(RadarConfig config) {
        this.config = config;
    }
    
    public void add(Gob g) {
        if (this.contains(g)) return;
        String rn = g.resname();
        if (rn != null && rn.equals("")) {
            // resource isn't loaded yet? 
            undefined.put(g.id, g);
        } else {
            add(rn, g);
        }
    }
    
    private boolean add(String rn, Gob gob) {
        if (rn == null) return false;
        MarkerClass m = config.getmarker(rn);
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
                String rn = gob.resname();
                if (rn != null && !rn.equals("")) {
                    add(rn, gob);
                    undefined.remove(gob.id);
                }
            }
        }
    }
    
    private boolean contains(Gob g) {
        return undefined.containsKey(g.id) || markers.containsKey(g.id);
    }
    
    public Iterable<Marker> getmarkers() {
        checkundefined();
        return markers.values();
    }
    
    public synchronized void remove(int gobid) {
        markers.remove(gobid);
        undefined.remove(gobid);
    }
    
    public void setcolor(int gobid, Color c) {
        Marker m = markers.get(gobid);
        if (m != null)
            m.color = c;
    }
}
