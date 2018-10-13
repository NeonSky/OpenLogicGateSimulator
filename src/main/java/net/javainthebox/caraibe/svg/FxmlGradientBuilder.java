package net.javainthebox.caraibe.svg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Builds FXML gradients.
 */
public class FxmlGradientBuilder {
    public static final String USER_SPACE_ON_USE = "userSpaceOnUse";
    private final SvgContentBuilder svgContentBuilder;

    public FxmlGradientBuilder(SvgContentBuilder svgContentBuilder) {
        this.svgContentBuilder = svgContentBuilder;
    }

    private List<Stop> buildStops(XMLEventReader reader, String kindOfGradient) throws
            XMLStreamException {
        List<Stop> stops = new ArrayList<Stop>();

        while (true) {
            XMLEvent event = reader.nextEvent();
            if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(
                    kindOfGradient)) {
                break;
            } else if (event.isStartElement()) {
                StartElement element = event.asStartElement();
                if (!element.getName().getLocalPart().equals("stop")) {
                    Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                            "LinearGradient doesn''t supports: {0}", element);
                    continue;
                }

                double offset = Double.NaN;
                String color = null;
                double opacity = 1.0;

                @SuppressWarnings("unchecked")
                Iterator<Attribute> it = element.getAttributes();
                while (it.hasNext()) {

                    Attribute attribute = it.next();
                    switch (attribute.getName().getLocalPart()) {
                        case "offset":
                            offset = Double.parseDouble(attribute.getValue());
                            break;
                        case "style":
                            String style = attribute.getValue();
                            StringTokenizer tokenizer = new StringTokenizer(style, ";");
                            while (tokenizer.hasMoreTokens()) {
                                String item = tokenizer.nextToken().trim();
                                if (item.startsWith("stop-color")) {
                                    color = item.substring(11);
                                } else if (item.startsWith("stop-opacity")) {
                                    opacity = Double.parseDouble(item.substring(13));
                                } else {
                                    Logger.getLogger(SvgContentBuilder.class.getName()).log(Level
                                                    .INFO, "[{1}] ''{2}''" + "LinearGradient Stop "
                                                    + "doesn''t supports: {0} ",
                                                    Arrays.asList(attribute, element, item));
                                }
                            }
                            break;
                        default:
                            Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                                    "LinearGradient Stop doesn''t supports: {0} [{1}]",
                                            Arrays.asList(attribute, element));
                            break;
                    }
                }

