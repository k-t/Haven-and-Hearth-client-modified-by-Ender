import static sys.*
import static drunkard.*

bouldersmash()

void bouldersmash() {
    while (getHunger() > 63) {
        if (getStamina() > 31) {
            int rock = findObjectByName("caveinstone", 50)
            if (rock == 0)
                return
            sendAction("destroy")
            waitCursor("mine")
            doClick(findObjectByName("caveinstone", 50),1,0);
            waitHourglass()
        } else {
            drinkWater(true)
            // if stamina not refilled
            if (getStamina() <= 31)
                return
        }
    }
}