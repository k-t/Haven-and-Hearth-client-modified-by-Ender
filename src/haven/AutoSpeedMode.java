package haven;

public enum AutoSpeedMode {
    Crawl(0), Walk(1), Run(2), Sprint(3);
    
    private final int value;
    
    AutoSpeedMode(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static AutoSpeedMode fromInteger(int value) throws IllegalArgumentException {
        switch (value) {
        case 0: return Crawl;
        case 1: return Walk;
        case 2: return Run;
        case 3: return Sprint;
        }
        throw new IllegalArgumentException();
    }
}
