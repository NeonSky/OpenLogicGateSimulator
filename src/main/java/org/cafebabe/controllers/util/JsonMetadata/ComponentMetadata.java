package org.cafebabe.controllers.util.JsonMetadata;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComponentMetadata {
    public final Iterable<PortMetadata> inPortMetadata;
    public final Iterable<PortMetadata> outPortMetadata;

    public ComponentMetadata(JSONObject metadataJson) {
        JSONArray inPortsJson = (JSONArray) metadataJson.get("inPorts");
        List<PortMetadata> mutableInPortMetadata = new ArrayList<>();
        for(Object inPortJson: inPortsJson) {
            mutableInPortMetadata.add(new PortMetadata((JSONObject) inPortJson));
        }
        this.inPortMetadata = mutableInPortMetadata;

        JSONArray outPortsJson = (JSONArray) metadataJson.get("outPorts");
        List<PortMetadata> mutableOutPortMetadata = new ArrayList<>();
        for(Object outPortJson: outPortsJson) {
            mutableOutPortMetadata.add(new PortMetadata((JSONObject) outPortJson));
        }
        this.outPortMetadata = mutableOutPortMetadata;
    }
}
