package com.cheqroom;

import com.cheqroom.resources.CustomerResource;
import com.cheqroom.resources.ItemResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        CheqroomConfig config = new CheqroomConfig();
        CheqroomClient client = new CheqroomClient(config);
        ItemResource items = new ItemResource(client);
        CustomerResource customers = new CustomerResource(client);

        /*
        System.out.println("\n========== ITEMS ==========");
        JsonNode result = items.searchItems(1, 0);
        printItems(result);

        System.out.println("\n========== CUSTOMERS ==========");
        JsonNode result2 = customers.search(10, 0, "active_or_blocked");
        printCustomers(result2);
        */

        // Example: Search by SN (Serial Number)
        System.out.println("\n========== SEARCH BY SN ==========");
        JsonNode snResult = items.searchBySN("J00269552", 10, 0);
        printItems(snResult);

        // Example: Search by Description
        System.out.println("\n========== SEARCH BY DESCRIPTION ==========");
        JsonNode descriptionResult = items.searchByDescription("Accelerometer", 10, 0);
        printItems(descriptionResult);

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
        
        JsonNode fields = item.path("fields");
        
        // Display only the requested fields
        System.out.printf("  Name                 : %s%n", text(item, "name"));
        System.out.printf("  Serial Number        : %s%n", textField(fields, "SN"));
        System.out.printf("  Current Location     : %s%n", textField(fields, "CurrentLocation"));
        System.out.printf("  Model Number         : %s%n", textField(fields, "Model Number"));
        System.out.printf("  Manufacturer         : %s%n", textField(fields, "Manufacturer"));
        System.out.printf("  Next Due Date        : %s%n", calculateNextDueDate(fields));
        
        System.out.println();
    }

    /**
     * Calculate Next Due Date based on LastCalibrationDate + CalibrationFrequency
     * Frequency format is expected to be in days (e.g., "365", "180", etc.)
     */
    private static String calculateNextDueDate(JsonNode fields) {
        if (fields.isMissingNode() || !fields.isObject()) {
            return "-";
        }

        String lastCalibrationStr = fields.path("LastCalibrationDate").asText(null);
        String frequencyStr = fields.path("CalibrationFrequency").asText(null);

        if (lastCalibrationStr == null || lastCalibrationStr.isBlank() ||
            frequencyStr == null || frequencyStr.isBlank()) {
            return "-";
        }

        try {
            // Parse the last calibration date
            // Try multiple date formats including ISO 8601 with timezone
            LocalDate lastCalibration = null;
            String[] dateFormats = {
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",      // ISO 8601 with ms and timezone (like 2024-12-18T00:00:00.000+00:00)
                "yyyy-MM-dd'T'HH:mm:ssZ",           // ISO 8601 with timezone (like 2024-12-18T00:00:00+00:00)
                "yyyy-MM-dd",                       // ISO format: 2024-01-15
                "dd/MM/yyyy",                       // European: 15/01/2024
                "MM/dd/yyyy",                       // US: 01/15/2024
                "yyyy-MM-dd HH:mm:ss",              // With time
                "dd.MM.yyyy"                        // German: 15.01.2024
            };

            for (String formatStr : dateFormats) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
                    lastCalibration = LocalDate.parse(lastCalibrationStr, formatter);
                    break;
                } catch (Exception e) {
                    // Try next format
                }
            }

            // If still not parsed, try ISO offset date time (handles timezone automatically)
            if (lastCalibration == null) {
                try {
                    lastCalibration = OffsetDateTime.parse(lastCalibrationStr).toLocalDate();
                } catch (Exception e) {
                    return "-";
                }
            }

            // Parse frequency as number of MONTHS
            long frequencyMonths;
            try {
                frequencyMonths = Long.parseLong(frequencyStr.trim());
            } catch (NumberFormatException e) {
                // If frequency is not a simple number, try extracting digits
                String numbersOnly = frequencyStr.replaceAll("[^0-9]", "").trim();
                if (numbersOnly.isEmpty()) {
                    return "-";
                }
                frequencyMonths = Long.parseLong(numbersOnly);
            }

            // Validate frequency is reasonable (not 0 or negative)
            if (frequencyMonths <= 0) {
                return "-";
            }

            // Calculate next due date (add months, not days)
            LocalDate nextDueDate = lastCalibration.plusMonths(frequencyMonths);

            // Format the result
            return nextDueDate.format(DateTimeFormatter.ISO_DATE);

        } catch (Exception e) {
            // If parsing fails, return a dash
            return "-";
        }
    }

    private static String textField(JsonNode fields, String fieldName) {
        if (fields.isMissingNode() || !fields.isObject()) {
            return "-";
        }
        String val = fields.path(fieldName).asText("");
        return val.isBlank() ? "-" : val;
    }

    private static String text(JsonNode node, String field) {
        String val = node.path(field).asText("");
        return val.isBlank() ? "-" : val;
    }
}