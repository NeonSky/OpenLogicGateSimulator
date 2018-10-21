package net.javainthebox.caraibe.svg;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

/**
 * OUR GROUP IS NOT RESPONSIBLE FOR THIS FILE, IT IS SIMPLY A DEPENDENCY.
 * Build FXML shapes.
 */
public class FxmlShapeBuilder {
    public static final String MITER = "miter";
    public static final String ROUND = "round";
    public static final String BEVEL = "bevel";
    public static final String SQUARE = "square";
    public static final String BUTT = "butt";
    public static final String ROUND1 = "round";
    private final SvgContentBuilder svgContentBuilder;

    public FxmlShapeBuilder(SvgContentBuilder svgContentBuilder) {
        this.svgContentBuilder = svgContentBuilder;
    }

    Shape buildRect(StartElement element) {
        Attribute xattribute = element.getAttributeByName(new QName("x"));
        Attribute yattribute = element.getAttributeByName(new QName("y"));
        Attribute widthAttribute = element.getAttributeByName(new QName("width"));
        Attribute heightAttribute = element.getAttributeByName(new QName("height"));

        double x = 0.0;
        double y = 0.0;

        if (xattribute != null) {
            x = Double.parseDouble(xattribute.getValue());
        }
        if (yattribute != null) {
            y = Double.parseDouble(yattribute.getValue());
        }
        return new Rectangle(x, y,
                Double.parseDouble(widthAttribute.getValue()),
                Double.parseDouble(heightAttribute.getValue()));
    }

    Shape buildCircle(StartElement element) {
        Attribute cxAttribute = element.getAttributeByName(new QName("cx"));
        Attribute cyAttribute = element.getAttributeByName(new QName("cy"));
        Attribute radiusAttribute = element.getAttributeByName(new QName("r"));

        return new Circle(Double.parseDouble(cxAttribute.getValue()),
                Double.parseDouble(cyAttribute.getValue()),
                Double.parseDouble(radiusAttribute.getValue()));
    }

    Shape buildEllipse(StartElement element) {
        Attribute cxAttribute = element.getAttributeByName(new QName("cx"));
        Attribute cyAttribute = element.getAttributeByName(new QName("cy"));
        Attribute radiusXAttribute = element.getAttributeByName(new QName("rx"));
        Attribute radiusYAttribute = element.getAttributeByName(new QName("ry"));

        return new Ellipse(Double.parseDouble(cxAttribute.getValue()),
                Double.parseDouble(cyAttribute.getValue()),
                Double.parseDouble(radiusXAttribute.getValue()),
                Double.parseDouble(radiusYAttribute.getValue()));
    }

    Shape buildPath(StartElement element) {
        Attribute dattribute = element.getAttributeByName(new QName("d"));

        SVGPath path = new SVGPath();
        path.setContent(dattribute.getValue());

        return path;
    }

    Shape buildPolygon(StartElement element) {
        Attribute pointsAttribute = element.getAttributeByName(new QName("points"));
        Polygon polygon = new Polygon();

        StringTokenizer tokenizer = new StringTokenizer(pointsAttribute.getValue(), " ");
        while (tokenizer.hasMoreTokens()) {
            String point = tokenizer.nextToken();

            StringTokenizer tokenizer2 = new StringTokenizer(point, ",");
            Double x = Double.valueOf(tokenizer2.nextToken());
            Double y = Double.valueOf(tokenizer2.nextToken());

            polygon.getPoints().add(x);
            polygon.getPoints().add(y);
        }

        return polygon;
    }

    Shape buildLine(StartElement element) {
        Attribute x1Attribute = element.getAttributeByName(new QName("x1"));
        Attribute y1Attribute = element.getAttributeByName(new QName("y1"));
        Attribute x2Attribute = element.getAttributeByName(new QName("x2"));
        Attribute y2Attribute = element.getAttributeByName(new QName("y2"));

        if (x1Attribute == null || y1Attribute == null || x2Attribute == null || y2Attribute
                == null) {
            return null;
        } else {
            double x1 = Double.parseDouble(x1Attribute.getValue());
            double y1 = Double.parseDouble(y1Attribute.getValue());
            double x2 = Double.parseDouble(x2Attribute.getValue());
            double y2 = Double.parseDouble(y2Attribute.getValue());

            return new Line(x1, y1, x2, y2);
        }
    }

