package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/8/18.
 */

public class Gasolineras {

    @SerializedName("gas_id")
    private int id;
    @SerializedName("nombre_gas")
    private String name;
    @SerializedName("coordenadas")
    private String coordinates;
    @SerializedName("direccion")
    private String address;

    public Gasolineras(int id, String name, String coordinates, String address) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

