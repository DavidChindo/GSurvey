package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by david.barrera on 2/8/18.
 */

public class OptionResponse {

    @SerializedName("opcion_id")
    private int optionId;
    @SerializedName("opcion_contenido")
    private String optionDescription;


    public OptionResponse(int optionId, String optionDescription) {
        this.optionId = optionId;
        this.optionDescription = optionDescription;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionDescription() {
        return optionDescription;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }
}


