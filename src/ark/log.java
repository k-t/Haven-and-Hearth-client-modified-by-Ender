package ark;

import java.io.*;
import haven.*;
import haven.LogManager;

public class log {
    private static ILog console = LogManager.getlog("Console");
    private static ILog messages = LogManager.getlog("Messages");
    private static ILog output = LogManager.getlog("Output");
    
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
    	messages.write(msg);
    }
    
    private static void ConsolePrint(String msg) {
    	console.write(msg.trim());
    }
    
    public static void OutputPrint(String msg) {
    	output.write(msg);
    }

    static void LogWarning(String Msg) {
        LogPrint("WARNING: "+Msg);
    }

    static void LogError(String Msg) {
        LogPrint("ERROR: "+Msg);
    }
}
