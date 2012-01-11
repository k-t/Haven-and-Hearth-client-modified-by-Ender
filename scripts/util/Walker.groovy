package util

import static Utils.*

class Walker {
    enum Start { NEAREST, NW, NE, SW, SE }
    enum Orientation { VERTICAL, HORIZONTAL }
    
    private final Start start
    private final Orientation or
    
    private Closure ontile      // action on tile, void f()
    private Closure beforetile  // action before moving on tile, boolean f(Position pos)
    
    public Walker() {
        this(Start.NEAREST, Orientation.HORIZONTAL)
    }
    
    public Walker(Orientation or) {
        this(Start.NEAREST, or)
    }
    
    public Walker(Start start) {
        this(start, Orientation.HORIZONTAL)
    }
    
    public Walker(Start start, Orientation or) {
        this.start = start
        this.or = or
    }
    
    /** Sets action that should be executed on each tile. */
    void setAction(action) {
        this.ontile = action
    }
    
    /** Sets action which should be executed before coming on next tile. */
    void setAdvanceAction(action) {
        this.beforetile = action
    }
    
    /** Walks through area executing the specified action on each tile. */
    void walk(Area area) {
        def startpos = getStartPosition(start, area)
        // check position and move to start position if needed
        def mypos = getMyCoord()
        if (mypos.tileX != startpos.tileX || mypos.tileY != startpos.tileY) {
            // execute action before going on the first tile
            if (beforetile != null) {
                boolean cancel = !beforetile(startpos)
                if (cancel) return
            }
            // go to the first tile of the area
            mapAbsClick(startpos.x, startpos.y, MouseButton.LEFT, 0)
            waitMove()
        }
        // determine initial direction of movement
        def curdir = getStartDirection(startpos, area, or)
        def chdir = getRowChangeDirection(startpos, area, or)
        def asz = sizeOf(area)
        // movement
        int x = ((or == Orientation.HORIZONTAL) ? asz.x : asz.y) - 1
        int y = (or == Orientation.HORIZONTAL) ? asz.y : asz.x
        int cy = 0
        while (cy < y) {
            if (cy != 0) {
                // step to other row
                boolean cancel = !advanceTile(chdir)
                if (cancel) return
            }            
            int cx = 0
            if (ontile != null) ontile()
            while (cx < x) {
                boolean cancel = !advanceTile(curdir)
                if (cancel) return
                cx++
            }
            // turn
            curdir = curdir.reverse()
            cy++
        }
    }
    
    private boolean advanceTile(Direction d) {
        if (beforetile != null) {
            boolean cancel = !beforetile(getMyCoord().offset(d))
            if (cancel) return false
        }
        step(d)
        if (ontile != null)
            ontile()
        return true
    }
    
    /** Steps on on tile in specified direction. */
    private static void step(Direction d) {
        int x = 0
        int y = 0
        switch (d) {
            case Direction.NORTH : y = -1; break;
            case Direction.SOUTH : y =  1; break;
            case Direction.WEST  : x = -1; break;
            case Direction.EAST  : x =  1; break;
        }        
        mapMoveStep(x, y)
        waitMove()
    }
    
    /** Returns the size of area in tiles. */
    private static Coord sizeOf(Area a) {
        def nw = northWestOf(a)
        def se = southEastOf(a)
        return new Coord(se.tileX - nw.tileX + 1, se.tileY - nw.tileY + 1)
    }
    
    private static Direction getRowChangeDirection(Position start, Area a, Orientation or) {
        switch (or) {
            case Orientation.HORIZONTAL:
                if (start.y == a.north)
                    return Direction.SOUTH
                else if (start.y == a.south)
                    return Direction.NORTH
            case Orientation.VERTICAL:
                if (start.x == a.west)
                    return Direction.EAST
                else if (start.x == a.east)
                    return Direction.WEST
        }
        throw new IllegalArgumentException()
    }
    
    private static Direction getStartDirection(Position start, Area a, Orientation or) {
        switch (or) {
            case Orientation.HORIZONTAL:
                if (start.x == a.west)
                    return Direction.EAST
                else if (start.x == a.east)
                    return Direction.WEST
            case Orientation.VERTICAL:
                if (start.y == a.north)
                    return Direction.SOUTH
                else if (start.y == a.south)
                    return Direction.NORTH
        }
        throw new IllegalArgumentException()
    }
    
    private static Position getStartPosition(Start start, Area a) {
        switch (start) {
            case Start.NW: return northWestOf(a)
            case Start.NE: return northEastOf(a)
            case Start.SW: return southWestOf(a)
            case Start.SE: return southEastOf(a)
            case Start.NEAREST:
                def my = getMyCoord()
                def corners = [northEastOf(a), northWestOf(a), southWestOf(a), southEastOf(a)]
                return corners.min { p -> distance(p, my) }
        }
        throw new IllegalArgumentException()
    }
    
    private static double distance(Position p1, Position p2) {
        long dx = p2.x - p1.x;
        long dy = p2.y - p1.y;
        return Math.sqrt((dx * dx) + (dy * dy));
    }
}