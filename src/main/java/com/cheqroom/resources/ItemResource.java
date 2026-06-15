package com.cheqroom.resources;

import com.cheqroom.CheqroomClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemResource {

    private static final String CUSTOM_FIELDS =
            "fields.SN,fields.Model Number,fields.Type,fields.LastCalibrationDate," +
            "fields.CalibrationFrequency,fields.CalibratedBy,fields.Description," +
            "fields.StorageLocation,fields.CurrentLocation,fields.Notes," +
            "fields.Derogation,fields.Manufacturer,fields.Priority";

    private static final String ITEM_FIELDS =
            "name,status," +
            "kit.name,cover,codes,barcodes," +
            "category,category.name,location,location.name," +
            "flag,custody,canReserve,canOrder,canCustody," +
            "allowReserve,allowOrder,allowCustody," +
            CUSTOM_FIELDS;

    private final CheqroomClient client;

    public ItemResource(CheqroomClient client) {
        this.client = client;
    }


    public JsonNode searchItems(int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", ITEM_FIELDS);
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        return client.post("/items/search", params);
    }

    public JsonNode getItemsById(String... ids) {
        String idPath = String.join(",", ids);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", ITEM_FIELDS + ",comments.*,comments.by.name,attachments.*,attachments.by.name");
        return client.post("/items/" + idPath, params);
    }

    /**
     * Search for items by Serial Number (SN).
     * FIXED: Using correct API format with double underscore for custom field filter
     */
    public JsonNode searchBySN(String sn, int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", ITEM_FIELDS);
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        // FIXED: Using fields__SN (double underscore) instead of fields.SN
        params.put("fields__SN", sn);
        return client.post("/items/search", params);
    }

    /**
     * Search for items by Description custom field.
     * FIXED: Using correct API format with double underscore for custom field filter
     */
    public JsonNode searchByDescription(String description, int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", ITEM_FIELDS);
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        // FIXED: Using fields__Description (double underscore) instead of fields.Description
        params.put("fields__Description", description);
        return client.post("/items/search", params);
    }

    /**
     * Search for items by Manufacturer custom field.
     */
    public JsonNode searchByManufacturer(String manufacturer, int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", ITEM_FIELDS);
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        params.put("fields__Manufacturer", manufacturer);
        return client.post("/items/search", params);
    }

    /**
     * Search for items by Model Number custom field.
     */
    public JsonNode searchByModelNumber(String modelNumber, int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", ITEM_FIELDS);
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        params.put("fields__Model Number", modelNumber);
        return client.post("/items/search", params);
    }

    /**
     * Search items by status (available, in_custody, maintenance, repair, etc.)
     */
    public JsonNode searchByStatus(String status, int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", ITEM_FIELDS);
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        params.put("status", status);
        return client.post("/items/search", params);
    }

    public JsonNode searchKits(int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields",
                "name,description,itemSummary,created,fields,status,cover,codes,barcodes," +
                "items,items.cover,items.canReserve,items.canOrder,items.canCustody," +
                "canReserve,canOrder,canCustody,allowReserve,allowOrder,allowCustody");
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        return client.post("/kits/search", params);
    }
}