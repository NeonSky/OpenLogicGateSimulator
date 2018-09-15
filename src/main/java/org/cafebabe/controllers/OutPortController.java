package org.cafebabe.controllers;

public class OutPortController extends PortController {
    public OutPortController(String name, double x, double y) {
        super(name, x, y);
        connectionNodeCircle.getStyleClass().add("outPort");
    }
}
