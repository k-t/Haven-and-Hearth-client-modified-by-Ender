class MouseButton {
    final static int LEFT = 1
    final static int RIGHT = 3
}

class TileType {
    final static int DEEP_WATER = 0
    final static int SHALLOW_WATER = 1
    
    final static int BRICK_RED = 3
    final static int BRICK_YELLOW = 4
    final static int BRICK_BLACK = 5
    final static int BRICK_BLUE = 6
    final static int BRICK_WHITE = 7
    
    final static int STONE = 8
    final static int PLOWED = 9
    
    final static int FOREST_CONIFEROUS = 10
    final static int FOREST_BROADLEAF = 11
    
    final static int THICKET = 12
    final static int GRASS = 13
    final static int HEATH = 14
    final static int MOOR = 15
    
    final static int SWAMP_1 = 16
    final static int SWAMP_2 = 17
    final static int SWAMP_3 = 18
    
    final static int MUD = 19
    final static int SAND = 20
    
    final static int HOUSE_FLOOR = 21
    final static int HOUSE_CELLAR = 22
    
    final static int MINE = 24
    final static int CAVE = 25
    final static int MOUNTAIN = 26
    
    final static int VOID = 255
}

enum Cursor {
    Dig("dig"), Arrow("arw"), Harvest("harvest"), Chi("chi"), Scroll("scroll")
    Cursor(String value) { this.value = value }
    private final String value
    public String value() { return value }
}

static waitContextMenu() {
    def m = getContextMenu()
    while (m == null) {
        Thread.sleep 100
        m = getContextMenu()
    }
    return m
}

static void waitCursor(Cursor c) {
    waitCursor(c.value())
}

static void waitCursor(String curname) {
    while (!getCursor().equals(curname)) Thread.sleep 100
}

static waitInventory() {
    if (!hasInventory("Inventory"))
        openInventory()
    return waitInventory("Inventory")
}

static waitInventory(String name) {
    def inv = getInventory(name)
    while (inv == null) {
        Thread.sleep 100 
        inv = getInventory(name)
    }
    return inv
}

static void wait(predicate) {
    while(predicate()) {
        Thread.sleep 200
    }
}

static void waitDrag() { while (getDragItem() == null) Thread.sleep 100 }
static void waitDrop() { while (getDragItem() != null) Thread.sleep 100 }

static void waitHourglassOn() { while (!checkHourglass()) Thread.sleep 100 }
static void waitHourglassOff() { while (checkHourglass()) Thread.sleep 100 }

static void waitHourglass() {
   while (true) {
      waitHourglassOn();
      waitHourglassOff();
      Thread.sleep 1000
      if (!checkHourglass())
        break;
   }
}

static void waitBeginMove() { while (!isMoving()) Thread.sleep 100 }
static void waitEndMove() { while (isMoving()) Thread.sleep 100 }

static void waitMove() {
    while (true) {
        waitBeginMove()
        waitEndMove()
        Thread.sleep 500
        if (!isMoving())
            break;
   }
}

static void resetCursor() {
    if (getCursor() != "arw") {
        mapClick(0, 0, 3, 0)
        waitCursor("arw")
    }
}

static void waitHourglass(int t1, int t2) {
      int t=0;
      while (!checkHourglass()&& t<t1) {Thread.sleep 100; t=t+100;}
      t=0;
   while (checkHourglass()&& t<t2) {Thread.sleep 100; t=t+100;}
}

static boolean waitMove(int t1, int t2) {  
   int t; 
   while (true) {
      t=0;
      while (!isMoving() && t<t1) {Thread.sleep 100 ; t=t+100;}
   t=0;
   if (!isMoving()) return false;
      while (isMoving() && t<t2) {Thread.sleep 100 ; t=t+100;}
   return true;
   }
}

