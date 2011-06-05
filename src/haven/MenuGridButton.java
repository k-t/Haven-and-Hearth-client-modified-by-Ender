package haven;

import haven.Resource.Image;

import java.io.*;
import java.util.*;

public abstract class MenuGridButton implements Comparable<MenuGridButton> {
    protected final MenuGrid grid;
    protected final MenuGridButton parent;
    
    public MenuGridButton(MenuGridButton parent, MenuGrid grid) {
        this.parent = parent;
        this.grid = grid;
    }
    
    @Override
    public int compareTo(MenuGridButton o) {
        if (o == null)
            return 1;
        return String.CASE_INSENSITIVE_ORDER.compare(this.name(), o.name());
    }
    
    public MenuGridButton parent() {
        return parent;
    }
    
    public Resource.Pagina pagina() {
        return null;
    }
    
    public abstract String id();
    public abstract Tex tex();
    public abstract String name();
    public abstract char hk();
    public abstract boolean hasTooltip();
    public abstract MenuGridButton[] children();
    public abstract void use();
    
    protected static MenuGridButton scriptroot;
    public static MenuGridButton getScriptRootButton() {
        if (scriptroot == null)
            scriptroot = new ScriptRootButton();
        return scriptroot;
    }
    
    public static MenuGridButton fromResource(String resname) {
        return new ResourceButton(Resource.load(resname), null, null);
    }
    
    public static MenuGridButton fromResource(String resname, MenuGrid grid) {
        return new ResourceButton(Resource.load(resname), null, grid);
    }
    
    public static MenuGridButton fromResource(Resource res, MenuGrid grid) {
        return new ResourceButton(res, null, grid);
    }
    
    public static MenuGridButton fromString(String id, MenuGrid grid) {
        if (id.startsWith(ScriptRootButton.typeid))
            return getScriptRootButton();
        else if (id.startsWith(ScriptButton.typeid))
            // make name of the script by elimination of the type information
            return new ScriptButton(id.replace(ScriptButton.typeid, ""));
        else
            return fromResource(id, grid);
    }
    
    private abstract static class BasicButton extends MenuGridButton {
        private final Tex tex;
        private final String name;
        private final char hk;
        
        public BasicButton(MenuGridButton parent, MenuGrid grid, Tex tex, String name, char hk) {
            super(parent, grid);
            this.tex = tex;
            this.name = name;
            this.hk = hk;
        }

        @Override
        public Tex tex() { return tex; }

        @Override
        public String name() { return name; }

        @Override
        public char hk() { return hk; }

        @Override
        public boolean hasTooltip() { return true; }

        @Override
        public MenuGridButton[] children() { return new MenuGridButton[0]; }

        @Override
        public void use() { }
    }
    
    private static class ScriptRootButton extends BasicButton {
        public final static String typeid = "['ScriptRoot]";
        private static Tex tex = Resource.loadtex("gfx/hud/script");

        public ScriptRootButton() {
            super(null, null, tex, "Scripts", 'S');
        }
        
        public String id() {
            return typeid;
        }

