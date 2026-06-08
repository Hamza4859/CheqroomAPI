package com.cheqroom;

import com.cheqroom.resources.CustomerResource;
import com.cheqroom.resources.ItemResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {



    private static final String[] CUSTOM_FIELD_NAMES = {
        "SN", "Model Number", "Type", "LastCalibrationDate",
        "CalibrationFrequency", "CalibratedBy", "Description",
        "StorageLocation", "CurrentLocation", "Notes",
        "Derogation", "Manufacturer", "PurchasePO", "RetirementDate"
    };

    public static void main(String[] args) throws Exception {
        CheqroomConfig config = new CheqroomConfig();
        CheqroomClient client = new CheqroomClient(config);
        ItemResource items = new ItemResource(client);
        CustomerResource customers = new CustomerResource(client);


        String itemId = "mHQgDcwJtGCvznaf8UEs7Y"; // replace with your item's ID
        JsonNode result = items.getItemsById(itemId);
        printItems(result);

        /*
        System.out.println("\n========== ITEMS ==========");
        JsonNode result = items.searchItems(1, 0);
        printItems(result);
        */

        /*
        System.out.println("\n========== CUSTOMERS ==========");
        JsonNode result2 = customers.search(10, 0, "active_or_blocked");
        printCustomers(result2);
        */
    }

    private static void printCustomers(JsonNode response) {
        JsonNode docs = response.path("docs");
        if (docs.isMissingNode() || !docs.isArray()) {
            printSingleCustomer(response);
            return;
        }
        System.out.printf("Total customers: %d%n%n", response.path("count").asInt());
        for (JsonNode customer : docs) {
            printSingleCustomer(customer);
        }
    }

    private static void printItems(JsonNode response) {
        JsonNode docs = response.path("docs");
        if (docs.isMissingNode() || !docs.isArray()) {
            printSingleItem(response);
            return;
        }
        System.out.printf("Total items: %d%n%n", response.path("count").asInt());
        for (JsonNode item : docs) {
            printSingleItem(item);
        }
    }


    private static void printSingleCustomer(JsonNode customer) {
        System.out.println("─────────────────────────────────────────");
        System.out.printf("  Name      : %s%n", text(customer, "name"));
        System.out.printf("  Email     : %s%n", text(customer, "email"));
        System.out.printf("  Status    : %s%n", text(customer, "status"));
        System.out.printf("  Kind      : %s%n", text(customer, "kind"));
        System.out.printf("  Blocked   : %s%n", customer.path("blocked").asBoolean(false) ? "Yes" : "No");
        System.out.println();
    }

    private static void printSingleItem(JsonNode item) {
        System.out.println("─────────────────────────────────────────");
        System.out.printf("  ID        : %s%n", text(item, "_id"));
        System.out.printf("  Name      : %s%n", text(item, "name"));
        System.out.printf("  Status    : %s%n", text(item, "status"));
        System.out.printf("  Category  : %s%n", item.path("category").path("name").asText("-"));
        System.out.printf("  Location  : %s%n", item.path("location").path("name").asText("-"));

        JsonNode fields = item.path("fields");
        if (!fields.isMissingNode() && fields.isObject()) {
            System.out.println("  ── Custom Fields ──");
            for (String fieldName : CUSTOM_FIELD_NAMES) {
                JsonNode val = fields.path(fieldName);
                if (!val.isMissingNode() && !val.asText().isBlank()) {
                    System.out.printf("    %-25s: %s%n", fieldName, val.asText());
                }
            }
        }
        System.out.println();
    }

    private static String text(JsonNode node, String field) {
        String val = node.path(field).asText("");
        return val.isBlank() ? "-" : val;
    }
}