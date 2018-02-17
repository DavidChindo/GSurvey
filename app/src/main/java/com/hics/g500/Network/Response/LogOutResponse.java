package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/16/18.
 */

public class LogOutResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

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
}

