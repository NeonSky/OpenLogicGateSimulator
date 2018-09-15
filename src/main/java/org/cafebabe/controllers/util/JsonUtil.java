package org.cafebabe.controllers.util;

import org.cafebabe.controllers.util.JsonMetadata.ComponentMetadata;
import org.cafebabe.model.components.Component;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class JsonUtil {
    private static File getComponentMetadataFile(Component component) {
        try {
            return new File(component.getClass().getResource("/gates/metadata/" + component.getAnsiName() + ".json").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ComponentMetadata loadMetadataFile(File metadataJsonFile) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject metadata = (JSONObject) parser.parse(new FileReader(metadataJsonFile));
            return new ComponentMetadata(metadata);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Parsing Metadata Failed");
    }

    public static ComponentMetadata getComponentMetadata(Component component) {
        return JsonUtil.loadMetadataFile(JsonUtil.getComponentMetadataFile(component));
    }
}

