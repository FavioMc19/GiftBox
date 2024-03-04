package net.kokoricraft.giftbox.objects;

public class PartData {
    private final String name;
    private final double location_x;
    private final double location_y;
    private final double location_z;
    private final double scale_x;
    private final double scale_y;
    private final double scale_z;
    private boolean glow;
    private String glow_color = "&e";
    private boolean temporal = false;
    private String type = "skin";
    private String material;

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

    public void setGlow(boolean glow){
        this.glow = glow;
    }

    public boolean isGlowing(){
        return glow;
    }

    public void setGlowColor(String glow_color){
        if(glow_color == null) return;
        this.glow_color = glow_color;
    }

    public String getGlowColor(){
        return glow_color;
    }

    public void setTemporal(boolean temporal){
        this.temporal = temporal;
    }

    public boolean isTemporal(){
        return temporal;
    }

    public void setMaterial(String material){
        this.material = material;
    }

    public String getMaterial(){
        return material;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
