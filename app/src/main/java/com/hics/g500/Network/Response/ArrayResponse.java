package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/8/18.
 */

public class ArrayResponse {

    @SerializedName("jwt")
    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
