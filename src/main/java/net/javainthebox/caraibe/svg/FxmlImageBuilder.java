package net.javainthebox.caraibe.svg;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

/**
 * OUR GROUP IS NOT RESPONSIBLE FOR THIS FILE, IT IS SIMPLY A DEPENDENCY.
 * Builds FXML images.
 */
public class FxmlImageBuilder {
    private final SvgContentBuilder svgContentBuilder;

    public FxmlImageBuilder(SvgContentBuilder svgContentBuilder) {
        this.svgContentBuilder = svgContentBuilder;
    }

    ImageView buildImage(XMLEventReader reader, StartElement element) throws IOException {
        Attribute widthAttribute = element.getAttributeByName(new QName("width"));
        double width = Double.parseDouble(widthAttribute.getValue());
        Attribute heightAttribute = element.getAttributeByName(new QName("height"));
        double height = Double.parseDouble(heightAttribute.getValue());
        Attribute hrefAttribute = element.getAttributeByName(new QName("href"));

        URL imageUrl = null;
        try {
            imageUrl = new URL(hrefAttribute.getValue());
        } catch (MalformedURLException ex) {
            try {
                imageUrl = new URL(this.svgContentBuilder.getUrl(), hrefAttribute.getValue());
            } catch (MalformedURLException ex1) {
                Logger.getLogger(SvgContentBuilder.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        Image image = new Image(imageUrl.toString(), width, height, true, true);

        return new ImageView(image);
    }
}