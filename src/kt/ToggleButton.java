package kt;

import haven.Button;
import haven.Coord;
import haven.Widget;

import java.awt.image.BufferedImage;

public class ToggleButton extends Button {

	public ToggleButton(Coord c, Integer w, Widget parent, String text) {
	    super(c, w, parent, text);
    }
	
	public ToggleButton(Coord c, Integer w, Widget parent, BufferedImage cont) {
	    super(c, w, parent, cont);
    }
	
	@Override
    public boolean mousedown(Coord c, int button) {
    	if(button != 1)
    	    return(false);
    	return(true);
    }
	
	@Override
    public boolean mouseup(Coord c, int button) {
    	click();
		return(false);
    }
	
	public boolean istoggled() {
		return a;
	}
	
	public void settoggled(boolean value) {
		a = value;
	}
	
	public void toggle() {
		a = !a;
	}
}
