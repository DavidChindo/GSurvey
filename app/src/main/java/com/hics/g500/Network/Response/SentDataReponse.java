package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/15/18.
 */

public class SentDataReponse {

    @SerializedName("error")
    private String error;
    @SerializedName("message")
    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
