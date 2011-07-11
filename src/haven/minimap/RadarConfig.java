package haven.minimap;

import haven.*;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;

public class RadarConfig {
    private final File file;
    private final Set<String> unknowncache = new HashSet<String>();
    private final Map<String, MarkerClass> markercache = new HashMap<String, MarkerClass>();
    private final Map<String, MarkerConfig> infomap = new HashMap<String, MarkerConfig>();
    private final List<MarkerConfig> infolist = new ArrayList<MarkerConfig>();
    
    public RadarConfig(String configfile) {
        this(new File(configfile));
    }
    
    public RadarConfig(File configfile) {
        this.file = configfile;
        try { load(); } catch (Exception e) { System.out.println(e.getMessage()); }
    }
    
    public synchronized MarkerClass getmarker(Gob gob) {
        for (String name : gob.resnames()) {
            MarkerClass mc = getmarker(name);
            // return first matched marker
            if (mc != null)
                return mc;
        }
        return null;                        
    }
    
    private synchronized MarkerClass getmarker(String resname) {
        if (unknowncache.contains(resname)) return null;
        // try to get already created marker
        MarkerClass m = markercache.get(resname);
        if (m != null) return m;
        // lookup for marker info for the resource 
        MarkerConfig minfo = infomap.get(resname);
        if (minfo == null) {
            // try to match resource name with known patterns
            for (MarkerConfig mi : infolist)
                if (resname.matches(mi.pattern)) {
                    minfo = mi;
                    break;
                }
        }
        if (minfo != null) {
            // create and return the new marker
            m = newmarker(resname, minfo);
            markercache.put(resname, m);
            return m;
        }
        unknowncache.add(resname);
        return null;
    }
    
    private MarkerClass newmarker(String resname, MarkerConfig mi) {
        Tex tex = mi.isgenerated() ? Utils.generatemarkertex(resname) : Resource.loadtex(mi.res);
        return new MarkerClass(tex, mi.show, resname.startsWith("gfx/borka/body"));
    }
    
    /** Loads marker configuration. */
    private void load() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        NodeList groups = doc.getElementsByTagName("group");
        for (int i = 0; i < groups.getLength(); i++)
            parsegroup(groups.item(i));
    }
    
    private void parsegroup(Node node) {
        if (node.getNodeType() != Node.ELEMENT_NODE)
            return;
        Element el = (Element)node;
        String name = el.getAttribute("name");
        boolean show = !el.getAttribute("show").equals("false"); // show is true by default
        Group group = new Group(name, show);
        NodeList markers = el.getElementsByTagName("marker");
        for (int i = 0; i < markers.getLength(); i++)
            parsemarker(group, markers.item(i));
    }

    private void parsemarker(Group group, Node node) {
        if (node.getNodeType() != Node.ELEMENT_NODE)
            return;
        Element el = (Element)node;
        String res = el.getAttribute("res");
        boolean show = el.hasAttribute("show") ? el.getAttribute("show").equals("true") : group.show;
        String pattern;
        if (el.hasAttribute("match")) {
            pattern = el.getAttribute("match");
            infomap.put(pattern, new MarkerConfig(pattern, show, res));
        } else if (el.hasAttribute("pattern")) {
            pattern = el.getAttribute("pattern");
            infolist.add(new MarkerConfig(pattern, show, res));
        }
    }
    
    private class Group {
        public final String name;
        public final boolean show;
        
        public Group(String name, boolean show) {
            this.name = name;
            this.show = show;
        }
    }

    private class MarkerConfig {
        public final String pattern;
        public final boolean show;
        public final String res;
        
        public MarkerConfig(String pattern, boolean show, String res) {
            this.pattern = pattern;
            this.show = show;
            this.res = res;
        }
        
        public boolean isgenerated() {
            return (res == null || res.equals(""));
        }
    }
}
