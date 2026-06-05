package com.cheqroom.resources;

import com.cheqroom.CheqroomClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemResource {


    private static final String CUSTOM_FIELDS =
            "fields.SN,fields.Model Number,fields.Type,fields.LastCalibrationDate," +
            "fields.CalibrationFrequency,fields.CalibratedBy,fields.Description," +
            "fields.StorageLocation,fields.CurrentLocation,fields.Notes," +
            "fields.Derogation,fields.Manufacturer,fields.PurchasePO,fields.RetirementDate";

    private static final String ITEM_FIELDS =
            "name,status,brand,model," +
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
        params.put("listName", "active");
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
    public JsonNode searchKits(int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields",
                "name,description,itemSummary,created,fields,status,cover,codes,barcodes," +
                "items,items.cover,items.canReserve,items.canOrder,items.canCustody," +
                "canReserve,canOrder,canCustody,allowReserve,allowOrder,allowCustody");
        params.put("listName", "active");
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        return client.post("/kits/search", params);
    }
}