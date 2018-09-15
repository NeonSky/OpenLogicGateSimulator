package org.cafebabe.controllers;

public class InPortController extends PortController {
    public InPortController(String name, double x, double y) {
        super(name, x, y);
        connectionNodeCircle.getStyleClass().add("inPort");
    }
}
