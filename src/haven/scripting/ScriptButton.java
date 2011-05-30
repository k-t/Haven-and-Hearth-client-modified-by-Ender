package haven.scripting;

import haven.*;

public class ScriptButton extends ScriptWidget {
    private final Button button;
    
    public ScriptButton(Button button) {
        super(button);       
        this.button = button;
    }
    
    public void click() {
        button.click();
    }
    
    public String getCaption() {
        return (button.text != null) ? button.text.text : "";
    }
}
