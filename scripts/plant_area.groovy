import static sys.*
import util.AreaWalker

// select harvest area
def area = selectArea("Select area to plant.")
if (area == null)
    return;
// sort seeds
println "Gathering seeds..."
def inv = waitInventory()
openAll(inv)
def seeds = getBagsSeeds() + getSeeds(inv)
if (seeds.size() == 0) {
    println "No seeds to plant."
    return
}
println "Sorting seeds..."
seeds.sort { a, b -> b.q - a.q }
println "Best seed q: ${seeds.first().q}"
// plant
int i = 0
def walker = new AreaWalker(area)
for (tile in walker.eachTile()) {
    def p = getMyCoord()
    if (getTileType(tile) != TileType.PLOWED) 
        continue; // don't plow and move to next tile
    if (findMapObject("", 1, tile.tileX - p.tileX, tile.tileY - p.tileY) > 0)
        continue; // something already planted
    def s = seeds.getAt(i)
    if (s == null) return // no more seeds
    def seed = s.bag.getItemAt(s.sx, s.sy);
    if (seed == null) return
    seed.take()
    waitDrag()
    mapAbsInteractClick(tile.x, tile.y, 0)
    waitDrop()
    i++
}

class SeedInfo { def q; def bag; def sx; def sy }

/** Returns all seeds from seedbags. */
def getBagsSeeds() {
    def seeds = []
    for (seedbag in findWindows("Seedbag"))
        seeds += getSeeds(seedbag.inventory)
    return seeds
}

/** Returns all seeds from the iventory. */
def getSeeds(cont) {
    def seeds = []
    for (seed in cont.items) {
        if (!isSeed(seed))
            continue;
        seeds.add(new SeedInfo(
            q:seed.quality,
            bag:cont,
            sx:seed.row,
            sy:seed.column))
    }
    return seeds
}

boolean isSeed(item) {
    return !item.isName("bag-seed")
}

/** Opens all seedbags in the given container. */
void openAll(inv) {
    for (item in inv.items)
        if (item.isName("bag-seed-f")) {
            item.act()
            Thread.sleep 100 // wait a bit
        }
    Thread.sleep 500
}