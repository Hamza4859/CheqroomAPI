package com.cheqroom;

import com.cheqroom.resources.CustomerResource;
import com.cheqroom.resources.ItemResource;
import com.cheqroom.resources.OrderResource;
import com.cheqroom.resources.ReservationResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) throws Exception {
        CheqroomConfig config = new CheqroomConfig();
        CheqroomClient client = new CheqroomClient(config);
        ObjectMapper mapper = client.getMapper();

        CustomerResource customers = new CustomerResource(client);
        ItemResource items = new ItemResource(client);
        ReservationResource reservations = new ReservationResource(client);
        OrderResource orders = new OrderResource(client);

        System.out.println("\n========== CUSTOMERS ==========");
        JsonNode customerResult = customers.search(10, 0, "active_or_blocked");
        prettyPrint(mapper, customerResult);

        System.out.println("\n========== ITEMS ==========");
        JsonNode itemResult = items.searchItems(10, 0);
        prettyPrint(mapper, itemResult);

        System.out.println("\n========== RESERVATIONS (open) ==========");
        JsonNode reservationResult = reservations.search(10, 0, "open");
        prettyPrint(mapper, reservationResult);

        System.out.println("\n========== CHECKOUTS (open) ==========");
        JsonNode orderResult = orders.search(10, 0, "open");
        prettyPrint(mapper, orderResult);

        // --- Uncomment to try individual lookups ---
        // JsonNode oneItem = items.getItemsById("ITEM_ID_HERE");
        // prettyPrint(mapper, oneItem);

        // --- Uncomment to create a contact ---
        // JsonNode newContact = customers.create("Jane Doe", "jane@example.com");
        // prettyPrint(mapper, newContact);

        // --- Uncomment to create a draft reservation ---
        // JsonNode newReservation = reservations.create(
        //     "CUSTOMER_ID",
        //     "LOCATION_ID",
        //     "2025-07-01T09:00:00.000Z",
        //     "2025-07-05T17:00:00.000Z"
        // );
        // prettyPrint(mapper, newReservation);
    }

    private static void prettyPrint(ObjectMapper mapper, JsonNode node) throws Exception {
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
    }
}