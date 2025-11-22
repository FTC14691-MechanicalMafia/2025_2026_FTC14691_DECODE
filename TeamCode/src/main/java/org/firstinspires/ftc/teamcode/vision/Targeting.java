package org.firstinspires.ftc.teamcode.vision;

public class Targeting {

    private final Double range;
    private final Double bearing;
    private final Double elevation;

    public Targeting(Double range, Double bearing, Double elevation) {
        this.range = range;
        this.bearing = bearing;
        this.elevation = elevation;
    }

    public Double getRange() {
        return range;
    }

    public Double getBearing() {
        return bearing;
    }

    public Double getElevation() {
        return elevation;
    }
}
