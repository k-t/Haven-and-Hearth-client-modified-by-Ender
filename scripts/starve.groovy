import static sys.*
import static drunkard.*

starve()

void starve() {
    while (getHunger() > 60) {
        if (getStamina() > 35) {
	 sendAction("plow")
	 waitCursor("dig")
         mapClick(0, 0, 1, 0)
         waitHourglass()
        } else {
            drinkWater(true)
            // if stamina not refilled
            if (getStamina() <= 35)
                return
        }
    }
}