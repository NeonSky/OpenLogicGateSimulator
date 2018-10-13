package net.javainthebox.caraibe.svg;

import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Builds FXML text.
 */
public class FxmlTextBuilder {

    Shape buildText(XMLEventReader reader, StartElement element) throws XMLStreamException {
        Attribute fontFamilyAttribute = element.getAttributeByName(new QName("font-family"));
        Attribute fontSizeAttribute = element.getAttributeByName(new QName("font-size"));

        Font font = null;
        if (fontFamilyAttribute != null && fontSizeAttribute != null) {
            font = Font.font(fontFamilyAttribute.getValue().replace("'", ""),
                    Double.parseDouble(fontSizeAttribute.getValue().replaceAll("[^\\d.]", "")));
        }

        XMLEvent event = reader.nextEvent();

        Attribute xattribute = element.getAttributeByName(new QName("x"));
        Attribute yattribute = element.getAttributeByName(new QName("y"));

        double x = 0.0;
        double y = 0.0;

        if (xattribute != null) {
            x = Double.parseDouble(xattribute.getValue());
        }
        if (yattribute != null) {
            y = Double.parseDouble(yattribute.getValue());
        }

        if (event.isCharacters()) {
            Text text = new Text(((Characters) event).getData());
            if (font != null) {
                text.setFont(font);
            }
            text.setX(x);
            text.setY(y);
            return text;
        } else {
            throw new XMLStreamException("Illegal Element: " + event);
        }
    }
}