/* Picks best seeds to the player inventory from seedbags inside the chest. */

import static sys.*

def cont = getInventory("Chest")
if (cont == null) {
    println "No chest :\\"
    return
}

// open all seedbags
openAll(cont)
// collect seeds info across all opened bags
def seeds = getSeeds(cont)
if (seeds.size() == 0) {
    println "No seeds :\\"
    return
}
// sort
seeds.sort { a, b -> b.q - a.q }
// pick seeds in order
int i = 0
def inv = waitInventory()
for (x in 0..<inv.rowCount) {
    for (y in 0..<inv.columnCount) {
        def s = seeds.getAt(i)
        def seed = s.bag.getItemAt(s.sx, s.sy);
        if (seed != null) {
            seed.take()
            waitDrag()
            inv.drop(x, y)
            waitDrop()
        }
        i++
        if (i >= seeds.size())
            return
    }
}

class SeedInfo { def q; def bag; def sx; def sy }

/** Opens all seedbags in the given container. */
void openAll(cont) {
    for (item in cont.items)
        if (item.isName("bag-seed")) {
            item.act()
            Thread.sleep 100 // wait a bit
        }
    Thread.sleep 500
}

def getSeeds(cont) {
    def seeds = []
    for (seedbag in findWindows("Seedbag")) {
        def inv = seedbag.inventory
        for (seed in inv.items) {
            seeds.add(new SeedInfo(
                q:seed.quality,
                bag:inv,
                sx:seed.row,
                sy:seed.column))
        }
    }
    return seeds
}