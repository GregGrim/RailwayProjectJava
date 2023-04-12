package railroad.exceptions;

import railroad.rollingStock.Locomotive;

public class RailroadHazard extends RuntimeException {
    public RailroadHazard(Locomotive locomotive) {
        super(locomotive.getName()+" speed exceeded 200");
    }
}
