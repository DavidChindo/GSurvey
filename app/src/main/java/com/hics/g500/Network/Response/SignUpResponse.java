package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/16/18.
 */

public class SignUpResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("email")
    private String email;
    @SerializedName("createdAt")
    private String created;
    @SerializedName("array")
    private ArrayResponse array;
    @SerializedName("message")
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public ArrayResponse getArray() {
        return array;
    }

    public void setArray(ArrayResponse array) {
        this.array = array;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
