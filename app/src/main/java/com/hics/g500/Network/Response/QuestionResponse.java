package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by david.barrera on 2/8/18.
 */

public class QuestionResponse {

    @SerializedName("pregunta_id")
    private int quesionId;
    @SerializedName("pregunta_obligatoria")
    private int mandatory;
    @SerializedName("pregunta_encabezado")
    private String title;
    @SerializedName("pregunta_tipo")
    private String questionType;
    @SerializedName("tipo_min")
    private int minLength;
    @SerializedName("tipo_max")
    private int maxLegth;
    @SerializedName("tipo_dato")
    private String dataType;
    @SerializedName("num_opciones")
    private int optionesNum;
    @SerializedName("opciones")
    private ArrayList<OptionResponse> optionResponses;

    public QuestionResponse(int quesionId, int mandatory, String title, String questionType, int minLength,
                            int maxLegth, String dataType, int optionesNum, ArrayList<OptionResponse> optionResponses) {
        this.quesionId = quesionId;
        this.mandatory = mandatory;
        this.title = title;
        this.questionType = questionType;
        this.minLength = minLength;
        this.maxLegth = maxLegth;
        this.dataType = dataType;
        this.optionesNum = optionesNum;
        this.optionResponses = optionResponses;
    }

    public int getQuesionId() {
        return quesionId;
    }

    public void setQuesionId(int quesionId) {
        this.quesionId = quesionId;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLegth() {
        return maxLegth;
    }

    public void setMaxLegth(int maxLegth) {
        this.maxLegth = maxLegth;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getOptionesNum() {
        return optionesNum;
    }

    public void setOptionesNum(int optionesNum) {
        this.optionesNum = optionesNum;
    }

    public ArrayList<OptionResponse> getOptionResponses() {
        return optionResponses;
    }

    public void setOptionResponses(ArrayList<OptionResponse> optionResponses) {
        this.optionResponses = optionResponses;
    }
}

