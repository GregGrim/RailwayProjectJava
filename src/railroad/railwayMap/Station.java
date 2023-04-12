package railroad.railwayMap;

public class Station {
    private String name;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final Station other = (Station) obj;
        return (other.name.equals(this.name));
    }
}
