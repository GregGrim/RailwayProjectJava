package railroad.railwayMap;

/**
 * Basic railroad world class that simulates stations characterized by name
 */
public class Station {
    private final String name;
    private static int counter = 1;

    public Station () {
        this.name="Station-"+ (counter++);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    /**
     * overriden method equals that comparing stations by name
     * @param station to be compared
     * @return boolean
     */
    @Override
    public boolean equals(Object station) {
        if (station == null) {
            return false;
        }
        if (station.getClass() != this.getClass()) {
            return false;
        }
        final Station other = (Station) station;
        return (other.name.equals(this.name));
    }
}
