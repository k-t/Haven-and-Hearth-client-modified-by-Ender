package ark;
// start at 11.07.2010
import java.awt.*;
import java.io.*;
import java.util.Vector;

import haven.*;

public class log {
    public static boolean Drawable = false;
    static Vector<String> Messages = new Vector<String>();
    static int MaxMessages = 20;
    static Coord LogScreenPos = new Coord(30, 300);
    static int MeesageHeight = 14;
    static int LogScreenWidth = 650;

    public static void Draw(GOut g) {
        if (!Drawable) return;

        g.chcolor(new Color(0,0,0,100));
        g.frect(LogScreenPos, new Coord(LogScreenWidth, MeesageHeight*MaxMessages+MeesageHeight));
        g.chcolor(Color.WHITE);
        String s;
        int y = LogScreenPos.y + MeesageHeight;
        for (int i = 0; i < Messages.size(); i++){
            s = (String)Messages.elementAt(i);
            g.atext(s, new Coord(LogScreenPos.x, y), 0, 1);
            y += MeesageHeight;
        }
    }

    public static void LogPrint(String Msg) {
        while (Messages.size() > MaxMessages-1)
            Messages.remove(0);
        Messages.addElement(Msg);
//        try {
//            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("log.txt", true), "Windows-1251"));
//            pw.append(Msg);
//            pw.append("\r\n");
//            pw.flush();
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }

    static void LogWarning(String Msg) {
        LogPrint("WARNING: "+Msg);
    }

    static void LogError(String Msg) {
        LogPrint("ERROR: "+Msg);
    }

    public static void Show() {
        Drawable = true;
    }

    public static void Hide() {
        Drawable = false;
    }
}
