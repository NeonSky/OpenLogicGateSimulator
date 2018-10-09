package org.cafebabe.gui.util;


/**
 * Represents the metadata for a specific port.
 * Where it is positioned relative to the owner component and what its name is.
 */
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
