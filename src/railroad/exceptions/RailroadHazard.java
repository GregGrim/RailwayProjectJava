package railroad.exceptions;

import railroad.rollingStock.Locomotive;

public class RailroadHazard extends Exception {
    public RailroadHazard(Locomotive locomotive) {
        super(locomotive.getName()+" speed exceeded 200km/h\n"+locomotive.getTrainset());
    }
}
