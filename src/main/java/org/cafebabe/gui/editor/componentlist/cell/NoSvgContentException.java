package org.cafebabe.gui.editor.componentlist.cell;

/**
 * Exception thrown when reading of an SVG file
 * fails because the content or one or more paths
 * is null or empty.
 */
class NoSvgContentException extends RuntimeException {
    private static final long serialVersionUID = 7569788582324205030L;

    NoSvgContentException(String s) {
        super(s);
    }
}