package org.cafebabe.controllers.util.JsonMetadata;

import org.json.simple.JSONObject;
import java.lang.Number;

public class PortMetadata {
    public final String name;
    public final double x;
    public final double y;

    public PortMetadata(JSONObject portMetadataJson) {
        this.name = (String) portMetadataJson.get("name");
        this.x = ((Number) portMetadataJson.get("x")).doubleValue();
        this.y = ((Number) portMetadataJson.get("y")).doubleValue();
    }
}
