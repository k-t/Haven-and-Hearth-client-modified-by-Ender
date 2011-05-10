package ark;

import java.io.*;
import haven.*;

public class log {
    public static boolean Drawable = false;
    
    static {
    	OutputStream out = new OutputStream() {  
	    	@Override  
		    public void write(final int b) throws IOException {  
	    		String s = String.valueOf((char) b);
	    		System.console().printf("%s", s);
	    		ConsolePrint(s);
		    }  
	  
		    @Override  
		    public void write(byte[] b, int off, int len) throws IOException {  
		    	String s = new String(b, off, len);
		    	System.console().printf("%s", s);
		    	ConsolePrint(s);
		    }  
	  
		    @Override  
		    public void write(byte[] b) throws IOException {  
		      write(b, 0, b.length);  
		    }  
	  };
	  System.setOut(new PrintStream(out, true));  
	  System.setErr(new PrintStream(out, true));
    }

    public static void LogPrint(String msg) {
    	if (UI.instance.root.logwindow != null)
    		UI.instance.root.logwindow.write("Messages", msg);
    }
    
    private static void ConsolePrint(String msg) {
    	if (UI.instance.root.logwindow != null) {
    		UI.instance.root.logwindow.write("Console", msg.trim());
    	}
    }
    
    public static void OutputPrint(String msg) {
    	if (UI.instance.root.logwindow != null)
    		UI.instance.root.logwindow.write("Output", msg);
    }

    static void LogWarning(String Msg) {
        LogPrint("WARNING: "+Msg);
    }

    static void LogError(String Msg) {
        LogPrint("ERROR: "+Msg);
    }
}
