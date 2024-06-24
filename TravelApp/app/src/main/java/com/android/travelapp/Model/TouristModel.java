package com.android.travelapp.Model;

public class TouristModel {
    private String al_name_tour;
    private int al_price_tour;
    private byte[] al_img_tour;
    private String al_desc_tour;
    private String al_location;

    public TouristModel(String al_name_tour, int al_price_tour, byte[] al_img_tour, String al_desc_tour, String al_location) {
        this.al_name_tour = al_name_tour;
        this.al_price_tour = al_price_tour;
        this.al_img_tour = al_img_tour;
        this.al_desc_tour = al_desc_tour;
        this.al_location = al_location;
    }

    public byte[] getAl_img_tour() {
        return al_img_tour;
    }

    public void setAl_img_tour(byte[] al_img_tour) {
        this.al_img_tour = al_img_tour;
    }

    public String getAl_name_tour() {
        return al_name_tour;
    }

    public String getAl_desc_tour() {
        return al_desc_tour;
    }

    public int getAl_price_tour() {
        return al_price_tour;
    }

    public String getAl_location() {
        return al_location;
    }

    public void setAl_name_tour(String al_name_tour) {
        this.al_name_tour = al_name_tour;
    }

    public void setAl_desc_tour(String al_desc_tour) {
        this.al_desc_tour = al_desc_tour;
    }

    public void setAl_price_tour(int al_price_tour) {
        this.al_price_tour = al_price_tour;
    }

    public void setAl_location(String al_location) {
        this.al_location = al_location;
    }
}
