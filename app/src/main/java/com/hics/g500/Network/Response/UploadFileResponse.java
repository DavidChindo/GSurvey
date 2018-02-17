package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by david.barrera on 2/15/18.
 */

public class UploadFileResponse {

    @SerializedName("error")
    private Boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("files")
    private ArrayList<ArrayList<String>> files;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public ArrayList<ArrayList<String>> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<ArrayList<String>> files) {
        this.files = files;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
