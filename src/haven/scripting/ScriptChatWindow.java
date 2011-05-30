package haven.scripting;

import haven.ChatHW;

public class ScriptChatWindow extends ScriptWidget {
    private final ChatHW chatwnd;

    public ScriptChatWindow(ChatHW chatwnd) {
        super(chatwnd);
        this.chatwnd = chatwnd;
    }
    
    public void say(String message) {
        chatwnd.wdgmsg("msg", message);
    }
}
