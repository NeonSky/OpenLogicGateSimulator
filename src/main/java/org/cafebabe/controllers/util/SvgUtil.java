package org.cafebabe.controllers.util;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import java.io.File;
import org.w3c.dom.NodeList;

import org.cafebabe.model.components.Component;

public class SvgUtil {

    /** Returns the full SVG path of the given component's associated SVG file. */
    private static String loadSvgPath(File file) {
        StringBuilder svgPath = new StringBuilder();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList paths = document.getElementsByTagName("path");
            for(int i = 0; i < paths.getLength(); i++) {
                svgPath.append(paths.item(i).getAttributes().getNamedItem("d").getNodeValue());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return svgPath.toString();
    }


    /** Returns the component's associated SVG file. */
    private static File getComponentSvg(Component component) {
        return new File(component.getClass().getResource("/images/gates/" + component.getAnsiName() + ".svg").getFile());
    }

    /** Returns the component's associated SVG path. */
    public static String getComponentSvgPath(Component component) {
        return loadSvgPath(SvgUtil.getComponentSvg(component));
    }
}
