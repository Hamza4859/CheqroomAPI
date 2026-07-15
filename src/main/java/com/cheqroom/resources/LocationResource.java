package com.cheqroom.resources;



import com.cheqroom.CheqroomClient;

import com.fasterxml.jackson.databind.JsonNode;



public class LocationResource {



    private final CheqroomClient client;



    public LocationResource(CheqroomClient client) {

        this.client = client;

    }



    /**

     * Retrieves locations from the Cheqroom API.

     * We'll fetch them using a standard GET request.

     */

    public JsonNode searchLocations() {
    return client.get("locations?_limit=200&_skip=0");
    }

} 

