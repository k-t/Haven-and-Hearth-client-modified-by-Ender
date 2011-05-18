package haven;

import java.io.*;
import java.util.*;

public class LogManager {
	private static final int LogSize = 100;
	private static LogManager instance;
	
	static {
	    // redirect output to our logger
	    OutputStream out = new OutputStream() {  
	        @Override  
            public void write(final int b) throws IOException {  
                String s = String.valueOf((char) b);
                System.console().printf("%s", s);
                getlog("Console").write(s.trim());
            }  
      
            @Override  
            public void write(byte[] b, int off, int len) throws IOException {  
                String s = new String(b, off, len);
                System.console().printf("%s", s);
                getlog("Console").write(s.trim());
            }  
      
            @Override  
            public void write(byte[] b) throws IOException {  
                write(b, 0, b.length);  
            }  
      };
      System.setOut(new PrintStream(out, true));  
      System.setErr(new PrintStream(out, true));
	}
	
	private static LogManager getInstance() {
		if (instance == null)
			instance = new LogManager();
		return instance;
	}
	
	private final HashMap<String, Log> logs = new HashMap<String, Log>();
	private final List<CustomLogWindow> views = new ArrayList<CustomLogWindow>();
	
	public static void addwindow(CustomLogWindow wnd) {
		LogManager instance = getInstance();
		instance.views.add(wnd);
		for (String logname : instance.logs.keySet()) {
			wnd.addlog(logname, instance.logs.get(logname));
		}
	}
	
	public static ILog getlog(String name) {
		LogManager instance = getInstance();
		synchronized (instance.logs) {
			Log log = instance.logs.get(name);
			if (log != null)
				return log;
			else {
				log = new Log();
				instance.logs.put(name, log);
				// add log to views
				for (CustomLogWindow view : instance.views) {
					view.addlog(name, log);
				}
				return log;
			}
		}
	}
	
	public static interface LogView {
		void append(String message);
		void clear();
		void removefirst();
	}
	
	public static class Log implements ILog {
		private final LinkedList<String> lines = new LinkedList<String>();
		private LogView view = null;
		
		public void clear() {
			lines.clear();
			if (view != null)
				view.clear();
		}
		
		public List<String> lines() {
			return lines;
		}
		
		public void setview(LogView view) {
			this.view = view;
		}
		
		@Override
        public void write(String message) {
			synchronized (lines) {
				lines.add(message);
				if (view != null)
					view.append(message);
				if (lines.size() > LogSize) {
					// delete first entry
					lines.removeFirst();
					if (view != null)
						view.removefirst();
				}
			}
        }
	}
}
