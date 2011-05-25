package haven.scripting

import haven.FlowerMenu;

public class ScriptFlowerMenu {
    private final FlowerMenu menu;
    
    public ScriptFlowerMenu(FlowerMenu menu) {
        this.menu = menu;
    }
    
    public String[] getOptions() {
        return menu.getOptions();
    }
    
    public void select(String optname) {
        menu.selectOpt(optname);
    }
}
