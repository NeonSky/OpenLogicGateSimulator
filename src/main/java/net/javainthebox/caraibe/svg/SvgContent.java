package net.javainthebox.caraibe.svg;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * OUR GROUP IS NOT RESPONSIBLE FOR THIS FILE, IT IS SIMPLY A DEPENDENCY.
 * SvgContent express SVG content.
 */
public class SvgContent extends Group {
    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, Group> groups = new HashMap<>();

    void putNode(String id, Node node) {
        this.nodes.put(id, node);
    }

    /**
     * Gets node object indicated by id.
     * When there is no node indicated by id, return null.
     *
     * @param id the name of node
     * @return node  represented by id
     */
    public Node getNode(String id) {
        return this.nodes.get(id);
    }

    void putGroup(String id, Group group) {
        this.groups.put(id, group);
    }

    /**
     * Gets group object indicated by id.
     * When there is no group indicated by id, return null.
     *
     * @param id the name of group
     * @return group represented by id
     */
    public Group getGroup(String id) {
        return this.groups.get(id);
    }

    public Collection<Node> selectNodesWithClasses(String... styleClasses) {
        return subnodes(this, styleClasses).collect(Collectors.toList());
    }

    public Collection<String> getStyleClassesRecursive() {
        return getStyleClassesRecursive(this).collect(Collectors.toSet());
    }

    private Stream<String> getStyleClassesRecursive(Node node) {
        if (node instanceof Parent) {
            return Stream.concat(
                    node.getStyleClass().stream(),
                    ((Parent) node).getChildrenUnmodifiable().stream()
                            .flatMap(this::getStyleClassesRecursive)
            );
        }
        return node.getStyleClass().stream();
    }

    private Stream<Node> subnodes(Node node, String... styleClasses) {
        Boolean appendSelf = Arrays.stream(styleClasses)
                .anyMatch(s -> node.getStyleClass().contains(s));

        Stream<Node> partialStream = appendSelf ? Stream.of(node) : Stream.of();

        if (node instanceof Parent) {
            return Stream.concat(
                    partialStream,
                    ((Parent) node).getChildrenUnmodifiable().stream()
                            .flatMap(n -> subnodes(n, styleClasses))
            );
        } else {
            return partialStream;
        }
    }
}
