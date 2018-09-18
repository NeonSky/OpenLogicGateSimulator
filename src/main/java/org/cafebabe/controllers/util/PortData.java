package org.cafebabe.controllers.util;

public class PortData {
    public String name;
    public double x;
    public double y;

    public PortData(String name, String x, String y) {
        this.name = name;
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }
}
