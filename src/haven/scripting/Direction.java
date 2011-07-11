package haven.scripting;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;
    
    public Direction reverse() {
        switch (this) {
        case NORTH : return SOUTH;
        case WEST  : return EAST;
        case SOUTH : return NORTH;
        case EAST  : return WEST;
        }
        throw new IllegalArgumentException();
    }
    
    public Direction turnLeft() {
        switch (this) {
        case NORTH : return WEST;
        case WEST  : return SOUTH;
        case SOUTH : return EAST;
        case EAST  : return NORTH;
        }
        throw new IllegalArgumentException();
    }
    
    public Direction turnRight() {
        switch (this) {
        case NORTH : return EAST;
        case EAST  : return SOUTH;
        case SOUTH : return WEST;
        case WEST  : return NORTH;
        }
        throw new IllegalArgumentException();
    }
}
