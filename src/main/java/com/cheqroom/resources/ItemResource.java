package com.cheqroom.resources;

import com.cheqroom.CheqroomClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemResource {

    private static final String ITEM_FIELDS =
            "name,created,expired,fields,status,brand,model,purchasePrice,purchaseDate," +
            "warrantyDate,kit.name,cover,codes,barcodes,order._id,order.customer," +
            "order.customer.name,order.status,order.started,order.due,order.location.name," +
            "category,category.name,location,location.name,flag,custody,residualValue," +
            "depreciatedValue,canReserve,canOrder,canCustody,allowReserve,allowOrder,allowCustody,address";

    private static final String KIT_FIELDS =
            "name,description,itemSummary,created,fields,status,cover,codes,barcodes," +
            "items,items.cover,items.canReserve,items.canOrder,items.canCustody," +
            "canReserve,canOrder,canCustody,allowReserve,allowOrder,allowCustody";

    private final CheqroomClient client;

    public ItemResource(CheqroomClient client) {
        this.client = client;
    }

    /** Search items. listName is typically "active" */
    public JsonNode searchItems(int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", ITEM_FIELDS);
        params.put("listName", "active");
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        return client.post("/items/search", params);
    }

    /** Get one or more items by id (comma-separated) */
    public JsonNode getItemsById(String... ids) {
        String idPath = String.join(",", ids);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields",
                "name,status,cover,flag,codes,barcodes,category.name,location.name," +
                "comments.*,comments.by.name,comments.by.picture,attachments.*," +
                "attachments.by.name,attachments.by.picture,geo,address,model,brand," +
                "warrantyDate,purchaseDate,purchasePrice,residualValue,fields," +
                "order.customer.name,order.started,order.due,order.status,order.location," +
                "expired,custody.name,kit.name,canReserve,canOrder,canCustody," +
                "allowReserve,allowOrder,allowCustody,flagged,catalog");
        return client.post("/items/" + idPath, params);
    }

    /** Search kits */
    public JsonNode searchKits(int limit, int skip) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", KIT_FIELDS);
        params.put("listName", "active");
        params.put("_sort", "name");
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        return client.post("/kits/search", params);
    }
}