package com.hics.g500.Network.Request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/8/18.
 */

public class GasolinerasRequest {

    @SerializedName("email")
    private String email;
    @SerializedName("coordenadas")
    private String coordinates;

    public GasolinerasRequest(String email, String coordinates) {
        this.email = email;
        this.coordinates = coordinates;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
