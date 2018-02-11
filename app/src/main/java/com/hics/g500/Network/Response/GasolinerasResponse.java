package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by david.barrera on 2/8/18.
 */

public class GasolinerasResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("gascercanas")
    private ArrayList<com.hics.g500.db.Gasolineras> gasolinerases;

    public GasolinerasResponse(boolean error, String message, ArrayList<com.hics.g500.db.Gasolineras> gasolinerases) {
        this.error = error;
        this.message = message;
        this.gasolinerases = gasolinerases;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<com.hics.g500.db.Gasolineras> getGasolinerases() {
        return gasolinerases;
    }

    public void setGasolinerases(ArrayList<com.hics.g500.db.Gasolineras> gasolinerases) {
        this.gasolinerases = gasolinerases;
    }
}
