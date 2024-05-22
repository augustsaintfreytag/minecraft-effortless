package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.api.math.Range1d;
import dev.huskuraft.effortless.api.math.Vector3d;

public interface PositionBounded {

    Range1d POSITION_RANGE = new Range1d(-30000000, 30000000);

    boolean isInBounds(Vector3d position);

}
