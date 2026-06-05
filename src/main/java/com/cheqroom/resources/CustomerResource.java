package com.cheqroom.resources;

import com.cheqroom.CheqroomClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomerResource {

    private static final String FIELDS =
            "name,cover,email,barcodes,status,blocked,fields,kind,user.name,user.sync,user.picture,created";

    private final CheqroomClient client;

    public CustomerResource(CheqroomClient client) {
        this.client = client;
    }


    public JsonNode search(int limit, int skip, String listName) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", FIELDS);
        params.put("_listName", listName);
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        return client.post("/customers/search", params);
    }


    public JsonNode searchByEmail(String email) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", FIELDS);
        params.put("_listName", "active_or_blocked");
        params.put("email__in", "[\"" + email + "\"]");
        params.put("_limit", "25");
        params.put("_skip", "0");
        return client.post("/customers/search", params);
    }


    public JsonNode getById(String... ids) {
        String idPath = String.join(",", ids);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields",
                "name,cover,email,status,kind,barcodes,archived,blocked," +
                "user.name,user.sync,user.picture,comments.*," +
                "comments.by.name,comments.by.picture,attachments.*," +
                "attachments.by.name,attachments.by.picture,fields");
        return client.post("/customers/" + idPath, params);
    }


    public JsonNode create(String name, String email) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("email", email);
        return client.post("/customers/create", params);
    }

    public JsonNode update(String contactId, String name, String email) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("email", email);
        return client.post("/customers/" + contactId + "/update", params);
    }


    public JsonNode archive(String contactId) {
        // The docs specify GET for archive
        return client.post("/customers/" + contactId + "/archive", Map.of());
    }
}