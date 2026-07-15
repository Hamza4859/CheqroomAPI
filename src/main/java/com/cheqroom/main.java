package com.cheqroom;

import com.cheqroom.resources.LocationResource;
import com.fasterxml.jackson.databind.JsonNode;

public class Main {

    public static void main(String[] args) throws Exception {
        CheqroomConfig config = new CheqroomConfig();
        CheqroomClient client = new CheqroomClient(config);
        
        LocationResource locations = new LocationResource(client);

        System.out.println("\n========== FETCHING ALL LOCATIONS ==========");
        JsonNode locationsResult = locations.searchLocations();

        System.out.println("\n========== PARSING LOCATION RAW DATA ==========");
        printRawLocations(locationsResult);
    }

    private static void printRawLocations(JsonNode response) {
        JsonNode docs;
        if (response.isArray()) {
            docs = response;
        } else if (response.has("docs") && response.path("docs").isArray()) {
            docs = response.path("docs");
        } else {
            System.out.println("No locations found in response. Raw response: " + response.toString());
            return;
        }

        System.out.println("─────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("  %-30s | %-30s | %s%n", "LOCATION NAME", "LOCATION ID", "PARENT INFO (RAW)");
        System.out.println("─────────────────────────────────────────────────────────────────────────────────");

        for (JsonNode doc : docs) {
            String id = doc.path("_id").asText("");
            if (id.isEmpty()) {
                id = doc.path("id").asText("");
            }
            String name = doc.path("name").asText("");
            JsonNode parent = doc.path("parent");

            System.out.printf("  %-30s | %-30s | %s%n", name, id, parent.toString());
        }
        System.out.println("─────────────────────────────────────────────────────────────────────────────────");
    }
}