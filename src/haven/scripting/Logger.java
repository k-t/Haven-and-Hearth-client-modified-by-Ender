package haven.scripting;

public class Logger {
    public void message(String text) {
        ark.log.LogPrint(text);
    }
    
    public void warning(String text) {
        ark.log.LogPrint(text);
    }
}
