package net.javainthebox.caraibe.svg;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;

/**
 * OUR GROUP IS NOT RESPONSIBLE FOR THIS FILE, IT IS SIMPLY A DEPENDENCY.
 * SvgLoaderUtil is a class for loading SVG file.
 *
 * <p><pre> URL url = ...;
 * SvgContent content SvgLoaderUtil.load(url);
 *
 * container.getChildren.add(content);</pre>
 */
public final class SvgLoaderUtil {
    private SvgLoaderUtil() {
    }

    /**
     * Load SVG file and convert it to JavaFX.
     *
     * @param url The location of SVG file
     * @return a SvgContent object that indicates SVG content
     */
    public static SvgContent load(String url) {
        SvgContent root = null;

        URL tempUrl = null;
        try {
            tempUrl = new URL(url);
        } catch (MalformedURLException ex) {
            tempUrl = SvgLoaderUtil.class.getResource(url);
            if (tempUrl == null) {
                try {
                    tempUrl = new File(url).toURI().toURL();
                } catch (final MalformedURLException ex1) {
                    Logger.getLogger(SvgLoaderUtil.class.getName()).log(Level.SEVERE, null, ex1);
                    return root;
                }
            }
        }

        SvgContentBuilder builder = new SvgContentBuilder(tempUrl);
        try {
            root = builder.build();
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(SvgLoaderUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return root;
    }
}
