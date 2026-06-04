package com.cheqroom.resources;

import com.cheqroom.CheqroomClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReservationResource {

    // Possible listName values:
    // all, creating, open, closed_manually, closed, today, upcoming, overdue, cancelled, archived
    private static final String FIELDS =
            "status,number,fromDate,toDate,created,archived,reserved,cancelled," +
            "closed_manually,name,itemSummary,items,customer,customer.name," +
            "location.name,fields,repeatId,repeatFrequency,label,by.name,by.picture,order._id";

    private final CheqroomClient client;

    public ReservationResource(CheqroomClient client) {
        this.client = client;
    }

    /** Search reservations by status list */
    public JsonNode search(int limit, int skip, String listName) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields", FIELDS);
        params.put("_sort", "created");
        params.put("_listname", listName);
        params.put("_limit", String.valueOf(limit));
        params.put("_skip", String.valueOf(skip));
        return client.post("/reservations/search", params);
    }

    /** Get one or more reservations by id (comma-separated) */
    public JsonNode getById(String... ids) {
        String idPath = String.join(",", ids);
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_fields",
                "name,status,label,fromDate,toDate,order,number,archived,cancelled," +
                "items.name,items.flag,items.status,items.category,items.fields," +
                "items.codes,items.barcodes,items.location,items.order,items.kit," +
                "items.cover,items.canReserve,items.canOrder,items.canCustody," +
                "itemSummary,fields,customer.name,customer.email,customer.barcodes," +
                "customer.fields,customer.kind,customer.status,customer.user.picture," +
                "customer.cover,location.name,comments.*,comments.by.name," +
                "comments.by.picture,attachments.*,attachments.by.name," +
                "attachments.by.picture,order,repeatId,repeatFrequency");
        params.put("_sort", "created");
        params.put("_listName", "all");
        params.put("_limit", "25");
        params.put("_skip", "0");
        return client.post("/reservations/" + idPath, params);
    }

    /**
     * Create a draft reservation.
     * dates should be ISO-8601, e.g. "2025-07-01T09:00:00.000Z"
     */
    public JsonNode create(String customerId, String locationId, String fromDate, String toDate) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("customer", customerId);
        params.put("location", locationId);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        return client.post("/reservations/create", params);
    }

    /** Set a custom field value on a reservation */
    public JsonNode setField(String reservationId, String fieldId, String value) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("field", fieldId);
        params.put("value", value);
        return client.post("/reservations/" + reservationId + "/setField", params);
    }
}