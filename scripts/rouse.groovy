/* Auto rousing */

import static sys.*
import groovy.time.*

def roused = [:]

while (true) {
    for (gob in getGobs()) {
        String speaktext = gob.getSpeakingText()
        if (speaktext != null && speaktext.equals(":rouse")) {
            Date now = new Date()
            def ptime = roused[gob.id]
            if (ptime == null ||                
                // old message show time expired for sure
                TimeCategory.minus(now, ptime).seconds > 10) {
                // memorize roused character and time
                roused[gob.id] = now
                rouse(gob)
            }
        }
    }
    Thread.sleep 1000
}

void rouse(gob) {
    sendAction("gov", "rouse")
    waitCursor(Cursor.Scroll)
    doClick(gob.id, MouseButton.LEFT, 0)
}