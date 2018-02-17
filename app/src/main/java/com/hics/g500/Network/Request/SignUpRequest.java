package com.hics.g500.Network.Request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/16/18.
 */

public class SignUpRequest {

    @SerializedName("email")
    private String email;
    @SerializedName("passwd")
    private String password;

    public SignUpRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