                if (color != null) {
                    Color colour = Color.web(color, opacity);
                    Stop stop = new Stop(offset, colour);
                    stops.add(stop);
                }
            }
        }

        return stops;
    }

    void buildRadialGradient(XMLEventReader reader, StartElement element) throws
            IOException, XMLStreamException {
        String id = null;
        Double fx = null;
        Double fy = null;
        Double cx = null;
        Double cy = null;
        Double r = null;
        Transform transform = null;

        @SuppressWarnings("unchecked")
        Iterator<Attribute> it = element.getAttributes();
        while (it.hasNext()) {
            Attribute attribute = it.next();
            switch (attribute.getName().getLocalPart()) {
                case "id":
                    id = attribute.getValue();
                    break;
                case "gradientUnits":
                    String gradientUnits = attribute.getValue();
                    if (!gradientUnits.equals(USER_SPACE_ON_USE)) {
                        Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                                "LinearGradient supports only userSpaceOnUse: {0}", element);
                        return;
                    }
                    break;
                case "fx":
                    fx = Double.valueOf(attribute.getValue());
                    break;
                case "fy":
                    fy = Double.valueOf(attribute.getValue());
                    break;
                case "cx":
                    cx = Double.valueOf(attribute.getValue());
                    break;
                case "cy":
                    cy = Double.valueOf(attribute.getValue());
                    break;
                case "r":
                    r = Double.valueOf(attribute.getValue());
                    break;
                case "gradientTransform":
                    transform = this.svgContentBuilder.extractTransform(attribute.getValue());
                    break;
                default:
                    Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                            "RadialGradient doesn''t supports: {0}", element);
                    break;
            }
        }

        List<Stop> stops = buildStops(reader, "radialGradient");

        if (id != null && cx != null && cy != null && r != null) {
            double fdistance = 0.0;
            double fangle = 0.0;

            if (transform instanceof Affine) {
                double tempCx = cx;
                double tempCy = cy;
                double tempR = r;

                Affine affine = (Affine) transform;
                cx = tempCx * affine.getMxx() + tempCy * affine.getMxy() + affine.getTx();
                cy = tempCx * affine.getMyx() + tempCy * affine.getMyy() + affine.getTy();

                r = Math.sqrt(tempR * affine.getMxx() * tempR * affine.getMxx()
                        + tempR * affine.getMyx() * tempR * affine.getMyx());

                if (fx == null || fy == null) {
                    fangle = Math.asin(affine.getMyx()) * 180.0 / Math.PI;
                    fdistance = Math.sqrt((cx - tempCx) * (cx - tempCx) + (cy - tempCy) * (cy
                            - tempCy));
                } else {
                    double tempFx = fx;
                    double tempFy = fy;
                    fx = tempFx * affine.getMxx() + tempFy * affine.getMxy() + affine.getTx();
                    fy = tempFx * affine.getMyx() + tempFy * affine.getMyy() + affine.getTy();
                }
            }

            if (fx != null && fy != null) {
                fdistance = Math.sqrt((fx - cx) * (fx - cx) + (fy - cy) * (fy - cy)) / r;
                fangle = Math.atan2(cy - fy, cx - fx) * 180.0 / Math.PI;
            }

            RadialGradient gradient = new RadialGradient(fangle, fdistance, cx, cy, r, false,
                    CycleMethod.NO_CYCLE, stops);
            this.svgContentBuilder.getGradients().put(id, gradient);
        }
    }

    void buildLinearGradient(XMLEventReader reader, StartElement element) throws
            IOException, XMLStreamException {
        String id = null;
        double x1 = Double.NaN;
        double y1 = Double.NaN;
        double x2 = Double.NaN;
        double y2 = Double.NaN;
        Transform transform = null;

        @SuppressWarnings("unchecked")
        Iterator<Attribute> it = element.getAttributes();
        while (it.hasNext()) {
            Attribute attribute = it.next();
            switch (attribute.getName().getLocalPart()) {
                case "id":
                    id = attribute.getValue();
                    break;
                case "gradientUnits":
                    String gradientUnits = attribute.getValue();
                    if (!gradientUnits.equals(USER_SPACE_ON_USE)) {
                        Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                                "LinearGradient supports only userSpaceOnUse: {0}", element);
                        return;
                    }
                    break;
                case "x1":
                    x1 = Double.parseDouble(attribute.getValue());
                    break;
                case "y1":
                    y1 = Double.parseDouble(attribute.getValue());
                    break;
                case "x2":
                    x2 = Double.parseDouble(attribute.getValue());
                    break;
                case "y2":
                    y2 = Double.parseDouble(attribute.getValue());
                    break;
                case "gradientTransform":
                    transform = this.svgContentBuilder.extractTransform(attribute.getValue());
                    break;
                default:
                    Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.INFO,
                            "LinearGradient doesn''t supports: {0}:{1}", Arrays.asList(
                                    attribute,
                                    element));
                    break;
            }
        }

        List<Stop> stops = buildStops(reader, "linearGradient");

        if (id != null) {
            if (transform instanceof Affine) {
                double x1d = x1;
                double y1d = y1;
                double x2d = x2;
                double y2d = y2;
                Affine affine = (Affine) transform;
                x1 = x1d * affine.getMxx() + y1d * affine.getMxy() + affine.getTx();
                y1 = x1d * affine.getMyx() + y1d * affine.getMyy() + affine.getTy();
                x2 = x2d * affine.getMxx() + y2d * affine.getMxy() + affine.getTx();
                y2 = x2d * affine.getMyx() + y2d * affine.getMyy() + affine.getTy();
            }

            LinearGradient gradient = new LinearGradient(x1, y1, x2, y2, false, CycleMethod
                    .NO_CYCLE, stops);
            this.svgContentBuilder.getGradients().put(id, gradient);
        }
    }
}