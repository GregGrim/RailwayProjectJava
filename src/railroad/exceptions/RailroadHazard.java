package railroad.exceptions;

import railroad.rollingStock.Locomotive;

/**
 * Exception class which is raised when train exceeds 200km/h
 */
public class RailroadHazard extends Exception {
    public RailroadHazard(Locomotive locomotive) {
        super(locomotive.getName()+" speed exceeded 200km/h\n"+locomotive.getTrainset());
    }
}
