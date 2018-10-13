package net.javainthebox.caraibe.svg;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Transform;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Builder for SVG Content.
 */
public class SvgContentBuilder {

    public static final String NONE = "none";
    private final FxmlGradientBuilder fxmlGradientBuilder = new FxmlGradientBuilder(this);
    private final FxmlShapeBuilder fxmlShapeBuilder = new FxmlShapeBuilder(this);
    private final FxmlTransformBuilder fxmlTransformBuilder = new FxmlTransformBuilder();
    private final FxmlTextBuilder fxmlTextBuilder = new FxmlTextBuilder();
    private final FxmlImageBuilder fxmlImageBuilder = new FxmlImageBuilder(this);
    private final URL url;
    private final SvgContent root;
    private final Map<String, Paint> gradients;

    public SvgContentBuilder(URL url) {
        this.url = url;
        this.root = new SvgContent();

        this.gradients = new HashMap<>();
    }

    protected SvgContent build() throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty("javax.xml.stream.isValidating", false);
        factory.setProperty("javax.xml.stream.isNamespaceAware", false);
        factory.setProperty("javax.xml.stream.supportDTD", false);

        try (BufferedInputStream bufferedStream = new BufferedInputStream(this.url.openStream())) {
            XMLEventReader reader = factory.createXMLEventReader(bufferedStream);

            eventLoop(reader, this.root);
            reader.close();
        }

        return this.root;
    }

    private void eventLoop(XMLEventReader reader, Group groupParam) throws IOException,
            XMLStreamException {
        Group group = groupParam;
        if (groupParam == null) {
            group = new Group();
        }

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if (event.isStartElement()) {
                StartElement element = (StartElement) event;

                Node node = svgElemToFxml(reader, element);
                if (node != null) {
                    group.getChildren().add(node);
                }
            } else if (event.isEndElement()) {
                EndElement element = (EndElement) event;
                if (element.getName().toString().equals("g")) {
                    return;
                }
            }
        }
    }

    private Node svgElemToFxml(XMLEventReader reader, StartElement element) throws
            XMLStreamException, IOException {
        Node node = null;
        switch (element.getName().toString()) {
            case "rect":
                node = this.fxmlShapeBuilder.buildRect(element);
                break;
            case "circle":
                node = this.fxmlShapeBuilder.buildCircle(element);
                break;
            case "ellipse":
                node = this.fxmlShapeBuilder.buildEllipse(element);
                break;
            case "path":
                node = this.fxmlShapeBuilder.buildPath(element);
                break;
            case "polygon":
                node = this.fxmlShapeBuilder.buildPolygon(element);
                break;
            case "line":
                node = this.fxmlShapeBuilder.buildLine(element);
                break;
            case "polyline":
                node = this.fxmlShapeBuilder.buildPolyline(element);
                break;
            case "text":
                node = this.fxmlTextBuilder.buildText(reader, element);
                break;
            case "image":
                node = this.fxmlImageBuilder.buildImage(reader, element);
                break;
            case "svg":
            case "net/javainthebox/caraibe/svg":
            case "g":
                node = buildGroup(reader);
                break;
            case "linearGradient":
                this.fxmlGradientBuilder.buildLinearGradient(reader, element);
                break;
            case "radialGradient":
                this.fxmlGradientBuilder.buildRadialGradient(reader, element);
                break;
            case "inport":
            case "outport":
                break;
            default:
                Logger.getLogger(SvgContentBuilder.class.getName())
                        .log(Level.INFO, "Support Element: {0}" + "Non ", element);
                break;
        }
        if (node != null) {
            setCommonNodeProperties(element, node);
        }
        return node;
    }

    private void setCommonNodeProperties(StartElement element, Node node) {
        if (node instanceof Shape) {
            this.fxmlShapeBuilder.setShapeStyle((Shape) node, element);
        }

        setOpacity(node, element);
        this.fxmlTransformBuilder.setTransform(node, element);

        Attribute idAttribute = element.getAttributeByName(new QName("id"));

        if (idAttribute != null) {
            this.root.putNode(idAttribute.getValue(), node);
        }

        Attribute classAttribute = element.getAttributeByName(new QName("class"));

        if (classAttribute != null) {
            node.getStyleClass().setAll(classAttribute.getValue().split("[\\s]+"));
        }
    }

    private Group buildGroup(XMLEventReader reader) throws IOException,
            XMLStreamException {
        Group group = new Group();
        eventLoop(reader, group);

        return group;
    }

    Transform extractTransform(String transforms) {

        return this.fxmlTransformBuilder.extractTransform(transforms);
    }

    private void setOpacity(Node node, StartElement element) {
        Attribute opacityAttribute = element.getAttributeByName(new QName("opacity"));
        if (opacityAttribute != null) {
            double opacity = Double.parseDouble(opacityAttribute.getValue());
            node.setOpacity(opacity);
        }
    }

    Paint expressPaint(String value) {
        Paint paint = null;
        if (!NONE.equals(value)) {
            if (value.startsWith("url(#")) {
                String id = value.substring(5, value.length() - 1);
                paint = this.gradients.get(id);
            } else {
                paint = Color.web(value);
            }
        }

        return paint;
    }

    public Map<String,Paint> getGradients() {
        return this.gradients;
    }

    public URL getUrl() {
        return this.url;
    }
}
