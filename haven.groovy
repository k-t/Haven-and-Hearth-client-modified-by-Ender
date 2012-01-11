import java.awt.event.KeyEvent
import haven.scripting.Engine
import haven.Config
import static sys.*

boolean handleKeyEvent(KeyEvent ev) {
    int code = ev.getKeyCode()
    // Alt+F1
    if (code == KeyEvent.VK_F1 && ev.isAltDown()) {
        // directly accesses scripting engine object to run script
        // defined in the client configuration
        Engine.getInstance().run(Config.bot_name1)
        return true
    // Alt+F2
    } else if (code == KeyEvent.VK_F2 && ev.isAltDown()) {
        Engine.getInstance().run(Config.bot_name2)
    // Alt+F3, stop script execution
    } else if (code == KeyEvent.VK_F3 && ev.isAltDown()) {
        Engine.getInstance().stop()
        return true
    // Ctrl+F4
    } else if (code == KeyEvent.VK_F4 && ev.isControlDown()) {
    }
    return false
}

boolean handleConsoleCmd(String[] cmdline) {
    assert cmdline.size() > 0
    String name = cmdline[0]
    if (name == "set_bot1") {
        assert cmdline.size() > 1, "Not enough parameters for command $name"
        Config.bot_name1 = cmdline[1]
        Config.saveOptions()
        return true
    } else if (name == "set_bot2") {
        assert cmdline.size() > 1, "Not enough parameters for command $name"
        Config.bot_name2 = cmdline[1]
        Config.saveOptions()
        return true
    }
    return false
}