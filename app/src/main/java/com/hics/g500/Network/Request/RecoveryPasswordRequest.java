package com.hics.g500.Network.Request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/16/18.
 */

public class RecoveryPasswordRequest {

    @SerializedName("email")
    private String email;

    public RecoveryPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
