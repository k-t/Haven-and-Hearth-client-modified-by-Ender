import static sys.*
import static drunkard.*

chip()

void chip() {
    while (getHunger() > 63) {
        if (getStamina() > 31) {
            int rock = findObjectByName("bumlings/02", 5)
            if (rock == 0)
                return
            doClick(rock, MouseButton.RIGHT, 0)
            def menu = waitContextMenu()
            menu.select("Chip stone")
            waitHourglass()
            dropStone()
        } else {
            drinkWater(true)
            // if stamina not refilled
            if (getStamina() <= 31)
                return
        }
    }
}

void dropStone() {
    def di = getDragItem()
    if (di != null && isStone(di))
        drop()
    for (item in waitInventory().getItems()) {
        if (isStone(item)) {
            item.take()
            waitDrag()
            drop()
        }
    }
}

boolean isStone(item) { item.isName("stone") }