package org.cafebabe.model.editor.util;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.javainthebox.caraibe.svg.SvgContent;
import net.javainthebox.caraibe.svg.SvgLoaderUtil;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentData;
import org.cafebabe.model.editor.workspace.circuit.component.PortData;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * A utility class for loading data from svg files.
 */
public final class SvgUtil {

    private SvgUtil() {}

    /* Public */

    public static ComponentData getComponentMetadata(Component component) {
        return loadMetadata(getComponentSvgFile(component));
    }

    public static SvgContent loadComponentSvg(Component component) {
        try {
            return SvgLoaderUtil.load(component.getClass().getResource(
                    "/gates/" + component.getIdentifier() + ".svg").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Private */

    /**
     * Returns the component's associated SVG file.
     */
    private static File getComponentSvgFile(Component component) {
        try {
            return new File(component.getClass().getResource(
                    "/gates/" + component.getIdentifier() + ".svg").toURI()
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static NodeList getNodesWithTag(File file, String tag) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            return document.getElementsByTagName(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ComponentData loadMetadata(File file) {
        ComponentData metadata = new ComponentData();
        metadata.inPortMetadata = loadPortData(file, "inport");
        metadata.outPortMetadata = loadPortData(file, "outport");
        return metadata;
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private static List<PortData> loadPortData(File file, String type) {
        NodeList nodes = getNodesWithTag(file, type);
        List<PortData> ports = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(nodes).getLength(); i++) {
            NamedNodeMap attr = nodes.item(i).getAttributes();
            String name = attr.getNamedItem("name").getNodeValue();
            String x = attr.getNamedItem("x").getNodeValue();
            String y = attr.getNamedItem("y").getNodeValue();
            ports.add(new PortData(name, x, y));
        }
        return ports;
    }
}
