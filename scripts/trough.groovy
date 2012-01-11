import static sys.*

// search for the nearest trough
def trough = findObjectByNames(50, "trough", "coop")

if (trough == null) {
    println "No trough and no chicken coop!"
    return
}
// close all seedbags
findWindows("Seedbag").each { it.close() }
// wait until all seedbags will be closed
wait({ getInventory("Seedbag") != null })
// search for first food item in the inventory (including seedbags)
def foodItem = findFood()
if (foodItem == null) {
    println "No food :("
    return
}
// take it
foodItem.take()
waitDrag()
// drop all with shift
while (true) {
    mapInteractClick(trough.id, 1)
    // wait for next food item
    Thread.sleep 200
    // if there is no more food
    if (getDragItem() == null)
        return
}

/* Checks whether the specified item is food. */
boolean isFood(item) {
    item.isName("beetroot") ||  // beetroot and beetroot leaves
    item.isName("carrot") || // carrot
    item.isName("flaxseed") ||  // flax
    item.isName("seed-hemp") || // hemp
    item.isName("seed-grape") || // grape
    item.isName("seed-tobacco") || // tobacco
    item.isName("straw") ||
    item.isName("pumpkin")
}

/* Returns the first food item in the player inventory (including seedbags) */
def findFood() {
    def inv = waitInventory()
    for (item in inv.getItems()) {
        if (isFood(item))
            return item
        else if (item.isName("bag-seed-f")) {
            item.act()
            for (seed in waitInventory("Seedbag").items) {
                if (isFood(seed)) {
                    //closeBag()
                    return seed
                }
            }
            closeBag()
        }
    }
}

void closeBag() {
    findWindow("Seedbag")?.close()
    while (getInventory("Seedbag") != null) Thread.sleep 200
}

def waitInventory(String name) {
    def inv = getInventory(name)
    while (inv == null) {
        inv = getInventory(name)
        Thread.sleep 100
    }
    return inv
}