package haven.scripting;

import haven.*;

public class Position {
    private final int x, y;
    private final int tx, ty;
    
    public Position(Coord c) {
        this.x = c.x;
        this.y = c.y;
        Coord t = c.div(MCache.tilesz);
        this.tx = t.x;
        this.ty = t.y;
    }
    
    public Position(int x, int y) {
        this(new Coord(x, y));
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public int getTileX() { return tx; }
    public int getTileY() { return ty; }
    
    public static Position fromTileCoord(Coord c) {
        return new Position(MapView.tilify(c.mul(MCache.tilesz)));
    }
    
    public static Position fromTileCoord(int tx, int ty) {
        return fromTileCoord(new Coord(tx, ty));
    }
    
    public Coord toCoord() {
        return new Coord(x, y);
    }
    
    /** Returns new position with specified offset in tiles relative to this position. */
    public Position offset(int tx, int ty) {
        return new Position(x + tx * MCache.tilesz.x, y + ty * MCache.tilesz.y);        
    }
    
    public Position offset(Direction dir) {
        return offset(dir, 1);
    }
    
    public Position offset(Direction dir, int off) {
        switch (dir) {
        case NORTH : return north(off);
        case WEST  : return west(off);
        case SOUTH : return south(off);
        case EAST  : return east(off);
        }
        throw new IllegalArgumentException();
    }
    
    public Position north()        { return north(1); }
    public Position north(int off) { return offset(0, -off); }
    
    public Position south()        { return south(1); }
    public Position south(int off) { return offset(0, off); }
    
    public Position east()        { return east(1); }
    public Position east(int off) { return offset(off, 0); }
    
    public Position west()        { return west(1); }
    public Position west(int off) { return offset(-off, 0); }
}
