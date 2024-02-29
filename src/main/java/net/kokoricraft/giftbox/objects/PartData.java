package net.kokoricraft.giftbox.objects;

public class PartData {
    private final String name;
    private final double location_x;
    private final double location_y;
    private final double location_z;
    private final double scale_x;
    private final double scale_y;
    private final double scale_z;

    public PartData(String name, double location_x, double location_y, double location_z, double scale_x, double scale_y, double scale_z) {
        this.name = name;
        this.location_x = location_x;
        this.location_y = location_y;
        this.location_z = location_z;
        this.scale_x = scale_x;
        this.scale_y = scale_y;
        this.scale_z = scale_z;
    }

    public String getName() {
        return name;
    }

    public double getlocationX() {
        return location_x;
    }

    public double getlocationY() {
        return location_y;
    }

    public double getlocationZ() {
        return location_z;
    }

    public double getScaleX() {
        return scale_x;
    }

    public double getScaleY() {
        return scale_y;
    }

    public double getScaleZ() {
        return scale_z;
    }
}