    Shape buildPolyline(StartElement element) {
        Polyline polyline = new Polyline();
        Attribute pointsAttribute = element.getAttributeByName(new QName("points"));

        StringTokenizer tokenizer = new StringTokenizer(pointsAttribute.getValue(), " ");
        while (tokenizer.hasMoreTokens()) {
            addPointFromToken(polyline, tokenizer);
        }

        return polyline;
    }

    private void addPointFromToken(Polyline polyline, StringTokenizer tokenizer) {
        String points = tokenizer.nextToken();
        StringTokenizer tokenizer2 = new StringTokenizer(points, ",");
        double x = Double.parseDouble(tokenizer2.nextToken());
        double y = Double.parseDouble(tokenizer2.nextToken());
        polyline.getPoints().add(x);
        polyline.getPoints().add(y);
    }

    void setShapeStyle(Shape shape, StartElement element) {
        Attribute fillAttribute = element.getAttributeByName(new QName("fill"));
        if (fillAttribute != null) {
            shape.setFill(this.svgContentBuilder.expressPaint(fillAttribute.getValue()));
        }

        Attribute strokeAttribute = element.getAttributeByName(new QName("stroke"));
        if (strokeAttribute != null) {
            shape.setStroke(this.svgContentBuilder.expressPaint(strokeAttribute.getValue()));
        }

        Attribute strokeWidthAttribute = element.getAttributeByName(new QName("stroke-width"));
        if (strokeWidthAttribute != null) {
            double strokeWidth = Double.parseDouble(strokeWidthAttribute.getValue().replaceAll(
                    "[^\\d.]", ""));
            shape.setStrokeWidth(strokeWidth);
        }

        Attribute styleAttribute = element.getAttributeByName(new QName("style"));
        if (styleAttribute != null) {
            String styles = styleAttribute.getValue();
            StringTokenizer tokenizer = new StringTokenizer(styles, ";");
            while (tokenizer.hasMoreTokens()) {
                String style = tokenizer.nextToken();
                setStyleFromTokens(shape, element, style);
            }
        }
    }

    private void setStyleFromTokens(Shape shape, StartElement element, String style) {
        StringTokenizer tokenizer2 = new StringTokenizer(style, ":");
        String styleName = tokenizer2.nextToken();
        String styleValue = tokenizer2.nextToken();

        switch (styleName) {
            case "fill":
                shape.setFill(this.svgContentBuilder.expressPaint(styleValue));
                break;
            case "stroke":
                shape.setStroke(this.svgContentBuilder.expressPaint(styleValue));
                break;
            case "stroke-width":
                double strokeWidth = Double.parseDouble(styleValue);
                shape.setStrokeWidth(strokeWidth);
                break;
            case "stroke-linecap":
                StrokeLineCap linecap = StrokeLineCap.BUTT;
                if (styleValue.equals(ROUND1)) {
                    linecap = StrokeLineCap.ROUND;
                } else if (styleValue.equals(SQUARE)) {
                    linecap = StrokeLineCap.SQUARE;
                } else if (!styleValue.equals(BUTT)) {
                    Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                            "No Support Style: {0} {1}", new Object[]{style, element});
                }

                shape.setStrokeLineCap(linecap);
                break;
            case "stroke-miterlimit":
                double miterLimit = Double.parseDouble(styleValue);
                shape.setStrokeMiterLimit(miterLimit);
                break;
            case "stroke-linejoin":
                StrokeLineJoin linejoin = StrokeLineJoin.MITER;
                if (styleValue.equals(BEVEL)) {
                    linejoin = StrokeLineJoin.BEVEL;
                } else if (styleValue.equals(ROUND)) {
                    linejoin = StrokeLineJoin.ROUND;
                } else if (!styleValue.equals(MITER)) {
                    Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                            "No Support Style: {0} {1}",
                            Arrays.asList(style, element));
                }

                shape.setStrokeLineJoin(linejoin);
                break;
            case "opacity":
                double opacity = Double.parseDouble(styleValue);
                shape.setOpacity(opacity);
                break;
            default:
                Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                        "Support Style: {0} {1}" + "No ",
                        Arrays.asList(style, element));
                break;
        }
    }
}