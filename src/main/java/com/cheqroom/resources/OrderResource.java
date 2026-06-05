package com.cheqroom.resources;

import com.cheqroom.CheqroomClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class OrderResource {


    private static final String FIELDS =
            "number,status,created,started,due,finished,archived,name,itemSummary," +
            "items,fields,customer.name,location.name,fields,label,by.name,by.picture";

    private final CheqroomClient client;

    public OrderResource(CheqroomClient client) {
        this.client = client;
    }


    public JsonNode search(int limit, int skip, String listName) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", FIELDS);
        params.put("_sort", "started");
        params.put("_listName", listName);
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        return client.post("/orders/search", params);
    }


    public JsonNode getById(String... ids) {
        String idPath = String.join(",", ids);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields",
                "name,status,label,created,started,due,finished,number,archived," +
                "items.name,items.flag,items.status,items.category,items.fields," +
                "items.codes,items.barcodes,items.kit,items.order,items.location," +
                "items.cover,items.canOrder,items.canReserve,items.canCustody," +
                "itemSummary,fields,customer.name,customer.email,customer.fields," +
                "customer.barcodes,customer.status,customer.kind,customer.user.picture," +
                "customer.cover,location.name,comments.*,comments.by.name," +
                "comments.by.picture,attachments.*,attachments.by.name," +
                "attachments.by.picture,reservation,reservation.comments.*," +
                "reservation.comments.by.name,reservation.comments.by.picture," +
                "reservation.attachments.*,reservation.attachments.by.name," +
                "reservation.attachments.by.picture");
        return client.post("/orders/" + idPath, params);
    }
}