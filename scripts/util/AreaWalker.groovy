package util

import static Utils.*

class AreaWalker {
    enum Start { NEAREST, NW, NE, SW, SE }
    enum Orientation { VERTICAL, HORIZONTAL }
    
    private final Area area
    private final Start start
    private final Orientation or
    
    AreaWalker(Area area) {
        this(area, Start.NEAREST, Orientation.HORIZONTAL)
    }
    
    AreaWalker(Area area, Orientation or) {
        this(area, Start.NEAREST, or)
    }
    
    AreaWalker(Area area, Start start) {
        this(area, start, Orientation.HORIZONTAL)
    }
    
    AreaWalker(Area area, Start start, Orientation or) {
        this.area = area
        this.start = start
        this.or = or
    }
    
    Iterable<Position> eachTile() {
        return new Iterable<Position>() {
            Iterator<Position> iterator() {
                return new AreaIterator()
            }
        }
    }
    
    private class AreaIterator implements Iterator<Position> {
        private final Direction chflowdir // direction for changing flow
        private Direction curdir          // current direction to next tile
        private Position p                // current position
        
        private int x, y
        private int cx, cy
        
        public AreaIterator() {
            this.p = getStartPosition(this.start, this.area)
            def asz = sizeOf(area)
            // movement
            this.x = ((this.or == Orientation.HORIZONTAL) ? asz.x : asz.y) - 1
            this.y = (this.or == Orientation.HORIZONTAL) ? asz.y : asz.x
            this.cy = 0
            this.cx = 0
            // determine initial direction of movement
            this.curdir = getStartDirection(this.p, this.area, this.or)
            this.chflowdir = getRowChangeDirection(this.p, this.area, this.or)
        }
        
        boolean hasNext() {
            return (cy < y)
        }
        
        Position next() {
            def oldp = p
            if (cy >= y) return null
            if (cx < x) {
                p = p.offset(curdir)
                cx++
            } else {
                // change flow
                p = p.offset(chflowdir)
                // reverse direction
                curdir = curdir.reverse()
                cx = 0
                cy++
            }
            return oldp
        }
        
        void remove() { }
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