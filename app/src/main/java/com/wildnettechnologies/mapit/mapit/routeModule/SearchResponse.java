package com.wildnettechnologies.mapit.mapit.routeModule;

/**
 * Created by Ajay Panwar on 2/8/2016.
 */
public class SearchResponse {

    private String description;
    private String id;
    private String place_id;
    private String reference;

    public SearchResponse(String description, String placeId) {
        this.description = description;
        this.place_id = placeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
