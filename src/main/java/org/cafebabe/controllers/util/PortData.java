package org.cafebabe.controllers.util;

public class PortData {
    public final String name;
    public final double x;
    public final double y;

    public PortData(String name, String x, String y) {
        this.name = name;
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }
}
