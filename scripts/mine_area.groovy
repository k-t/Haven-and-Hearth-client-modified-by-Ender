import static sys.*
import static drunkard.*
import util.Walker

def area = selectArea("Select area to mine.")
if (area == null)
    return;
def walker = new Walker()
// we will mine next tile and advance only then
walker.setAdvanceAction(this.&mine)
walker.walk(area)

boolean mine(Position nexttile) {
    while (getTileType(nexttile) == TileType.VOID) { // mine until we done
        if (getStamina() < 30) {
            boolean refilled = refillStamina()
            // no more water apparently
            if (!refilled) return false
        }
        if (getCursor() != "mine") {
            sendAction("mine")
            waitCursor("mine")
        }
        mapAbsClick(nexttile.x, nexttile.y, 1, 0)
        waitHourglass()
        if (hasDragItem()) drop()
    }
    // phew, all clear, we can continue mining
    resetCursor()
    return true
}

boolean refillStamina() {
    resetCursor()
    mapAbsClick(getMyCoordX(), getMyCoordY(), 1, 0);
    return drinkWater(true)
}