        @Override
        public MenuGridButton[] children() {
            MenuGridButton[] ss = new MenuGridButton[0];
            File scriptdir = new File("./scripts/");
            File[] scripts  = scriptdir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".groovy");
                }
            });
            ArrayList<MenuGridButton> bs = new ArrayList<MenuGridButton>();
            for (File script : scripts) {
                String n = script.getName();
                // add file name without extension
                bs.add(new ScriptButton(n.substring(0, n.lastIndexOf("."))));
            }
            return bs.toArray(ss);
        }
    }
    
    private static class ScriptButton extends BasicButton {
        public final static String typeid = "['Script]";
        private static Tex tex = Resource.loadtex("gfx/hud/groovy");

        public ScriptButton(String scriptname) {
            super(null, null, tex, scriptname, (char)0);
        }
        
        public String id() {
            return typeid + name();
        }

        @Override
        public void use() {
            haven.scripting.Engine.getInstance().run(name());
        }
    }
    
    private static class ResourceButton extends MenuGridButton {
        private final Resource res;
        private Resource.AButton ad;
        private Resource.Image img;
        
        public ResourceButton(Resource res, MenuGridButton parent, MenuGrid grid) {
            super(parent, grid);
            this.res = res;
        }
        
        private Resource.AButton ad() {
            if (ad == null)
                ad = res.layer(Resource.action); 
            return ad;
        }
        
        private Resource.Image img() {
            if (img == null)
                img = res.layer(Resource.imgc);
            return img;
        }
        
        @Override
        public String id() {
            return res.name;
        }
        
        @Override
        public Tex tex() {
            Image img = img();
            return img != null ? img.tex() : null;
        }

        @Override
        public String name() {
            return (ad() != null) ? ad().name : res.name;
        }

        @Override
        public char hk() {
            return (ad() != null) ? ad().hk : 0;
        }

        @Override
        public boolean hasTooltip() {
            return ad() != null;
        }
        
        @Override
        public Resource.Pagina pagina() {
            return res.layer(Resource.pagina);
        }

        @Override
        public MenuGridButton[] children() {
            MenuGridButton[] bs = new MenuGridButton[0];
            if (grid == null)
                return bs;
            Collection<MenuGridButton> tobe = new HashSet<MenuGridButton>();
            for(Resource r : grid.resbuttons()) {
                if(r.layer(Resource.action).parent == this.res)
                tobe.add(new ResourceButton(r, this, grid));
            }
            return(tobe.toArray(bs));
        }

        @Override
        public void use() {
            if (grid == null || this.ad() == null)
                return;
            String [] ad = this.ad().ad;
            if(ad[0].equals("@")) {
                usecustom(ad);
                // return to root menu
                grid.gotoroot();
//          } else if (ad[0].equals("declaim")){
//          new DeclaimVerification(ui.root, ad);
            } else {
                int k = 0;
                if (ad[0].equals("crime")){k = -1;}
                if (ad[0].equals("tracking")){k = -2;}
                if (ad[0].equals("swim")){k = -3;}
                if(k<0){
                    synchronized (grid.ui.sess.glob.buffs) {
                    if(grid.ui.sess.glob.buffs.containsKey(k)){
                        grid.ui.sess.glob.buffs.remove(k);
                    } else {
                        Buff buff = new Buff(k, res.indir());
                        buff.major = true;
                        grid.ui.sess.glob.buffs.put(k, buff);
                    }
                    }
                }
                grid.wdgmsg("act", (Object[])ad);
            }
        }
        
        private void usecustom(String[] list) {
            if (grid == null)
                return;
            if(list[1].equals("radius")) {
                Config.showRadius = !Config.showRadius;
                String str = "Radius highlight is turned "+((Config.showRadius)?"ON":"OFF");
                grid.ui.cons.out.println(str);
                grid.ui.slen.error(str);
                Config.saveOptions();
            } else if(list[1].equals("hidden")) {
                Config.showHidden = !Config.showHidden;
                String str = "Hidden object highlight is turned "+((Config.showHidden)?"ON":"OFF");
                grid.ui.cons.out.println(str);
                grid.ui.slen.error(str);
                Config.saveOptions();
            } else if(list[1].equals("hide")) {
                for(int i=2;i<list.length;i++){
                String item = list[i];
                if(Config.hideObjectList.contains(item)){
                    Config.hideObjectList.remove(item);
                } else {
                    Config.hideObjectList.add(item);
                }
                }
            } else if(list[1].equals("simple plants")) {
                Config.simple_plants = !Config.simple_plants;
                String str = "Simplified plants is turned "+((Config.simple_plants)?"ON":"OFF");
                grid.ui.cons.out.println(str);
                grid.ui.slen.error(str);
                Config.saveOptions();
            } else if(list[1].equals("timers")) {
                TimerPanel.toggle();
            } else if(list[1].equals("animal")) {
                Config.showBeast = !Config.showBeast;
                String str = "Animal highlight is turned "+((Config.showBeast)?"ON":"OFF");
                grid.ui.cons.out.println(str);
                grid.ui.slen.error(str);
                Config.saveOptions();
            } else if(list[1].equals("globalchat")) {
                grid.ui.root.wdgmsg("gk", 3);
            } else if(list[1].equals("wiki")) {
                if(grid.ui.wiki == null) {
                new WikiBrowser(MainFrame.getCenterPoint().sub(115, 75), Coord.z, grid.ui.root);
                } else {
                    grid.ui.wiki.wdgmsg(grid.ui.wiki.cbtn, "click");
                }
            }
        }

        @Override
        public int compareTo(MenuGridButton o) {
            if (o instanceof ResourceButton) {
                ResourceButton a = (ResourceButton)o;
                if (this.ad() != null && a.ad() != null) {
                    Resource.AButton aa = this.ad();
                    Resource.AButton ab = a.ad();
                    if((aa.ad.length == 0) && (ab.ad.length > 0))
                        return(-1);
                    if((aa.ad.length > 0) && (ab.ad.length == 0))
                        return(1);
                }
            }
            return super.compareTo(o);
        }
    }

    private static class ItemButton extends MenuGridButton {
        private int slot;
        private Resource res;
        
        public ItemButton(MenuGridButton parent, MenuGrid grid, int slot) {
            super(parent, grid);
            this.slot = slot;
        }

        @Override
        public String id() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Tex tex() {
            if (res == null) {
                Indir<Resource> indir = null; // getbelt(slot);
                if (indir == null) {
                    res = null;
                } else {
                    res = indir.get();
                }
            }
            if (res != null)
                return res.layer(Resource.imgc).tex();
            else 
                return null;
        }

        @Override
        public String name() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public char hk() { return 0; }

        @Override
        public boolean hasTooltip() { return true; }

        @Override
        public MenuGridButton[] children() { return new MenuGridButton[0]; }

        @Override
        public void use() {
            if (slot >= 0) {
                UI.instance.slen.wdgmsg("belt", slot, 1, UI.instance.modflags());
            }
        }
    }
}

