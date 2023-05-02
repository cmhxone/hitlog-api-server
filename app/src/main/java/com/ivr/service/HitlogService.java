package com.ivr.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivr.config.ServerProperties;
import com.ivr.dto.Hitlog;
import com.ivr.queue.FileGeneratorQueue;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ServiceName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServiceName(value = "HitlogService")
public class HitlogService {

    private final String KEY_ID;
    private final String IVR_ID;
    private static final ServerProperties properties = ServerProperties.getInstance();
    private static final FileGeneratorQueue queue = FileGeneratorQueue.getInstance();

    /**
     * Constructor
     */
    public HitlogService() {
        queue.poll();
        KEY_ID = Optional.ofNullable(properties.getString("hitlog.key.id")).orElse("callkey");
        IVR_ID = Optional.ofNullable(properties.getString("hitlog.ivr.id")).orElse("hostname");
    }

    /**
     * Hitlog HTTP POST Request handler
     * 
     * @param body
     * @return
     * @throws Exception
     */
    @Post("/hitlog")
    public HttpResponse storeHitlog(String body) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        if (body.startsWith("[")) { // When JSON List

            List<Map<String, Object>> list = objectMapper.readValue(body,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            for (var item : list) {
                queue.add(generateHitlogFile(item));
            }
        } else { // When JSON Object

            Map<String, Object> map = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
            });

            queue.add(generateHitlogFile(map));
        }

        return HttpResponse.of("storeHitlog");
    }

    /**
     * Generate Hitlog File
     * 
     * @param object
     * @throws Exception
     */
    private Hitlog generateHitlogFile(Map<String, Object> object) throws Exception {

        String callkey = "";
        String ivrname = "";
        StringBuilder sb = new StringBuilder();

        int idx = 0;

        // Serialize into JSON
        sb.append("{");
        for (var entry : object.entrySet()) {

            idx++;

            if (KEY_ID.equals(entry.getKey())) {
                callkey = entry.getValue().toString();
            } else if (IVR_ID.equals(entry.getKey())) {
                ivrname = entry.getValue().toString();
            } else {
                sb.append("\"");
                sb.append(entry.getKey());
                sb.append("\": \"");
                sb.append(entry.getValue().toString());
                sb.append("\"");
                sb.append(idx != object.size() ? "," : "");
            }
        }
        sb.append("}");

        // throw an exception when CallKey or IVRName are empty
        if (callkey.isEmpty() || ivrname.isEmpty()) {
            log.error("Empty CallKey or IVRName: {}", object.toString());
            throw new Exception("Invalid object: " + object.toString());
        }

        return new Hitlog(sb.toString(), ivrname, callkey);
    }
}
