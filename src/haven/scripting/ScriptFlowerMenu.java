package haven.scripting;

import haven.FlowerMenu;

public class ScriptFlowerMenu {
    private final FlowerMenu menu;
    
    public ScriptFlowerMenu(FlowerMenu menu) {
        this.menu = menu;
    }
    
    public Object[] getOptions() {
        return menu.getOptions();
    }
    
    public void selectOpt(String optname) {
        menu.selectOpt(optname);
    }
}
