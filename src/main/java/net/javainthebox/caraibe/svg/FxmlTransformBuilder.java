package net.javainthebox.caraibe.svg;

import java.util.StringTokenizer;
import javafx.scene.Node;
import javafx.scene.transform.Transform;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

/**
 * OUR GROUP IS NOT RESPONSIBLE FOR THIS FILE, IT IS SIMPLY A DEPENDENCY.
 * Builds FXML transforms.
 */
public class FxmlTransformBuilder {

    void setTransform(Node node, StartElement element) {
        Attribute transformAttribute = element.getAttributeByName(new QName("transform"));
        if (transformAttribute != null) {
            String transforms = transformAttribute.getValue();

            Transform transform = extractTransform(transforms);
            node.getTransforms().add(transform);
        }
    }

    Transform extractTransform(String transforms) {
        Transform transform = null;

        StringTokenizer tokenizer = new StringTokenizer(transforms, ")");

        while (tokenizer.hasMoreTokens()) {
            String transformTxt = tokenizer.nextToken();
            transform = tokenToTransform(transform, transformTxt);
        }

        return transform;
    }

    Transform tokenToTransform(Transform transformParam, String transformTxtParam) {
        Transform transform = transformParam;
        String transformTxt = transformTxtParam;
        if (transformTxt.startsWith("translate(")) {
            throw new UnsupportedOperationException("Transform:Translate");
        } else if (transformTxt.startsWith("scale(")) {
            throw new UnsupportedOperationException("Transform:Scale");
        } else if (transformTxt.startsWith("rotate(")) {
            throw new UnsupportedOperationException("Transform:Rotate");
        } else if (transformTxt.startsWith("skewX(")) {
            throw new UnsupportedOperationException("Transform:SkewX");
        } else if (transformTxt.startsWith("skewY(")) {
            throw new UnsupportedOperationException("Transform:SkewY");
        } else if (transformTxt.startsWith("matrix(")) {
            transformTxt = transformTxt.substring(7);
            StringTokenizer tokenizer2 = new StringTokenizer(transformTxt, " ");
            double mxx = Double.parseDouble(tokenizer2.nextToken());
            double myx = Double.parseDouble(tokenizer2.nextToken());
            double mxy = Double.parseDouble(tokenizer2.nextToken());
            double myy = Double.parseDouble(tokenizer2.nextToken());
            double tx = Double.parseDouble(tokenizer2.nextToken());
            double ty = Double.parseDouble(tokenizer2.nextToken());

            transform = Transform.affine(mxx, myx, mxy, myy, tx, ty);
        }
        return transform;
    }
}