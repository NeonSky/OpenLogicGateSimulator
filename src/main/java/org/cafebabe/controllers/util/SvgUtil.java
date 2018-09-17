package org.cafebabe.controllers.util;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import java.io.File;
import java.net.URISyntaxException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.cafebabe.model.components.Component;

public class SvgUtil {

    private static NodeList getSvgPathNodes(File file) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList paths = document.getElementsByTagName("path");
            return paths;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isWirePath(Node node) {
        return node.getAttributes().getNamedItem("iswire").getNodeValue().equals("true");
    }

    /** Returns the full SVG path of the given component's associated SVG file. */
    private static String loadSvgPath(File file, boolean loadBarePath) {
        StringBuilder svgPath = new StringBuilder();
        NodeList paths = getSvgPathNodes(file);
        for(int i = 0; i < paths.getLength(); i++) {
            if(loadBarePath && isWirePath(paths.item(i))) {
                continue;
            }
            svgPath.append(paths.item(i).getAttributes().getNamedItem("d").getNodeValue());
        }
        return svgPath.toString();
    }


    /** Returns the component's associated SVG file. */
    private static File getComponentSvg(Component component) {
        try {
            return new File(component.getClass().getResource("/gates/images/" + component.getAnsiName() + ".svg").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Returns the component's associated SVG path. */
    public static String getComponentSvgPath(Component component) {
        return loadSvgPath(SvgUtil.getComponentSvg(component), false);
    }

    /** Returns the component's associated SVG path, but excludes any wire visuals. */
    public static String getBareComponentSvgPath(Component component) {
        return loadSvgPath(SvgUtil.getComponentSvg(component), true);
    }
}
