package org.cafebabe.controllers.editor;

/**
 * Exception thrown when reading of an SVG file
 * fails because the content or one or more paths
 * is null or empty.
 */
public class NoSvgContentException extends RuntimeException {
    private static final long serialVersionUID = 7569788582324205030L;

    public NoSvgContentException(String s) {
        super(s);
    }
}